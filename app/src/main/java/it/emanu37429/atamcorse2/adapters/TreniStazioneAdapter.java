package it.emanu37429.atamcorse2.adapters;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.trainsfrag.InfoTrenoDaStaz;
import it.emanu37429.atamcorse2.trainsfrag.StazioneArriviPartenze;

public class TreniStazioneAdapter extends RecyclerView.Adapter<TreniStazioneAdapter.StViewHolder> {

    private List<StazioneArriviPartenze.TrenoDaStazione> list;
    private View.OnClickListener listener;


    public TreniStazioneAdapter(List<StazioneArriviPartenze.TrenoDaStazione> list) {
        this.list = list;
    }

    class StViewHolder extends RecyclerView.ViewHolder {
        TextView txtBin, txtBinRe, txtOrientamento, txtTrenoT, txtOrigine, txtStato;
        CardView cd;
        AppCompatRadioButton rbTrenoOrario;

        StViewHolder(View view) {
            super(view);
            rbTrenoOrario = view.findViewById(R.id.rbTrenoOrario);
            txtBin = view.findViewById(R.id.txtBin);
            cd = view.findViewById(R.id.crd);
            txtStato = view.findViewById(R.id.txtstato);
            txtOrientamento = view.findViewById(R.id.txtOrientamento);
            txtBinRe = view.findViewById(R.id.txtBinReT);
            txtTrenoT = view.findViewById(R.id.txtTrenoM);
            txtOrigine = view.findViewById(R.id.txtOrigine);
        }
    }

    @Override
    public StViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_lst_treno_arr_part_stazioni, parent, false);
        itemView.setOnClickListener(listener);
        return new StViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StViewHolder holder, int i) {
        final StazioneArriviPartenze.TrenoDaStazione treno = list.get(i);
        if (treno.Attivo) {
            holder.rbTrenoOrario.setChecked(true);
        } else {
            holder.rbTrenoOrario.setClickable(false);
        }
        if (treno.Orientamento.length() > 5)
            holder.txtOrientamento.setText(treno.Orientamento);
        else
            holder.txtOrientamento.setVisibility(View.GONE);
        SpannableString st = new SpannableString(treno.OrarioArrivo);
        st.setSpan(new StyleSpan(Typeface.BOLD), 0, treno.OrarioArrivo.length(), 0);
        holder.rbTrenoOrario.setText(st);
        holder.txtTrenoT.setText(treno.Treno);
        holder.txtTrenoT.setPaintFlags(holder.txtTrenoT.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.txtBin.setText(treno.Binario);
        holder.txtBinRe.setText(treno.BinarioReale);
        holder.txtOrigine.setText(treno.StazPartenza);
        if(!treno.Ritardo.equals("")){
            holder.txtStato.setText(treno.Ritardo);
            holder.txtStato.setVisibility(View.VISIBLE);
        }
        holder.cd.setOnClickListener(view -> {
            Intent act = new Intent(view.getContext(), InfoTrenoDaStaz.class);
            act.putExtra("cod", treno.CodTreno);
            act.putExtra("staz", treno.CodOrigine);
            view.getContext().startActivity(act);
        });
        int color = Color.GRAY;
        switch (treno.StatoRitardo) {
            case "regolare_line":
                color = Color.GREEN;
                break;
            case "ritardo01_line":
                color = Color.YELLOW;
                break;
            case "ritardo02_line":
                color = Color.parseColor("#e59400");
                break;
            case "ritardo03_line":
                color = Color.RED;
                break;
        }
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checkable}
                },
                new int[]{
                        Color.DKGRAY,
                        color,
                        Color.DKGRAY
                }
        );
        CompoundButtonCompat.setButtonTintList(holder.rbTrenoOrario, colorStateList);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
