package it.emanu37429.atamcorse2.offline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;

import java.util.ArrayList;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.MainActivity;
import it.emanu37429.atamcorse2.R;

public class OfflineActivity extends BaseActivity {
    ProgressDialog pDialog;
    AppCompatActivity act;
    ArrayList<ClassU.Linea> listlinee = new ArrayList<>();
    public static JSONArray objOff;
    private static final int NUM_PAGES = 2;
    String[] title = new String[]{"Linee", "Fermate"};
    Fragment[] fr = new Fragment[]{new FragmentOfflineLinee(), new FragmentOfflinePaline()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fragment_linee_fermate);
        this.setTitle("ATAM Corse (offline)");
        pDialog = ProgressDialog.show(this,"Caricamento in corso", "Attendere prego...");
        act = this;
        ViewPager mPager = findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new LFSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        getSupportActionBar().setElevation(0);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs_tr);
        tabLayout.setupWithViewPager(mPager);
        new Thread(() -> {
            objOff = getObjOffline(act);
            runOnUiThread(() -> {
                pDialog.dismiss();
                new AlertDialog.Builder(this)
                        .setTitle("Attenzione")
                        .setMessage("I dati utilizzati per la modalità offline sono reperiti dal dataset passaggi corse con ultimo aggiornamento a fine 2015. Alcune corse presenti potrebbero non essere più attive e viceversa potrebbero esserne state inserite di nuove che non vengono riportate. Semmai aggiorneranno i dataset sul sito OpenData provvedo a sistemare. Intanto non fare eccessivo affidamento sui dati forniti dalla modalità offline")
                        .setPositiveButton("Capito", null)
                        .show();
            });
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.switch_online, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ClassU.isNetworkAvailable(this)) {
            startActivity(new Intent(act, MainActivity.class));
            finish();
        } else {
            Toast.makeText(act, "Connessione non rilevata", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public static JSONArray getObjOffline(Context cx) {
        if (objOff != null) return objOff;
        else {
            try {
                return new JSONArray(ClassU.StringFromResource(cx, R.raw.offlinedataset));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
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
