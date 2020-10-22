package it.emanu37429.atamcorse2.atamcorse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.AdapterCercaCorse;

public class CercaBusDaA extends BaseActivity {

    ProgressDialog pDialog;
    List<ClassU.BusPercorso> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_bus_da);
        Bundle bnd = getIntent().getExtras();
        String idpart = bnd.getString("ferpart");
        String idarr = bnd.getString("ferarr");
        String dt = bnd.getString("dt");
        String url = ClassU.linkCercaPercorso + "?idfpart=" + idpart + "&idfarr=" + idarr + "&dt=" + dt;
        pDialog = ProgressDialog.show(this,"Caricamento in corso", "Attendere prego...");
        final Activity act = this;
        new Thread(() -> {
            try {
                String str = ClassU.DownloadString(url);
                if(str!=null && str.length() > 20) {
                    String[] buses = str.split("\\n");
                    for (int i = 0; i < buses.length; i++) {
                        String[] bus = buses[i].split(";");
                        list.add(new ClassU.BusPercorso(bus[0], bus[1], bus[2], bus[3], bus[4], bus[5]));
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
                        Collections.sort(list, ((x, y) -> {
                            try {
                                return sdf.parse(x.oraFerPart).compareTo(sdf.parse(y.oraFerPart));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }));
                    runOnUiThread(() -> {
                        RecyclerView mRecyclerView = findViewById(R.id.recCorseBus);
                        mRecyclerView.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        llm.setOrientation(RecyclerView.VERTICAL);
                        mRecyclerView.setLayoutManager(llm);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                        mRecyclerView.addItemDecoration(dividerItemDecoration);
                        mRecyclerView.setAdapter(new AdapterCercaCorse(list));
                        pDialog.cancel();
                    });
                } else{
                    runOnUiThread(() -> {
                        pDialog.cancel();
                        AlertDialog diag = new AlertDialog.Builder(act)
                                .setTitle("Avviso")
                                .setMessage("Non sono presenti autobus diretti tra le due fermate inserite")
                                .setPositiveButton("Chiudi", (a,b) -> finish()).setCancelable(false).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> pDialog.cancel());
            }
        }).start();
    }
}
