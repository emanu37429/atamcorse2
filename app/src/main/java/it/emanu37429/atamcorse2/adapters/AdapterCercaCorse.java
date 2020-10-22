package it.emanu37429.atamcorse2.adapters;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.atamcorse.GetCorsaActivity;

public class AdapterCercaCorse extends RecyclerView.Adapter<AdapterCercaCorse.MViewHolder> {

    private List<ClassU.BusPercorso> list;

    public AdapterCercaCorse(List<ClassU.BusPercorso> list) {
        this.list = list;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ITALIAN);

    public class MViewHolder extends RecyclerView.ViewHolder {
        TextView txtDurata;
        RadioButton rbFerPart, rbFerArr, rbLinea;

        MViewHolder(View view) {
            super(view);
            txtDurata = view.findViewById(R.id.txtDurataCrs);
            rbFerArr = view.findViewById(R.id.rbFerArr);
            rbFerPart = view.findViewById(R.id.rbFerPart);
            rbLinea = view.findViewById(R.id.rbLinea);
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righelstcercacorse, parent, false);
        return new MViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MViewHolder holder, int i2) {
        final ClassU.BusPercorso bus = list.get(i2);
        try {
            Long diff = sdf.parse(bus.oraFerArr).getTime() - sdf.parse(bus.oraFerPart).getTime();
            holder.txtDurata.setText(String.format(Locale.ITALIAN, "%dh%02dm",
                    TimeUnit.MILLISECONDS.toHours(diff),
                    TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SpannableStringBuilder strda = new SpannableStringBuilder(bus.oraFerPart + " " + bus.nomeFerPart);
        strda.setSpan(new StyleSpan(Typeface.BOLD), 0, bus.oraFerPart.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder stra = new SpannableStringBuilder(bus.oraFerArr + " " + bus.nomeFerArr);
        stra.setSpan(new StyleSpan(Typeface.BOLD), 0, bus.oraFerArr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.rbFerPart.setText(strda);
        holder.rbFerArr.setText(stra);
        holder.rbLinea.setText(bus.nomeLinea);

        holder.rbLinea.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), GetCorsaActivity.class);
            i.putExtra("id", bus.idCorsa);
            view.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
