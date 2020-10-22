package it.emanu37429.atamcorse2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

public class AdapterChangelog  extends RecyclerView.Adapter<AdapterChangelog.ChangelogViewHolder> {
    private List<ClassU.Versione> list;
    String id;

    public AdapterChangelog(List<ClassU.Versione> list) {
        this.list = list;
    }

    public class ChangelogViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtdata, txttitolo;

        public ChangelogViewHolder(View view) {
            super(view);
            txtdata = view.findViewById(R.id.txtNewsdata);
            txttitolo = view.findViewById(R.id.txtNewstitolo);
        }
    }

    @Override
    public ChangelogViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_news, parent, false);
        return new ChangelogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChangelogViewHolder holder, int i) {
        final ClassU.Versione versione = list.get(i);
        holder.txtdata.setText(versione.vers);
        holder.txttitolo.setText(versione.desc);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}