package com.cgs.kkfh;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class FindActivity extends Fragment implements View.OnClickListener {
    EditText txt_people;
    EditText txt_patient;
    Spinner spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find1, container, false);

        txt_people = (EditText) view.findViewById(R.id.editText);
        txt_patient = (EditText) view.findViewById(R.id.editText2);

        Button click = (Button) view.findViewById(R.id.eiei);
        click.setOnClickListener(this);

        spinner = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.province_spin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View view) {
        Bundle data = new Bundle();
        data.putString("l_id", String.valueOf(spinner.getSelectedItemId()+1));
        data.putString("people", txt_people.getText().toString());
        data.putString("patient_people", txt_patient.getText().toString());

        Fragment fm = new Find_listview();
        fm.setArguments(data);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, fm);
        ft.commit();
    }
}
