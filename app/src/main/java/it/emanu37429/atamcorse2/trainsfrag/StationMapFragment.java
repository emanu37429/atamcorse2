package it.emanu37429.atamcorse2.trainsfrag;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

import static it.emanu37429.atamcorse2.ClassU.StringFromResource;
import static it.emanu37429.atamcorse2.ClassU.bitmapDescriptorFromVector;

public class StationMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


    public StationMapFragment() {
    }

    JSONArray jArray;
    FloatingActionButton getpos, searchbyaddr;
    MapView mapView;
    GoogleMap googleMap;
    Marker mypos, addrpos;
    ProgressDialog pDialog;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    boolean myposact = false;
    boolean first = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getpos = v.findViewById(R.id.fabMapPos);
        searchbyaddr = v.findViewById(R.id.fabAddrMap);
        mFusedLocationClient = new FusedLocationProviderClient(getContext());
        pDialog = ProgressDialog.show(getContext(),"Caricamento in corso", "Attendere prego...");
        return v;
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        map.setOnInfoWindowClickListener(this);
        map.setMaxZoomPreference(18);
        map.getUiSettings().setMapToolbarEnabled(false);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (mypos != null) mypos.remove();
                    mypos = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(ClassU.bitmapDescriptorFromVector(getContext(), R.drawable.ic_baseline_place_24px)));
                    if (first) {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mypos.getPosition(), 17));
                        first = false;
                    }
                }
            }
        };
        new Thread(() -> {
            try {
                NumberFormat format = NumberFormat.getInstance(Locale.ITALY);
                GeoJsonLayer layer = new GeoJsonLayer(map, R.raw.railways_italy,
                        getContext());
                layer.getDefaultLineStringStyle().setWidth(2f);
                layer.getDefaultLineStringStyle().setColor(Color.parseColor("grey"));
                getActivity().runOnUiThread(() -> {
                    try {
                        layer.addLayerToMap();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                try {
                    jArray = new JSONArray(StringFromResource(getContext(), R.raw.stazioni));
                    final LatLngBounds.Builder bnd = new LatLngBounds.Builder();
                    BitmapDescriptor bmp = bitmapDescriptorFromVector(getContext(), R.drawable.dot);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject stazione = jArray.getJSONObject(i);
                        if(!stazione.getString("lat").isEmpty() && !stazione.getString("idvt").isEmpty()) {
                            final LatLng latLng = new LatLng(format.parse(stazione.getString("lat")).doubleValue(), format.parse(stazione.getString("lng")).doubleValue());
                            bnd.include(latLng);
                            final String nomest = stazione.getString("name");
                            getActivity().runOnUiThread(() -> map.addMarker(new MarkerOptions().position(latLng).icon(bmp).title(nomest)));
                        }
                    }
                    getActivity().runOnUiThread(() -> map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.114444, 15.650000), 12)));
                    pDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
                pDialog.dismiss();
            }
        }).start();
        getpos.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (myposact) {
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                    getpos.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_fixed_24px));
                    myposact = false;
                    first = true;
                    mypos.remove();
                } else {
                    final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        LocationRequest locreq = new LocationRequest();
                        locreq.setInterval(5000);
                        mFusedLocationClient.requestLocationUpdates(locreq, mLocationCallback, null);
                        getpos.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_off_24px));
                        myposact = true;
                        Toast.makeText(getContext(), "Recupero posizione in corso", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "GPS non attivo. Attivalo prima di usare questa funzione", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
        });
        searchbyaddr.setOnClickListener(view -> {
            if (addrpos != null) addrpos.remove();
            EditText addr = new EditText(view.getContext());
            AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
            alert.setTitle("Ricerca indirizzo");
            alert.setMessage("Inserisci un indirizzo per effettuare la ricerca");
            alert.setView(addr);
            alert.setNegativeButton("Chiudi", null);
            alert.setPositiveButton("Cerca", (dialogInterface, i) -> {
                if (addr.getText().length() > 0) {
                    try {
                        String[] data = ClassU.DownloadString(ClassU.linkCercaIndirizzo + addr.getText().toString().replace(" ", "%20")).split(";;");
                        double latitude = Double.parseDouble(data[1]);
                        double longitude = Double.parseDouble(data[2]);
                        if (addrpos != null)
                            addrpos.remove();
                        addrpos = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(data[0]).icon(ClassU.bitmapDescriptorFromVector(getContext(), R.drawable.ic_baseline_place_24px_color2)));
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(addrpos.getPosition(), 17));
                        addrpos.showInfoWindow();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(view.getContext(), "Errore nel recupero dell'indirizzo. Riprovare", Toast.LENGTH_LONG).show();
                    }
                }
            });
            alert.show();
        });
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 0) {
            if (permissions.length == 2 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(getContext(), "Non posso accedere alla tua posizione se non concedi il permesso", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent act = new Intent(getContext(), StazioneArriviPartenze.class);
        try {
            String mkt = marker.getTitle();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject js = jArray.getJSONObject(i);
                if (mkt.equals(js.getString("name"))) {
                    act.putExtra("id", js.getString("idvt"));
                    break;
                }
            }
            startActivity(act);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        getpos.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_off_24px));
        super.onPause();
    }

}
