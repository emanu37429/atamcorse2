package it.emanu37429.atamcorse2.trainsfrag;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.AdapterPrefOrari;

public class FragmentCercaDaAInput extends Fragment {


    public FragmentCercaDaAInput() {
    }

    Calendar cl = Calendar.getInstance();
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    EditText data, ora;
    public static AutoCompleteTextView selectda, selecta;
    FloatingActionButton btncerca, btnswap;
    Boolean cn = false;
    JSONArray jArray;
    public static VectorDrawableCompat img;
    ArrayAdapter adapter;
    AdapterPrefOrari adapterpref;
    ArrayList<String> list;
    ArrayList<ClassU.PreferenzaTreno> lstfn;
    RecyclerView mRecyclerView;
    Long now = Calendar.getInstance().getTimeInMillis();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_cerca_da_ainput, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        editor = prefs.edit();
        data = v.findViewById(R.id.txtCercaData);
        data.setFocusable(false);
        ora = v.findViewById(R.id.txtCercaOra);
        ora.setFocusable(false);
        selecta = v.findViewById(R.id.txtCercaA);
        selectda = v.findViewById(R.id.txtCercaDa);
        btncerca = v.findViewById(R.id.btnCercaCerca);
        btnswap = v.findViewById(R.id.btnswap);
        mRecyclerView = v.findViewById(R.id.recpreforario);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        img = VectorDrawableCompat.create(getResources(), R.drawable.ic_baseline_check_24px, getContext().getTheme());
        addList();
        new Thread(() -> {
            try {
                jArray = new JSONArray(ClassU.StringFromResource(getContext(), R.raw.stazioni));
                adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item);
                for (int i = 0; i < jArray.length(); i++) {
                    adapter.add(jArray.getJSONObject(i).getString("name"));
                }
                getActivity().runOnUiThread(() -> {
                    selectda.setAdapter(adapter);
                    selecta.setAdapter(adapter);
                    data.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(cl.getTime()));
                    ora.setText(String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
                    data.setOnClickListener(view -> {
                        DatePickerDialog dg = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
                            cl.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                            data.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(cl.getTime()));
                        }, cl.get(Calendar.YEAR), cl.get(Calendar.MONTH), cl.get(Calendar.DAY_OF_MONTH));
                        dg.getDatePicker().setMinDate(now);
                        dg.show();
                    });
                    ora.setOnClickListener(view -> {
                        final NumberPicker numberPicker = new NumberPicker(getActivity());
                        numberPicker.setMinValue(0);
                        numberPicker.setMaxValue(23);
                        numberPicker.setValue(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Scegli l'ora di partenza");
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            ora.setText(String.valueOf(numberPicker.getValue()));
                        });
                        builder.setNegativeButton("Annulla", null);
                        builder.setView(numberPicker);
                        builder.show();
                    });
                    selectda.setOnItemClickListener((adapterView, view, i, l) -> {
                        selectda.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    });
                    selectda.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (!selectda.isPerformingCompletion() && !cn) {
                                selectda.setCompoundDrawables(null, null, null, null);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    selecta.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (!selecta.isPerformingCompletion() && !cn) {
                                selecta.setCompoundDrawables(null, null, null, null);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    selecta.setOnItemClickListener((adapterView, view, i, l) -> {
                        selecta.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    });
                    btncerca.setOnClickListener(view -> {
                        if (selecta.getCompoundDrawables()[2] != null && selectda.getCompoundDrawables()[2] != null) {
                            Intent actcercadaa = new Intent(getContext(), CercaTrenoDaA.class);
                            actcercadaa.putExtra("data", data.getText().toString());
                            actcercadaa.putExtra("ora", ora.getText().toString());
                            actcercadaa.putExtra("part", selectda.getText().toString().toUpperCase());
                            actcercadaa.putExtra("arr", selecta.getText().toString().toUpperCase());
                            if (list == null) list = new ArrayList<>();
                            if (list.size() > 10) {
                                list.remove(0);
                            }
                            list.add(selectda.getText().toString() + ";" + selectda.getText().toString() + ";" + selecta.getText().toString() + ";" + selectda.getText().toString());
                            editor.putStringSet("prefcerca", new HashSet<>(list)).commit();
                            addList();
                            startActivity(actcercadaa);
                        } else
                            Toast.makeText(getContext(), "Seleziona prima le stazioni.", Toast.LENGTH_LONG).show();
                    });
                    btnswap.setOnClickListener(view -> {
                        if (selecta.getCompoundDrawables()[2] != null && selectda.getCompoundDrawables()[2] != null) {
                            cn = true;
                            String tmp = selectda.getText().toString();
                            selectda.setText(selecta.getText());
                            selecta.setText(tmp);
                            selectda.dismissDropDown();
                            selecta.dismissDropDown();
                            selectda.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                            selecta.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                            cn = false;
                        }
                    });
                });
            } catch (Exception e) {
                OnError(e);
            }
        }).start();
        return v;
    }

    void addList() {
        if (prefs.contains("prefcerca")) {
            list = new ArrayList<>(prefs.getStringSet("prefcerca", null));
            lstfn = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                String[] pref = list.get(i).split(";");
                lstfn.add(new ClassU.PreferenzaTreno(pref[0], pref[1], pref[2], pref[3]));
            }
            adapterpref = new AdapterPrefOrari(lstfn);
            mRecyclerView.setAdapter(adapterpref);
        }
    }

    public void OnError(Exception e) {
        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
    }
}
