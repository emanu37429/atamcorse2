package it.emanu37429.atamcorse2.adapters;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

public class AdapterTreniSelect extends RecyclerView.Adapter<AdapterTreniSelect.SelectTrenoViewHolder> {
    private List<ClassU.TrenoStzPartN> list;

    public AdapterTreniSelect(List<ClassU.TrenoStzPartN> list) {
        this.list = list;
    }

    public class SelectTrenoViewHolder extends RecyclerView.ViewHolder {
        protected TextView txttr;
        RadioButton rbda, rba;
        FloatingActionButton select;

        public SelectTrenoViewHolder(View view) {
            super(view);
            txttr = view.findViewById(R.id.txtNomeTreno);
            rbda = view.findViewById(R.id.rb000003);
            rba = view.findViewById(R.id.rb000002);
            select = view.findViewById(R.id.fabSelezionaTreno);
        }
    }

    @Override
    public SelectTrenoViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_trenodp_select, parent, false);
        return new SelectTrenoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectTrenoViewHolder holder, int i) {
        final ClassU.TrenoStzPartN treno = list.get(i);
        holder.txttr.setText(treno.nomeTr);
        SpannableStringBuilder spnpart = new SpannableStringBuilder(treno.orapart + " " + treno.NomeStaz);
        SpannableStringBuilder spnarr = new SpannableStringBuilder(treno.oraarr + " " + treno.nomeStazArr);
        spnarr.setSpan(new StyleSpan(Typeface.BOLD), 0, treno.oraarr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spnpart.setSpan(new StyleSpan(Typeface.BOLD), 0, treno.orapart.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.rbda.setText(spnpart);
        holder.rba.setText(spnarr);
        holder.select.setOnClickListener(view -> {
            treno.fr.alert.dismiss();
            treno.fr.Allplus(treno.objTreno);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

