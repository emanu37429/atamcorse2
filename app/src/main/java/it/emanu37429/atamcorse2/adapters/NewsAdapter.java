package it.emanu37429.atamcorse2.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.varie.WebViewActivity;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<ClassU.News> list;

    public NewsAdapter(List<ClassU.News> list) {
        this.list = list;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTesto;
        protected RadioButton rbData;
        CardView cd;

        public NewsViewHolder(View view) {
            super(view);
            rbData = view.findViewById(R.id.rbData);
            txtTesto = view.findViewById(R.id.txtNews);
            cd = view.findViewById(R.id.CardNews);
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
        final ClassU.News notizia = list.get(i);
        holder.rbData.setText(notizia.datan);
        holder.txtTesto.setText(notizia.titolo);
        if(notizia.linkn!=null)
            holder.cd.setOnClickListener(view -> {
                Intent act = new Intent(holder.cd.getContext(), WebViewActivity.class);
                act.putExtra("url", notizia.linkn);
                holder.cd.getContext().startActivity(act);
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}