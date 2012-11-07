package com.shankarlabs.carlog.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shankarlabs.carlog.R;
import com.shankarlabs.carlog.core.VehicleDBHelper;

public class VehiclesFragment extends SherlockFragment {

    private static Context mContext;
    private static boolean mDualPane;
    private final static String LOGTAG = "CarLog";
    private static VehicleDBHelper vehicleDBHelper;
    private Spinner vehicleNames, unitsUsed;
    private EditText vehicleCode, vehicleName, vehicleDescription;
    private CheckBox defaultVehicle;


    public VehiclesFragment(Context context) {
        super();

        mContext = context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View fragmentView = inflater.inflate(R.layout.vehicles, container, false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the status of the dual panes
        View pane2 = getSherlockActivity().findViewById(R.id.pane2_fragment);
        mDualPane = pane2 != null &&  pane2.getVisibility() == View.VISIBLE;

        if(!mDualPane) { // Set the following only if there's a spinner available. As in, no dual panes
            getSherlockActivity().getActionBar().setSelectedNavigationItem(2);
        }

        vehicleNames = (Spinner) getSherlockActivity().findViewById(R.id.vehiclenames);
        vehicleCode = (EditText) getSherlockActivity().findViewById(R.id.vehiclecode);
        vehicleName = (EditText) getSherlockActivity().findViewById(R.id.vehiclename);
        defaultVehicle = (CheckBox) getSherlockActivity().findViewById(R.id.defaultvehicle);
        unitsUsed = (Spinner) getSherlockActivity().findViewById(R.id.units);
        vehicleDescription = (EditText) getSherlockActivity().findViewById(R.id.description);

        vehicleNames.setOnItemSelectedListener(vehicleNamesOnItemSelectedListener);

        // Get the DB instances
        vehicleDBHelper = new VehicleDBHelper(mContext);

        // Populate the Vehicle types
        Cursor cursor = vehicleDBHelper.getAllVehicles();
        if(cursor == null) {
            Log.w(LOGTAG, "VehiclesFragment : onActivityCreated : All Vehicle Names Cursor is null");
            Toast.makeText(mContext, "No vehicle names found", Toast.LENGTH_SHORT).show();
        } else if(cursor.getCount() == 0) {
            Log.w(LOGTAG, "VehiclesFragment : onActivityCreated : Zero vehicles found");
            Toast.makeText(mContext, "No vehicles saved", Toast.LENGTH_SHORT).show();
        } else {
            // cursor.moveToFirst(); // Move to first to start reading
            // cursor.moveToNext(); // Move to the next one to skip one default row

            String[] projection = {"NAME"};
            int[] mapTo = {android.R.id.text1};

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(mContext,
                    R.layout.cl_spinner_item,
                    cursor,
                    projection,
                    mapTo,
                    0);

            // simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            vehicleNames.setAdapter(simpleCursorAdapter);

            // If we have more than one vehicles saved, set the default vehicle in the spinner
            // Save one Cursor and calls to DB by searching for default vehicle right here
            int isDefault = cursor.getInt(3);
            while(cursor.moveToNext()) {
                if(isDefault == 1) {
                    cursor.moveToPrevious(); // We already moved one ahead. Rewind one.
                    vehicleNames.setSelection(cursor.getPosition());
                    // populateVehicleData(defaultVehicleId);
                    break;
                } else {
                    isDefault = cursor.getInt(3);
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // MenuInflater menuInflater = getSherlockActivity().getSupportMenuInflater();
        menu.clear(); // First clear out all the elements in the menu
        getSherlockActivity().onCreateOptionsMenu(menu); // Add the Activity's options menu
        menuInflater.inflate(R.menu.vehicles, menu);

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
            case android.R.id.home :
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SherlockFragment fillUpFragment = new FillUpFragment(mContext);
                if(mDualPane)
                    ft.replace(R.id.pane2_fragment, fillUpFragment);
                else
                    ft.replace(R.id.pane1_fragment, fillUpFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                ft.commit();
                break;
            case R.id.savevehicle:
                Log.d(LOGTAG, "VehiclesFragment : onOptionsItemSelected : Saving Vehicle " + vehicleNames.getSelectedItem().toString());
                Toast.makeText(mContext, "Saving vehicle " + vehicleNames.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                saveVehicle();
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    AdapterView.OnItemSelectedListener vehicleNamesOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            // When a vehicle is selected, get vehicle data and populate the fields
            populateVehicleData(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // Not sure when this is triggered. So do nothing
            Log.d(LOGTAG, "VehiclesFragment : onNothingSelected : Nothing selected");
        }
    };

    private boolean saveVehicle() {
        return vehicleDBHelper.saveVehicle(Integer.parseInt(vehicleCode.getText().toString()),
                vehicleName.getText().toString(),
                defaultVehicle.isChecked() ? 1 : 0,
                unitsUsed.getSelectedItemPosition(),
                vehicleDescription.getText().toString());
    }

    private void populateVehicleData(int id) {
        String[] vehicleData = vehicleDBHelper.getVehicleData(id);
        if(vehicleData != null) {
            // Populate all fields
            vehicleCode.setText(vehicleData[1]);
            vehicleName.setText(vehicleData[2]);
            unitsUsed.setSelection(Integer.parseInt(vehicleData[4]));
            vehicleDescription.setText(vehicleData[5]);
        } else {
            Log.w(LOGTAG, "VehiclesFragment : populateVehicleData : Vehicle Data is null for position " + id);
            Toast.makeText(mContext, "No vehicle data found for ID " + id, Toast.LENGTH_SHORT).show();
        }

    }
}
