package it.emanu37429.atamcorse2.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.atamcorse.GetCorseActivity;

public class LineeAdapter extends RecyclerView.Adapter<LineeAdapter.LineeViewHolder> {
    private List<ClassU.Linea> list;
    String id;

    public LineeAdapter(List<ClassU.Linea> list) {
        this.list = list;
    }

    public class LineeViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtLinea, txtNome;
        CardView cd;

        public LineeViewHolder(View view) {
            super(view);
            txtLinea = view.findViewById(R.id.txtNumlinea);
            txtNome = view.findViewById(R.id.txtNomlinea);
            cd = view.findViewById(R.id.cardLinea);
        }
    }

    @Override
    public LineeViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_linee, parent, false);
        return new LineeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LineeViewHolder holder, int i) {
        final ClassU.Linea linea = list.get(i);
        holder.txtNome.setText(linea.nome);
        holder.txtLinea.setText(linea.num);
        holder.cd.setOnClickListener(view -> {
            Intent i1 = new Intent(view.getContext(), GetCorseActivity.class);
            i1.putExtra("id", linea.id);
            view.getContext().startActivity(i1);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
