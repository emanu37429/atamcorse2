package it.emanu37429.atamcorse2.atamcorse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {


    public MapFragment() {
    }

    GoogleMap map;
    MapView mapView;
    FloatingActionButton getpos, searchbyaddr;
    Marker mypos, addrpos;
    LatLngBounds.Builder bnd;
    ClusterManager<FermataMap> mClusterManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    boolean myposact = false;
    boolean first = true;
    Activity act;

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
        act = getActivity();
        return v;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        mClusterManager = new ClusterManager<>(getContext(), map);
        this.map = map;
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
                JSONArray paline = new JSONArray(ClassU.StringFromResource(getContext(), R.raw.palinemap));
                NumberFormat format = NumberFormat.getInstance(Locale.ITALY);
                bnd = new LatLngBounds.Builder();
                for (int i = 0; i < paline.length(); i++) {
                    JSONObject obj = paline.getJSONObject(i);
                    LatLng latlng = new LatLng(format.parse(obj.getString("lat")).doubleValue(), format.parse(obj.getString("lng")).doubleValue());
                    bnd.include(latlng);
                    mClusterManager.addItem(new FermataMap(latlng, obj.getString("fermata"), obj.getString("id")));
                }
                getActivity().runOnUiThread(() -> {
                    map.setOnCameraIdleListener(mClusterManager);
                    map.setOnMarkerClickListener(mClusterManager);
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bnd.build(), 0));
                    map.setOnInfoWindowClickListener(mClusterManager);
                    map.getUiSettings().setZoomControlsEnabled(false);
                    mClusterManager.setOnClusterItemInfoWindowClickListener(fermataMap -> {
                        Intent i = new Intent(getContext(), GetPalinaActivity.class);
                        i.putExtra("id", fermataMap.Id);
                        startActivity(i);
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        getpos.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (myposact) {
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                    getpos.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_off_24px));
                    myposact = false;
                    first = true;
                    if (mypos != null) mypos.remove();
                } else {
                    final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        LocationRequest locreq = new LocationRequest();
                        locreq.setInterval(5000);
                        mFusedLocationClient.requestLocationUpdates(locreq, mLocationCallback, null);
                        getpos.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_fixed_24px));
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
                String text = addr.getText().toString();
                if (text.length() > 0) {
                    try {
                        new Thread(() -> {
                            String[] data = ClassU.DownloadString(ClassU.linkCercaIndirizzo + text.replace(" ", "%20")).split(";;");
                            double latitude = Double.parseDouble(data[1]);
                            double longitude = Double.parseDouble(data[2]);
                            act.runOnUiThread(() -> {
                                if (addrpos != null)
                                    addrpos.remove();
                                addrpos = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(data[0]).icon(ClassU.bitmapDescriptorFromVector(getContext(), R.drawable.ic_baseline_place_24px_color2)));
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(addrpos.getPosition(), 17));
                                addrpos.showInfoWindow();
                            });
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(view.getContext(), "Errore nel recupero dell'indirizzo. Riprovare", Toast.LENGTH_LONG).show();
                    }
                }
            });
            alert.show();
        });
    }

    public class FermataMap implements ClusterItem {
        private LatLng pos;
        private String Title, Id;

        public FermataMap(LatLng latlng, String title, String id) {
            pos = latlng;
            Title = title;
            Id = id;
        }

        @Override
        public LatLng getPosition() {
            return pos;
        }

        @Override
        public String getTitle() {
            return Title;
        }

        @Override
        public String getSnippet() {
            return null;
        }
    }

    @Override
    public void onPause() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        getpos.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_off_24px));
        super.onPause();
    }
}
