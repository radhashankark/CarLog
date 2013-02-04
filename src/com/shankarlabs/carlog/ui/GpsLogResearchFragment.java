package com.shankarlabs.carlog.ui;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.shankarlabs.carlog.R;

import java.util.Date;

public class GpsLogResearchFragment extends SherlockFragment {
    private final static String LOGTAG = "CarLog";
    private boolean recordingData = false;
    private static boolean gettingLocationUpdates = false;
    private View gpsIndicator, nwIndicator;
    private TextView gpslongitude, gpslatitude, gpsaltitude, gpsaccuracy, gpsbearing, gpstime, gpssatellites;
    private TextView nwlongitude, nwlatitude, nwaltitude, nwaccuracy, nwbearing, nwtime, nwloctype, nwlocsrc, nwtrvlstate;
    private Button record, stopupdate;
    // private Location bestLocation;
    private LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSherlockActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, viewGroup, savedInstanceState);
        return layoutInflater.inflate(R.layout.gpslogresearch, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Indicators
        gpsIndicator = getSherlockActivity().findViewById(R.id.glrgpsindicator);
        nwIndicator = getSherlockActivity().findViewById(R.id.glrnwindicator);

        // GPS Fields
        gpslongitude = (TextView) getSherlockActivity().findViewById(R.id.glrgpslongitude);
        gpslatitude = (TextView) getSherlockActivity().findViewById(R.id.glrgpslatitude);
        gpsaltitude = (TextView) getSherlockActivity().findViewById(R.id.glrgpsaltitude);
        gpsaccuracy = (TextView) getSherlockActivity().findViewById(R.id.glrgpsaccuracy);
        gpsbearing = (TextView) getSherlockActivity().findViewById(R.id.glrgpsbearing);
        gpstime = (TextView) getSherlockActivity().findViewById(R.id.glrgpstime);
        gpssatellites = (TextView) getSherlockActivity().findViewById(R.id.glrgpssatellites);

        // Network Fields
        nwlongitude = (TextView) getSherlockActivity().findViewById(R.id.glrnwlongitude);
        nwlatitude = (TextView) getSherlockActivity().findViewById(R.id.glrnwlatitude);
        nwaltitude = (TextView) getSherlockActivity().findViewById(R.id.glrnwaltitude);
        nwaccuracy = (TextView) getSherlockActivity().findViewById(R.id.glrnwaccuracy);
        nwbearing = (TextView) getSherlockActivity().findViewById(R.id.glrnwbearing);
        nwtime = (TextView) getSherlockActivity().findViewById(R.id.glrnwtime);
        nwloctype = (TextView) getSherlockActivity().findViewById(R.id.glrnwloctype);
        nwlocsrc = (TextView) getSherlockActivity().findViewById(R.id.glrnwlocsrc);
        nwtrvlstate = (TextView) getSherlockActivity().findViewById(R.id.glrnwtrvlstate);

        // Buttons
        record = (Button) getSherlockActivity().findViewById(R.id.glrrecordbtn);
        stopupdate = (Button) getSherlockActivity().findViewById(R.id.glrstopupdatesbtn);

        record.setOnClickListener(recordOnClickListener);
        stopupdate.setOnClickListener(stopUpdateOnClickListener);

        getLocation(); // That is all we need to do here.
    }

    public void getLocation() {
        // updatesTextView.setText("Getting location");
        Log.d(LOGTAG, "GpsLogFragment : getLocation : Getting Location");


        String locationString = null;
        long now = new Date().getTime();
        Location localCacheLocation = null, lastGPSLocation = null, lastNetworkLocation = null, bestLocation;

        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.d(LOGTAG, "GpsLogFragment : getLocation : Network Provider enabled. Getting last known");
            lastNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Log.d(LOGTAG, "GpsLogFragment : getLocation : Last known network location is " + (now - lastNetworkLocation.getTime())/60000 + " minutes old");
            // updatesTextView.setText("Last known network location is " + (now - lastNetworkLocation.getTime()) / 60000 + " minutes old");
            updatenwLocData(lastNetworkLocation);
        }

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d(LOGTAG, "GpsLogFragment : getLocation : GPS Provider enabled. Getting last known");
            lastGPSLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Log.d(LOGTAG, "GpsLogFragment : getLocation : Last known GPS location is " + (now - lastGPSLocation.getTime())/60000 + " minutes old");
            // updatesTextView.setText("Last known GPS location is " + (now - lastGPSLocation.getTime()) / 60000 + " minutes old");
            updateGPSLocData(lastGPSLocation);
        }


        // Request for new updates.
        Log.i(LOGTAG, "GpsLogFragment : getLocation : Requesting all location updates");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationListener);

        gettingLocationUpdates = true;

        // listeningToGPS = true;
        // listeningToNetwork = true;
    }

    View.OnClickListener recordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!recordingData) { // We're not recording yet. Start recording
                Log.d(LOGTAG, "GpsLogFragment : recordOnClickListener : Starting GPS recording");
                // recordIndicator.setBackgroundColor(0xFF00FF00); // Green
                record.setText("Stop Recording");
                recordingData = true;
            } else {
                Log.d(LOGTAG, "GpsLogFragment : recordOnClickListener : Stopping GPS recording");
                // recordIndicator.setBackgroundColor(0xFFFF0000); // Red
                record.setText("Start Recording");
                recordingData = false;
            }

            // TODO Insert actual code to actually record the GPS data
        }
    };

    View.OnClickListener stopUpdateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(LOGTAG, "GpsLogFragment : recordOnClickListener : Saving location as favorite");

            // For now, use it as a way to stop listening to updates
            Log.d(LOGTAG, "GpsLogFragment : recordOnClickListener : Stopping listening to all updates");
            locationManager.removeUpdates(gpsLocationListener);
            locationManager.removeUpdates(networkLocationListener);
        }
    };

    LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Figure out if the obtained location is good enough
            Log.d(LOGTAG, "GpsLogFragment : gpsLocationListener : onLocationChanged");
            gpsIndicator.setBackgroundColor(0xFFFFFF00);
            updateGPSLocData(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Not sure what to do here
            Log.i(LOGTAG, "GpsLogFragment : gpsLocationListener : GPS Provider status changed to " + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Not sure what to do here
            Log.i(LOGTAG, "GpsLogFragment : gpsLocationListener : Provider " + provider + " enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Not sure what to do here
            Log.w(LOGTAG, "GpsLogFragment : gpsLocationListener : Provider " + provider + " disabled");
        }
    };

    LocationListener networkLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Figure out if the obtained location is good enough
            Log.d(LOGTAG, "GpsLogFragment : networkLocationListener : onLocationChanged");
            nwIndicator.setBackgroundColor(0xFFFFFF00);
            updatenwLocData(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Not sure what to do here
            Log.i(LOGTAG, "GpsLogFragment : networkLocationListener : Network Provider status changed to " + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Not sure what to do here
            Log.i(LOGTAG, "GpsLogFragment : networkLocationListener : Provider " + provider + " enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Not sure what to do here
            Log.w(LOGTAG, "GpsLogFragment : networkLocationListener : Provider " + provider + " disabled");
        }
    };

    /* Create a synchronized method to store location updates so there's no conflicts.
    private synchronized void updateBestLocation(Location newLocation) {
        long now = new Date().getTime();
        // Log.d(LOGTAG, "GpsLogFragment : updateBestLocation : Updating best location");

        if(bestLocation == null) { // If no current best location, use what we got.
            Log.d(LOGTAG, "GpsLogFragment : updateBestLocation : Best Location is null. Saving newLocation");
            bestLocation = newLocation;
        } else { // New Location fix is less than 10 minutes old
            Log.d(LOGTAG, "GpsLogFragment : updateBestLocation : newLocation is " + (now - newLocation.getTime())/1000 + " seconds old");
            if(Math.abs(newLocation.getTime() - bestLocation.getTime()) <= 60000) {
                // If both the fixes are within one minute of each other, get the one with better accuracy
                // Log.d(LOGTAG, "GpsLogFragment : updateBestLocation : new and best location are " + Math.abs(newLocation.getTime() - bestLocation.getTime())/1000 + " seconds apart");
                if(newLocation.getAccuracy() <= bestLocation.getAccuracy()) { // The new location is better.
                    Log.d(LOGTAG, "GpsLogFragment : updateBestLocation : New location is " + (bestLocation.getAccuracy() - newLocation.getAccuracy()) + " meters more accurate. Saving.");
                    bestLocation = newLocation;
                }
            } else if(newLocation.getTime() > bestLocation.getTime()) { // If more than a minute apart, save the new one
                Log.d(LOGTAG, "GpsLogFragment : updateBestLocation : new location is " + Math.abs(newLocation.getTime() - bestLocation.getTime())/1000 + " seconds newer. Saving.");
                bestLocation = newLocation;
            }
        }

        /* All said and done, if the bestLocation has good accuracy, stop listening
        if(bestLocation.getAccuracy() <= 10) {
            Log.d(LOGTAG, "GpsLogFragment : updateBestLocation : We have accuracy of 10m. Stopping updates");
            if(listeningToGPS)
                locationManager.removeUpdates(gpsLocationListener);

            if(listeningToNetwork)
                locationManager.removeUpdates(networkLocationListener);
        } else {
            Log.d(LOGTAG, "GpsLogFragment : updateBestLocation : Location Accuracy of " + newLocation.getAccuracy() + " not matching required value, 10");
        }
        */

        /* Save the best location to Prefs so if the app decides to use it, it'll have something
        String locationString = bestLocation.getLatitude() + "," +
                bestLocation.getLongitude() + "," +
                bestLocation.getTime() + "," +
                bestLocation.getAccuracy() + "," +
                bestLocation.getProvider();
        Log.d(LOGTAG, "GpsLogFragment : updateBestLocation : Save bestLocation to Prefs : " + locationString);
        sharedPreferenceEditor.putString("localGPSCache", locationString);
        sharedPreferenceEditor.commit();
        */

        /*
        // Reset the indicators
        String bestProvider = bestLocation.getProvider();
        if(bestProvider.equalsIgnoreCase("network")) {
            nwIndicator.setBackgroundColor(0xFF00FF00);
        } else if(bestProvider.equalsIgnoreCase("GPS")) {
            gpsIndicator.setBackgroundColor(0xFF00FF00);
        }

        /* Update Data on the Fragment
        longitude.setText("" + bestLocation.getLongitude());
        latitude.setText("" + bestLocation.getLatitude());

        if(bestLocation.hasAltitude())
            altitude.setText("" + bestLocation.getAltitude() + " m");
        else
            altitude.setText("N/A");

        if(bestLocation.hasAccuracy())
            accuracy.setText("" + bestLocation.getAccuracy() + " m");
        else
            accuracy.setText("N/A");

        if(bestLocation.hasBearing())
            bearing.setText("" + bestLocation.getBearing() + " m");
        else
            bearing.setText("N/A");

        time.setText(DateUtils.formatDateTime(getSherlockActivity().getApplicationContext(), bestLocation.getTime(), 17));
        provider.setText(bestLocation.getProvider());

        Bundle gpsData = bestLocation.getExtras();
        Set<String> gpsDataKeys = gpsData.keySet();
        for(String key : gpsDataKeys) {
            String dataType = gpsData.get(key).getClass().getName();

            Log.d(LOGTAG, "GpsLogFragment : updateBestLocation : Key = " + key + ", type = " + dataType);
        }

    } */

    public void updateGPSLocData(Location location) {
        // Update Data on the Fragment
        gpslongitude.setText("" + location.getLongitude());
        gpslatitude.setText("" + location.getLatitude());

        if(location.hasAltitude())
            gpsaltitude.setText("" + location.getAltitude() + " m");
        else
            gpsaltitude.setText("N/A");

        if(location.hasAccuracy())
            gpsaccuracy.setText("" + location.getAccuracy() + " m");
        else
            gpsaccuracy.setText("N/A");

        if(location.hasBearing())
            gpsbearing.setText("" + location.getBearing() + " m");
        else
            gpsbearing.setText("N/A");

        gpstime.setText(DateUtils.formatDateTime(getSherlockActivity().getApplicationContext(), location.getTime(), 17));

        Bundle gpsData = location.getExtras();
        gpssatellites.setText(gpsData.getString("satellites"));
    }

    public void updatenwLocData(Location location) {
        // Update Data on the Fragment
        nwlongitude.setText("" + location.getLongitude());
        nwlatitude.setText("" + location.getLatitude());

        if(location.hasAltitude())
            nwaltitude.setText("" + location.getAltitude() + " m");
        else
            nwaltitude.setText("N/A");

        if(location.hasAccuracy())
            nwaccuracy.setText("" + location.getAccuracy() + " m");
        else
            nwaccuracy.setText("N/A");

        if(location.hasBearing())
            nwbearing.setText("" + location.getBearing() + " m");
        else
            nwbearing.setText("N/A");

        nwtime.setText(DateUtils.formatDateTime(getSherlockActivity().getApplicationContext(), location.getTime(), 17));

        Bundle gpsData = location.getExtras();
        nwloctype.setText(gpsData.getString("networkLocationType"));
        nwlocsrc.setText(gpsData.getString("networkLocationSource"));
        nwtrvlstate.setText(gpsData.getString("travelState"));
    }

    @Override
    public void onResume() {
        super.onResume();

        // Start/Register for the GPS updates
        if(!gettingLocationUpdates) {
            // Request for new updates.
            Log.d(LOGTAG, "GpsLogFragment : onResume : Requesting all location updates");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationListener);

            gettingLocationUpdates = true;
        } else {
            Log.d(LOGTAG, "GpsLogFragment : onResume : Already listening to GPS updates");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Pause/Unregister the GPS updates
        if(gettingLocationUpdates) {
            Log.d(LOGTAG, "GpsLogFragment : onPause : Pausing listening to all updates");
            locationManager.removeUpdates(gpsLocationListener);
            locationManager.removeUpdates(networkLocationListener);

            gettingLocationUpdates = false;
        } else {
            Log.d(LOGTAG, "GpsLogFragment : onPause : Not listening for updates anyway.");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Pause/Unregister the GPS updates
        if(gettingLocationUpdates) {
            Log.d(LOGTAG, "GpsLogFragment : onDestroyView : Stopping listening to all updates");
            locationManager.removeUpdates(gpsLocationListener);
            locationManager.removeUpdates(networkLocationListener);

            gettingLocationUpdates = false;
        } else {
            Log.d(LOGTAG, "GpsLogFragment : onDestroyView : Not listening for updates anyway.");
        }
    }
}
