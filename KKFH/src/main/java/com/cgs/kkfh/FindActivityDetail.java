package com.cgs.kkfh;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class FindActivityDetail extends Fragment {
    /**
     * Called when the activity is first created.
     */
    private TextView mTitleText;
    private TextView mDateText;
    private TextView mDescriptionText;
    private TextView mMaxPeople;
    private TextView mCurrentPeople;
    private Button btn;

    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;
    private double mlat;
    private double mLong;
    private String mName;
    private int number_picker = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find3, container, false);

        // Get XML values from previous Bundle
        String title = this.getArguments().getString(Find_listview.KEY_NAME);
        mName = title;
        String date = this.getArguments().getString(Find_listview.KEY_DATE);
        String description = this.getArguments().getString(Find_listview.KEY_DESCRIPTION);
        String current = this.getArguments().getString(Find_listview.KEY_CURRENT_PEOPLE);
        String max = this.getArguments().getString(Find_listview.KEY_MAX_PEOPLE);
        mlat = Double.parseDouble(this.getArguments().getString(Find_listview.KEY_W_LAT));
        mLong = Double.parseDouble(this.getArguments().getString(Find_listview.KEY_W_LONG));

        Log.d("KKFHD", Double.toString(mlat) + "|" + Double.toString(mLong));

        btn = (Button) view.findViewById(R.id.button);
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.l_lat = 16.44476176300003;
                MainActivity.l_long = 102.81387556000004;
                if (MainActivity.l_lat != 0 && MainActivity.l_long != 0) {
                    /*string Text HomeActivity*/
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.title_findActivityDetail))
                            .setMessage(getString(R.string.msg_findActivityDetail));

                    final FrameLayout frameView = new FrameLayout(getActivity());
                    builder.setView(frameView);
                    builder.setNegativeButton(getString(R.string.cancel_helpActivity), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.setPositiveButton(getString(R.string.send_helpActivity), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d("KKFHD", Integer.toString(number_picker));

                            //Send data and change fragment
                            Bundle data = new Bundle();
                            data.putString(RouteActivity.KEY_D_LAT, Double.toString(mlat));
                            data.putString(RouteActivity.KEY_D_LONG, Double.toString(mLong));
                            data.putString(RouteActivity.KEY_C_LAT, Double.toString(MainActivity.l_lat));
                            data.putString(RouteActivity.KEY_C_LONG, Double.toString(MainActivity.l_long));
                            data.putString(RouteActivity.KEY_WATER_LEVEL, Integer.toString(number_picker));
                            data.putString(RouteActivity.KEY_NAME, mName);

                            Fragment fm = new RouteActivity();
                            fm.setArguments(data);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.container, fm);
                            ft.commit();
                        }
                    });

                    final AlertDialog alertDialog = builder.create();
                    LayoutInflater inflater = alertDialog.getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.fragment_help_dialog, frameView);
                    alertDialog.show();

                    final NumberPicker np = (NumberPicker) dialoglayout.findViewById(R.id.numberPicker1);
                    np.setMaxValue(100);
                    np.setMinValue(0);
                    np.setWrapSelectorWheel(false);
                    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                            number_picker = i2;
                        }
                    });
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.fail_helpActivity))
                            .setMessage(getString(R.string.failMsg_helpActivity));
                    builder1.setPositiveButton(getString(R.string.ok_helpActivity), null);
                    builder1.show();
                }
            }
        });

        mTitleText = (TextView) view.findViewById(R.id.tvName_find3);
        mDateText = (TextView) view.findViewById(R.id.tvDoc_find3);
        mDescriptionText = (TextView) view.findViewById(R.id.tvAdd_find3);
        mCurrentPeople = (TextView) view.findViewById(R.id.tvPeo_find3);
        mMaxPeople = (TextView) view.findViewById(R.id.tvGet_find3);


        mTitleText.setText(title);
        mDateText.setText(date);
        mDescriptionText.setText(description);
        mCurrentPeople.setText(current);
        mMaxPeople.setText(max);


        try {
            MapsInitializer.initialize(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO handle this situation
        }

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded(view);

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
    }

    private void setUpMapIfNeeded(View inflatedView) {
        if (mMap == null) {
            mMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
            mMap.setMyLocationEnabled(true);

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mlat, mLong))
                    .title(mName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mlat, mLong), 14));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
}
