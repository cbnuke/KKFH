//package com.cgs.kkfh;
//
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//
//
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//public class Find_listview extends Fragment implements OnClickListener {
//    // All static variables
//
//
//    //static final String URL = "http://10.199.6.11/select_sweet3.php";
//    private String URL = "";
//
//    // XML node keys
//    static final String KEY_THUMB_URL = "img";
//    static final String KEY_STATUS = "status";
//    static final String KEY_WATERZERO = "waterzero";
//    static final String KEY_WATERSTORE = "waterstore";
//    static final String KEY_ID = "id";
//    static final String KEY_NAME = "name";
//    static final String KEY_DESCRIPTION = "description";
//    static final String KEY_LOC = "loc";
//    static final String KEY_L_ID = "l_id";
//    static final String KEY_DATE = "date";
//    static final String KEY_TEL = "tel";
//    static final String KEY_IN_PEOPLE = "in_people";
//    static final String KEY_OUT_PEOPLE = "out_people";
//    static final String KEY_MAX_PEOPLE = "max_people";
//
//    //Data
//    static final String DATA_L_ID = "l_id";
//    static final String DATA_PEOPLE = "people";
//    static final String DATA_PATIENT_PEOPLE = "patient_people";
//
//    ListView list;
//    LazyAdapter adapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_find2, container, false);
//
//        String l_id = this.getArguments().getString(DATA_L_ID);
//        String people = this.getArguments().getString(DATA_PEOPLE);
//        String patient_people = this.getArguments().getString(DATA_PATIENT_PEOPLE);
//
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy =
//                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
//
//        this.URL = "http://kunmee.com/gcon/watercenter.php?id="+l_id;
//
//        ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
//
//        XMLParser parser = new XMLParser();
//        String xml = parser.getXmlFromUrl(URL); // getting XML from URL
//        Document doc = parser.getDomElement(xml); // getting DOM element
//
//
//        NodeList n_check = doc.getElementsByTagName(KEY_WATERZERO);
//        Element e_ncheck = (Element) n_check.item(0);
//        String status = parser.getValue(e_ncheck, KEY_STATUS);
//
//        if (status.equalsIgnoreCase("true")) {
//            NodeList nl = doc.getElementsByTagName(KEY_WATERSTORE);
//            // looping through all song nodes <song>
//            for (int i = 0; i < nl.getLength(); i++) {
//                // creating new HashMap
//                HashMap<String, String> map = new HashMap<String, String>();
//                Element e = (Element) nl.item(i);
//                // adding each child node to HashMap key => value
//                map.put(KEY_ID, parser.getValue(e, KEY_ID));
//                map.put(KEY_NAME, parser.getValue(e, KEY_NAME));
//                map.put(KEY_DESCRIPTION, parser.getValue(e, KEY_DESCRIPTION));
//                map.put(KEY_LOC, parser.getValue(e, KEY_LOC));
//                map.put(KEY_L_ID, parser.getValue(e, KEY_L_ID));
//                map.put(KEY_DATE, parser.getValue(e, KEY_DATE));
//                map.put(KEY_TEL, parser.getValue(e, KEY_TEL));
//                map.put(KEY_IN_PEOPLE, parser.getValue(e, KEY_IN_PEOPLE));
//                map.put(KEY_OUT_PEOPLE, parser.getValue(e, KEY_OUT_PEOPLE));
//                map.put(KEY_MAX_PEOPLE, parser.getValue(e, KEY_MAX_PEOPLE));
//                map.put(KEY_THUMB_URL,"http://kunmee.com/gcon/img1/"+parser.getValue(e, KEY_ID)+".jpg");
//
//                // adding HashList to ArrayList
//                dataList.add(map);
//            }
//        }else {
//            Log.d("KKFHD","5");
//        }
//
//        list = (ListView) view.findViewById(R.id.list);
//
//        Button back = (Button) view.findViewById(R.id.back);
//        back.setOnClickListener(this);
//
//        // Getting adapter by passing xml data ArrayList
//        LazyAdapter adapter = new LazyAdapter(getActivity(), dataList);
//
//        list.setAdapter(adapter);
//
//        // Click event for single list row
//        list.setOnItemClickListener(new OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                // getting values from selected ListItem
//                String title = ((TextView) view.findViewById(R.id.txtF_name))
//                        .getText().toString();
//
//                String description = ((TextView) view
//                        .findViewById(R.id.txtF_des)).getText().toString();
//
//                Bundle data = new Bundle();
//                data.putString(KEY_NAME, title);
//                data.putString(KEY_DESCRIPTION, description);
//
//                Fragment fm = new FindActivityDetail();
//                fm.setArguments(data);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.container, fm);
//                ft.commit();
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public void onClick(View v) {
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, new FindActivity())
//                .commit();
//    }
//}