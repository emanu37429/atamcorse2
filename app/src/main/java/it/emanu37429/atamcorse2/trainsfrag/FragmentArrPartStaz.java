package it.emanu37429.atamcorse2.trainsfrag;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.TreniStazioneAdapter;

public class FragmentArrPartStaz extends Fragment {


    public FragmentArrPartStaz() {
    }

    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fragment_arr_part_staz, container, false);
        pDialog = ProgressDialog.show(getContext(),"Caricamento in corso", "Attendere prego...");
        final Bundle bnd = getArguments();
        new Thread(() -> {
            String url = bnd.getString("url").replace(" ", "%20");
            String bin = bnd.getString("bin");
            String binre = bnd.getString("binre");
            String origine = bnd.getString("origine");
            String dp = bnd.getString("dp");
            final List<StazioneArriviPartenze.TrenoDaStazione> list = new ArrayList<>();
            List<String> lstTipo = Arrays.asList("FR", "FB", "FA", "RV", "REG", "ICN", "EN", "IC", "EC", "ES*");
            List<String> lstDef = Arrays.asList("FrecciaRossa - FR", "FrecciaBianca - FB", "FrecciaArgento - FA", "Regionale Veloce - RV", "Regionale - REG", "InterCity Notte - ICN", "EuroNotte ", "InterCity - IC", "EuroCity ", "Freccia ");
            try {
                Log.d("url", url);
                JSONArray array = new JSONArray(ClassU.DownloadString(url));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject treno = array.getJSONObject(i);
                    String trenost, binrt, binn;
                    trenost = binrt = binn = "-";
                    for (int i2 = 0; i2 < lstTipo.size(); i2++) {
                        if (treno.getString("compNumeroTreno").contains(lstTipo.get(i2))) {
                            trenost = lstDef.get(i2) + treno.getString("numeroTreno");
                            break;
                        }
                    }
                    if (!treno.isNull(binre))
                        binrt = treno.getString(binre);
                    if (!treno.isNull(bin))
                        binn = treno.getString(bin);
                    String stato = "Stato: ";
                    if(dp.equals("Per: ") && !treno.getJSONArray("compInStazionePartenza").getString(0).equals(""))
                        stato += "Partito, ";
                    else if(dp.equals("Da: ") && treno.getBoolean("inStazione"))
                        stato += "Arrivato, ";
                    list.add(new StazioneArriviPartenze.TrenoDaStazione(
                            dp + treno.getString(origine),
                            trenost,
                            treno.getString(bnd.getString("ora")),
                            binn,
                            binrt,
                            treno.getBoolean("circolante"),
                            treno.getJSONArray("compOrientamento").getString(0),
                            treno.optString("compClassRitardoLine"),
                            treno.optString("numeroTreno"),
                            treno.getString("codOrigine"),
                            stato + treno.getJSONArray("compRitardo").getString(0)
                    ));
                }
                getActivity().runOnUiThread(() -> {
                    RecyclerView mRecyclerView = v.findViewById(R.id.recStazioniArrPart);
                    mRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(RecyclerView.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                    mRecyclerView.addItemDecoration(dividerItemDecoration);
                    mRecyclerView.setAdapter(new TreniStazioneAdapter(list));
                    pDialog.dismiss();
                });
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

}
