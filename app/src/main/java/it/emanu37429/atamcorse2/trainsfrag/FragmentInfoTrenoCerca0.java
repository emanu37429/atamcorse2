package it.emanu37429.atamcorse2.trainsfrag;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.emanu37429.atamcorse2.R;

public class FragmentInfoTrenoCerca0 extends Fragment {


    public FragmentInfoTrenoCerca0() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_info_treno_cerca0, container, false);
        FloatingActionButton btn = v.findViewById(R.id.btncercatreno);
        final EditText txttreno = v.findViewById(R.id.editTextNTreno);
        btn.setOnClickListener(view -> {
            Bundle bnd = new Bundle();
            bnd.putString("Cod", txttreno.getText().toString());
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            Fragment fragment = new FragmentInfoTreno();
            fragment.setArguments(bnd);
            ft.replace(R.id.container, fragment);
            ft.commit();
        });
        return v;
    }

}
