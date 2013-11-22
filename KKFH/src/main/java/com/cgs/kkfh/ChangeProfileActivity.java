package com.cgs.kkfh;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by cbnuke on 11/23/13 AD.
 */
public class ChangeProfileActivity extends Fragment implements View.OnClickListener{
    TextView txt_name;
    TextView txt_phone;
    TextView txt_disease;
    Button btn_sign;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_register, container, false);

        btn_sign = (Button) view.findViewById(R.id.btnReg_signup);
        btn_sign.setOnClickListener(this);

        txt_name = (TextView) view.findViewById(R.id.etReg_name);
        txt_phone = (TextView) view.findViewById(R.id.etReg_phone);
        txt_disease = (TextView) view.findViewById(R.id.etReg_cd);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnReg_signup) {
            String name = txt_name.getText().toString();
            String phone = txt_phone.getText().toString();
            String disease = txt_disease.getText().toString();

            if (name.isEmpty() || phone.isEmpty() || disease.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all of information.", Toast.LENGTH_LONG).show();
            } else {
                SQLiteControl db = new SQLiteControl(getActivity());
                db.getWritableDatabase();
                if (db.updateData(name, phone, disease)) {
                    Toast.makeText(getActivity(), "Update complete.", Toast.LENGTH_LONG).show();

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new MainActivity.PlaceholderFragment().newInstance(1))
                            .commit();
                }else{
                    Toast.makeText(getActivity(), "Update fail.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
