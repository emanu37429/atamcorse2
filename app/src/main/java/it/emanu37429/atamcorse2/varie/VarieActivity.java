package it.emanu37429.atamcorse2.varie;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.AdapterVeicoli;

public class VarieActivity extends BaseActivity {
    RecyclerView mRecyclerView;
    SearchView search;
    AppCompatActivity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varie);
        mRecyclerView = findViewById(R.id.recVarie);
        search = findViewById(R.id.searchVarie);
        act = this;
        int pos = getIntent().getExtras().getInt("pos");
        try {
            if (pos == 0) {
                Veicoli();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void Veicoli() {
        ProgressDialog pDialog = ProgressDialog.show(this,"Caricamento in corso", "Attendere prego...");
        new Thread(() -> {
            try {
                JSONArray statoVec = new JSONArray(ClassU.DownloadString(ClassU.linkElencoVeicoli));
                ArrayList<ClassU.Veicolo> lista = new ArrayList<>();
                for (int i = 0; i < statoVec.length(); i++) {
                    JSONObject obj = statoVec.getJSONObject(i);
                    lista.add(new ClassU.Veicolo(obj.getString("id"), obj.getString("mod"), obj.getString("stato")));
                }
                runOnUiThread(() -> {
                    mRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                    llm.setOrientation(RecyclerView.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                    mRecyclerView.addItemDecoration(dividerItemDecoration);
                    final AdapterVeicoli adapter = new AdapterVeicoli(lista);
                    mRecyclerView.setAdapter(adapter);
                    search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            adapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                    pDialog.dismiss();
                });
            } catch (Exception e) {
                e.printStackTrace();
                pDialog.dismiss();
            }
        }).start();
    }
}
