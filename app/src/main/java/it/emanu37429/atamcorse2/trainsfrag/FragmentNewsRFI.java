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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.NewsAdapter;

public class FragmentNewsRFI extends Fragment {

    public FragmentNewsRFI() {
    }

    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fragment_news_ti, container, false);
        pDialog = ProgressDialog.show(getContext(),"Caricamento in corso", "Attendere prego...");
        new Thread(() -> {
            try {
                String news = ClassU.DownloadString(ClassU.linkRSSRFI);
                String[] newsArray = news.split("#\"!");
                String splChar = "!#";
                SimpleDateFormat parser = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALIAN);
                final List<ClassU.News> list = new ArrayList<>();
                for (String str : newsArray) {
                    String[] newsSingle = str.split(splChar);
                    list.add(new ClassU.News(
                            formatter.format(parser.parse(newsSingle[0])),
                            newsSingle[1],
                            newsSingle[2]
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
                    mRecyclerView.setAdapter(new NewsAdapter(list));
                    pDialog.dismiss();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return v;
    }

}
