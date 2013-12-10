package com.cgs.kkfh;

import android.app.Activity;
;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, LocationListener {

    /**
     * Location update every time for use.
     */
    public static double l_lat;
    public static double l_long;
    Location location; // location
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The 10 second time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10;

    private static LocationManager mlocManager;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Initial GPS
         */
        InitialGPS();


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        Log.d("KKFHD", "Start App");
        //Chech register
        SQLiteControl sql = new SQLiteControl(this);
        sql.getWritableDatabase();
        String data[] = sql.selectMember();

        if (data[0].equals("0")) {
            Log.d("KKFHD", "Null");
            finish();
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            adb.setIcon(R.drawable.ic_launcher);
            adb.setTitle(getString(R.string.app_name));
            adb.setMessage(getString(R.string.app_exit));
            adb.setNegativeButton(getString(R.string.app_exit_y), new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    finish();
                }
            });
            adb.setPositiveButton(getString(R.string.app_exit_n), null);
            adb.show();
            return true;
        }
        return false;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new HomeActivity())
                        .commit();
                onSectionAttached(1);
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new TestActivity())
                        .commit();
                onSectionAttached(2);
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FindActivity())
                        .commit();
                onSectionAttached(3);
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AboutusActivity())
                        .commit();
                onSectionAttached(4);
                break;
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.app_name);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Destroy GPS when Pause
        mlocManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Active GPS when Resume
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        //Check Location setting enable
        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            finish();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

        }
        //Check Internet
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            new AlertDialog.Builder(this)
                    .setTitle("Request internet connect")
                    .setMessage("Please check your internet connect")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ChangeProfileActivity())
                        .commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void InitialGPS() {

        /* Use the LocationManager class to obtain GPS locations */
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
        } else {
            if (isGPSEnabled) {
                if (location == null) {
                    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (location != null) {
                        location = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location loc) {

        l_lat = loc.getLatitude();
        l_long = loc.getLongitude();

        String Text = "My current location is: " +
                "Latitud = " + loc.getLatitude() +
                "Longitud = " + loc.getLongitude();

        Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
