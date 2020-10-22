package it.emanu37429.atamcorse2.atamcorse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.AdapterFermata;

public class GetPalinaActivity extends BaseActivity {
    ProgressDialog pDialog;
    ArrayList<ClassU.FermataCorsaP> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_palina);
        final String id = getIntent().getExtras().getString("id");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String nc = String.valueOf(pref.getInt("ncorsepal", 15));
        pDialog = ProgressDialog.show(this, "Caricamento in corso", "Attendere prego...");

        final AppCompatActivity act = this;
        new Thread(() -> {
            try {
                String str = ClassU.DownloadString(ClassU.linkCorsePalina + id + "&nc=" + nc);
                if (str != null && str.length() > 60) {
                    String[] buses = str.split("\\n");
                    for (int i = 1; i < buses.length; i++) {
                        String[] bus = buses[i].split(";", -1);
                        Boolean nav = !bus[5].equals("NonAggiornato");
                        String vec = "";
                        if (!bus[3].equals(""))
                            vec = ClassU.getVeicoloMod(bus[3], getApplicationContext());
                        list.add(new ClassU.FermataCorsaP(bus[0], bus[1], bus[2], vec, bus[4], nav, act));
                    }
                    AdapterFermata adapter = new AdapterFermata(list);
                    runOnUiThread(() -> {
                        RecyclerView mRecyclerView = findViewById(R.id.recCorsePalina);
                        mRecyclerView.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        llm.setOrientation(RecyclerView.VERTICAL);
                        mRecyclerView.setLayoutManager(llm);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                        mRecyclerView.addItemDecoration(dividerItemDecoration);
                        mRecyclerView.setAdapter(adapter);
                        pDialog.dismiss();
                    });
                } else if (str == null) {
                    runOnUiThread(() -> {
                        pDialog.dismiss();
                        new AlertDialog.Builder(act)
                                .setTitle("Errore")
                                .setMessage("Errore di connessione o tempo di caricamento esaurito")
                                .setPositiveButton("Chiudi", (a, b) -> finish())
                                .show();
                    });
                } else {
                    runOnUiThread(() -> {
                        pDialog.dismiss();
                        new AlertDialog.Builder(act)
                                .setTitle("Errore")
                                .setMessage("Nessun passaggio previsto")
                                .setPositiveButton("Chiudi", (a,b) -> finish())
                                .show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> pDialog.dismiss());
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_icon, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        recreate();
        return super.onOptionsItemSelected(item);
    }
}
