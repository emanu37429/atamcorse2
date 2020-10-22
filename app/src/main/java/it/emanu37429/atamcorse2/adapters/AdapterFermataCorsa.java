package it.emanu37429.atamcorse2.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.atamcorse.GetPalinaActivity;

public class AdapterFermataCorsa extends RecyclerView.Adapter<AdapterFermataCorsa.FermataCorsaViewHolder> {
    private List<ClassU.FermataCorsaBase> list;
    public boolean evLst;
    public static boolean dsnPl = false;
    public AppCompatActivity act;

    public AdapterFermataCorsa(List<ClassU.FermataCorsaBase> list) {
        this.list = list;
        setHasStableIds(true);
    }

    public class FermataCorsaViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtpassrt, txtpassst;
        RadioButton rbFermata;
        View viewin, viewout;
        ConstraintLayout clc;

        public FermataCorsaViewHolder(View view) {
            super(view);
            rbFermata = view.findViewById(R.id.rbFermataBus);
            txtpassrt = view.findViewById(R.id.txtPassRe);
            txtpassst = view.findViewById(R.id.txtPassSt);
            viewin = view.findViewById(R.id.viewin);
            viewout = view.findViewById(R.id.viewout);
            clc = view.findViewById(R.id.CLayoutCorsa);
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
        int id = R.layout.righe_fcorsa;
        if(dsnPl) id = R.layout.righe_fcorsa_dp;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        return new FermataCorsaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FermataCorsaViewHolder holder, int i) {
        final ClassU.FermataCorsaBase fermata = list.get(i);
        if (evLst && fermata.out) {
            holder.rbFermata.setTypeface(null, Typeface.NORMAL);
            holder.clc.setBackground(ContextCompat.getDrawable(holder.clc.getContext(), android.R.color.white));
        }
        holder.rbFermata.setText(fermata.nome);
        holder.txtpassst.setText(fermata.orast);
        if (fermata.pass) {
            holder.rbFermata.setChecked(true);
            holder.txtpassrt.setText(fermata.orart);
            if (!fermata.scost.equals("")) {
                try {
                    int res = Math.round(Integer.valueOf(fermata.scost));
                    if (res <= -1) {
                        String resstr = " (" + res + ")";
                        Spannable wordtoSpan = new SpannableString(fermata.nome + resstr);
                        wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), fermata.nome.length(), fermata.nome.length() + resstr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.rbFermata.setText(wordtoSpan);
                    }
                    if (res >= 1) {
                        String resstr = " (+" + res + ")";
                        Spannable wordtoSpan = new SpannableString(fermata.nome + resstr);
                        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), fermata.nome.length(), fermata.nome.length() + resstr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.rbFermata.setText(wordtoSpan);
                    }
                } catch (Exception ignored) {
                }
            }
            if (fermata.out) holder.viewout.setVisibility(View.VISIBLE);
            if (i > 0)
                holder.viewin.setVisibility(View.VISIBLE);
            if (evLst && !fermata.out) {
                holder.rbFermata.setTypeface(null, Typeface.BOLD);
                holder.clc.setBackground(ContextCompat.getDrawable(holder.clc.getContext(), R.drawable.border_clc));
            }
        } else {
            holder.rbFermata.setOnClickListener(v -> holder.rbFermata.setChecked(false));
            holder.rbFermata.setChecked(false);
            holder.viewin.setVisibility(View.GONE);
            holder.viewout.setVisibility(View.GONE);
        }
        holder.rbFermata.setOnLongClickListener(v -> {
            Intent intent = new Intent(v.getContext(), GetPalinaActivity.class);
            intent.putExtra("id", fermata.id);
            v.getContext().startActivity(intent);
            return false;
        });
    }

    public void updateList(List<ClassU.FermataCorsaBase> lst) {
        list = lst;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

