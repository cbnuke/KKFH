package com.cgs.kkfh;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by cbnuke on 11/1/13 AD.
 */
public class TestActivity extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);


        ImageView img = (ImageView)view.findViewById(R.id.imageView);
        img.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getActivity(), "msg msg", Toast.LENGTH_LONG).show();
    }
}
