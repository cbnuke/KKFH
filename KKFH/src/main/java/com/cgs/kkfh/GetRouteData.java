package com.cgs.kkfh;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbnuke on 12/22/13 AD.
 */
public class GetRouteData {

    String URL_Submit = "http://g-con.giskku.in.th/ArcGIS/rest/services/Route/GPServer/Route/submitJob";
    String URL_Submit_Flood = "http://g-con.giskku.in.th/ArcGIS/rest/services/flood/GPServer/flood/submitJob";

    //Data
    String jobId, jobStatus;

    public boolean SendData(String c_lat, String c_long, String d_lat, String d_long, String water_level) {
        String User_Point = "{\"features\":[{\"attributes\":{\"FID\":1},\"geometry\":{\"x\":" + c_lat + ",\"y\":" + c_long + ",\"spatialReference\":{\"wkid\":4326}}},{\"attributes\":{\"FID\":2},\"geometry\":{\"x\":" + d_lat + ",\"y\":" + d_long + ",\"spatialReference\":{\"wkid\":4326}}}],\"geometryType\":\"esriGeometryPoint\"}";
        String Water_Level = water_level;
        String Car_Type = "Truck";
        String Out_SR = "4326";
        String Format = "pjson";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("User_Point", User_Point));
        params.add(new BasicNameValuePair("Water_Level", Water_Level));
        params.add(new BasicNameValuePair("Car_Type", Car_Type));
        params.add(new BasicNameValuePair("env:outSR", Out_SR));
        params.add(new BasicNameValuePair("f", Format));
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL_Submit);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
                return false;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            JSONObject data = new JSONObject(str.toString());
            jobId = data.getString("jobId");
            jobStatus = data.getString("jobStatus");
            return true;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean CheckData(String id) {
        String url = "http://g-con.giskku.in.th/ArcGIS/rest/services/Route/GPServer/Route/jobs/" + id + "?f=pjson";

        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("KKFHD", "Failed to download result..");
                return false;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            JSONObject data = new JSONObject(str.toString());
            jobStatus = data.getString("jobStatus");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("KKFHD", url);
        return true;
    }

    public String ResultData(String id) {
        String url = "http://g-con.giskku.in.th/ArcGIS/rest/services/Route/GPServer/Route/jobs/" + id + "/results/Routes?f=pjson";

        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("KKFHD", "Failed to download result..");
                return null;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //tv4.setText(str.toString());
        Log.d("KKFHD", url);
        return str.toString();
    }

    public boolean SendData_flood(String c_lat, String c_long,String water) {
        String Feature_Set = "{\"features\":[{\"attributes\":{\"FID\":0},\"geometry\":{\"x\":"+c_long+",\"spatialReference\":{\"wkid\":4326},\"y\":"+c_lat+"}}],\"geometryType\":\"esriGeometryPoint\"}";
        String Water_Level = water;
        String Out_SR = "4326";
        String Format = "pjson";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Feature_Set", Feature_Set));
        params.add(new BasicNameValuePair("water_level", Water_Level));
        params.add(new BasicNameValuePair("env:outSR", Out_SR));
        params.add(new BasicNameValuePair("f", Format));
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL_Submit_Flood);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
                return false;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            JSONObject data = new JSONObject(str.toString());
            jobId = data.getString("jobId");
            jobStatus = data.getString("jobStatus");
            return true;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean CheckData_flood(String id) {
        String url="http://g-con.giskku.in.th/ArcGIS/rest/services/flood/GPServer/flood/jobs/"+id+"?f=pjson";

        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("KKFHD", "Failed to download result..");
                return false;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            JSONObject data = new JSONObject(str.toString());
            jobStatus = data.getString("jobStatus");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("KKFHD", url);
        return true;
    }
    public String ResultData_flood(String id) {
        String url="http://g-con.giskku.in.th/ArcGIS/rest/services/flood/GPServer/flood/jobs/"+id+"/results/flood_shp?f=pjson";

        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("KKFHD", "Failed to download result..");
                return null;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //tv4.setText(str.toString());
        Log.d("KKFHD", url);
        return str.toString();
    }
}
