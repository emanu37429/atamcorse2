package it.emanu37429.atamcorse2.atamcorse;

import android.os.Bundle;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.R;

public class FermataActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fermata);
        String id = getIntent().getExtras().getString("id");
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}
