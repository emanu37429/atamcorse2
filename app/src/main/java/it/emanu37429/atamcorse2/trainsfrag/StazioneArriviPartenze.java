package it.emanu37429.atamcorse2.trainsfrag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

public class StazioneArriviPartenze extends BaseActivity {

    private static final int NUM_PAGES = 2;
    String id, data;
    String[] title = {"Partenze", "Arrivi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stazione_arrivi_partenze);
        try {
            getSupportActionBar().setElevation(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewPager mPager = findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs_tr);
        tabLayout.setupWithViewPager(mPager);
        id = getIntent().getExtras().getString("id");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        data = sdf.format(Calendar.getInstance().getTime());
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fr = new FragmentArrPartStaz();
            Bundle bnd = new Bundle();
            switch (position) {
                case 0:
                    bnd.putString("url", ClassU.linkVGTPartenze + id + "/" + data);
                    bnd.putString("bin", "binarioProgrammatoPartenzaDescrizione");
                    bnd.putString("binre", "binarioEffettivoPartenzaDescrizione");
                    bnd.putString("ora", "compOrarioPartenza");
                    bnd.putString("origine", "destinazione");
                    bnd.putString("dp", "Per: ");
                    break;
                case 1:
                    bnd.putString("url", ClassU.linkVGTArrivi + id + "/" + data);
                    bnd.putString("bin", "binarioProgrammatoArrivoDescrizione");
                    bnd.putString("binre", "binarioEffettivoArrivoDescrizione");
                    bnd.putString("ora", "compOrarioArrivo");
                    bnd.putString("origine", "origine");
                    bnd.putString("dp", "Da: ");
                    break;
            }
            fr.setArguments(bnd);
            return fr;
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

    public static class TrenoDaStazione {
        public String StazPartenza, Treno, OrarioArrivo, Binario, BinarioReale, Orientamento, StatoRitardo, CodTreno, CodOrigine, Ritardo;
        public Boolean Attivo;

        public TrenoDaStazione(String stazPartenza, String treno, String orarioArrivo, String binario, String binarioReale, Boolean attivo, String orientamento, String statoRitardo, String codTreno, String codOrigine, String ritardo) {
            this.StazPartenza = stazPartenza;
            this.Treno = treno;
            this.OrarioArrivo = orarioArrivo;
            this.Binario = binario;
            this.BinarioReale = binarioReale;
            this.Attivo = attivo;
            this.Orientamento = orientamento;
            this.StatoRitardo = statoRitardo;
            this.CodTreno = codTreno;
            this.CodOrigine = codOrigine;
            this.Ritardo = ritardo;
        }
    }
}
