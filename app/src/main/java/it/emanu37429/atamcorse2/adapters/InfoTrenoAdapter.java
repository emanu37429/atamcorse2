package it.emanu37429.atamcorse2.adapters;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.trainsfrag.StazioneArriviPartenze;

public class InfoTrenoAdapter extends RecyclerView.Adapter<InfoTrenoAdapter.InfoViewHolder> {

    private List<ClassU.Stazione> list;


    public InfoTrenoAdapter(List<ClassU.Stazione> list) {
        this.list = list;
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtPartPrev, txtArrPrev, txtBinPrev, txtPartRe, txtBinRe, txtArrRe, txtStazione;
        protected RadioButton rbStazione;
        View viewin, viewout;

        public InfoViewHolder(View view) {
            super(view);
            rbStazione = view.findViewById(R.id.rbStazione);
            txtStazione = view.findViewById(R.id.txtStazionealtRb);
            txtArrPrev = view.findViewById(R.id.txtArrPrev);
            txtArrRe = view.findViewById(R.id.txtArrRe);
            txtBinPrev = view.findViewById(R.id.txtBinPrev);
            txtBinRe = view.findViewById(R.id.txtBinRe);
            txtPartPrev = view.findViewById(R.id.txtPartPrev);
            txtPartRe = view.findViewById(R.id.txtPartRe);
            viewin = view.findViewById(R.id.viewin);
            viewout = view.findViewById(R.id.viewout);
        }
    }

    @Override
    public InfoViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righelststazionitreno, parent, false);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        };
        itemView.setOnClickListener(listener);
        return new InfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int i) {
        final ClassU.Stazione stazione = list.get(i);
        holder.txtStazione.setText(stazione.stazione);
        holder.txtStazione.setPaintFlags(holder.rbStazione.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.txtStazione.setOnClickListener(view -> {
            Intent act = new Intent(view.getContext(), StazioneArriviPartenze.class);
            act.putExtra("id", stazione.idstazione);
            view.getContext().startActivity(act);
        });
        holder.txtPartRe.setText(stazione.partenzareale);
        holder.txtPartPrev.setText(stazione.partenza);
        holder.txtBinRe.setText(stazione.binarioreale);
        holder.txtBinPrev.setText(stazione.binario);
        holder.txtArrRe.setText(stazione.arrivoreale);
        holder.txtArrPrev.setText(stazione.arrivo);
        holder.rbStazione.setClickable(stazione.Passato);
        if (stazione.Passato) {
            holder.rbStazione.setChecked(true);
            if (stazione.out) holder.viewout.setVisibility(View.VISIBLE);
            if (i > 0)
                holder.viewin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
