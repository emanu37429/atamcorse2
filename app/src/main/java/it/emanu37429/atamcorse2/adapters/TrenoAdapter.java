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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.trainsfrag.TrenoInfoActivity;

public class TrenoAdapter extends RecyclerView.Adapter<TrenoAdapter.MViewHolder> {

    private List<ClassU.TrenoPercorsoTre> list;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
    public String datastart;
    SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
    public Map<String, String> cookies;

    public TrenoAdapter(List<ClassU.TrenoPercorsoTre> list) {
        this.list = list;
    }


    public class MViewHolder extends RecyclerView.ViewHolder {
        TextView txtDurata, txtCambi;
        TextView[] txtCoin = new TextView[3];
        RadioButton[] rbFermate = new RadioButton[8];
        RadioButton[] rbTreni = new RadioButton[4];
        View[] viewPass = new View[4];
        FloatingActionButton fabInfo;

        MViewHolder(View view) {
            super(view);
            txtDurata = view.findViewById(R.id.txtFDCdurata);
            txtCambi = view.findViewById(R.id.txtTrCambi);
            txtCoin[0] = view.findViewById(R.id.txtFDCwr1);
            txtCoin[1] = view.findViewById(R.id.txtFDCwr2);
            txtCoin[2] = view.findViewById(R.id.txtFDCwr3);
            rbTreni[0] = view.findViewById(R.id.rbFDCt1);
            rbTreni[1] = view.findViewById(R.id.rbFDCt2);
            rbTreni[2] = view.findViewById(R.id.rbFDCt3);
            rbTreni[3] = view.findViewById(R.id.rbFDCt4);
            rbFermate[0] = view.findViewById(R.id.rbFDCf1);
            rbFermate[1] = view.findViewById(R.id.rbFDCf2);
            rbFermate[2] = view.findViewById(R.id.rbFDCf3);
            rbFermate[3] = view.findViewById(R.id.rbFDCf4);
            rbFermate[4] = view.findViewById(R.id.rbFDCf5);
            rbFermate[5] = view.findViewById(R.id.rbFDCf6);
            rbFermate[6] = view.findViewById(R.id.rbFDCf7);
            rbFermate[7] = view.findViewById(R.id.rbFDCf8);
            viewPass[0] = view.findViewById(R.id.passFDCt1);
            viewPass[1] = view.findViewById(R.id.passFDCt2);
            viewPass[2] = view.findViewById(R.id.passFDCt3);
            viewPass[3] = view.findViewById(R.id.passFDCt4);
            fabInfo = view.findViewById(R.id.fabInfo);
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righelsttreno, parent, false);
        return new MViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MViewHolder holder, int i2) {
        final ClassU.TrenoPercorsoTre treni = list.get(i2);
        holder.txtCambi.setText(String.valueOf(treni.soluzione.length - 1));
        for (int i = 0; i < treni.soluzione.length; i++) {
            ClassU.TrenoPercorsoF2 treno = treni.soluzione[i];
            if(!datastart.equals(dtf.format(treno.oraFerPart))) holder.rbTreni[i].setText(treno.idTreno+"*"); else
            holder.rbTreni[i].setText(treno.idTreno);
            String orapart = sdf.format(treno.oraFerPart);
            String oraarr = sdf.format(treno.oraFerArr);
            SpannableStringBuilder strda = new SpannableStringBuilder(orapart + " " + treno.nomeFerPart);
            strda.setSpan(new StyleSpan(Typeface.BOLD), 0, orapart.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            SpannableStringBuilder stra = new SpannableStringBuilder(oraarr + " " + treno.nomeFerArr);
            stra.setSpan(new StyleSpan(Typeface.BOLD), 0, oraarr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.rbFermate[i * 2].setText(strda);
            holder.rbFermate[i * 2 + 1].setText(stra);
            if (i > 0) {
                holder.txtCoin[i - 1].setText(computeCoin(treni.soluzione[i - 1].oraFerArr, treno.oraFerPart));
            }
        }
        if (treni.soluzione.length < 4) {
            for (int i = 3; i >= treni.soluzione.length; i--) {
                holder.txtCoin[i - 1].setVisibility(View.GONE);
                holder.rbTreni[i].setVisibility(View.GONE);
                holder.rbFermate[i * 2].setVisibility(View.GONE);
                holder.rbFermate[i * 2 + 1].setVisibility(View.GONE);
                holder.viewPass[i].setVisibility(View.GONE);
            }
        }
        try {
            Long diff = treni.soluzione[treni.soluzione.length - 1].oraFerArr - treni.soluzione[0].oraFerPart;
            holder.txtDurata.setText(String.format(Locale.ITALIAN, "%dh%02dm",
                    TimeUnit.MILLISECONDS.toHours(diff),
                    TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.fabInfo.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), TrenoInfoActivity.class);
            i.putExtra("idsol", treni.idSol);
            TrenoInfoActivity.mapcookies = cookies;
            view.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String computeCoin(long oraPart, long oraArr) {
        try {
            Long diff = oraArr - oraPart;
            return "--- " + TimeUnit.MILLISECONDS.toMinutes(diff) + " minuti per il cambio ---";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
