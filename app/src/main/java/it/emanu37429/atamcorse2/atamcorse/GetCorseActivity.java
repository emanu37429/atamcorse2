package it.emanu37429.atamcorse2.atamcorse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.CorsaListAdapter;

public class GetCorseActivity extends BaseActivity {
    List<ClassU.Corsa> list = new ArrayList<>();
    AlertDialog pDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_corse);
        pDialog = ProgressDialog.show(this,"Caricamento in corso", "Attendere prego...");
        final String id = getIntent().getExtras().getString("id");
        final AppCompatActivity act = this;
        new Thread(() -> {
            try {
                String tt = ClassU.DownloadString(ClassU.linkElencoCorse + id);
                if (tt != null) {
                    if (tt.length() > 50) {
                        String[] corse = tt.split("\\n");
                        for (int i = 1; i < corse.length; i++) {
                            String[] corsa = corse[i].split(";");
                            list.add(new ClassU.Corsa(corsa[4], corsa[3], corsa[1], corsa[2], corsa[0], act));
                        }
                        runOnUiThread(() -> {
                            RecyclerView mRecyclerView = findViewById(R.id.recCorse);
                            mRecyclerView.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            llm.setOrientation(RecyclerView.VERTICAL);
                            mRecyclerView.setLayoutManager(llm);
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                            mRecyclerView.addItemDecoration(dividerItemDecoration);
                            mRecyclerView.setAdapter(new CorsaListAdapter(list));
                            pDialog.cancel();
                        });
                    } else if (tt.trim().equals("outdate")) {
                        runOnUiThread(() -> {
                            pDialog.cancel();
                            new AlertDialog.Builder(act)
                                    .setTitle("Errore")
                                    .setMessage("Servizio attualmente non disponibile.")
                                    .setPositiveButton("Chiudi", (a,b) -> finish()).show();
                        });
                    } else if (tt.trim().equals("idcorsa;partenza;capolinea;orapart;orafin")) {
                        runOnUiThread(() -> {
                            pDialog.cancel();
                            new AlertDialog.Builder(act)
                                    .setTitle("Avviso")
                                    .setMessage("Corsa non attiva o tempo di caricamento esaurito")
                                    .setPositiveButton("Chiudi", (a,b) -> finish())
                                    .show();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        pDialog.cancel();
                        new AlertDialog.Builder(act)
                                .setTitle("Errore")
                                .setMessage("Errore di connessione. Per favore, controlla la tua connessione e riprova")
                                .setPositiveButton("Chiudi", (a,b) -> finish()).show();
                    });
                }
            } catch (final Exception e) {
                runOnUiThread(() -> {
                    pDialog.cancel();
                    ClassU.OnError(act, e);
                });
                e.printStackTrace();
            }
        }).start();
    }
}

