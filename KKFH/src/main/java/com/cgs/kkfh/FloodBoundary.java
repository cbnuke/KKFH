package com.cgs.kkfh;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbnuke on 12/25/13 AD.
 */
public class FloodBoundary extends Fragment {
    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;
    private int number_picker = 0;

    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        try {
            MapsInitializer.initialize(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO handle this situation
        }
        MainActivity.l_lat = 16.465120;
        MainActivity.l_long = 102.815290;
        if (MainActivity.l_lat != 0 && MainActivity.l_long != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.title_findActivityDetail))
                    .setMessage(getString(R.string.msg_findActivityDetail));

            final FrameLayout frameView = new FrameLayout(getActivity());
            builder.setView(frameView);
            builder.setNegativeButton(getString(R.string.cancel_helpActivity), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Fragment fm = new HomeActivity();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fm);
                    ft.commit();
                }
            });
            builder.setPositiveButton(getString(R.string.send_helpActivity), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.d("KKFHD", Integer.toString(number_picker));

                    //Process check//
                    FloodBoundaryAsynTask obj = new FloodBoundaryAsynTask(Double.toString(16.4728667),
                            Double.toString(102.8211667), Integer.toString(number_picker));
                    obj.execute();


                    //DrawPolygon();
                }
            });
            final AlertDialog alertDialog = builder.create();
            LayoutInflater inflater2 = alertDialog.getLayoutInflater();
            View dialoglayout = inflater2.inflate(R.layout.fragment_help_dialog, frameView);
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
            builder1.setPositiveButton(getString(R.string.ok_helpActivity), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Fragment fm = new HomeActivity();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fm);
                    ft.commit();
                }
            });
            builder1.show();
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

    private class FloodBoundaryAsynTask extends AsyncTask<Context, Integer, String> {
        private String C_LAT, C_LONG, WATER_LEVEL;

        GetRouteData obj;
        int MAX_CHECK_LOOP = 20;
        String json_data;

        public FloodBoundaryAsynTask(String c_lat, String c_long, String water_level) {
            C_LAT = c_lat;
            C_LONG = c_long;
            WATER_LEVEL = water_level;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            obj = new GetRouteData();
            dialog = ProgressDialog.show(getActivity(), getString(R.string.loading), getString(R.string.please_wait), true);
        }

        protected String doInBackground(Context... params) {

            obj.SendData_flood(C_LAT, C_LONG, WATER_LEVEL);

            while (obj.CheckData_flood(obj.jobId) && MAX_CHECK_LOOP > 0) {
                MAX_CHECK_LOOP--;
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    Log.d("KKFHD", "Error get data route");
                }

                if (obj.jobStatus.equalsIgnoreCase("esriJobSucceeded")) {
                    json_data = obj.ResultData_flood(obj.jobId);
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("KKFHD", "DATA ID:" + obj.jobId);
            Log.d("KKFHD", "DATA STATUS:" + obj.jobStatus);

            if (obj.jobStatus.equalsIgnoreCase("esriJobSucceeded")) {
                try {
                    JSONObject data = new JSONObject(json_data);
                    String paraName = data.getString("paramName");
                    JSONObject getDataValue = data.getJSONObject("value");
                    JSONArray getArrayfeatures = getDataValue.getJSONArray("features");


                    for (int i = 0; i < getArrayfeatures.length(); i++) {
                        JSONObject features = getArrayfeatures.getJSONObject(i);
                        JSONArray ring = features.getJSONObject("geometry").getJSONArray("rings");
                        Log.d("KKFHD", "No:" + i);
                        for (int j = 0; j < ring.length(); j++) {
                            //ring j
                            JSONArray ArRing = ring.getJSONArray(j);
                            Log.d("KKFHD", "Ring:" + j);
                            PolygonOptions rectOptions = new PolygonOptions();
                            ArrayList<LatLng> data2 = new ArrayList<LatLng>();
                            for (int k = 0; k < ArRing.length(); k++) {
                                //Array in lud long
                                String x = ArRing.getJSONArray(k).getString(1);
                                String y = ArRing.getJSONArray(k).getString(0);
                                data2.add(new LatLng(Double.parseDouble(x), Double.parseDouble(y)));
                            }
                            for (LatLng location : data2) {
                                rectOptions.add(location);
                            }
                            Polygon polygon = mMap.addPolygon(rectOptions.strokeColor(Color.CYAN).fillColor(0x9933b5e5));
                        }

//                        PolygonOptions rectOptions = new PolygonOptions();
//                        ArrayList<LatLng> data2 = new ArrayList<LatLng>();
//                        data2.add(new LatLng(16.475496627000041,102.78394238500005));
//                        data2.add(new LatLng(16.476209023000024,102.78411516400007));
//                        data2.add(new LatLng(16.476434219000055,102.78359285200008));
//                        data2.add(new LatLng(16.475634628000023,102.78296526400004));
//                        data2.add(new LatLng(16.475496627000041,102.78394238500005));
//                        rectOptions.addHole(data2);
//                        Polygon polygon = mMap.addPolygon(rectOptions);

                        //Animated to current location
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(16.489271507000069, 102.80697096000006), 14));
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(16.475496627000041, 102.78394238500005), 18));
                    }

                    //Close progress box
                    dialog.dismiss();
                    //DrawRoute();
                } catch (JSONException e) {
                    Log.e("KKFHD", "Error parsing data " + e.toString());
                }
            } else {
                //Close progress box
                dialog.dismiss();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.fail_helpActivity))
                        .setMessage(getString(R.string.failMsg_navigation));
                builder1.setPositiveButton(getString(R.string.ok_helpActivity), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Fragment fm = new HomeActivity();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container, fm);
                        ft.commit();
                    }
                });
                builder1.show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
