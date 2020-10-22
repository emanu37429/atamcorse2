package it.emanu37429.atamcorse2.trainsfrag;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.NewsAdapterVT;

public class FragmentNewsTI extends Fragment {


    public FragmentNewsTI() {
    }

    ProgressDialog pDialog;
    public String link;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fragment_news_ti, container, false);
        pDialog = ProgressDialog.show(getContext(), "Caricamento in corso", "Attendere prego...");
        new Thread(() -> {
            try {
                String news = ClassU.DownloadString(link);
                JSONArray newsArray = new JSONArray(news);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALIAN);
                final List<ClassU.NewsVT> list = new ArrayList<>();
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject element = newsArray.getJSONObject(i);
                    list.add(new ClassU.NewsVT(
                            formatter.format(new Date(element.getLong("data"))),
                            element.getString("testo"),
                            element.getBoolean("primoPiano")
                    ));
                }
                getActivity().runOnUiThread(() -> {
                    RecyclerView mRecyclerView = v.findViewById(R.id.recNews);
                    mRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(RecyclerView.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                    mRecyclerView.addItemDecoration(dividerItemDecoration);
                    Collections.sort(list, (y, x) -> {
                        try {
                            return formatter.parse(x.datan).compareTo(formatter.parse(y.datan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    });
                    Collections.sort(list, (y, x) -> x.primopiano.compareTo(y.primopiano));
                    mRecyclerView.setAdapter(new NewsAdapterVT(list));
                    pDialog.dismiss();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return v;
    }

}
