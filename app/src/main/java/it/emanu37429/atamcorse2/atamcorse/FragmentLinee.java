package it.emanu37429.atamcorse2.atamcorse;


import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.List;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.LineeAdapter;

public class FragmentLinee extends Fragment {


    public FragmentLinee() {
    }

    List<ClassU.Linea> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fragment_linee, container, false);
        AsyncTask.execute(() -> {
            try {
                list = new ArrayList<>();
                JSONArray linee = new JSONArray(ClassU.StringFromResource(getContext(), R.raw.linee));
                for(int i=0;i<linee.length();i++){
                    JSONObject linea = linee.getJSONObject(i);
                    list.add(new ClassU.Linea(linea.getString("id"),linea.getString("nome"),linea.getString("linea")));
                }
                getActivity().runOnUiThread(() -> {
                    RecyclerView mRecyclerView = v.findViewById(R.id.recLinee);
                    mRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(RecyclerView.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                    mRecyclerView.addItemDecoration(dividerItemDecoration);
                    mRecyclerView.setAdapter(new LineeAdapter(list));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return v;
    }
}
