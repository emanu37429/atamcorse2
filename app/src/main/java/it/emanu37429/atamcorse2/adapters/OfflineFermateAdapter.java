package it.emanu37429.atamcorse2.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.offline.OfflineGetPalina;

public class OfflineFermateAdapter extends RecyclerView.Adapter<OfflineFermateAdapter.FermateViewHolder> implements Filterable {
    private List<ClassU.Fermata> list;
    private List<ClassU.Fermata> lstall;

    public OfflineFermateAdapter(List<ClassU.Fermata> list) {
        this.list = list;
        lstall = list;
    }

    public class FermateViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtLinea, txtNome;
        CardView cd;

        public FermateViewHolder(View view) {
            super(view);
            txtLinea = view.findViewById(R.id.txtNumlinea);
            txtNome = view.findViewById(R.id.txtNomlinea);
            cd = view.findViewById(R.id.cardLinea);
        }
    }

    @Override
    public FermateViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_linee, parent, false);
        return new FermateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FermateViewHolder holder, int i) {
        final ClassU.Fermata fermata = list.get(i);
        holder.txtNome.setText(fermata.nome);
        holder.txtLinea.setText(fermata.num);
        holder.cd.setOnClickListener(view -> {
            Intent i1 = new Intent(holder.cd.getContext(), OfflineGetPalina.class);
            i1.putExtra("id", fermata.id);
            view.getContext().startActivity(i1);
        });
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
                    ArrayList<ClassU.Fermata> filteredList = new ArrayList<>();
                    for (ClassU.Fermata fermata : lstall) {

                        if (fermata.nome.toLowerCase().contains(charString) || fermata.id.toLowerCase().contains(charString)) {
                            filteredList.add(fermata);
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
                list = (ArrayList<ClassU.Fermata>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
