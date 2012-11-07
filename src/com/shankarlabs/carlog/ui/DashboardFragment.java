package com.shankarlabs.carlog.ui;

import android.content.Context;
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
    private Button fillupsTextView, maintenanceTextView, vehicleTypesTextView, maintenanceTypesTextView, statisticsTextView;
    private static Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getSherlockActivity().getApplicationContext();
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

        fillupsTextView = (Button) getActivity().findViewById(R.id.fillups);
        maintenanceTextView = (Button) getActivity().findViewById(R.id.maintenance);
        vehicleTypesTextView = (Button) getActivity().findViewById(R.id.vehicletypes);
        maintenanceTypesTextView = (Button) getActivity().findViewById(R.id.maintenancetypes);
        statisticsTextView = (Button) getActivity().findViewById(R.id.statistics);

        fillupsTextView.setOnClickListener(fillupOnClickListener);
        maintenanceTextView.setOnClickListener(maintenanceOnClickListener);
        vehicleTypesTextView.setOnClickListener(vehicleTypesOnClickListener);
        maintenanceTypesTextView.setOnClickListener(maintenanceTypesOnClickListener);
        statisticsTextView.setOnClickListener(statisticsOnClickListener);
    }

    View.OnClickListener fillupOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDualPane) { // If we have dual panes, put a fragment in the second pane
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SherlockFragment fillUpFragment = new FillUpFragment(mContext);
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
                SherlockFragment maintenanceFragment = new MaintenanceFragment(mContext);
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
                SherlockFragment vehiclesFragment = new VehiclesFragment(mContext);
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
                SherlockFragment maintenanceTypesFragment = new MaintenanceTypesFragment(mContext);
                ft.replace(R.id.pane2_fragment, maintenanceTypesFragment);
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
                SherlockFragment statisticsFragment = new StatisticsFragment(mContext);
                ft.replace(R.id.pane2_fragment, statisticsFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                ft.commit();
            }
        }
    };
}
