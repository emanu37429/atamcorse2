package it.emanu37429.atamcorse2.trainsfrag;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.TrenoAdapter;

public class CercaTrenoDaA extends BaseActivity {

    private RecyclerView mRecyclerView;
    ProgressDialog pDialog;
    List<ClassU.TrenoPercorsoTre> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_treno_da);
        Bundle extras = getIntent().getExtras();
        final String urlTI = ("https://www.lefrecce.it/msite/api/solutions?origin=" + extras.getString("part")
                + "&destination=" + extras.getString("arr")
                + "&arflag=A&adate=" + extras.getString("data")
                + "&atime=" + extras.getString("ora")
                + "&adultno=1&childno=0&direction=A&frecce=false&onlyRegional=false").replace(" ", "%20");
        pDialog = ProgressDialog.show(this,"Caricamento in corso", "Attendere prego...");
        mRecyclerView = findViewById(R.id.rectreni);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ITALY);
        final AppCompatActivity act = this;
        new Thread(() -> {
            try {
                Connection conn = Jsoup.connect(urlTI);
                Connection.Response res = conn.method(Connection.Method.GET).ignoreContentType(true).execute();
                String str = res.body();
                if (str.length() > 10) {
                    JSONArray array01 = new JSONArray(str);
                    for (int i = 0; i < array01.length(); i++) {
                        JSONObject soluzione = array01.getJSONObject(i);
                        JSONArray infosoluzione = new JSONArray(Jsoup.connect("https://www.lefrecce.it/msite/api/solutions/" + soluzione.getString("idsolution") + "/info").cookies(res.cookies()).ignoreContentType(true).execute().body());
                        ClassU.TrenoPercorsoF2[] soluz = new ClassU.TrenoPercorsoF2[infosoluzione.length()];
                        for (int i2 = 0; i2 < infosoluzione.length(); i2++) {
                            JSONObject object = infosoluzione.getJSONObject(i2);
                            String idf = object.getString("trainidentifier");
                            if(idf.equals("UrbV")) idf="Percorso con mezzi urbani";
                            else if (idf.equals("Walk")) idf = "Percorso a piedi";
                            soluz[i2] = new ClassU.TrenoPercorsoF2(idf,
                                    object.getString("departurestation"),
                                    object.getLong("departuretime"),
                                    object.getString("arrivalstation"),
                                    object.getLong("arrivaltime"));
                        }
                        list.add(new ClassU.TrenoPercorsoTre(soluzione.getString("idsolution"),soluz));
                    }
                    act.runOnUiThread(() -> {
                        mRecyclerView.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(act);
                        llm.setOrientation(RecyclerView.VERTICAL);
                        mRecyclerView.setLayoutManager(llm);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                        mRecyclerView.addItemDecoration(dividerItemDecoration);
                        TrenoAdapter adapter = new TrenoAdapter(list);
                        adapter.datastart = extras.getString("data");
                        adapter.cookies = res.cookies();
                        mRecyclerView.setAdapter(adapter);
                        TrenoInfoActivity.parentact = act;
                        pDialog.cancel();
                    });
                } else {
                    act.runOnUiThread(() -> {
                        pDialog.cancel();
                        Toast.makeText(act, "Errore nel recupero delle soluzioni", Toast.LENGTH_LONG).show();
                        finish();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                act.runOnUiThread(() -> {
                    pDialog.cancel();
                    Toast.makeText(act, "Errore nel recupero delle soluzioni", Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        }).start();
    }
}
