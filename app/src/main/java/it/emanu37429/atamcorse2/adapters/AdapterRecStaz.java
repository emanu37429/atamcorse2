package it.emanu37429.atamcorse2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.trainsfrag.FragmentInputStazione;

public class AdapterRecStaz extends RecyclerView.Adapter<AdapterRecStaz.RecStazViewHolder> {
    private List<ClassU.RecStazione> list;

    public AdapterRecStaz(List<ClassU.RecStazione> list) {
        this.list = list;
    }

    public class RecStazViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtst;
        CardView cd;

        public RecStazViewHolder(View view) {
            super(view);
            txtst = view.findViewById(R.id.stazioneRec);
            cd = view.findViewById(R.id.cdRecPref);
        }
    }

    @Override
    public RecStazViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_rec_stazioni, parent, false);
        return new RecStazViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecStazViewHolder holder, int i) {
        final ClassU.RecStazione stazione = list.get(i);
        holder.txtst.setText(stazione.stazione);
        holder.cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentInputStazione.inputStazione.setText(stazione.stazione);
                FragmentInputStazione.inputStazione.dismissDropDown();
                FragmentInputStazione.inputStazione.setCompoundDrawablesWithIntrinsicBounds(null, null, FragmentInputStazione.img, null);
                FragmentInputStazione.idStazione = stazione.cod;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<ClassU.RecStazione> lst) {
        this.list = lst;
        notifyDataSetChanged();
    }
}
