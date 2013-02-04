package com.shankarlabs.carlog.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shankarlabs.carlog.R;
import com.shankarlabs.carlog.core.FillupDBHelper;
import com.shankarlabs.carlog.core.VehicleDBHelper;

import java.util.Date;

public class FillUpFragment extends SherlockFragment {

    private static final String LOGTAG = "CarLog";
    private static Context mContext;
    private boolean mDualPane;
    private Spinner fillupFor;
    private EditText volumeEditText, priceEditText, distanceEditText, commentsEditText, dateEditText, timeEditText;
    private CheckBox saveLocation, partialFillup;
    private Button saveFillupButton;
    private VehicleDBHelper vehicleDBHelper;
    private FillupDBHelper fillupDBHelper;
    private SharedPreferences sharedPreferences;

    public FillUpFragment () {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // Home/first screen. Doesn't need Home as Up
        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fillup, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getSherlockActivity().getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        // Get Pane2 status
        View pane2 = getSherlockActivity().findViewById(R.id.pane2_fragment);
        mDualPane = pane2 != null && pane2.getVisibility() == View.VISIBLE;

        if(!mDualPane) { // Set the following only if there's a spinner available. As in, no dual panes
            getSherlockActivity().getActionBar().setSelectedNavigationItem(0);
        }

        vehicleDBHelper = new VehicleDBHelper(mContext);
        fillupDBHelper = new FillupDBHelper(mContext);

        fillupFor = (Spinner) getSherlockActivity().findViewById(R.id.fillupfor);
        volumeEditText = (EditText) getSherlockActivity().findViewById(R.id.volumeedittext);
        priceEditText = (EditText) getSherlockActivity().findViewById(R.id.priceedittext);
        distanceEditText = (EditText) getSherlockActivity().findViewById(R.id.distanceedittext);
        commentsEditText = (EditText) getSherlockActivity().findViewById(R.id.commentsedittext);
        saveLocation = (CheckBox) getSherlockActivity().findViewById(R.id.savelocationckbx);
        partialFillup = (CheckBox) getSherlockActivity().findViewById(R.id.partialfillupckbx);
        saveFillupButton = (Button) getSherlockActivity().findViewById(R.id.savefillupbtn);
        dateEditText = (EditText) getSherlockActivity().findViewById(R.id.dateedittext);
        timeEditText = (EditText) getSherlockActivity().findViewById(R.id.timeedittext);

        long millis = new Date().getTime();
        String date = DateUtils.formatDateTime(mContext, millis, DateUtils.FORMAT_SHOW_DATE);
        String time = DateUtils.formatDateTime(mContext, millis, DateUtils.FORMAT_SHOW_TIME + DateUtils.FORMAT_24HOUR);

        dateEditText.setText(date);
        timeEditText.setText(time);

        saveFillupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFillup();
                resetFragment();
            }
        });

        Cursor cursor = vehicleDBHelper.getAllVehicles();
        if(cursor == null) {
            Log.w(LOGTAG, "FillUpFragment : onActivityCreated : All Vehicle Names Cursor is null");
            Toast.makeText(mContext, "No vehicle names found", Toast.LENGTH_SHORT).show();
        } else if(cursor.getCount() == 0) {
            Log.w(LOGTAG, "FillUpFragment : onActivityCreated : Zero vehicles found");
            Toast.makeText(mContext, "No vehicles saved", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst(); // Move to first to start reading
            // cursor.moveToNext(); // Move to the next one to skip one default row

            String[] projection = new String[] {"NAME"};
            int[] mapTo = new int[] {android.R.id.text1};

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(mContext,
                    R.layout.cl_spinner_item,
                    cursor,
                    projection,
                    mapTo,
                    0);

            fillupFor.setAdapter(simpleCursorAdapter);

            // Save one Cursor and calls to DB by searching for default vehicle right here
            int isDefault = cursor.getInt(3);
            while(cursor.moveToNext()) {
                if(isDefault == 1) {
                    cursor.moveToPrevious(); // We already moved one ahead. Rewind one.
                    fillupFor.setSelection(cursor.getPosition());
                    // populateVehicleData(defaultVehicleId);
                    break;
                } else {
                    isDefault = cursor.getInt(3);
                }
            }
        }

        vehicleDBHelper.dumpDbToLogs();
        fillupDBHelper.dumpDbToLogs();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // MenuInflater menuInflater = getSherlockActivity().getSupportMenuInflater();
        menu.clear(); // First clear out all the elements in the menu
        getSherlockActivity().onCreateOptionsMenu(menu); // Add the Activity's options menu
        menuInflater.inflate(R.menu.fillup, menu);

        super.onCreateOptionsMenu(menu, menuInflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        // This is a good time to figure out if we have dual panes
        // TODO Figure out if mDualpane needs detection at a later point of time
        // View pane2 = findViewById(R.id.pane2_fragment);
        // mDualPane = pane2 != null &&  pane2.getVisibility() == View.VISIBLE;

        switch (itemId) {
            case R.id.save:
                Log.d(LOGTAG, "FillUpFragment : onOptionsItemSelected : Saving all data");
                Toast.makeText(mContext, "Saving", Toast.LENGTH_SHORT).show();
                saveFillup();
                resetFragment();
                break;
            case R.id.camera:
                Log.d(LOGTAG, "FillUpFragment : onOptionsItemSelected : Starting to take a photo");
                Toast.makeText(mContext, "Starting to take a photo", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private boolean saveFillup() {
        Log.d(LOGTAG, "FillUpFragment : saveFillup : Saving fillup, volume " + volumeEditText.getText().toString() +
                        ", distance " + distanceEditText.getText().toString() +
                        ", price " + priceEditText.getText().toString() +
                        ", date " + dateEditText.getText().toString() + " " + timeEditText.getText().toString() +
                        ", isPartial " + (partialFillup.isChecked() ? "yes" : "no") +
                        // ", Vehicle " + vehicleDBHelper.getVehicleData(fillupFor.getSelectedItemPosition())[1] +
                        ", Vehicle " + ((Cursor) fillupFor.getSelectedItem()).getInt(1) +
                        ", Latitude " + (saveLocation.isChecked() ? sharedPreferences.getString("localGPSCache", ",,,,").split(",")[0] : "0.0000") +
                        ", Longitude " + (saveLocation.isChecked() ? sharedPreferences.getString("localGPSCache", ",,,,").split(",")[1] : "0.0000") +
                        ", Comments " + commentsEditText.getText().toString() +
                        ", reset calculations : No");

        return fillupDBHelper.saveFillup(
                Float.parseFloat(volumeEditText.getText().toString()),
                distanceEditText.getText().toString(),
                Float.parseFloat(priceEditText.getText().toString()),
                dateEditText.getText().toString() + " " + timeEditText.getText().toString(),
                partialFillup.isChecked(),
                ((Cursor) fillupFor.getSelectedItem()).getInt(1),
                saveLocation.isChecked() ? sharedPreferences.getString("localGPSCache", ",,,,").split(",")[0] : "0.0000",
                saveLocation.isChecked() ? sharedPreferences.getString("localGPSCache", ",,,,").split(",")[1] : "0.0000",
                commentsEditText.getText().toString(),
                0,
                null);

    }

    private void resetFragment() {
        fillupFor.setSelection(vehicleDBHelper.getDefaultVehicleId());
        volumeEditText.setText("");
        priceEditText.setText("");
        distanceEditText.setText("");
        commentsEditText.setText("");
        // saveLocation.setChecked(true); // Leave it's state at what it was for the last fillup
        partialFillup.setChecked(false);

        long millis = new Date().getTime();
        String date = DateUtils.formatDateTime(mContext, millis, DateUtils.FORMAT_SHOW_DATE);
        String time = DateUtils.formatDateTime(mContext, millis, DateUtils.FORMAT_SHOW_TIME + DateUtils.FORMAT_24HOUR);

        dateEditText.setText(date);
        timeEditText.setText(time);
    }
}
