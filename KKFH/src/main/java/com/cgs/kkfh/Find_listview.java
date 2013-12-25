package com.cgs.kkfh;


import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Find_listview extends Fragment implements OnClickListener {
    // All static variables
    // XML node keys
    static final String KEY_STATUS = "status";
    static final String KEY_WATERZERO = "waterzero";
    static final String KEY_WATERSTORE = "waterstore";
    static final String KEY_W_LAT = "w_lat";
    static final String KEY_W_LONG = "w_long";
    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_L_ID = "l_id";
    static final String KEY_DATE = "date";
    static final String KEY_TEL = "tel";
    static final String KEY_IN_PEOPLE = "in_people";
    static final String KEY_OUT_PEOPLE = "out_people";
    static final String KEY_MAX_PEOPLE = "max_people";
    static final String KEY_CURRENT_PEOPLE = "current_people";

    //Data
    static final String DATA_L_ID = "l_id";
    static final String DATA_PEOPLE = "people";
    static final String DATA_PATIENT_PEOPLE = "patient_people";

    ListView list;
    ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find2, container, false);

        String l_id = this.getArguments().getString(DATA_L_ID);
        String people = this.getArguments().getString(DATA_PEOPLE);
        String patient_people = this.getArguments().getString(DATA_PATIENT_PEOPLE);


        Find_listviewAsyntask obj = new Find_listviewAsyntask("http://kunmee.com/gcon/watercenter.php?id=" + l_id);
        obj.execute();

        list = (ListView) view.findViewById(R.id.list);
        pb = (ProgressBar) view.findViewById(R.id.progressBar);

        Button back = (Button) view.findViewById(R.id.back);
        back.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new FindActivity())
                .commit();
    }

    private class Find_listviewAsyntask extends AsyncTask<Context, Integer, String> {
        String URL;
        XMLParser parser;
        String xml;

        public Find_listviewAsyntask(String link) {
            URL = link;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            parser = new XMLParser();
        }

        protected String doInBackground(Context... params) {
            xml = parser.getXmlFromUrl(URL); // getting XML from URL
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Document doc = parser.getDomElement(xml); // getting DOM element

            ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

            NodeList n_check = doc.getElementsByTagName(KEY_WATERZERO);
            Element e_ncheck = (Element) n_check.item(0);
            String status = parser.getValue(e_ncheck, KEY_STATUS);

            if (status.equalsIgnoreCase("true")) {
                NodeList nl = doc.getElementsByTagName(KEY_WATERSTORE);
                // looping through all song nodes <song>
                for (int i = 0; i < nl.getLength(); i++) {
                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();
                    Element e = (Element) nl.item(i);
                    // adding each child node to HashMap key => value
                    map.put(KEY_ID, parser.getValue(e, KEY_ID));
                    map.put(KEY_NAME, parser.getValue(e, KEY_NAME));
                    map.put(KEY_DESCRIPTION, parser.getValue(e, KEY_DESCRIPTION));
                    map.put(KEY_L_ID, parser.getValue(e, KEY_L_ID));
                    map.put(KEY_DATE, parser.getValue(e, KEY_DATE));
                    map.put(KEY_TEL, parser.getValue(e, KEY_TEL));
                    map.put(KEY_IN_PEOPLE, parser.getValue(e, KEY_IN_PEOPLE));
                    map.put(KEY_OUT_PEOPLE, parser.getValue(e, KEY_OUT_PEOPLE));
                    map.put(KEY_MAX_PEOPLE, parser.getValue(e, KEY_MAX_PEOPLE));
                    map.put(KEY_W_LAT, parser.getValue(e, KEY_W_LAT));
                    map.put(KEY_W_LONG, parser.getValue(e, KEY_W_LONG));

                    String temp1 = parser.getValue(e, KEY_IN_PEOPLE);
                    String temp2 = parser.getValue(e, KEY_OUT_PEOPLE);
                    String temp3 = parser.getValue(e, KEY_MAX_PEOPLE);
                    int current = Integer.parseInt(temp3) - (Integer.parseInt(temp1) - Integer.parseInt(temp2));
                    map.put(KEY_CURRENT_PEOPLE, Integer.toString(current));

                    // adding HashList to ArrayList
                    dataList.add(map);
                }
            } else {
                Log.d("KKFHD", "5");
            }

            // Getting adapter by passing xml data ArrayList
            LazyAdapter adapter = new LazyAdapter(getActivity(), dataList);
            list.setAdapter(adapter);

            pb.setVisibility(View.GONE);

            // Click event for single list row
            list.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    // getting values from selected ListItem
                    String title = ((TextView) view.findViewById(R.id.txtF_name))
                            .getText().toString();

                    String description = ((TextView) view
                            .findViewById(R.id.txtF_des)).getText().toString();

                    String date = ((TextView) view
                            .findViewById(R.id.txtF_date)).getText().toString();

                    String current = ((TextView) view
                            .findViewById(R.id.txtF_current)).getText().toString();

                    String max = ((TextView) view
                            .findViewById(R.id.txtF_max)).getText().toString();

                    String w_lat = ((TextView) view
                            .findViewById(R.id.txtF_lat)).getText().toString();

                    String w_long = ((TextView) view
                            .findViewById(R.id.txtF_long)).getText().toString();

                    Bundle data = new Bundle();
                    data.putString(KEY_NAME, title);
                    data.putString(KEY_DESCRIPTION, description);
                    data.putString(KEY_DATE, date);
                    data.putString(KEY_CURRENT_PEOPLE, current);
                    data.putString(KEY_MAX_PEOPLE, max);
                    data.putString(KEY_W_LAT, w_lat);
                    data.putString(KEY_W_LONG, w_long);

                    Fragment fm = new FindActivityDetail();
                    fm.setArguments(data);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fm);
                    ft.commit();
                }
            });
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}