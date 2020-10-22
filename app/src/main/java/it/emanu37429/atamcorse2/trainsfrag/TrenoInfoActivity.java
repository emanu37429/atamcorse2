package it.emanu37429.atamcorse2.trainsfrag;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.AdapterTrenoInfo;

public class TrenoInfoActivity extends BaseActivity {
    public static Map mapcookies;
    public static AppCompatActivity parentact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treno_info);
        String urlsol = "https://www.lefrecce.it/msite/api/solutions/" + getIntent().getExtras().getString("idsol") + "/details";
        Activity act = this;
        final ProgressDialog pDialog = ProgressDialog.show(this,"Caricamento in corso", "Attendere prego...");
        RecyclerView mRecyclerView = findViewById(R.id.recInfoTreno);
        List<ClassU.TrenoInfo> treninfo = new ArrayList<>();
        new Thread(() -> {
            try {
                JSONArray infosoluzione = new JSONArray(Jsoup.connect(urlsol).cookies(mapcookies).ignoreContentType(true).execute().body());
                for (int i = 0; i < infosoluzione.length(); i++) {
                    JSONObject soluzione = infosoluzione.getJSONObject(i);
                    List<ClassU.TrenoFermata> fermate = new ArrayList<>();
                    JSONArray fermatejson = soluzione.getJSONArray("stoplist");
                    List<String> imgs = new ArrayList<>();
                    for (int i2 = 0; i2 < fermatejson.length(); i2++) {
                        JSONObject fermata = fermatejson.getJSONObject(i2);
                        String arrivo, partenza;
                        arrivo = partenza = " - ";
                        if(!fermata.isNull("arrivaltime"))
                            arrivo = fermata.getString("arrivaltime").split("T")[1].substring(0, 5);
                        if(!fermata.isNull("departuretime"))
                            partenza = fermata.getString("departuretime").split("T")[1].substring(0, 5);
                        fermate.add(new ClassU.TrenoFermata(
                                fermata.getString("stationname"),
                                arrivo,
                                partenza,
                                i2
                        ));
                    }
                    JSONArray imgsjson = soluzione.getJSONArray("servicelist");
                    for (int i2 = 0; i2 < imgsjson.length(); i2++)
                        imgs.add(imgsjson.getJSONObject(i2).getString("imagedata"));
                    treninfo.add(new ClassU.TrenoInfo(
                            soluzione.getString("trainidentifier"),
                            imgs,
                            fermate
                    ));
                }
                act.runOnUiThread(() -> {
                    mRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(act);
                    llm.setOrientation(RecyclerView.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                    mRecyclerView.addItemDecoration(dividerItemDecoration);
                    mRecyclerView.setAdapter(new AdapterTrenoInfo(treninfo));
                    new Handler().postDelayed(() -> mRecyclerView.scrollToPosition(0), 1000);
                });

                pDialog.dismiss();
            } catch (Exception e) {
                act.runOnUiThread(() -> {
                    e.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(act, "Sessione scaduta. Effettuare una nuova ricerca", Toast.LENGTH_LONG).show();
                    parentact.finish();
                    finish();
                });
            }
        }).start();
    }
}
