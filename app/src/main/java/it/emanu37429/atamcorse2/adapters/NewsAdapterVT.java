package it.emanu37429.atamcorse2.adapters;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;


public class NewsAdapterVT extends RecyclerView.Adapter<NewsAdapterVT.NewsViewHolder> {
    private List<ClassU.NewsVT> list;

    public NewsAdapterVT(List<ClassU.NewsVT> list) {
        this.list = list;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTesto;
        protected RadioButton rbData;

        public NewsViewHolder(View view) {
            super(view);
            rbData = view.findViewById(R.id.rbData);
            txtTesto = view.findViewById(R.id.txtNews);
        }
    }

    @Override
    public NewsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_news, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, int i) {
        final ClassU.NewsVT notizia = list.get(i);
        if(notizia.primopiano){
            Spannable data = new SpannableString(notizia.datan);
            data.setSpan(new ForegroundColorSpan(Color.RED), 0, notizia.datan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Spannable testo = new SpannableString(notizia.testo);
            testo.setSpan(new ForegroundColorSpan(Color.RED), 0, notizia.testo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.rbData.setText(data);
            holder.txtTesto.setText(testo);
        }else {
            holder.rbData.setText(notizia.datan);
            holder.txtTesto.setText(notizia.testo);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}