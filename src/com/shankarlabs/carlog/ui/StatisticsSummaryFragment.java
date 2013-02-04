package com.shankarlabs.carlog.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.shankarlabs.carlog.R;
import com.shankarlabs.carlog.core.FillupDBHelper;

public class StatisticsSummaryFragment extends SherlockFragment {

    private Context mContext;
    private FillupDBHelper fillupDBHelper;
    private static final String LOGTAG = "CarLog";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillupDBHelper = new FillupDBHelper(getSherlockActivity().getApplicationContext());

        Bundle statsData = getArguments(); // We're always passed the vehicleCode for which to display the Data
        int vehicleCode = statsData.getInt("vehicleCode", -1);
        Log.d(LOGTAG, "StatSummary : onCreate : Working with vehicleCode " + vehicleCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fillupstatisticssummary, container, false); // Don't attach to root
        Log.d(LOGTAG, "StatSummary : onCreateView : Inflating Summary Fragment");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(LOGTAG, "StatSummary : onActivityCreated : Got to get the stats and calculate the Summary");

    }

}
