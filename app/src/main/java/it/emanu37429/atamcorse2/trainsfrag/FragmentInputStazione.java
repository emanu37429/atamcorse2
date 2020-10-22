package it.emanu37429.atamcorse2.trainsfrag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

import it.emanu37429.atamcorse2.ClassU;
import it.emanu37429.atamcorse2.R;
import it.emanu37429.atamcorse2.adapters.AdapterRecStaz;


public class FragmentInputStazione extends Fragment {
    public FragmentInputStazione() {
    }

    JSONArray jArray;
    ArrayAdapter<String> adapter;
    public static String idStazione;
    public static AutoCompleteTextView inputStazione;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    FloatingActionButton btncerca;
    AdapterRecStaz adapterpref;
    public static VectorDrawableCompat img;
    ArrayList<String> list;
    RecyclerView mRecyclerView;
    ArrayList<ClassU.RecStazione> lstfn = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fragment_input_stazione, container, false);
        inputStazione = v.findViewById(R.id.inputStazione);
        btncerca = v.findViewById(R.id.btnCercaStazione);
        img = VectorDrawableCompat.create(getResources(), R.drawable.ic_baseline_check_24px, getContext().getTheme());
        prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        editor = prefs.edit();
        mRecyclerView = v.findViewById(R.id.recStazioniPref);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), llm.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        AsyncTask.execute(() -> {
            try {
                jArray = new JSONArray(ClassU.StringFromResource(getContext(), R.raw.stazioni));
                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item);
                for (int i = 0; i < jArray.length(); i++) {
                    if (!jArray.getJSONObject(i).getString("idvt").equals(""))
                        adapter.add(jArray.getJSONObject(i).getString("name"));
                }
                addList();
                getActivity().runOnUiThread(() -> {
                    inputStazione.setAdapter(adapter);
                    inputStazione.setThreshold(1);
                    inputStazione.setAdapter(adapter);
                    btncerca.setOnClickListener(view -> {
                        if (!idStazione.equals("")) {
                            Intent actStazioneArriviPartenze = new Intent(getContext(), StazioneArriviPartenze.class);
                            actStazioneArriviPartenze.putExtra("id", idStazione);
                            String cd = inputStazione.getText().toString() + ";" + idStazione;
                            if (list == null) list = new ArrayList<>();
                            if (list.size() > 10) {
                                list.remove(10);
                            }
                            if (list.contains(cd)) {
                                list.remove(list.indexOf(cd));
                                list.add(0, cd);
                            } else
                                list.add(0, cd);
                            editor.putString("prefstaz1", TextUtils.join("#", list)).commit();
                            addList();
                            startActivity(actStazioneArriviPartenze);
                        } else {
                            Toast.makeText(getContext(), "Seleziona una stazione", Toast.LENGTH_SHORT).show();
                        }
                    });
                    inputStazione.setOnItemClickListener((adapterView, view, i, l) -> {
                        try {
                            String tc = adapter.getItem(i);
                            for (int i2 = 0; i2 < jArray.length(); i2++) {
                                if (jArray.getJSONObject(i2).getString("name").equals(tc)) {
                                    idStazione = jArray.getJSONObject(i2).getString("idvt");
                                    inputStazione.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                    inputStazione.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (!inputStazione.isPerformingCompletion()) {
                                inputStazione.setCompoundDrawables(null, null, null, null);
                                idStazione = "";
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return v;
    }

    void addList() {
        if (prefs.contains("prefstaz1")) {
            list = new ArrayList<String>(Arrays.asList(prefs.getString("prefstaz1", null).split("#")));
            lstfn = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                String[] pref = list.get(i).split(";");
                lstfn.add(new ClassU.RecStazione(pref[0], pref[1]));
            }
            getActivity().runOnUiThread(() -> {
                adapterpref = new AdapterRecStaz(lstfn);
                mRecyclerView.setAdapter(adapterpref);
            });
        }
    }
}
