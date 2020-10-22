package it.emanu37429.atamcorse2.trainsfrag;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.InfoTrenoAdapter;

public class InfoTrenoDaStaz extends BaseActivity {
    RecyclerView mRecyclerView;
    TextView txtTreno, txtRil, txtPart, txtArr, txtOr, txtAvv, txtStato;
    RadioButton rbPart, rbArr;
    ConstraintLayout ll3;
    int i2 = 0;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_treno);
        txtTreno = findViewById(R.id.txtITNTreno);
        txtRil = findViewById(R.id.txtITRilevamento);
        txtOr = findViewById(R.id.txtITOrientamento);
        ll3 = findViewById(R.id.constrlayout);
        txtPart = findViewById(R.id.txtITStazPart);
        txtArr = findViewById(R.id.txtITStazArr);
        txtAvv = findViewById(R.id.txtAvv);
        rbArr = findViewById(R.id.rbITOraArr);
        rbPart = findViewById(R.id.rbITOraPart);
        txtStato = findViewById(R.id.txtITStato);
        pDialog = ProgressDialog.show(this,"Caricamento in corso", "Attendere prego...");
        final Bundle bnd = getIntent().getExtras();
        final Activity act = this;
        new Thread(() -> {
            try {
                final JSONObject andamentoTreno = new JSONObject(ClassU.DownloadString("http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/andamentoTreno/" + bnd.getString("staz") + "/" + bnd.getString("cod")));
                List<String> lstTipo = Arrays.asList("FR", "FB", "FA", "RV", "REG", "ICN", "EN", "IC", "EC", "ES*");
                List<String> lstDef = Arrays.asList("FrecciaRossa - FR", "FrecciaBianca - FB", "FrecciaArgento - FA", "Regionale Veloce - RV", "Regionale - REG", "InterCity Notte - ICN", "EuroNotte ", "InterCity - IC", "EuroCity ", "Freccia ");
                String trenoTp = andamentoTreno.getString("compNumeroTreno");
                for (int i = 0; i < lstTipo.size(); i++) {
                    if (trenoTp.contains(lstTipo.get(i))) {
                        trenoTp = lstDef.get(i) + andamentoTreno.getString("numeroTreno");
                    }
                }
                final String finalTrenoTp = trenoTp;
                act.runOnUiThread(() -> {
                    try {
                        txtTreno.setText(finalTrenoTp);
                        if (!andamentoTreno.isNull("oraUltimoRilevamento"))
                            txtRil.setText("Ultimo rilevamento: " + andamentoTreno.getString("stazioneUltimoRilevamento") + " alle ore " + andamentoTreno.getString("compOraUltimoRilevamento"));
                        else {
                            txtRil.setVisibility(View.GONE);
                            txtOr.setVisibility(View.GONE);
                        }
                        if (!andamentoTreno.isNull("subTitle")) {
                            txtAvv.setVisibility(View.VISIBLE);
                            txtAvv.setText(andamentoTreno.getString("subTitle"));
                        }
                        txtArr.setText(andamentoTreno.getString("destinazione"));
                        rbArr.setText(andamentoTreno.getString("compOrarioArrivo"));
                        txtPart.setText(andamentoTreno.getString("origine"));
                        rbPart.setText(andamentoTreno.getString("compOrarioPartenza"));
                        txtStato.setText("Stato: " + andamentoTreno.getJSONArray("compRitardo").getString(0));
                        ll3.setVisibility(View.VISIBLE);
                        String orientamento = andamentoTreno.getJSONArray("compOrientamento").getString(0);
                        if (!orientamento.equals("--"))
                            txtOr.setText("Orientamento: " + andamentoTreno.getJSONArray("compOrientamento").getString(0));
                        else txtOr.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                JSONArray fermate = andamentoTreno.getJSONArray("fermate");
                final List<ClassU.Stazione> stazioni = new ArrayList<>();
                DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
                for (int i = 0; i < fermate.length(); i++) {
                    JSONObject fermata = fermate.getJSONObject(i);
                    String arrivo = "-";
                    String arrivoreale = "-";
                    String partenza = "-";
                    String partenzareale = "-";
                    String ritardoarrivo = "-";
                    String ritardopartenza = "-";
                    String binario = "-";
                    String binarioreale = "-";
                    boolean pass = false;
                    if (i != 0) {
                        if (!fermata.isNull("arrivo_teorico"))
                            arrivo = formatter.format(new Date(fermata.getLong("arrivo_teorico")));
                        if (!fermata.isNull("arrivoReale")) {
                            arrivoreale = formatter.format(new Date(fermata.getLong("arrivoReale")));
                            pass = true;
                        }
                        ritardoarrivo = fermata.getString("ritardoArrivo");
                        if (!fermata.isNull("binarioProgrammatoArrivoDescrizione"))
                            binario = fermata.getString("binarioProgrammatoArrivoDescrizione");
                        if (!fermata.isNull("binarioEffettivoArrivoDescrizione"))
                            binarioreale = fermata.getString("binarioEffettivoArrivoDescrizione");
                    }
                    if (i != fermate.length() - 1) {
                        partenza = formatter.format(new Date(fermata.optLong("partenza_teorica")));
                        if (!fermata.isNull("partenzaReale")) {
                            partenzareale = formatter.format(new Date(fermata.getLong("partenzaReale")));
                            pass = true;
                        }
                        ritardopartenza = fermata.optString("ritardoPartenza", "-");
                        if (!fermata.isNull("binarioProgrammatoPartenzaDescrizione"))
                            binario = fermata.getString("binarioProgrammatoPartenzaDescrizione");
                        if (!fermata.isNull("binarioEffettivoPartenzaDescrizione"))
                            binarioreale = fermata.optString("binarioEffettivoPartenzaDescrizione");
                    }
                    if (pass) i2 = i;
                    stazioni.add(new ClassU.Stazione(
                            fermata.getString("stazione"),
                            arrivo,
                            arrivoreale,
                            partenza,
                            partenzareale,
                            binario,
                            binarioreale,
                            ritardoarrivo,
                            ritardopartenza,
                            pass,
                            fermata.getString("id"),
                            false
                    ));
                }
                if (andamentoTreno.getBoolean("inStazione")) {
                    ClassU.Stazione st = stazioni.get(0);
                    st.Passato = true;
                    stazioni.set(0, st);
                }
                for (int i = 1; i <= i2; i++) {
                    ClassU.Stazione staz = stazioni.get(i - 1);
                    staz.out = true;
                    staz.Passato = true;
                    stazioni.set(i - 1, staz);
                }
                act.runOnUiThread(() -> {
                    mRecyclerView = findViewById(R.id.recStazioni);
                    mRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(act);
                    llm.setOrientation(RecyclerView.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    mRecyclerView.setNestedScrollingEnabled(false);
                    mRecyclerView.setAdapter(new InfoTrenoAdapter(stazioni));
                    pDialog.dismiss();
                });
            } catch (Exception e) {
                e.printStackTrace();
                act.runOnUiThread(() -> {
                    pDialog.dismiss();
                    Toast.makeText(act, "Errore nel recupero dei dati", Toast.LENGTH_LONG).show();
                });
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
