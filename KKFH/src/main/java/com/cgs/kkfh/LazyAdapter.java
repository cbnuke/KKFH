package com.cgs.kkfh;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)

            vi = inflater.inflate(R.layout.fragment_findlist, null);

        TextView mTitle = (TextView) vi.findViewById(R.id.title); // title
        TextView mUpdate = (TextView) vi.findViewById(R.id.update); // update
        TextView mDescription = (TextView) vi.findViewById(R.id.description); // update
        TextView mImg = (TextView) vi.findViewById(R.id.img); // img text
        ImageView mThumb_image = (ImageView) vi.findViewById(R.id.list_image); // image


        HashMap<String, String> news = new HashMap<String, String>();
        news = data.get(position);

        // Setting all values in listview
        // Log.d("test", song.get(CustomizedListView.KEY_TITLE));
        mTitle.setText(news.get(Find_listview.KEY_TITLE));
        mDescription.setText(news.get(Find_listview.KEY_DESCRIPTION));// not
        // show
        mUpdate.setText(news.get(Find_listview.KEY_UPDATE));
        //mImg.setText(news.get(Food_list.KEY_THUMB_URL));// not show
        mImg.setText(news.get(Find_listview.KEY_ID));
        imageLoader.DisplayImage(news.get(Find_listview.KEY_THUMB_URL),
                mThumb_image);
//        Log.d("KKFHD", " TEXT  " + news.get(Find_listview.KEY_TITLE));
//        Log.d("KKFHD", " TEXT  " + news.get(Find_listview.KEY_ID));
//        Log.d("KKFHD", " TEXT  " + news.get(Find_listview.KEY_THUMB_URL));
        return vi;
    }

}