/**
 * The base Activity that starts the application
 */

package com.shankarlabs.carlog.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.shankarlabs.carlog.R;

import java.util.Date;

public class CarLogActivity extends SherlockFragmentActivity {

    private static final String LOGTAG = "CarLog";
    private boolean mDualPane; // Flag to indicate dual pane setup
    private boolean listeningToGPS = false, listeningToNetwork = false;
    private Context mContext;
    private LocationManager locationManager;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPreferenceEditor;
    private TextView updatesTextView;
    private Location bestLocation = null;
    private final long TEN_MINUTES = 10 * 60 * 1000; // 10 minutes in milliseconds for comparing time


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setSupportProgressBarIndeterminate(true);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPreferenceEditor = sharedPrefs.edit();

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        updatesTextView = (TextView) findViewById(R.id.updates);

        // Figure out if we have dual panes, and then initialize the second pane
        View pane2 = findViewById(R.id.pane2_fragment);
        mDualPane = pane2 != null &&  pane2.getVisibility() == View.VISIBLE;
        sharedPreferenceEditor.putBoolean("mDualPane", mDualPane);
        sharedPreferenceEditor.commit();

        if(mDualPane) { // We have two panes. Populate the second pane
            Log.d(LOGTAG, "CarLogActivity : onCreate : We have two panes. Showing FillupFragment.");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            SherlockFragment fillUpFragment = new FillUpFragment();
            ft.replace(R.id.pane2_fragment, fillUpFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
            ft.commit();
        } else { // we have just one pane. Setup spinner in actionbar
            Log.d(LOGTAG, "CarLogActivity : onCreate : We have just one pane. Setting up Spinner.");
            setTitle("");

            // Get the Action Bar ready to hold the Spinner
            ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

            SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.spinner_main, android.R.layout.simple_spinner_dropdown_item);

            ActionBar.OnNavigationListener spinnerNavigationListener = new ActionBar.OnNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Handle the item selected
                switch(itemPosition) { // Assuming the callback is a zero-based position system
                    case 0: // Fill Up
                        Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 0. Fill Up.");
                        SherlockFragment fillUpFragment = new FillUpFragment();
                        ft.replace(R.id.pane1_fragment, fillUpFragment);
                        // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                        ft.commit();
                        return true;  // We handled the callback; We don't need anyone else to handle it
                    case 1: // Oil Changes
                        Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 1. Maintenance.");
                        SherlockFragment maintenanceFragment = new MaintenanceFragment();
                        ft.replace(R.id.pane1_fragment, maintenanceFragment);
                        // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                        ft.commit();
                        return true;  // We handled the callback; We don't need anyone else to handle it
                    case 2: // Vehicle Types
                        Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 2. Vehicle Types.");
                        SherlockFragment vehicleTypesFragment = new VehiclesFragment();
                        ft.replace(R.id.pane1_fragment, vehicleTypesFragment);
                        // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                        ft.commit();
                        return true;  // We handled the callback; We don't need anyone else to handle it
                    case 3: // Maintenance Types
                        Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 3. Maintenance Types.");
                        SherlockFragment maintenanceTypesFragment = new MaintenanceTypesFragment();
                        ft.replace(R.id.pane1_fragment, maintenanceTypesFragment);
                        // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                        ft.commit();
                        return true;  // We handled the callback; We don't need anyone else to handle it
                    case 4: // GPS Log
                        Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 4. GPS Log.");
                        // SherlockFragment gpsLogFragment = new GpsLogResearchFragment(); // GpsLogFragment();
                        SherlockFragment gpsLogFragment = new GpsLogResearchFragment();
                        ft.replace(R.id.pane1_fragment, gpsLogFragment);
                        // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                        ft.commit();
                        return true;  // We handled the callback; We don't need anyone else to handle it
                    case 5: // Statistics
                        Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 5. Statistics.");
                        SherlockFragment statisticsFragment = new StatisticsFragment();
                        ft.replace(R.id.pane1_fragment, statisticsFragment);
                        // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                        ft.commit();
                        return true;  // We handled the callback; We don't need anyone else to handle it
                    default: // Safe fallback
                        Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Default : " + itemPosition + ". Unknown option");
                        return false;  // We don't know what it is; So we pass it along.
                }
                }
            };

            actionBar.setListNavigationCallbacks(mSpinnerAdapter, spinnerNavigationListener);
        }

        // Initialize the DB Objects so the DB gets populated
        // VehicleDBHelper vehicleDBHelper = new VehicleDBHelper(mContext);
        // MaintTypeDBHelper maintTypeDBHelper = new MaintTypeDBHelper(mContext);

        // vehicleDBHelper = null;
        // maintTypeDBHelper = null;

        // Get the location of the device, but in a thread
        getLocation();

        Log.d(LOGTAG, "CarLogActivity : onCreate : Main screen ready");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        // We handle only Settings in the Main menu; The rest of the options are added by the Fragments. We let them handle those.
        switch (itemId) {
            case R.id.settings:
                Log.d(LOGTAG, "CarLogActivity : onOptionsItemSelected : Settings coming right up");
                return super.onOptionsItemSelected(menuItem);
            default:
                return false;
        }
    }

    public void getLocation() {
        updatesTextView.setText("Getting location");
        Log.d(LOGTAG, "CarLogActivity : getLocation : Getting Location");


        String locationString = null;
        long now = new Date().getTime();
        Location localCacheLocation = null, lastGPSLocation = null, lastNetworkLocation = null, bestLocation;

        String localGPSCache = sharedPrefs.getString("localGPSCache", null);

        if(localGPSCache != null) { // We have a cache value; Convert it into Location
            Log.d(LOGTAG, "CarLogActivity : getLocation : localGPSCache is not null. Creating Location");
            localCacheLocation = new Location("LOCAL_GPS_CACHE");
            String localCacheData[] = localGPSCache.split(","); // It's a CSV set of lat, lng, time, acc, provider
            localCacheLocation.setLatitude(Double.parseDouble(localCacheData[0]));
            localCacheLocation.setLongitude(Double.parseDouble(localCacheData[1]));
            localCacheLocation.setTime(Long.parseLong(localCacheData[2]));
            localCacheLocation.setAccuracy(Float.parseFloat(localCacheData[3]));
            localCacheLocation.setProvider(localCacheData[4]);
            Log.d(LOGTAG, "CarLogActivity : getLocation : Local GPS cache is " + (now - localCacheLocation.getTime())/60000 + " minutes old");
            updatesTextView.setText("Local GPS cache is " + (now - localCacheLocation.getTime())/1000 + " seconds old");
            updateBestLocation(localCacheLocation);
        }

        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.d(LOGTAG, "CarLogActivity : getLocation : Network Provider enabled. Getting last known");
            lastNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Log.d(LOGTAG, "CarLogActivity : getLocation : Last known network location is " + (now - lastNetworkLocation.getTime())/60000 + " minutes old");
            updatesTextView.setText("Last known network location is " + (now - lastNetworkLocation.getTime()) / 60000 + " minutes old");
            updateBestLocation(lastNetworkLocation);
        }

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d(LOGTAG, "CarLogActivity : getLocation : GPS Provider enabled. Getting last known");
            lastGPSLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Log.d(LOGTAG, "CarLogActivity : getLocation : Last known GPS location is " + (now - lastGPSLocation.getTime())/60000 + " minutes old");
            updatesTextView.setText("Last known GPS location is " + (now - lastGPSLocation.getTime()) / 60000 + " minutes old");
            updateBestLocation(lastGPSLocation);
        }

        if(localGPSCache != null && (now - localCacheLocation.getTime()) < TEN_MINUTES) {
            // We have a saved location that is less than 10 minutes ago. use that.
            Log.d(LOGTAG, "CarLogActivity : getLocation : localGPSCache is " + (now - localCacheLocation.getTime()) / 60000 + " minutes old. Using it.");
            bestLocation = localCacheLocation;
        } else if(lastGPSLocation != null && (now - lastGPSLocation.getTime()) < TEN_MINUTES) {
            // Last known GPS fix is less than 10 minutes old. Should be good enough.
            Log.i(LOGTAG, "CarLogActivity : getLocation : lastGPSLocation is " + (now - lastGPSLocation.getTime()) / 60000 + " minutes old. Using it.");
            bestLocation = lastGPSLocation;
        } else if(lastNetworkLocation != null && (now - lastNetworkLocation.getTime()) < TEN_MINUTES) {
            // Last known network fix is less than 10 minutes old. Should be good enough.
            Log.i(LOGTAG, "CarLogActivity : getLocation : lastNetworkLocation is " + (now - lastNetworkLocation.getTime()) / 60000 + " minutes old. Using it.");
            bestLocation = lastNetworkLocation;
        } else {
            // We have no good locations in the recent past. Request for new updates.
            Log.i(LOGTAG, "CarLogActivity : getLocation : No usable known locations. Requesting all location updates");
            // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, networkLocationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);

            listeningToGPS = true;
            // listeningToNetwork = true;

            // Nothing is usable meaning we need to update the locationGPSCache to read null
            sharedPreferenceEditor.putString("localGPSCache", null);
            sharedPreferenceEditor.commit();
        }
    }

    private void updateBestLocation(Location newLocation) {
        long now = new Date().getTime();
        // Log.d(LOGTAG, "CarLogActivity : updateBestLocation : Updating best location");

        if(bestLocation == null) { // If no current best location, use what we got.
            Log.d(LOGTAG, "CarLogActivity : updateBestLocation : Best Location is null. Saving newLocation");
            bestLocation = newLocation;
        } else if(now - newLocation.getTime() > TEN_MINUTES) { // If the newLocation fix is more than 10 minutes old
            Log.d(LOGTAG, "CarLogActivity : updateBestLocation : newLocation is " + (now - newLocation.getTime())/60000 + " minutes old, unusable");
            if(newLocation.getTime() > bestLocation.getTime()) { // new location is more recent. Save it
                Log.d(LOGTAG, "CarLogActivity : updateBestLocation : New location is " + (newLocation.getTime() - bestLocation.getTime())/1000 + " seconds newer. Saving.");
                bestLocation = newLocation;
            }
        } else { // New Location fix is less than 10 minutes old
            Log.d(LOGTAG, "CarLogActivity : updateBestLocation : newLocation is " + (now - newLocation.getTime())/1000 + " seconds old");
            if(Math.abs(newLocation.getTime() - bestLocation.getTime()) <= 60000) {
                // If both the fixes are within one minute of each other, get the one with better accuracy
                // Log.d(LOGTAG, "CarLogActivity : updateBestLocation : new and best location are " + Math.abs(newLocation.getTime() - bestLocation.getTime())/1000 + " seconds apart");
                if(newLocation.getAccuracy() <= bestLocation.getAccuracy()) { // The new location is better.
                    Log.d(LOGTAG, "CarLogActivity : updateBestLocation : New location is " + (bestLocation.getAccuracy() - newLocation.getAccuracy()) + " meters more accurate. Saving.");
                    bestLocation = newLocation;
                }
            } else if(newLocation.getTime() > bestLocation.getTime()) { // If more than a minute apart, save the new one
                Log.d(LOGTAG, "CarLogActivity : updateBestLocation : new location is " + Math.abs(newLocation.getTime() - bestLocation.getTime())/1000 + " seconds newer. Saving.");
                bestLocation = newLocation;
            }
        }

        // All said and done, if the bestLocation has good accuracy, stop listening
        if(bestLocation.getAccuracy() <= 10) {
            Log.d(LOGTAG, "CarLogActivity : updateBestLocation : We have accuracy of 10m. Stopping updates");
            if(listeningToGPS)
                locationManager.removeUpdates(gpsLocationListener);

            if(listeningToNetwork)
                locationManager.removeUpdates(networkLocationListener);
        } else {
            Log.d(LOGTAG, "CarLogActivity : updateBestLocation : Location Accuracy of " + newLocation.getAccuracy() + " not matching required value, 10");
        }

        // Save the best location to Prefs so if the app decides to use it, it'll have something
        String locationString = bestLocation.getLatitude() + "," +
                bestLocation.getLongitude() + "," +
                bestLocation.getTime() + "," +
                bestLocation.getAccuracy() + "," +
                bestLocation.getProvider();
        Log.d(LOGTAG, "CarLogActivity : updateBestLocation : Save bestLocation to Prefs : " + locationString);
        sharedPreferenceEditor.putString("localGPSCache", locationString);
        sharedPreferenceEditor.commit();

        updatesTextView.setText("Location Provider " + bestLocation.getProvider().toUpperCase() +
                                ", accuracy " + bestLocation.getAccuracy() + "m" +
                                ", at " + DateUtils.formatDateTime(mContext, bestLocation.getTime(), DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_12HOUR|DateUtils.FORMAT_CAP_AMPM));

    }

    LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Figure out if the obtained location is good enough
            Log.d(LOGTAG, "CarLogActivity : gpsLocationListener : onLocationChanged");
            updateBestLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Not sure what to do here
            Log.i(LOGTAG, "CarLogActivity : gpsLocationListener : GPS Provider status changed to " + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Not sure what to do here
            Log.i(LOGTAG, "CarLogActivity : gpsLocationListener : Provider " + provider + " enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Not sure what to do here
            Log.w(LOGTAG, "CarLogActivity : gpsLocationListener : Provider " + provider + " disabled");
        }
    };

    LocationListener networkLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Figure out if the obtained location is good enough
            Log.d(LOGTAG, "CarLogActivity : networkLocationListener : onLocationChanged");
            updateBestLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Not sure what to do here
            Log.i(LOGTAG, "CarLogActivity : networkLocationListener : Network Provider status changed to " + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Not sure what to do here
            Log.i(LOGTAG, "CarLogActivity : networkLocationListener : Provider " + provider + " enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Not sure what to do here
            Log.w(LOGTAG, "CarLogActivity : networkLocationListener : Provider " + provider + " disabled");
        }
    };


/*
    All of the getting Location process cannot be done in a thread because registering for updates from a thread
    will not have a callback thread, unless looper is called, and hence that thread will never get the updates.
    public void getLocationInThread() {
        updatesTextView.setText("Getting GPS.");

        new Thread(new Runnable() {
            final long TEN_MINUTES = 10 * 60 * 1000; // 10 minutes in milliseconds for comparing time
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            private boolean listeningToGPS = false, listeningToNetwork = false;

            LocationListener gpsLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Figure out if the obtained location is good enough
                    Log.d(LOGTAG, "CarLogActivity : gpsLocationListener : onLocationChanged");
                    updateBestLocation(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // Not sure what to do here
                    Log.i(LOGTAG, "CarLogActivity : gpsLocationListener : GPS Provider status changed to " + status);
                }

                @Override
                public void onProviderEnabled(String provider) {
                    // Not sure what to do here
                    Log.i(LOGTAG, "CarLogActivity : gpsLocationListener : Provider " + provider + " enabled");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    // Not sure what to do here
                    Log.w(LOGTAG, "CarLogActivity : gpsLocationListener : Provider " + provider + " disabled");
                }
            };

            LocationListener networkLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Figure out if the obtained location is good enough
                    Log.d(LOGTAG, "CarLogActivity : networkLocationListener : onLocationChanged");
                    updateBestLocation(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // Not sure what to do here
                    Log.i(LOGTAG, "CarLogActivity : networkLocationListener : Network Provider status changed to " + status);
                }

                @Override
                public void onProviderEnabled(String provider) {
                    // Not sure what to do here
                    Log.i(LOGTAG, "CarLogActivity : networkLocationListener : Provider " + provider + " enabled");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    // Not sure what to do here
                    Log.w(LOGTAG, "CarLogActivity : networkLocationListener : Provider " + provider + " disabled");
                }
            };

            @Override
            public void run() {
                // Try and get the quickest locations first, moving on to more accurate fixes later
                // Check if local cache is less than 10 minutes old; If so, use that.
                // Next, check for last known locations on all providers.
                // If fix older than 10 minutes, get the network fix, and then GPS fix

                String locationString = null;
                long now = new Date().getTime();
                Location localCacheLocation = null, lastGPSLocation = null, lastNetworkLocation = null, bestLocation;



                // The beginning of the thread. Since we're trying to obtain the location, set it to null
                // sharedPreferenceEditor.putString("localGPSCache", null);
                // sharedPreferenceEditor.commit();

                // String locationProvider = LocationManager.NETWORK_PROVIDER;
                String localGPSCache = sharedPrefs.getString("localGPSCache", null);

                if(localGPSCache != null) { // We have a cache value; Convert it into Location
                    Log.i(LOGTAG, "CarLogActivity : locationThread : localGPSCache is not null. Creating Location");
                    localCacheLocation = new Location("LOCAL_GPS_CACHE");
                    String localCacheData[] = localGPSCache.split(","); // It's a CSV set of lat, lng, time, acc, provider
                    localCacheLocation.setLatitude(Double.parseDouble(localCacheData[0]));
                    localCacheLocation.setLongitude(Double.parseDouble(localCacheData[1]));
                    localCacheLocation.setTime(Long.parseLong(localCacheData[2]));
                    localCacheLocation.setAccuracy(Float.parseFloat(localCacheData[3]));
                    localCacheLocation.setProvider(localCacheData[4]);
                    updatesTextView.setText("Local GPS cache is " + (now - localCacheLocation.getTime())/1000 + " seconds old");
                    updateBestLocation(localCacheLocation);
                }

                if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Log.i(LOGTAG, "CarLogActivity : isProviderEnabled : Network Provider enabled. Getting last known");
                    lastNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    updatesTextView.setText("Last known network location is " + (now - lastNetworkLocation.getTime())/1000 + " seconds old");
                    updateBestLocation(lastNetworkLocation);
                }

                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.i(LOGTAG, "CarLogActivity : isProviderEnabled : GPS enabled. Getting last known");
                    lastGPSLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updatesTextView.setText("Last known GPS location is " + (now - lastGPSLocation.getTime())/1000 + " seconds old");
                    updateBestLocation(lastGPSLocation);
                }

                if(localGPSCache != null && (now - localCacheLocation.getTime()) < TEN_MINUTES) {
                    // We have a saved location that is less than 10 minutes ago. use that.
                    Log.i(LOGTAG, "CarLogActivity : locationThread : localGPSCache is " + (now - localCacheLocation.getTime()) / 1000 + " seconds old");
                    bestLocation = localCacheLocation;
                } else if(lastGPSLocation != null && (now - lastGPSLocation.getTime()) < TEN_MINUTES) {
                    // Last known GPS fix is less than 10 minutes old. Should be good enough.
                    Log.i(LOGTAG, "CarLogActivity : locationThread : lastGPSLocation is " + (now - lastGPSLocation.getTime()) / 1000 + " seconds old");
                    bestLocation = lastGPSLocation;
                } else if(lastNetworkLocation != null && (now - lastNetworkLocation.getTime()) < TEN_MINUTES) {
                    // Last known network fix is less than 10 minutes old. Should be good enough.
                    Log.i(LOGTAG, "CarLogActivity : locationThread : lastNetworkLocation is " + (now - lastNetworkLocation.getTime()) / 1000 + " seconds old");
                    bestLocation = lastNetworkLocation;
                } else {
                    // We have no good locations in the recent past. Request for new updates.
                    Log.i(LOGTAG, "CarLogActivity : locationThread : No usable known locations. Requesting all location updates");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, networkLocationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, gpsLocationListener);

                    listeningToGPS = true;
                    listeningToNetwork = true;
                }
            }

            private void updateBestLocation(Location newLocation) {
                long now = new Date().getTime();
                Log.d(LOGTAG, "CarLogActivity : updateBestLocation : Updating best location");

                if(bestLocation == null) { // If no current best location, use what we got.
                    Log.d(LOGTAG, "CarLogActivity : updateBestLocation : Best Location is null. Saving newLocation");
                    bestLocation = newLocation;
                } else if(now - newLocation.getTime() > TEN_MINUTES) { // If the newLocation fix is more than 10 minutes old
                    Log.d(LOGTAG, "CarLogActivity : updateBestLocation : newLocation is " + (now - newLocation.getTime())/1000 + " seconds old");
                    if(newLocation.getTime() > bestLocation.getTime()) { // new location is more recent. Save it
                        Log.d(LOGTAG, "CarLogActivity : updateBestLocation : Saving new location, " + (newLocation.getTime() - bestLocation.getTime())/1000 + " seconds newer");
                        bestLocation = newLocation;
                    }
                } else { // New Location fix is less than 10 minutes old
                    Log.d(LOGTAG, "CarLogActivity : updateBestLocation : newLocation is " + (now - newLocation.getTime())/1000 + " seconds old");
                    if(Math.abs(newLocation.getTime() - bestLocation.getTime()) <= 60000) {
                        // If both the fixes are within one minute of each other, get the one with better accuracy
                        Log.d(LOGTAG, "CarLogActivity : updateBestLocation : Best and new location are " + Math.abs(newLocation.getTime() - bestLocation.getTime())/1000 + " seconds apart");
                        if(newLocation.getAccuracy() < bestLocation.getAccuracy()) { // The new location is better.
                            Log.d(LOGTAG, "CarLogActivity : updateBestLocation : New location is " + (bestLocation.getAccuracy() - newLocation.getAccuracy()) + " meters more accurate");
                            bestLocation = newLocation;
                        }
                    } else if(newLocation.getTime() > bestLocation.getTime()) { // If more than a minute apart, save the new one
                        Log.d(LOGTAG, "CarLogActivity : updateBestLocation : Best and new location are " + Math.abs(newLocation.getTime() - bestLocation.getTime())/1000 + " seconds apart. Saving new Location");
                        bestLocation = newLocation;
                    }
                }

                // All said and done, if the bestLocation has good accuracy, stop listening
                if(bestLocation.getAccuracy() == Criteria.ACCURACY_MEDIUM) {
                    Log.d(LOGTAG, "CarLogActivity : updateBestLocation : We have medium accuracy. Stopping updates");
                    if(listeningToGPS)
                        locationManager.removeUpdates(gpsLocationListener);

                    if(listeningToNetwork)
                        locationManager.removeUpdates(networkLocationListener);
                }

                // Save the best location to Prefs so if the app decides to use it, it'll have something
                String locationString = bestLocation.getLatitude() + "," +
                                        bestLocation.getLongitude() + "," +
                                        bestLocation.getTime() + "," +
                                        bestLocation.getAccuracy() + "," +
                                        bestLocation.getProvider();
                Log.d(LOGTAG, "CarLogActivity : updateBestLocation : Save bestLocation to Prefs : " + locationString);
                sharedPreferenceEditor.putString("localGPSCache", locationString);
                sharedPreferenceEditor.commit();

            }
        }, "locationThread").run();
    }
    */
}
