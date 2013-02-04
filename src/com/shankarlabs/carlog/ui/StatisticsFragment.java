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
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shankarlabs.carlog.R;
import com.shankarlabs.carlog.core.VehicleDBHelper;

public class StatisticsFragment extends SherlockFragment {

    private static Context mContext;
    private final static String LOGTAG = "CarLog";
    private static boolean mDualPane;
    private Spinner statsFor, statsUnits;
    private TextView mileage, distance, volume, totalCost, statsWindow;
    private RadioGroup statisticsTypeRG;
    private static VehicleDBHelper vehicleDBHelper;
    // private FillupDBHelper fillupDBHelper;

    public StatisticsFragment() {
        super();
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

        mContext = getSherlockActivity().getApplicationContext();

        // Get the status of the dual panes
        View pane2 = getSherlockActivity().findViewById(R.id.pane2_fragment);
        mDualPane = pane2 != null &&  pane2.getVisibility() == View.VISIBLE;

        if(!mDualPane) { // Set the following only if there's a spinner available. As in, no dual panes
            getSherlockActivity().getActionBar().setSelectedNavigationItem(4);
        }

        // Check the Raw Stats Button
        RadioGroup rawStatsRadioGroup = (RadioGroup) getSherlockActivity().findViewById(R.id.statisticstype);
        // rawStatsRadioGroup.check(R.id.rawstatistics);

        statsFor = (Spinner) getSherlockActivity().findViewById(R.id.statsfor);
        statsUnits = (Spinner) getSherlockActivity().findViewById(R.id.statsunits);
        mileage = (TextView) getSherlockActivity().findViewById(R.id.mileage);
        distance = (TextView) getSherlockActivity().findViewById(R.id.distance);
        volume = (TextView) getSherlockActivity().findViewById(R.id.volume);
        totalCost = (TextView) getSherlockActivity().findViewById(R.id.totalcost);
        statsWindow = (TextView) getSherlockActivity().findViewById(R.id.statswindow);
        statisticsTypeRG = (RadioGroup) getSherlockActivity().findViewById(R.id.statisticstype);

        statsFor.setOnItemSelectedListener(statsForOnItemSelectedListener);
        // statsUnits.setOnItemSelectedListener(statsUnitsOnItemSelectedListener);
        statisticsTypeRG.setOnCheckedChangeListener(statTypeChangeListener);

        // Get the DB instances
        vehicleDBHelper = new VehicleDBHelper(mContext);
        // fillupDBHelper = new FillupDBHelper(mContext);

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

                    /* We don't have to show the raw fragment; Setting the Spinner will do it automatically for us.
                    // Pass the vehicleCode to the RawStatisticsFragment
                    int vehicleCode = cursor.getInt(1);
                    Log.d(LOGTAG, "StatisticsFragment : onActivityCreated : Showing all data for vehicle code " + vehicleCode);
                    Bundle fillupData = new Bundle();
                    fillupData.putInt("vehicleCode", vehicleCode);
                    SherlockListFragment rawStatisticsFragment = new RawStatisticsFragment();
                    rawStatisticsFragment.setArguments(fillupData);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.pane3_fragment, rawStatisticsFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    // ft.addToBackStack(null);
                    ft.commit(); // ABS needs commit
                    */
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
                SherlockFragment fillUpFragment = new FillUpFragment();
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
            // Any time you change the vehicle, revert to the Raw Stats
            statisticsTypeRG.check(R.id.rawstatistics);

            /* The change in the radio button will take care of loading the fragment into pane 3
            // Get the vehicle code
            Cursor tempCursor = (Cursor) statsFor.getSelectedItem();
            int vehicleCode = tempCursor.getInt(1); // 1 is the code of the vehicle

            // Pass the vehicleCode to the RawStatisticsFragment
            Log.d(LOGTAG, "StatisticsFragment : statsForOnItemSelectedListener : Showing all data for vehicle code " + vehicleCode);
            Bundle fillupData = new Bundle();
            fillupData.putInt("vehicleCode", vehicleCode);
            SherlockListFragment rawStatisticsFragment = new RawStatisticsFragment();
            rawStatisticsFragment.setArguments(fillupData);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.pane3_fragment, rawStatisticsFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // ft.addToBackStack(null);
            ft.commit(); // ABS needs commit,
            */
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // Nothing selected means you don't have anything to do.
            Log.d(LOGTAG, "StatisticsFragment : statsForOnItemSelectedListener : Nothing selected");
        }
    };

    AdapterView.OnItemSelectedListener statsUnitsOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Log.d(LOGTAG, "StatisticsFragment : statsUnitsOnItemSelectedListener : Units selected " + i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Log.d(LOGTAG, "StatisticsFragment : statsUnitsOnItemSelectedListener : Nothing selected");
        }
    };

    RadioGroup.OnCheckedChangeListener statTypeChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.d(LOGTAG, "StatisticsFragment : statTypeChangeListener : Stats type selected : " + checkedId);
            int vehicleCode = ((Cursor) statsFor.getSelectedItem()).getInt(1);
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            switch(checkedId) {
                case R.id.rawstatistics :
                    // Pass the vehicleCode to the RawStatisticsFragment
                    Log.d(LOGTAG, "StatisticsFragment : onActivityCreated : Showing all data for vehicle code " + vehicleCode);
                    Bundle fillupData = new Bundle();
                    fillupData.putInt("vehicleCode", vehicleCode);
                    SherlockListFragment rawStatisticsFragment = new RawStatisticsFragment();
                    rawStatisticsFragment.setArguments(fillupData);

                    ft.replace(R.id.pane3_fragment, rawStatisticsFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    // ft.addToBackStack(null);
                    ft.commit(); // ABS needs commit
                    break;
                case R.id.statisticssummary :
                    // Pass the vehicleCode to the RawStatisticsFragment
                    Log.d(LOGTAG, "StatisticsFragment : onActivityCreated : Showing summary for vehicle code " + vehicleCode);
                    Bundle summaryData = new Bundle();
                    summaryData.putInt("vehicleCode", vehicleCode);
                    SherlockFragment statisticsSummaryFragment = new StatisticsSummaryFragment();
                    statisticsSummaryFragment.setArguments(summaryData);

                    ft.replace(R.id.pane3_fragment, statisticsSummaryFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    // ft.addToBackStack(null);
                    ft.commit(); // ABS needs commit
                    break;
            }
        }
    };
}
