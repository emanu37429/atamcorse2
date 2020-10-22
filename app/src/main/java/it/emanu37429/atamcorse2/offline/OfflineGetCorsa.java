package it.emanu37429.atamcorse2.offline;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.OfflineCorsaAdapter;

public class OfflineGetCorsa extends BaseActivity {
    ProgressDialog pDialog;
    ArrayList<ClassU.FermataCorsaBase> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_corsa);
        setTitle("ATAM Corse (offline)");
        pDialog = ProgressDialog.show(this,"Caricamento in corso", "Attendere prego...");
        final String id = getIntent().getExtras().getString("id");
        new Thread(() -> {
            try {
                JSONArray array = OfflineActivity.getObjOffline(getApplicationContext());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    if (obj.getString("idCorsa").equals(id)) {
                        list.add(new ClassU.FermataCorsaBase(
                                obj.getString("nomePalina"), obj.getString("oraPassaggio"),null, null, null, null, null
                        ));
                    }
                    else if(list.size()>1) break;
                }
                runOnUiThread(() -> {
                    RecyclerView mRecyclerView = findViewById(R.id.recCorsaAct);
                    mRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                    llm.setOrientation(RecyclerView.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                    mRecyclerView.addItemDecoration(dividerItemDecoration);
                    mRecyclerView.setAdapter(new OfflineCorsaAdapter(list));
                    pDialog.cancel();
                });
            } catch (Exception e) {
                e.printStackTrace();
                pDialog.cancel();
            }
        }).start();
    }
}
