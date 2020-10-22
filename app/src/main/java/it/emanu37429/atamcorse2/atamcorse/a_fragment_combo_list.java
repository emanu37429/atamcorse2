package it.emanu37429.atamcorse2.atamcorse;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import it.emanu37429.atamcorse2.R;

public class a_fragment_combo_list extends Fragment {


    public a_fragment_combo_list() {    }

    public RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.a_fragment_getcorsalista, container, false);
        mRecyclerView = v.findViewById(R.id.recCorsaAct);
        return v;
    }

}
