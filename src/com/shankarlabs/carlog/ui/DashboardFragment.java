package com.shankarlabs.carlog.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockFragment;
import com.shankarlabs.carlog.R;

public class DashboardFragment extends SherlockFragment {

    private boolean mDualPane = true; // The mDualPane is always true when we're dealing with DashboardFragment
    private Button fillups, maintenance, vehicleTypes, maintenanceTypes, gpsLog, statistics;
    // private static Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // mContext = getSherlockActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View fragmentView = inflater.inflate(R.layout.dashboard, container, false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fillups = (Button) getActivity().findViewById(R.id.fillups);
        maintenance = (Button) getActivity().findViewById(R.id.maintenance);
        vehicleTypes = (Button) getActivity().findViewById(R.id.vehicletypes);
        maintenanceTypes = (Button) getActivity().findViewById(R.id.maintenancetypes);
        gpsLog = (Button) getActivity().findViewById(R.id.gpslog);
        statistics = (Button) getActivity().findViewById(R.id.statistics);

        fillups.setOnClickListener(fillupOnClickListener);
        maintenance.setOnClickListener(maintenanceOnClickListener);
        vehicleTypes.setOnClickListener(vehicleTypesOnClickListener);
        maintenanceTypes.setOnClickListener(maintenanceTypesOnClickListener);
        gpsLog.setOnClickListener(gpsLogOnClickListener);
        statistics.setOnClickListener(statisticsOnClickListener);
    }

    View.OnClickListener fillupOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDualPane) { // If we have dual panes, put a fragment in the second pane
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SherlockFragment fillUpFragment = new FillUpFragment();
                ft.replace(R.id.pane2_fragment, fillUpFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                ft.commit();
            }
        }
    };

    View.OnClickListener maintenanceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDualPane) { // If we have dual panes, put a fragment in the second pane
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SherlockFragment maintenanceFragment = new MaintenanceFragment();
                ft.replace(R.id.pane2_fragment, maintenanceFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                ft.commit();
            }
        }
    };

    View.OnClickListener vehicleTypesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDualPane) { // If we have dual panes, put a fragment in the second pane
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SherlockFragment vehiclesFragment = new VehiclesFragment();
                ft.replace(R.id.pane2_fragment, vehiclesFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                ft.commit();
            }
        }
    };

    View.OnClickListener maintenanceTypesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDualPane) { // If we have dual panes, put a fragment in the second pane
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SherlockFragment maintenanceTypesFragment = new MaintenanceTypesFragment();
                ft.replace(R.id.pane2_fragment, maintenanceTypesFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                ft.commit();
            }
        }
    };

    View.OnClickListener gpsLogOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDualPane) { // If we have dual panes, put a fragment in the second pane
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                // SherlockFragment gpsLogFragment = new GpsLogResearchFragment(); // GpsLogFragment();
                SherlockFragment gpsLogFragment = new GpsLogResearchFragment();
                ft.replace(R.id.pane2_fragment, gpsLogFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                ft.commit();
            }
        }
    };
    View.OnClickListener statisticsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDualPane) { // If we have dual panes, put a fragment in the second pane
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SherlockFragment statisticsFragment = new StatisticsFragment();
                ft.replace(R.id.pane2_fragment, statisticsFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                ft.commit();
            }
        }
    };
}
