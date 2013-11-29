package com.cgs.kkfh;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Amnart on 29/11/2556.
 */
public class HomeActivity extends Fragment implements View.OnClickListener {
    Button btn_help;
    Button btn_report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btn_help = (Button)view.findViewById(R.id.btnH_help);
        btn_help.setOnClickListener(this);
        btn_report = (Button)view.findViewById(R.id.btnH_report);
        btn_report.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (view.getId()){
            case R.id.btnH_help:
                break;
            case R.id.btnH_report:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ReportActivity())
                        .commit();
                break;
        }
    }
}
