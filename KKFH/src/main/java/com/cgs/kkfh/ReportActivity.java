package com.cgs.kkfh;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Amnart on 29/11/2556.
 */
public class ReportActivity extends Fragment implements View.OnClickListener {
    Button btn_send;
    Button btn_back;
    EditText edit_height;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        btn_back = (Button) view.findViewById(R.id.btnR_back);
        btn_back.setOnClickListener(this);
        btn_send = (Button) view.findViewById(R.id.btnR_send);
        btn_send.setOnClickListener(this);
        edit_height = (EditText) view.findViewById(R.id.editR_height);

        return view;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (view.getId()) {
            case R.id.btnR_back:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new HomeActivity())
                        .commit();
                break;
            case R.id.btnR_send:
                String txt = edit_height.getText().toString();
                /*string Toast ReportActivity*/
                if (!txt.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.toast1_reportActivity), Toast.LENGTH_LONG).show();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new HomeActivity())
                            .commit();
                } else {
                    /*string Toast ReportActivity*/
                    Toast.makeText(getActivity(), getString(R.string.toast2_reportActivity), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
