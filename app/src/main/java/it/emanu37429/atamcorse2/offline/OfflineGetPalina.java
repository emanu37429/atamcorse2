package it.emanu37429.atamcorse2.offline;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.OfflineFermataAdapter;

public class OfflineGetPalina extends BaseActivity {
    ProgressDialog pDialog;
    List list = new ArrayList<ClassU.FermataCorsaP>();
    Boolean festivo = false;
    Long now = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_palina);
        final String id = getIntent().getExtras().getString("id");
        pDialog = ProgressDialog.show(this,"Caricamento in corso", "Attendere prego...");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ITALIAN);
        Calendar cl = Calendar.getInstance();
        try {
            now = sdf.parse(cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":00").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final AppCompatActivity act = this;
        final String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        new Thread(() -> {
            try {
                JSONArray date = new JSONArray(ClassU.StringFromResource(getApplicationContext(), R.raw.dates));
                String today = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(Calendar.getInstance().getTime());
                for (int i = 0; i < date.length(); i++) {
                    if (date.getString(i).equals(today)) {
                        festivo = true;
                        break;
                    }
                }
                JSONArray corse = new JSONArray(ClassU.StringFromResource(act, R.raw.offlinecorse));
                Map corsalinea = new HashMap<String, String>();
                for (int i = 0; i < corse.length(); i++) {
                    JSONObject obj = corse.getJSONObject(i);
                    String idlinea = obj.getString("linea");
                    String cadenzap = obj.getString("cadenzacod");
                    switch (idlinea) {
                        case "107109":
                            idlinea = "107/109";
                            break;
                        case "123124":
                            idlinea="123/124";
                            break;
                        case "121122":
                            idlinea="121/122";
                            break;
                        case "05-ott":
                            idlinea="5/10";
                            break;
                    }
                    if ((!day.equals("Sunday") && !day.equals("Saturday")) && (cadenzap.equals("0ANNFER6") || cadenzap.equals("0SCOFER6") || cadenzap.equals("00NSFER6") || cadenzap.equals("0SNSFER6") || cadenzap.equals("00B1FER6") || cadenzap.equals("0ANN0010")) && !festivo)
                        corsalinea.put(obj.getString("idCorsa"), idlinea + " - " + obj.getString("capolinea"));
                    if ((day.equals("Sunday") && (cadenzap.equals("0ANN0FES") || cadenzap.equals("0ANN0067"))) && !festivo)
                        corsalinea.put(obj.getString("idCorsa"), idlinea + " - " + obj.getString("capolinea"));
                    if (day.equals("Saturday") && (cadenzap.equals("0ANNFER6") || cadenzap.equals("0SCOFER6") || cadenzap.equals("00NSFER6") || cadenzap.equals("0SNSFER6") || cadenzap.equals("00B1FER6") || cadenzap.equals("0ANN0067") || cadenzap.equals("0ANN00SA")) && !festivo)
                        corsalinea.put(obj.getString("idCorsa"), idlinea + " - " + obj.getString("capolinea"));
                    if (cadenzap.equals("0ANN0FES") && festivo)
                        corsalinea.put(obj.getString("idCorsa"), idlinea + " - " + obj.getString("capolinea"));
                }
                JSONArray dts = OfflineActivity.getObjOffline(getApplicationContext());
                for (int i = 0; i < dts.length(); i++) {
                    JSONObject obj = dts.getJSONObject(i);
                    if (obj.getString("idPalina").equals(id) && corsalinea.containsKey(obj.getString("idCorsa")) && sdf.parse(obj.getString("oraPassaggio")).getTime() > now) {
                        list.add(new ClassU.FermataCorsaP(obj.getString("idCorsa"), ((String) corsalinea.get(obj.getString("idCorsa"))), obj.getString("oraPassaggio"), null, null, null, act));
                    }
                }
                Collections.sort(list, (Comparator<ClassU.FermataCorsaP>) (o1, o2) -> {
                    try {
                        return sdf.parse(o1.ora).compareTo(sdf.parse(o2.ora));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                });
                runOnUiThread(() -> {
                    RecyclerView mRecyclerView = findViewById(R.id.recCorsePalina);
                    mRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                    llm.setOrientation(RecyclerView.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                    mRecyclerView.addItemDecoration(dividerItemDecoration);
                    mRecyclerView.setAdapter(new OfflineFermataAdapter(list));
                    pDialog.cancel();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> pDialog.cancel());
            }
        }).start();
    }
}
