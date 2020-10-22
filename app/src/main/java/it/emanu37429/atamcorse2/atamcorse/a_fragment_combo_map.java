package it.emanu37429.atamcorse2.atamcorse;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import it.emanu37429.atamcorse2.R;

public class a_fragment_combo_map extends Fragment implements OnMapReadyCallback {


    public a_fragment_combo_map() {
    }

    GetCorsaActivity act;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.a_fragment_getcorsamap, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        act = (GetCorsaActivity)getActivity();
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        act.Continue(googleMap);
    }
}
