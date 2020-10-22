package it.emanu37429.atamcorse2.trainsfrag;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.AdapterTreniSelect;
import it.emanu37429.atamcorse2.adapters.InfoTrenoAdapter;

public class FragmentInfoTreno extends Fragment {


    public FragmentInfoTreno() {
    }

    RecyclerView mRecyclerView;
    TextView txtTreno, txtRil, txtPart, txtArr, txtOr, txtAvv, txtStato;
    RadioButton rbPart, rbArr;
    View v;
    ConstraintLayout ll3;
    int i2 = 0;
    public AlertDialog alert;
    ProgressDialog pDialog;
    List<String> lstTipo = Arrays.asList("FR", "FB", "FA", "RV", "REG", "ICN", "EN", "IC", "EC", "ES*");
    List<String> lstDef = Arrays.asList("FrecciaRossa - FR", "FrecciaBianca - FB", "FrecciaArgento - FA", "Regionale Veloce - RV", "Regionale - REG", "InterCity Notte - ICN", "EuroNotte ", "InterCity - IC", "EuroCity ", "Freccia ");
    FragmentInfoTreno fr = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_info_treno, container, false);
        final String CodTreno = getArguments().getString("Cod");
        txtTreno = v.findViewById(R.id.txtITNTreno);
        txtRil = v.findViewById(R.id.txtITRilevamento);
        txtPart = v.findViewById(R.id.txtITStazPart);
        txtArr = v.findViewById(R.id.txtITStazArr);
        txtAvv = v.findViewById(R.id.txtAvv);
        rbArr = v.findViewById(R.id.rbITOraArr);
        rbPart = v.findViewById(R.id.rbITOraPart);
        txtOr = v.findViewById(R.id.txtITOrientamento);
        txtStato = v.findViewById(R.id.txtITStato);
        ll3 = v.findViewById(R.id.constrlayout);
        pDialog = ProgressDialog.show(getContext(),"Caricamento in corso", "Attendere prego...");
        final List<ClassU.TrenoStzPartN> list2 = new ArrayList<>();
        final RecyclerView rec0 = new RecyclerView(getContext());
        new Thread(() -> {
            try {
                final String[] ntreni = ClassU.DownloadString(ClassU.linkViaggiaTreno + "cercaNumeroTrenoTrenoAutocomplete/" + CodTreno).trim().split("\n");
                if (ntreni.length > 1) {
                    try {
                        for (int i = 0; i < ntreni.length; i++) {
                            String[] trenoS = ntreni[i].split("\\|")[1].split("-");
                            JSONObject objTreno = new JSONObject(ClassU.DownloadString(ClassU.linkViaggiaTreno + "andamentoTreno/" + trenoS[1] + "/" + CodTreno));
                            String trenoTp = objTreno.getString("compNumeroTreno");
                            for (int i2 = 0; i2 < lstTipo.size(); i2++) {
                                if (trenoTp.contains(lstTipo.get(i2))) {
                                    trenoTp = lstDef.get(i2) + objTreno.getString("numeroTreno");
                                }
                            }
                            list2.add(new ClassU.TrenoStzPartN(
                                    trenoS[0],
                                    trenoS[1],
                                    objTreno.getString("origine"),
                                    objTreno.getString("destinazione"),
                                    objTreno.getString("compOrarioPartenza"),
                                    objTreno.getString("compOrarioArrivo"),
                                    objTreno.getString("compDurata"),
                                    trenoTp,
                                    fr,
                                    objTreno
                            ));
                        }
                        getActivity().runOnUiThread(() -> {
                            rec0.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(getContext());
                            llm.setOrientation(RecyclerView.VERTICAL);
                            rec0.setLayoutManager(llm);
                            rec0.setNestedScrollingEnabled(false);
                            rec0.setAdapter(new AdapterTreniSelect(list2));
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Seleziona treno");
                            builder.setView(rec0);
                            pDialog.dismiss();
                            alert = builder.show();
                        });
                    } catch (Exception e) {
                    }
                } else if (!ntreni[0].equals("")) {
                    String[] tp0 = ntreni[0].split("\\|")[1].split("-");
                    All(tp0[0], tp0[1]);
                }
                else {
                    getActivity().runOnUiThread(() -> {
                        pDialog.dismiss();
                        Toast.makeText(getContext(), "Treno inesistente o non attivo oggi.", Toast.LENGTH_LONG).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "Errore nel recupero dei dati", Toast.LENGTH_LONG).show();
                });
            }
        }).start();
        return v;
    }

    void All(String cod, String stz) {
        try {
            getActivity().runOnUiThread(() -> {
                if (!pDialog.isShowing()) pDialog.show();
            });
            final JSONObject andamentoTreno = new JSONObject(ClassU.DownloadString( ClassU.linkViaggiaTreno + "andamentoTreno/" + stz + "/" + cod));
            String trenoTp = andamentoTreno.getString("compNumeroTreno");
            for (int i = 0; i < lstTipo.size(); i++) {
                if (trenoTp.contains(lstTipo.get(i))) {
                    trenoTp = lstDef.get(i) + andamentoTreno.getString("numeroTreno");
                }
            }
            final String finalTrenoTp = trenoTp;
            getActivity().runOnUiThread(() -> {
                try {
                    txtTreno.setText(finalTrenoTp);
                    if (!andamentoTreno.isNull("oraUltimoRilevamento"))
                        txtRil.setText("Ultimo rilevamento: " + andamentoTreno.getString("stazioneUltimoRilevamento") + " alle ore " + andamentoTreno.getString("compOraUltimoRilevamento"));
                    else txtRil.setVisibility(View.GONE);
                    if (!andamentoTreno.isNull("subTitle")) {
                        txtAvv.setVisibility(View.VISIBLE);
                        txtAvv.setText(andamentoTreno.getString("subTitle"));
                    }
                    txtStato.setText("Stato: " + andamentoTreno.getJSONArray("compRitardo").getString(0));
                    txtArr.setText(andamentoTreno.getString("destinazione"));
                    rbArr.setText(andamentoTreno.getString("compOrarioArrivo"));
                    txtPart.setText(andamentoTreno.getString("origine"));
                    rbPart.setText(andamentoTreno.getString("compOrarioPartenza"));
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
            getActivity().runOnUiThread(() -> {
                mRecyclerView = v.findViewById(R.id.recStazioni);
                mRecyclerView.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(llm);
                mRecyclerView.setNestedScrollingEnabled(false);
                mRecyclerView.setAdapter(new InfoTrenoAdapter(stazioni));
                pDialog.dismiss();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Allplus(final JSONObject andamentoTreno) {
        try {
            String trenoTp = andamentoTreno.getString("compNumeroTreno");
            for (int i = 0; i < lstTipo.size(); i++) {
                if (trenoTp.contains(lstTipo.get(i))) {
                    trenoTp = lstDef.get(i) + andamentoTreno.getString("numeroTreno");
                }
            }
            final String finalTrenoTp = trenoTp;
            getActivity().runOnUiThread(() -> {
                try {
                    txtTreno.setText(finalTrenoTp);
                    if (!andamentoTreno.isNull("oraUltimoRilevamento"))
                        txtRil.setText("Ultimo rilevamento: " + andamentoTreno.getString("stazioneUltimoRilevamento") + " alle ore " + andamentoTreno.getString("compOraUltimoRilevamento"));
                    else txtRil.setVisibility(View.GONE);
                    txtArr.setText(andamentoTreno.getString("destinazione"));
                    if (!andamentoTreno.isNull("subTitle")) {
                        txtAvv.setVisibility(View.VISIBLE);
                        txtAvv.setText(andamentoTreno.getString("subTitle"));
                    }
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
                    if(!andamentoTreno.isNull("arrivo_teorico"))
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
            getActivity().runOnUiThread(() -> {
                mRecyclerView = v.findViewById(R.id.recStazioni);
                mRecyclerView.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(llm);
                mRecyclerView.setNestedScrollingEnabled(false);
                mRecyclerView.setAdapter(new InfoTrenoAdapter(stazioni));
                pDialog.dismiss();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}