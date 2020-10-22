package it.emanu37429.atamcorse2.atamcorse;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

public class MarkerInfoWindowFer implements GoogleMap.InfoWindowAdapter {

    private AppCompatActivity context;

    public MarkerInfoWindowFer(AppCompatActivity context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.adapter_marker_info_fer, null);
        ClassU.FermataCorsaBase info = (ClassU.FermataCorsaBase) marker.getTag();
        if (info != null) {
            TextView txtNumFer = view.findViewById(R.id.txtNumFer);
            TextView txtNomeFer = view.findViewById(R.id.txtNomeFer);
            TextView txtPassProg = view.findViewById(R.id.txtProgFer);
            TextView txtPassRt = view.findViewById(R.id.txtRtFer);
            txtNumFer.setText(String.valueOf(Integer.parseInt(info.id) - 4000000));
            txtNomeFer.setText(info.nome);
            txtPassProg.setText(info.orast);
            txtPassRt.setText(info.orart);
            return view;
        } else return null;
    }
}