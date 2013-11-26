package com.cgs.kkfh;


import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Find_listview extends Fragment implements OnClickListener {
    // All static variables


    //static final String URL = "http://10.199.6.11/select_sweet3.php";
    private String URL = "";

    // XML node keys
    static final String KEY_TOPIC = "topic"; // parent node
    static final String KEY_ID = "topicID";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_THUMB_URL = "img";
    static final String KEY_UPDATE = "updated";


    ListView list;
    LazyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find2, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.URL = "http://farmacia-store.com/select_food3.php";

        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl(URL); // getting XML from URL
        Document doc = parser.getDomElement(xml); // getting DOM element
        Log.d("error", " 5 ");
        NodeList nl = doc.getElementsByTagName(KEY_TOPIC);
        // looping through all song nodes <song>
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key => value
            map.put(KEY_ID, parser.getValue(e, KEY_ID));
            map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
            map.put(KEY_DESCRIPTION, parser.getValue(e, KEY_DESCRIPTION));
            map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));
            map.put(KEY_UPDATE, parser.getValue(e, KEY_UPDATE));


            // adding HashList to ArrayList
            songsList.add(map);
        }

        list = (ListView) view.findViewById(R.id.list);

        Button back = (Button) view.findViewById(R.id.back);
        back.setOnClickListener(this);

        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(getActivity(), songsList);

        list.setAdapter(adapter);

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // getting values from selected ListItem
                String title = ((TextView) view.findViewById(R.id.title))
                        .getText().toString();
                String update = ((TextView) view.findViewById(R.id.update))
                        .getText().toString();


                String description = ((TextView) view
                        .findViewById(R.id.description)).getText().toString();

                String img = ((TextView) view.findViewById(R.id.img)).getText()
                        .toString();

                Bundle data = new Bundle();
                data.putString(KEY_TITLE, title);
                data.putString(KEY_UPDATE, update);
                data.putString(KEY_DESCRIPTION, description);
                data.putString(KEY_THUMB_URL, img);

                Fragment fm = new FindActivityDetail();
                fm.setArguments(data);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container,fm);
                ft.commit();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new FindActivity())
                .commit();
    }
}