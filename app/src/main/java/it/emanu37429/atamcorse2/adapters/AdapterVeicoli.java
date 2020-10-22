package it.emanu37429.atamcorse2.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

public class AdapterVeicoli extends RecyclerView.Adapter<AdapterVeicoli.VeicoliViewHolder> implements Filterable {
    private List<ClassU.Veicolo> list, lstall;

    public AdapterVeicoli(List<ClassU.Veicolo> list) {
        this.list = list;
        lstall = list;
    }

    public class VeicoliViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtid, txtmod, txtstato;

        public VeicoliViewHolder(View view) {
            super(view);
            txtid = view.findViewById(R.id.txtNumlinea);
            txtmod = view.findViewById(R.id.txtNomlinea);
            txtstato = view.findViewById(R.id.txtStatoVec);
        }
    }

    @Override
    public VeicoliViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_veicoli, parent, false);
        return new VeicoliViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VeicoliViewHolder holder, int i) {
        final ClassU.Veicolo veicolo = list.get(i);
        holder.txtid.setText(veicolo.id);
        holder.txtmod.setText(veicolo.mod);
        holder.txtstato.setText(veicolo.stat);
        if(veicolo.stat.equals("S"))
            holder.txtstato.setTextColor(Color.RED);
        else if(veicolo.stat.equals("A")) holder.txtstato.setTextColor(Color.parseColor("#009900"));
        else if (veicolo.stat.equals("D")) holder.txtstato.setTextColor(Color.parseColor("#003366"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    list = lstall;
                } else {
                    ArrayList<ClassU.Veicolo> filteredList = new ArrayList<>();
                    for (ClassU.Veicolo veicolo : lstall) {

                        if (veicolo.mod.toLowerCase().contains(charString) || veicolo.id.contains(charString)) {
                            filteredList.add(veicolo);
                        }
                    }
                    list = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<ClassU.Veicolo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
