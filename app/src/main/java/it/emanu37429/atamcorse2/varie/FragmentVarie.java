package it.emanu37429.atamcorse2.varie;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import it.emanu37429.atamcorse2.R;

public class FragmentVarie extends Fragment {

    ListView listView;
    public FragmentVarie() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_varie, container, false);
        listView=v.findViewById(R.id.listviewvarie);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent pos = new Intent(getContext(), VarieActivity.class);
            pos.putExtra("pos", i);
            startActivity(pos);
        });
        return v;
    }

}
