package it.emanu37429.atamcorse2.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

public class AdapterTrenoInfo extends RecyclerView.Adapter<AdapterTrenoInfo.MViewHolder>  {
    private List<ClassU.TrenoInfo> list;


    public AdapterTrenoInfo(List<ClassU.TrenoInfo> list) {
        this.list = list;
    }


    public class MViewHolder extends RecyclerView.ViewHolder {
        TextView txtNomeTreno;
        RecyclerView recFermateTreno;
        ImageView[] imgs = new ImageView[4];

        MViewHolder(View view) {
            super(view);
            txtNomeTreno = view.findViewById(R.id.txtNomeTreno);
            recFermateTreno = view.findViewById(R.id.recFermateTreno);
            imgs[0] = view.findViewById(R.id.icn0);
            imgs[1] = view.findViewById(R.id.icn1);
            imgs[2] = view.findViewById(R.id.icn2);
            imgs[3] = view.findViewById(R.id.icn3);
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.righe_fermate_treni, parent, false);
        return new MViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MViewHolder holder, int i2) {
        ClassU.TrenoInfo treno = list.get(i2);
        holder.txtNomeTreno.setText(treno.Treno);
        holder.recFermateTreno.setHasFixedSize(true);
        for(int i=0;i<treno.Imgs.size() && i<4;i++){
            byte[] decodedString = Base64.decode(treno.Imgs.get(i), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgs[i].setImageBitmap(decodedByte);
        }
        holder.recFermateTreno.setNestedScrollingEnabled(true);
        LinearLayoutManager llm = new LinearLayoutManager(holder.recFermateTreno.getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        holder.recFermateTreno.setLayoutManager(llm);
        holder.recFermateTreno.addItemDecoration(new DividerItemDecoration(holder.recFermateTreno.getContext(), 0));
        holder.recFermateTreno.setAdapter(new AdapterFermateTreno(treno.Fermate));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
