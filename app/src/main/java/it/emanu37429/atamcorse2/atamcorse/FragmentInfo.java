package it.emanu37429.atamcorse2.atamcorse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import it.emanu37429.atamcorse2.R;

public class FragmentInfo extends Fragment {

    public FragmentInfo() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_info, container, false);
    }

}
