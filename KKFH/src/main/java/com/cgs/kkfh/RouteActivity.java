package com.cgs.kkfh;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cbnuke on 11/1/13 AD.
 */
public class RouteActivity extends Fragment {
    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_route, container, false);

        try {
            MapsInitializer.initialize(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO handle this situation
        }

        mMapView = (MapView) inflatedView.findViewById(R.id.map);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded(inflatedView);

        LatLng test = new LatLng(16.44476176300003, 102.81387556000004);


        //Route Asyntask
//        RouteActivityAsynTask obj = new RouteActivityAsynTask("11445173.830729133", "1856251.5078629067",
//                "11446205.730610982", "1859681.6195071994", "0.1");

        RouteActivityAsynTask obj = new RouteActivityAsynTask("102.81387556000004", "16.44476176300003",
                "102.82301550400007", "16.47401208200006", "0.1");

        obj.execute();

        return inflatedView;
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

    private void DrawRoute(ArrayList<LatLng> data) {
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions();

        for (int i = 0; i < data.size(); i++) {
            rectOptions.add(data.get(i));
        }

        // Get back the mutable Polyline
        Polyline polyline = mMap.addPolyline(rectOptions);
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

    private class RouteActivityAsynTask extends AsyncTask<Context, Integer, String> {
        // JSON Node names
        private static final String TAG_PARAMNAME = "paramName";
        private static final String TAG_DATATYPE = "dataType";
        private static final String TAG_VALUE = "value";
        private static final String TAG_GEOMETRYTYPE = "geometryType";
        private static final String TAG_SPATIALREFERENCE = "spatialReference";
        private static final String TAG_WKID = "wkid";
        private static final String TAG_FEATURES = "features";
        private static final String TAG_GEOMETRY = "geometry";
        private static final String TAG_PATHS = "paths";

        private String C_LAT, C_LONG, D_LAT, D_LONG, WATER_LEVEL;

        GetRouteData obj;
        int MAX_CHECK_LOOP = 200;
        String json_data;

        public RouteActivityAsynTask(String c_lat, String c_long, String d_lat, String d_long, String water_level) {
            C_LAT = c_lat;
            C_LONG = c_long;
            D_LAT = d_lat;
            D_LONG = d_long;
            WATER_LEVEL = water_level;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            obj = new GetRouteData();
        }

        protected String doInBackground(Context... params) {

            obj.SendData(C_LAT, C_LONG, D_LAT, D_LONG, WATER_LEVEL);

            while (obj.CheckData(obj.jobId) && MAX_CHECK_LOOP > 0) {
                MAX_CHECK_LOOP--;
                if (obj.jobStatus.equalsIgnoreCase("esriJobSucceeded")) {
                    json_data = obj.ResultData(obj.jobId);
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
            //Log.d("KKFHD", "DATA STATUS:" + json_data);

            if (obj.jobStatus.equalsIgnoreCase("esriJobSucceeded")) {
                try {
                    JSONObject jsonObject = new JSONObject(json_data);
                    ArrayList<LatLng> data = new ArrayList<LatLng>();

                    //Features Node
                    JSONArray ft = jsonObject.getJSONObject(TAG_VALUE).getJSONArray(TAG_FEATURES);
                    //paths node
                    JSONArray arr = ft.getJSONObject(0).getJSONObject(TAG_GEOMETRY).getJSONArray(TAG_PATHS);
                    //Position node
                    JSONArray pointArr = arr.getJSONArray(0);
                    //child Position node
                    JSONArray AnatomypointArr = arr.getJSONArray(0).getJSONArray(0);

                    for (int i = 0; i < pointArr.length(); i++) {
                        String position = pointArr.getString(i);
                        data.add(new LatLng(Double.parseDouble(pointArr.getJSONArray(i).getString(1)),
                                Double.parseDouble(pointArr.getJSONArray(i).getString(0))));
                        String x = pointArr.getJSONArray(i).getString(1);
                        String y = pointArr.getJSONArray(i).getString(0);
                        Log.d("KKFHD", x + "," + y);
                    }

                    DrawRoute(data);

                } catch (JSONException e) {
                    Log.e("KKFHD", "Error parsing data " + e.toString());
                }
            }else{
                Toast.makeText(getActivity(),"",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
