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
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
        /*string Text HomeActivity*/
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.title_helpActivity))
                .setMessage(getString(R.string.msg_helpActivity));

        final FrameLayout frameView = new FrameLayout(getActivity());
        builder.setView(frameView);
        builder.setNegativeButton(getString(R.string.cancel_helpActivity), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        /*string Text HomeActivity*/
        builder.setPositiveButton(getString(R.string.send_helpActivity), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("KKFHD", Integer.toString(number_picker));
                if (helpData(number_picker)) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.success_helpActivity))
                            .setMessage(getString(R.string.successMsg_helpActivity));
                    builder1.setPositiveButton(getString(R.string.ok_helpActivity), null);
                    builder1.show();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.fail_helpActivity))
                            .setMessage(getString(R.string.failMsg_helpActivity));
                    builder1.setPositiveButton(getString(R.string.ok_helpActivity), null);
                    builder1.show();
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

        //Set policy for 4.0 up
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Check location empty
        if (MainActivity.l_lat == 0.0 || MainActivity.l_long == 0) {
            return false;
        }

        //Get location from GPS
        try {
            // Create data variable for sent values to server
            String post_data = URLEncoder.encode("h_name", "UTF-8")
                    + "=" + URLEncoder.encode(data[1], "UTF-8");

            post_data += "&" + URLEncoder.encode("tel", "UTF-8") + "="
                    + URLEncoder.encode(data[2], "UTF-8");

            post_data += "&" + URLEncoder.encode("people", "UTF-8")
                    + "=" + URLEncoder.encode(Integer.toString(people), "UTF-8");

            post_data += "&" + URLEncoder.encode("disease", "UTF-8")
                    + "=" + URLEncoder.encode(data[3], "UTF-8");

            post_data += "&" + URLEncoder.encode("h_lat", "UTF-8")
                    + "=" + URLEncoder.encode(Double.toString(MainActivity.l_lat), "UTF-8");

            post_data += "&" + URLEncoder.encode("h_long", "UTF-8")
                    + "=" + URLEncoder.encode(Double.toString(MainActivity.l_long), "UTF-8");

            String text = "";
            BufferedReader reader = null;

            // Send data
            // Defined URL  where to send data
            URL url = new URL("http://kunmee.com/gcon/insert_help.php");

            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(post_data);
            wr.flush();

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line);
            }

            text = sb.toString();
            if (text.equalsIgnoreCase("success")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }
}
