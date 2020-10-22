package it.emanu37429.atamcorse2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.NewsViewHolder> {
    private List<ClassU.News> list;
    String id;

    public AdapterNews(List<ClassU.News> list) {
        this.list = list;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtdata, txttitolo, txtcorpo;

        public NewsViewHolder(View view) {
            super(view);
            txtdata = view.findViewById(R.id.txtdatanews);
            txtcorpo = view.findViewById(R.id.txtcorponews);
            txttitolo = view.findViewById(R.id.txttitolonews);
        }
    }

    @Override
    public NewsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_news_new, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, int i) {
        final ClassU.News news = list.get(i);
        holder.txtdata.setText(news.datan);
        holder.txttitolo.setText(news.titolo);
        holder.txtcorpo.setText(news.linkn);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
