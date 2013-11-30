package com.cgs.kkfh;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amnart on 29/11/2556.
 */
public class HomeActivity extends Fragment implements View.OnClickListener {
    Button btn_help;
    Button btn_report;
    private int number_picker = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btn_help = (Button) view.findViewById(R.id.btnH_help);
        btn_help.setOnClickListener(this);
        btn_report = (Button) view.findViewById(R.id.btnH_report);
        btn_report.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (view.getId()) {
            case R.id.btnH_help:
                Help();
                break;
            case R.id.btnH_report:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ReportActivity())
                        .commit();
                break;
        }
    }

    private void Help() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("People")
                .setMessage("How many people want to help");

        final FrameLayout frameView = new FrameLayout(getActivity());
        builder.setView(frameView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("KKFHD", Integer.toString(number_picker));
                if(helpData(number_picker)){
                    Toast.makeText(getActivity(),"Success and wait for help",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(),"Fail please try again",Toast.LENGTH_LONG).show();
                }
            }
        });

        final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.fragment_help_dialog, frameView);
        alertDialog.show();

        final NumberPicker np = (NumberPicker) dialoglayout.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                number_picker = i2;
            }
        });
    }

    public boolean helpData(int people) {
        //Connect SQLite and Get information
        SQLiteControl obj = new SQLiteControl(getActivity());
        obj.getReadableDatabase();
        String data[] = obj.selectMember();
        if (data == null) {
            return false;
        }
        Log.d("KKFHD", "11");

        //Set policy for 4.0 up
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Log.d("KKFHD", "22");
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        Log.d("KKFHD", "221");
        HttpPost httppost = new HttpPost("http://kunmee.com/gcon/insert_help.php");
        Log.d("KKFHD", "222");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        Log.d("KKFHD", "223");
        try {
            // Add your data
            nameValuePairs.add(new BasicNameValuePair("h_name", data[1]));
            Log.d("KKFHD", "331");
            nameValuePairs.add(new BasicNameValuePair("tel", data[2]));
            Log.d("KKFHD", "332");
            nameValuePairs.add(new BasicNameValuePair("people", Integer.toString(people)));
            Log.d("KKFHD", "333");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.d("KKFHD", "33");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String w_data = bufferedReader.readLine();
            Log.d("KKFHD", "44");
            if(w_data.equalsIgnoreCase("success")){
                return true;
            }else {
                return false;
            }
        } catch (ClientProtocolException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
