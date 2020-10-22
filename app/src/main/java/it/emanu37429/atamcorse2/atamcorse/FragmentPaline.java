package it.emanu37429.atamcorse2.atamcorse;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
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
import it.emanu37429.atamcorse2.adapters.FermateAdapter;

public class FragmentPaline extends Fragment {


    public FragmentPaline() {
    }

    List<ClassU.Fermata> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fragment_paline, container, false);
        AsyncTask.execute(() -> {
            try {
                JSONArray paline = new JSONArray(ClassU.StringFromResource(getContext(), R.raw.palinelist));
                for (int i = 0; i < paline.length(); i++) {
                    JSONObject palina = paline.getJSONObject(i);
                    list.add(new ClassU.Fermata(palina.getString("id"),palina.getString("fermata"),palina.getString("num")));
                }
                getActivity().runOnUiThread(() -> {
                    final RecyclerView mRecyclerView = v.findViewById(R.id.recFermate);
                    mRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(RecyclerView.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
                    mRecyclerView.addItemDecoration(dividerItemDecoration);
                    final FermateAdapter adapter = new FermateAdapter(list);
                    mRecyclerView.setAdapter(adapter);
                    SearchView search = v.findViewById(R.id.searchPaline);
                    search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            adapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return v;
    }

}
