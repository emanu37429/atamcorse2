package it.emanu37429.atamcorse2.varie;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.emanu37429.atamcorse2.R;

public class FragmentPreferenze extends Fragment {


    public FragmentPreferenze() {
    }

    EditText updseccorsa, ncorsepal, offstferlp;
    CheckBox evLstFerChk, chkDsnPl;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_preferenze, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = pref.edit();
        FloatingActionButton btnsave = v.findViewById(R.id.btnsavepref);
        FloatingActionButton btnreset = v.findViewById(R.id.btnresetpref);
        updseccorsa = v.findViewById(R.id.txtSeccorsa);
        ncorsepal = v.findViewById(R.id.txtCorsePal);
        evLstFerChk = v.findViewById(R.id.chkLstFerLP);
        offstferlp = v.findViewById(R.id.txtOffLp);
        chkDsnPl = v.findViewById(R.id.chkDsnPl);
        Settext();
        btnsave.setOnClickListener(view -> SaveAll());
        btnreset.setOnClickListener(view -> {
            editor.remove("evLstFer");
            editor.remove("seccorsa");
            editor.remove("ncorsepal");
            editor.remove("offsetlp");
            editor.remove("dsnpl");
            editor.commit();
            Settext();
        });
        return v;
    }

    void Settext() {
        updseccorsa.setText(String.valueOf(pref.getInt("seccorsa", 20000) / 1000));
        offstferlp.setText(String.valueOf(pref.getInt("offsetlp", 1)));
        ncorsepal.setText(String.valueOf(pref.getInt("ncorsepal", 15)));
        chkDsnPl.setChecked(pref.getBoolean("dsnpl", false));
        evLstFerChk.setChecked(pref.getBoolean("evLstFer", false));
    }

    void SaveAll() {
        if (Integer.parseInt(updseccorsa.getText().toString()) >= 10 && Integer.parseInt(ncorsepal.getText().toString()) > 10) {
            editor.putInt("seccorsa", Integer.parseInt(updseccorsa.getText().toString()) * 1000);
            editor.putInt("ncorsepal", Integer.parseInt(ncorsepal.getText().toString()));
            editor.putBoolean("evLstFer", evLstFerChk.isChecked());
            editor.putBoolean("dsnpl", chkDsnPl.isChecked());
            editor.putInt("offsetlp", Integer.parseInt(offstferlp.getText().toString()));
            editor.commit();
            Toast.makeText(getContext(), "Fatto", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "Inserisci valori maggiori o uguali a 10", Toast.LENGTH_LONG).show();
    }
}
