package it.emanu37429.atamcorse2.atamcorse;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.AdapterNews;

public class FragmentNews extends Fragment {


    public FragmentNews() {
    }

    ProgressDialog pDialog;
    ArrayList<ClassU.News> lista = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fragment_news, container, false);
        final Activity act = getActivity();
        pDialog = ProgressDialog.show(getContext(),"Caricamento in corso", "Attendere prego...");
        new Thread(() -> {
            try {
                JSONArray news = new JSONArray(ClassU.DownloadString(ClassU.linkNewsAtam));
                    for (int i = 0; i < news.length(); i++) {
                        JSONObject obj = news.getJSONObject(i);
                        lista.add(new ClassU.News(obj.getString("data"),obj.getString("titolo"),obj.getString("corpo")));
                    }
                    act.runOnUiThread(() -> {
                        RecyclerView mRecyclerView = v.findViewById(R.id.recNews);
                        mRecyclerView.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        llm.setOrientation(RecyclerView.VERTICAL);
                        mRecyclerView.setLayoutManager(llm);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                        mRecyclerView.addItemDecoration(dividerItemDecoration);
                        mRecyclerView.setAdapter(new AdapterNews(lista));
                        pDialog.cancel();
                    });
            } catch (Exception e) {
                e.printStackTrace();
                act.runOnUiThread(()->{
                    Toast.makeText(getContext(), "Servizio attualmente non disponibile", Toast.LENGTH_LONG).show();
                    pDialog.cancel();
                });
            }
        }).start();
        return v;
    }
}