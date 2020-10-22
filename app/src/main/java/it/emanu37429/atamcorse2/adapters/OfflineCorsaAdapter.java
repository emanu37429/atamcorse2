package it.emanu37429.atamcorse2.adapters;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

public class OfflineCorsaAdapter extends RecyclerView.Adapter<OfflineCorsaAdapter.FermataCorsaViewHolder> {
    private List<ClassU.FermataCorsaBase> list;

    public OfflineCorsaAdapter(List<ClassU.FermataCorsaBase> list) {
        this.list = list;
        setHasStableIds(true);
    }

    public class FermataCorsaViewHolder extends RecyclerView.ViewHolder {
        AppCompatRadioButton rbFermata;

        public FermataCorsaViewHolder(View view) {
            super(view);
            rbFermata = view.findViewById(R.id.rbOffCorsafermata);
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
    public FermataCorsaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_corsa_offline, parent, false);
        return new FermataCorsaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FermataCorsaViewHolder holder, int i) {
        final ClassU.FermataCorsaBase fermata = list.get(i);
        SpannableStringBuilder spn = new SpannableStringBuilder(fermata.orast + " " + fermata.nome);
        spn.setSpan(new StyleSpan(Typeface.BOLD),0, fermata.orast.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.rbFermata.setText(spn);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
