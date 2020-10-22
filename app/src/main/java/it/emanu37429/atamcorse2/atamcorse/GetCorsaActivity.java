package it.emanu37429.atamcorse2.atamcorse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.AdapterFermataCorsa;

public class GetCorsaActivity extends BaseActivity {
    int i2 = 0, i3 = 0;
    RecyclerView mRecyclerView;
    ProgressDialog pDialog;
    public static AdapterFermataCorsa adapter;
    List lista;
    AppCompatActivity act = this;
    Handler handler = new Handler();
    TextView txtVec, txtStato, txtVel, txtPreVec, txtPreStato, txtPreVel, txtLinea, txtCap;
    LinearLayoutManager llm;
    ConstraintLayout constrCont, constrInfo;
    private static final int NUM_PAGES = 2;
    String[] title = new String[]{"Lista passaggi", "Mappa"};
    a_fragment_combo_list fragmentlist = new a_fragment_combo_list();
    a_fragment_combo_map fragmentmap = new a_fragment_combo_map();
    Fragment[] fr = new Fragment[]{fragmentlist, fragmentmap};
    String id, idvec;
    SharedPreferences pref;
    int offset, delay;
    NumberFormat format = NumberFormat.getInstance(Locale.US), formatit = NumberFormat.getInstance(Locale.ITALIAN);
    Marker[] markers = new Marker[200];
    HashMap mapPalina = new HashMap<String, LatLng>();
    Marker mkpos;
    GoogleMap gmap;
    Thread tr;
    Boolean dsnpl = false;
    Boolean exvec = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getExtras().getString("id");
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        offset = pref.getInt("offsetlp", 1) + 1;
        delay = pref.getInt("seccorsa", 20000);
        dsnpl = pref.getBoolean("dsnpl", false);
        AdapterFermataCorsa.dsnPl = dsnpl;
        if (!dsnpl)
            setContentView(R.layout.activity_getcorsa_combo);
        else {
            setContentView(R.layout.activity_getcorsa_combo_pl);
        }
        pDialog = ProgressDialog.show(this,"Caricamento in corso", "Attendere prego...");
        txtVec = findViewById(R.id.txtVeicolo);
        txtStato = findViewById(R.id.txtStato);
        txtVel = findViewById(R.id.txtVel);
        txtPreVec = findViewById(R.id.txtPreVeicolo);
        txtPreStato = findViewById(R.id.txtPreStato);
        txtPreVel = findViewById(R.id.txtPreVel);
        txtLinea = findViewById(R.id.txtMnlinea);
        txtCap = findViewById(R.id.txtMcaplinea);

        constrCont = findViewById(R.id.cl);
        constrInfo = findViewById(R.id.cntrdata);
        act = this;
        ViewPager mPager = findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new LFSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs_tr);
        tabLayout.setupWithViewPager(mPager);
    }

    public void Continue(GoogleMap map) {
        gmap = map;
        tr = new Thread(() -> {
            try {
                String info = ClassU.DownloadString(ClassU.linkInfoCorsa + id + "&vec=" + idvec);
                if (info != null && info.length() > 10) {
                    String[] infos = info.split(";");
                    exvec = infos.length > 2;
                    if (exvec) idvec = infos[2].trim();
                    runOnUiThread(() -> {
                        txtCap.setText(infos[1]);
                        txtLinea.setText(infos[0]);
                        constrCont.setVisibility(View.VISIBLE);
                        if (exvec)
                            txtVec.setText(ClassU.getVeicoloMod(idvec, getApplicationContext()));
                    });
                    lista = list(id);
                    adapter = new AdapterFermataCorsa(lista);
                    adapter.act = this;
                    adapter.evLst = pref.getBoolean("evLstFer", false);
                    if (exvec) {
                        try {
                            runOnUiThread(() -> {
                                if (fragmentlist.mRecyclerView == null) {
                                    do {
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    } while (mRecyclerView == null);
                                } else {
                                    mRecyclerView = fragmentlist.mRecyclerView;
                                    mRecyclerView.setHasFixedSize(true);
                                    llm = new LinearLayoutManager(getApplicationContext());
                                    llm.setOrientation(RecyclerView.VERTICAL);
                                    mRecyclerView.setLayoutManager(llm);
                                    mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), 0));
                                    mRecyclerView.setAdapter(adapter);
                                    mRecyclerView.scrollToPosition(i2 - offset);
                                    i3 = i2;
                                    map.getUiSettings().setMapToolbarEnabled(false);
                                    map.setInfoWindowAdapter(new MarkerInfoWindowFer(this));
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LoadPaline();
                        runOnUiThread(() ->
                        {
                            if (constrInfo.getVisibility() == View.GONE)
                                constrInfo.setVisibility(View.VISIBLE);
                        });
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Thread tr = new Thread(() -> {
                                    try {
                                        lista = list(id);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                                tr.start();
                                try {
                                    tr.join();
                                } catch (Exception exc) {
                                }
                                adapter.updateList(lista);
                                if (i2 != i3) {
                                    runOnUiThread(() -> new Handler().postDelayed(() -> llm.scrollToPositionWithOffset(i2 - offset, 0), 1000));
                                    i3 = i2;
                                }
                                UpdatePaline();
                                Log.d("Aggiornato: ", "n.d.");
                                handler.postDelayed(this, delay);
                            }
                        }, delay);
                    } else {
                        runOnUiThread(() -> {
                            mRecyclerView = fragmentlist.mRecyclerView;
                            mRecyclerView.setHasFixedSize(true);
                            llm = new LinearLayoutManager(getApplicationContext());
                            llm.setOrientation(RecyclerView.VERTICAL);
                            mRecyclerView.setLayoutManager(llm);
                            mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), 0));
                            mRecyclerView.setAdapter(adapter);
                            mRecyclerView.scrollToPosition(i2 - offset);
                            i3 = i2;
                            map.getUiSettings().setMapToolbarEnabled(false);
                            map.setInfoWindowAdapter(new MarkerInfoWindowFer(this));
                        });
                        LoadPaline();
                    }
                } else if (info != null) {
                    ShowDialog("Corsa non attiva");
                } else {
                    ShowDialog("Errore di connessione");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> pDialog.dismiss());
        });
        tr.start();
    }

    void ShowDialog(String text) {
        runOnUiThread(() -> {
            AlertDialog diag = new AlertDialog.Builder(this)
                    .setTitle("Errore").setMessage(text).setPositiveButton("Chiudi", (a,b) -> finish()).setCancelable(false).show();
        });
    }

    void UpdatePaline() {
        try {
            for (int i = 0; i < lista.size(); i++) {
                final int fin = i;
                runOnUiThread(() -> markers[fin].setTag(lista.get(fin)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void LoadPaline() {
        try {
            JSONArray paline = new JSONArray(ClassU.StringFromResource(getApplicationContext(), R.raw.palinemap));
            for (int i = 0; i < paline.length(); i++) {
                JSONObject palina = paline.getJSONObject(i);
                mapPalina.put(palina.getString("id"), new LatLng(formatit.parse(palina.getString("lat")).doubleValue(), formatit.parse(palina.getString("lng")).doubleValue()));
            }
            final LatLngBounds.Builder bnd = new LatLngBounds.Builder();
            for (int i = 0; i < lista.size(); i++) {
                ClassU.FermataCorsaBase sfer = (ClassU.FermataCorsaBase) lista.get(i);
                if (mapPalina.containsKey(sfer.id)) {
                    final int fni = i;
                    runOnUiThread(() -> {
                        try {
                            LatLng ltln = (LatLng) mapPalina.get(sfer.id);
                            markers[fni] = gmap.addMarker(new MarkerOptions().position(ltln).title(sfer.nome).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_18dp)));
                            markers[fni].setTag(sfer);
                            bnd.include(ltln);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    });
                }
            }
            if (mkpos == null)
                runOnUiThread(() -> gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bnd.build(), 50)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ClassU.FermataCorsaBase> list(String id) {
        String url = ClassU.linkGetCorsa + id;
        if (exvec && !url.contains("&vec")) url += "&vec=" + idvec;
        Log.d("mkpos", url);
        List<ClassU.FermataCorsaBase> lst = new ArrayList<>();
        String[] fermate = ClassU.DownloadString(url).split("\\n");
        if (exvec) {
            final String[] latlng = fermate[0].split(",");
            try {
                LatLng pos = new LatLng(format.parse(latlng[0]).doubleValue(), format.parse(latlng[1]).doubleValue());
                runOnUiThread(() -> {
                    if (mkpos == null) {
                        mkpos = gmap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_36dp)));
                        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(mkpos.getPosition(), 15));
                        txtVel.setText(latlng[2].trim() + " km/h");
                    } else if (!pos.equals(mkpos.getPosition())) {
                        mkpos.setPosition(pos);
                        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(mkpos.getPosition(), 15));
                        txtVel.setText(latlng[2].trim() + " km/h");
                        for (int i = 0; i < lista.size(); i++) {
                            final int fin = i;
                            markers[fin].setTag(lista.get(fin));
                        }
                    }
                });
            } catch (Exception e) {
                Log.d("url:", fermate[0]);
                e.printStackTrace();
            }
        }

        i2 = 0;
        for (int i = 2; i < fermate.length; i++) {
            String[] fermata = fermate[i].split(";");
            lst.add(new ClassU.FermataCorsaBase(fermata[0], fermata[1], fermata[2], fermata[3], fermata[4], false, false));
            if (!fermata[2].isEmpty()) i2 = i - 1;
        }
        if (i2 != 0) {
            for (int i = 0; i < i2; i++) {
                ClassU.FermataCorsaBase fr = lst.get(i);
                fr.pass = true;
                lst.set(i, fr);
                if (i > 0) {
                    ClassU.FermataCorsaBase fr0 = lst.get(i - 1);
                    fr0.out = true;
                    lst.set(i - 1, fr0);
                }
            }
            String scost = (!lst.get(i2 - 1).scost.contains("-") && !lst.get(i2-1).scost.equals("0")) ? ("+" + lst.get(i2 - 1).scost + " minuti") : ((lst.get(i2 - 1).scost) + " minuti");
            runOnUiThread(() -> txtStato.setText(scost));
        }
        return lst;
    }

    @Override
    protected void onPause() {
        handler.removeCallbacksAndMessages(null);
        finish();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacksAndMessages(null);
        finish();
    }

    private class LFSlidePagerAdapter extends FragmentPagerAdapter {
        public LFSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fr[position];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }
}
