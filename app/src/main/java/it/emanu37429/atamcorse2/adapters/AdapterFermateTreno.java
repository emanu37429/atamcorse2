package it.emanu37429.atamcorse2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

public class AdapterFermateTreno extends RecyclerView.Adapter<AdapterFermateTreno.FermataTrenoViewHolder> {
    private List<ClassU.TrenoFermata> list;

    public AdapterFermateTreno(List<ClassU.TrenoFermata> list) {
        this.list = list;
        setHasStableIds(true);
    }

    public class FermataTrenoViewHolder extends RecyclerView.ViewHolder {
        RadioButton rbFermata;
        TextView txtarr, txtpart;
        View viewin, viewout;

        public FermataTrenoViewHolder(View view) {
            super(view);
            rbFermata = view.findViewById(R.id.rbStazione);
            txtarr = view.findViewById(R.id.txtOraArr);
            txtpart = view.findViewById(R.id.txtOraPart);
            viewin = view.findViewById(R.id.lineain);
            viewout = view.findViewById(R.id.lineaout);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public FermataTrenoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_fermate_treno, parent, false);
        return new FermataTrenoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FermataTrenoViewHolder holder, int i) {
        final ClassU.TrenoFermata fermata = list.get(i);
        holder.rbFermata.setText(fermata.Fermata);
        holder.txtarr.setText(fermata.OraArr);
        holder.txtpart.setText(fermata.OraPart);
        if (fermata.type == 0)
            holder.viewin.setVisibility(View.GONE);
        if (fermata.type == list.size() - 1)
            holder.viewout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
