package it.emanu37429.atamcorse2.atamcorse;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;

public class FragmentCercaBusDaAInput extends Fragment {


    public FragmentCercaBusDaAInput() {
    }

    AutoCompleteTextView txtda, txta;
    EditText timeInput;
    FloatingActionButton fabgo;
    JSONArray jArray;
    ArrayAdapter adapter;
    VectorDrawableCompat img;
    SimpleDateFormat tmf = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_cerca_bus_da, container, false);
        txtda = v.findViewById(R.id.txtferpart);
        txta = v.findViewById(R.id.txtferarr);
        fabgo = v.findViewById(R.id.fabgo);
        timeInput = v.findViewById(R.id.txtCercaOra);
        img = VectorDrawableCompat.create(getResources(), R.drawable.ic_baseline_check_24px, getContext().getTheme());
        txtda.requestFocus();
        new Thread(() -> {
            try {
                jArray = new JSONArray(ClassU.StringFromResource(getContext(), R.raw.palinelist));
                ArrayList<String> listfer = new ArrayList<>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject obj = jArray.getJSONObject(i);
                    listfer.add(obj.getString("fermata") + " > " + obj.getString("num"));
                }
                getActivity().runOnUiThread(() -> {
                    adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, listfer);
                    txtda.setAdapter(adapter);
                    txta.setAdapter(adapter);
                    txtda.setOnItemClickListener((adapterView, view, i, l) -> {
                        txtda.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    });
                    txtda.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (!txtda.isPerformingCompletion()) {
                                txtda.setCompoundDrawables(null, null, null, null);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    txta.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (!txta.isPerformingCompletion()) {
                                txta.setCompoundDrawables(null, null, null, null);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    txta.setOnItemClickListener((adapterView, view, i, l) -> {
                        txta.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    });
                    fabgo.setOnClickListener(view -> {
                        try {
                            if (txta.getCompoundDrawables()[2] != null && txtda.getCompoundDrawables()[2] != null) {
                                Intent actcercadaa = new Intent(getContext(), CercaBusDaA.class);
                                actcercadaa.putExtra("ferpart", String.valueOf(Integer.parseInt(txtda.getText().toString().split(" > ")[1])+4000000));
                                actcercadaa.putExtra("ferarr", String.valueOf(Integer.parseInt(txta.getText().toString().split(" > ")[1])+4000000));
                                actcercadaa.putExtra("dt", timeInput.getText().toString());
                                startActivity(actcercadaa);
                            } else
                                Toast.makeText(getContext(), "Seleziona prima le fermate.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    timeInput.setFocusable(false);
                    timeInput.setText(tmf.format(Calendar.getInstance().getTimeInMillis()));
                    timeInput.setOnClickListener((tm)->{
                        String[] sr = timeInput.getText().toString().split(":");
                        int[] hm = new int[]{Integer.parseInt(sr[0]), Integer.parseInt(sr[1])};
                        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                timeInput.setText(hourOfDay + ":" + minute);
                            }
                        }, hm[0], hm[1],true).show();
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return v;
    }
}
