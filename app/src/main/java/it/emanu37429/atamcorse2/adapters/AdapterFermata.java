package it.emanu37429.atamcorse2.adapters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.atamcorse.GetCorsaActivity;

public class AdapterFermata extends RecyclerView.Adapter<AdapterFermata.FermataViewHolder> {
    private List<ClassU.FermataCorsaP> list;
    ProgressDialog pDialog;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ITALIAN);

    public AdapterFermata(List<ClassU.FermataCorsaP> list) {
        this.list = list;
        setHasStableIds(true);
    }

    public class FermataViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtpass, txtrit, txtvec, txtvtext, txtrtext, txtlcap, txtpassprev, txtpassprevre;
        RadioButton rbLineaeCap;
        CardView cd;

        public FermataViewHolder(View view) {
            super(view);
            txtpass = view.findViewById(R.id.txtPass);
            txtrit = view.findViewById(R.id.txtRit);
            txtvec = view.findViewById(R.id.txtVec);
            rbLineaeCap = view.findViewById(R.id.rbLineaeCap);
            cd = view.findViewById(R.id.cardFermataCorsa);
            txtvtext = view.findViewById(R.id.txtVtext);
            txtrtext = view.findViewById(R.id.txtRtext);
            txtlcap = view.findViewById(R.id.txtLCap);
            txtpassprev = view.findViewById(R.id.txtPassPrev);
            txtpassprevre = view.findViewById(R.id.txtPassPrevPre);
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
    public FermataViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_fermata, parent, false);
        return new FermataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FermataViewHolder holder, int i) {
        final ClassU.FermataCorsaP corsa = list.get(i);
        holder.txtpass.setText(corsa.ora);
        SpannableStringBuilder str = new SpannableStringBuilder(corsa.lincap);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, corsa.lincap.split("-")[0].trim().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.txtlcap.setText(str);
        holder.txtlcap.setPaintFlags(holder.rbLineaeCap.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.txtlcap.setOnClickListener(view -> {
            Intent it = new Intent(view.getContext(), GetCorsaActivity.class);
            it.putExtra("id", corsa.idc);
            view.getContext().startActivity(it);
        });
        if (corsa.nav) {
            holder.rbLineaeCap.setChecked(true);
            holder.txtvec.setText(corsa.vec);
            if (!corsa.rit.equals("") && !corsa.rit.equals("0")) {
                holder.txtrit.setText(corsa.rit + " minuti");
                holder.txtrit.setVisibility(View.VISIBLE);
                holder.txtrtext.setVisibility(View.VISIBLE);
                if (corsa.rit.contains("-")) holder.txtrtext.setText("Anticipo");
                else holder.txtrtext.setText("Ritardo");
                holder.txtpassprevre.setVisibility(View.VISIBLE);
                holder.txtpassprev.setVisibility(View.VISIBLE);
                if (corsa.rit.contains("-")) {
                    try {
                        Long ant = sdf.parse(corsa.ora).getTime() - (Integer.parseInt(corsa.rit.replace("-", "")) * 60000);
                        holder.txtpassprev.setText(sdf.format(new Date(ant)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Long rit = sdf.parse(corsa.ora).getTime() + (Integer.parseInt(corsa.rit) * 60000);
                        holder.txtpassprev.setText(sdf.format(new Date(rit)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                holder.txtrit.setVisibility(View.GONE);
                holder.txtrtext.setVisibility(View.GONE);
                holder.txtpassprevre.setVisibility(View.GONE);
                holder.txtpassprev.setVisibility(View.GONE);
            }
        } else {
            holder.txtvec.setVisibility(View.GONE);
            holder.txtrit.setVisibility(View.GONE);
            holder.txtrtext.setVisibility(View.GONE);
            holder.txtvtext.setVisibility(View.GONE);
            holder.txtpassprevre.setVisibility(View.GONE);
            holder.txtpassprev.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}