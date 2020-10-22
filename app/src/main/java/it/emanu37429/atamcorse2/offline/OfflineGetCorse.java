package it.emanu37429.atamcorse2.offline;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.OfflineCorseAdapter;

public class OfflineGetCorse extends BaseActivity {
    Boolean festivo = false;
    ArrayList<ClassU.Corsa> listacorse = new ArrayList<>();
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_corse);
        setTitle("ATAM Corse (offline)");
        pDialog = ProgressDialog.show(this,"Caricamento in corso", "Attendere prego...");
        final String id = getIntent().getExtras().getString("id");
        final String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        new Thread(() -> {
            try {
                JSONArray date = new JSONArray(ClassU.StringFromResource(getApplicationContext(), R.raw.dates));
                String today = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(Calendar.getInstance().getTime());
                for (int i = 0; i < date.length(); i++) {
                    if (date.getString(i).equals(today))
                        festivo = true;
                }
                JSONArray corse = new JSONArray(ClassU.StringFromResource(getApplicationContext(), R.raw.offlinecorse));
                for (int i = 0; i < corse.length(); i++) {
                    JSONObject obj = corse.getJSONObject(i);
                    String cadenzap = obj.getString("cadenzacod");
                    if (obj.getString("linea").equals(id)) {
                        if ((!day.equals("Sunday") && !day.equals("Saturday")) && (cadenzap.equals("0ANNFER6") || cadenzap.equals("0SCOFER6") || cadenzap.equals("00NSFER6") || cadenzap.equals("0SNSFER6") || cadenzap.equals("00B1FER6") || cadenzap.equals("0ANN0010")) && !festivo)
                            listacorse.add(new ClassU.Corsa(obj.getString("oraArrivo"), obj.getString("oraPartenza"), obj.getString("partenza"), obj.getString("capolinea"), obj.getString("idCorsa"), null));
                        if ((day.equals("Sunday") && (cadenzap.equals("0ANN0FES") || cadenzap.equals("0ANN0067"))) && !festivo)
                            listacorse.add(new ClassU.Corsa(obj.getString("oraArrivo"), obj.getString("oraPartenza"), obj.getString("partenza"), obj.getString("capolinea"), obj.getString("idCorsa"), null));
                        if (day.equals("Saturday") && (cadenzap.equals("0ANNFER6") || cadenzap.equals("0SCOFER6") || cadenzap.equals("00NSFER6") || cadenzap.equals("0SNSFER6") || cadenzap.equals("00B1FER6") || cadenzap.equals("0ANN0067") || cadenzap.equals("0ANN00SA")) && !festivo)
                            listacorse.add(new ClassU.Corsa(obj.getString("oraArrivo"), obj.getString("oraPartenza"), obj.getString("partenza"), obj.getString("capolinea"), obj.getString("idCorsa"), null));
                        if (cadenzap.equals("0ANN0FES") && festivo)
                            listacorse.add(new ClassU.Corsa(obj.getString("oraArrivo"), obj.getString("oraPartenza"), obj.getString("partenza"), obj.getString("capolinea"), obj.getString("idCorsa"), null));
                    }
                }
                runOnUiThread(() -> {
                    RecyclerView mRecyclerView = findViewById(R.id.recCorse);
                    mRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                    llm.setOrientation(RecyclerView.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                    mRecyclerView.addItemDecoration(dividerItemDecoration);
                    mRecyclerView.setAdapter(new OfflineCorseAdapter(listacorse));
                    pDialog.cancel();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
