package it.emanu37429.atamcorse2.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.trainsfrag.FragmentCercaDaAInput;

public class AdapterPrefOrari extends RecyclerView.Adapter<AdapterPrefOrari.PrefViewHolder> {
    private List<ClassU.PreferenzaTreno> list;

    public AdapterPrefOrari(List<ClassU.PreferenzaTreno> list) {
        this.list = list;
    }

    public class PrefViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtda, txta;
        CardView cd;

        public PrefViewHolder(View view) {
            super(view);
            txta = view.findViewById(R.id.txtPrefArr);
            txtda = view.findViewById(R.id.txtPrefPart);
            cd = view.findViewById(R.id.cdpref);
        }
    }

    @Override
    public PrefViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_cercadaapref, parent, false);
        return new PrefViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PrefViewHolder holder, int i) {
        final ClassU.PreferenzaTreno pref = list.get(i);
        holder.txtda.setText(pref.stazioneda);
        holder.txta.setText(pref.stazionea);
        holder.cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentCercaDaAInput.selecta.setText(pref.stazionea);
                FragmentCercaDaAInput.selectda.setText(pref.stazioneda);
                FragmentCercaDaAInput.selectda.dismissDropDown();
                FragmentCercaDaAInput.selecta.dismissDropDown();
                FragmentCercaDaAInput.selectda.setCompoundDrawablesWithIntrinsicBounds(null,null, FragmentCercaDaAInput.img, null);
                FragmentCercaDaAInput.selecta.setCompoundDrawablesWithIntrinsicBounds(null,null, FragmentCercaDaAInput.img, null);
                Log.d("a", pref.sta + " - " + pref.stda);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
