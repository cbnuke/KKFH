package com.cgs.kkfh;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

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
    private ImageView mImageView;
    private Button eiei;
    // XML node keys
    static String KEY_ID = "topicID";
    static String KEY_TITLE = "title";
    static String KEY_DESCRIPTION = "description";
    static String KEY_THUMB_URL = "img";
    static String KEY_UPDATE = "updated";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find3, container, false);

        // Get XML values from previous Bundle
        String title = this.getArguments().getString(KEY_TITLE);
        String date = this.getArguments().getString(KEY_UPDATE);
        String description = this.getArguments().getString(KEY_DESCRIPTION);
        String imgText = this.getArguments().getString(KEY_THUMB_URL);
        eiei = (Button) view.findViewById(R.id.eiei);
        eiei.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FindActivity())
                        .commit();
            }
        });

        mTitleText = (TextView) view.findViewById(R.id.titleText);
        mDateText = (TextView) view.findViewById(R.id.dateText);
        mDescriptionText = (TextView) view.findViewById(R.id.descriptionText);
        mImageView = (ImageView) view.findViewById(R.id.list_image2);

        String url = "http://farmacia-store.com/lotte/img/f" + imgText + ".jpg";

        ImageLoader imageLoader = new ImageLoader(getActivity());
        imageLoader.DisplayImage(url,mImageView,1);

        mTitleText.setText(title);
        mDateText.setText(date);
        mDescriptionText.setText(description);

        return view;
    }
}
