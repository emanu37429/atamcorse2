package it.emanu37429.atamcorse2.trainsfrag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.R;

public class InfoTreno extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_treno_fr);
        Fragment frg = new FragmentInfoTreno();
        frg.setArguments(getIntent().getExtras());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.TAfragg, frg);
        ft.commit();
    }
}
