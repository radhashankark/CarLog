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
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shankarlabs.carlog.R;
import com.shankarlabs.carlog.core.FillupDBHelper;
import com.shankarlabs.carlog.core.VehicleDBHelper;

public class StatisticsFragment extends SherlockFragment {

    private static Context mContext;
    private final static String LOGTAG = "CarLog";
    private static boolean mDualPane;
    private Spinner statsFor, statsUnits;
    private TextView mileage, distance, volume, totalCost, statsWindow;
    private VehicleDBHelper vehicleDBHelper;
    private FillupDBHelper fillupDBHelper;

    public StatisticsFragment(Context context) {
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
        View fragmentView = inflater.inflate(R.layout.statistics, container, false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the status of the dual panes
        View pane2 = getSherlockActivity().findViewById(R.id.pane2_fragment);
        mDualPane = pane2 != null &&  pane2.getVisibility() == View.VISIBLE;

        if(!mDualPane) { // Set the following only if there's a spinner available. As in, no dual panes
            getSherlockActivity().getActionBar().setSelectedNavigationItem(4);
        }

        statsFor = (Spinner) getSherlockActivity().findViewById(R.id.statsfor);
        statsUnits = (Spinner) getSherlockActivity().findViewById(R.id.statsunits);
        mileage = (TextView) getSherlockActivity().findViewById(R.id.mileage);
        distance = (TextView) getSherlockActivity().findViewById(R.id.distance);
        volume = (TextView) getSherlockActivity().findViewById(R.id.volume);
        totalCost = (TextView) getSherlockActivity().findViewById(R.id.totalcost);
        statsWindow = (TextView) getSherlockActivity().findViewById(R.id.statswindow);

        statsFor.setOnItemSelectedListener(statsForOnItemSelectedListener);
        statsUnits.setOnItemSelectedListener(statsUnitsOnItemSelectedListener);

        // Get the DB instances
        vehicleDBHelper = new VehicleDBHelper(mContext);
        fillupDBHelper = new FillupDBHelper(mContext);

        // Populate the Vehicle types
        Cursor cursor = vehicleDBHelper.getAllVehicles();
        if(cursor == null) {
            Log.w(LOGTAG, "StatisticsFragment : onActivityCreated : All Vehicle Names Cursor is null");
            Toast.makeText(mContext, "No vehicle names found", Toast.LENGTH_SHORT).show();
        } else if(cursor.getCount() == 0) {
            Log.w(LOGTAG, "StatisticsFragment : onActivityCreated : Zero vehicles found");
            Toast.makeText(mContext, "No vehicles saved", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst(); // Move to first to start reading
            // cursor.moveToNext(); // Move to the next one to skip one default row

            String[] projection = {"NAME"};
            int[] mapTo = {android.R.id.text1};

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(mContext,
                    R.layout.cl_spinner_item,
                    cursor,
                    projection,
                    mapTo,
                    0);

            statsFor.setAdapter(simpleCursorAdapter);

            // If we have more than one vehicles saved, set the default vehicle in the spinner
            // Save one Cursor and calls to DB by searching for default vehicle right here
            int isDefault = cursor.getInt(3);
            while(cursor.moveToNext()) {
                if(isDefault == 1) {
                    cursor.moveToPrevious(); // We already moved one ahead. Rewind one.
                    statsFor.setSelection(cursor.getPosition());
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
        // getSherlockActivity().onCreateOptionsMenu(menu); // Add the Activity's options menu
        // menuInflater.inflate(R.menu.vehicles, menu);

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
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }


    AdapterView.OnItemSelectedListener statsForOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };

    AdapterView.OnItemSelectedListener statsUnitsOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };
}
