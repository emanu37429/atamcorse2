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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.atamcorse.GetCorsaActivity;

public class CorsaListAdapter extends RecyclerView.Adapter<CorsaListAdapter.CorsaViewHolder> {
    private List<ClassU.Corsa> list;

    public CorsaListAdapter(List<ClassU.Corsa> list) {
        this.list = list;
    }

    public class CorsaViewHolder extends RecyclerView.ViewHolder {
        protected RadioButton rbPart, rbArr;
        CardView cd;

        public CorsaViewHolder(View view) {
            super(view);
            rbArr = view.findViewById(R.id.rbArr);
            rbPart = view.findViewById(R.id.rbPart);
            cd = view.findViewById(R.id.cardCorsa);
        }
    }

    @Override
    public CorsaViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_corse, parent, false);
        return new CorsaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CorsaViewHolder holder, int i) {
        final ClassU.Corsa linea = list.get(i);
        SpannableStringBuilder part = new SpannableStringBuilder(linea.oraarr + " " + linea.arr);
        part.setSpan(new StyleSpan(Typeface.BOLD), 0, linea.orapart.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder arr = new SpannableStringBuilder(linea.orapart + " " + linea.part);
        arr.setSpan(new StyleSpan(Typeface.BOLD), 0, linea.oraarr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.rbArr.setText(part);
        holder.rbPart.setText(arr);
        View.OnClickListener listener = view -> {
            Intent i1 = new Intent(view.getContext(), GetCorsaActivity.class);
            i1.putExtra("id", linea.id);
            view.getContext().startActivity(i1);
        };
        holder.rbArr.setOnClickListener(listener);
        holder.rbPart.setOnClickListener(listener);
        holder.cd.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}