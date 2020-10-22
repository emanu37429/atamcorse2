package it.emanu37429.atamcorse2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;

import it.emanu37429.atamcorse2.atamcorse.FragmentCercaBusDaAInput;
import it.emanu37429.atamcorse2.atamcorse.FragmentInfo;
import it.emanu37429.atamcorse2.atamcorse.FragmentLineeFermate;
import it.emanu37429.atamcorse2.atamcorse.FragmentNews;
import it.emanu37429.atamcorse2.atamcorse.MapFragment;
import it.emanu37429.atamcorse2.offline.OfflineActivity;
import it.emanu37429.atamcorse2.trainsfrag.FragmentCercaDaAInput;
import it.emanu37429.atamcorse2.trainsfrag.FragmentInfoTrenoCerca0;
import it.emanu37429.atamcorse2.trainsfrag.FragmentInputStazione;
import it.emanu37429.atamcorse2.trainsfrag.FragmentNewsRFI;
import it.emanu37429.atamcorse2.trainsfrag.FragmentNewsTI;
import it.emanu37429.atamcorse2.trainsfrag.FragmentWebViewVT;
import it.emanu37429.atamcorse2.trainsfrag.StationMapFragment;
import it.emanu37429.atamcorse2.varie.FragmentPreferenze;
import it.emanu37429.atamcorse2.varie.FragmentVarie;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    Toast ts;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!pref.getString("ver", "0").equals("41")) {
            SharedPreferences.Editor edit = pref.edit();
            edit.clear();
            edit.putString("ver", "41");
            edit.commit();
        }
        if (ClassU.isNetworkAvailable(this)) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_lineefermate);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new FragmentLineeFermate());
            ft.commit();
            ts = Toast.makeText(this, "Premere di nuovo per uscire", Toast.LENGTH_LONG);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Connessione assente")
                    .setMessage("Non rilevo alcuna connessione attiva. Abilita la connessione e riapri l'app. In alternativa, se non hai una connessione a disposizione, puoi provare la modalità offline. Ti verranno mostrate le corse previste in base a un dataset pubblicato dal comune sul sito OpenData nel 2015. Non si garantisce l'accuratezza dei dati forniti visti i cambiamenti occorsi alla programmazione delle corse in questi anni. Per caricare il dataset saranno necessari circa 30 secondi.")
                    .setNegativeButton("Chiudi", (a,b) -> {
                        finishAffinity();
                    })
                    .setPositiveButton("Modalità offline", (a,b) -> {
                        startActivity(new Intent(getApplicationContext(), OfflineActivity.class));
                        finish();
                    })
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (ts.getView().isShown()) {
                ts.cancel();
                super.onBackPressed();
            } else
                ts.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_offline) {
            startActivity(new Intent(getApplicationContext(), OfflineActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_lineefermate:
                fragment = new FragmentLineeFermate();
                break;
            case R.id.nav_mappa:
                fragment = new MapFragment();
                break;
            case R.id.nav_preferenze:
                fragment = new FragmentPreferenze();
                break;
            case R.id.nav_news:
                fragment = new FragmentNews();
                break;
            case R.id.nav_percorsi_cerca:
                fragment = new FragmentCercaBusDaAInput();
                break;
            case R.id.nav_varie:
                fragment = new FragmentVarie();
                break;
            case R.id.nav_mappast:
                fragment = new StationMapFragment();
                break;
            case R.id.nav_cercatr:
                fragment = new FragmentCercaDaAInput();
                break;
            case R.id.nav_infotreno:
                fragment = new FragmentInfoTrenoCerca0();
                break;
            case R.id.nav_infomb:
                fragment = new FragmentNewsRFI();
                break;
            case R.id.nav_infotr:
                fragment = new FragmentWebViewVT();
                break;
            case R.id.nav_atcnews:
                fragment = new FragmentNewsTI();
                ((FragmentNewsTI) fragment).link = ClassU.linkNewsAtamcorse;
                break;
            case R.id.cercast:
                fragment = new FragmentInputStazione();
                break;
            case R.id.nav_info:
                fragment = new FragmentInfo();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
