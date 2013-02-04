package com.shankarlabs.carlog.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.shankarlabs.carlog.R;
import com.shankarlabs.carlog.core.FillupDBHelper;

public class RawStatisticsFragment extends SherlockListFragment {

    private FillupDBHelper fillupDBHelper;
    private int vehicleCode;
    private static final String LOGTAG = "CarLog";
    private boolean mDualPane;
    private TextView rawStatsStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillupDBHelper = new FillupDBHelper(getSherlockActivity().getApplicationContext());

        Bundle statsData = getArguments(); // We're always passed the vehicleCode for which to display the Data
        vehicleCode = statsData.getInt("vehicleCode", -1);
        Log.d(LOGTAG, "RawStatsFragment : onCreate : Working with vehicleCode " + vehicleCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View fragmentView = inflater.inflate(R.layout.filluprawstatistics, container, false); // Don't attach to root
        // Log.d(LOGTAG, "RawStatsFragment : onCreateView : Inflated filluprawstatistics");
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get Pane2 status
        View pane2 = getSherlockActivity().findViewById(R.id.pane2_fragment);
        mDualPane = pane2 != null && pane2.getVisibility() == View.VISIBLE;

        Log.d(LOGTAG, "RawStatsFragment : onActivityCreated : Getting stats and displaying them");
        rawStatsStatus = (TextView) getSherlockActivity().findViewById(R.id.rawstatsstatus);

        // Populate the data
        CursorAdapter rawStatsCursorAdapter = null;
        Cursor rawStatsCursor = fillupDBHelper.getStatsFor(vehicleCode);

        if(rawStatsCursor == null) {
            Log.e(LOGTAG, "RawStatsFragment : onActivityCreated : Cursor null for vehicle code " + vehicleCode);
            Toast.makeText(getSherlockActivity().getApplicationContext(), "No data found for selected vehicle", Toast.LENGTH_SHORT).show();
        } else if(rawStatsCursor.getCount() == 0) {
            Log.w(LOGTAG, "RawStatsFragment : onActivityCreated : No rows for vehicle code " + vehicleCode);
            Toast.makeText(getSherlockActivity().getApplicationContext(), "No data found for selected vehicle", Toast.LENGTH_SHORT).show();
            rawStatsStatus.setVisibility(View.VISIBLE); // Show the Status Text
            rawStatsStatus.setText("No data found for selected vehicle");
        } else {
            Log.d(LOGTAG, "RawStatsFragment : onActivityCreated : " + rawStatsCursor.getCount() + " rows found for vehicle " + vehicleCode);
            rawStatsStatus.setVisibility(View.INVISIBLE); // Hide it, just in case it's visible
            rawStatsCursorAdapter = new RawStatsRowAdapter(getSherlockActivity().getApplicationContext(), rawStatsCursor);
            setListAdapter(rawStatsCursorAdapter);
        }
    }

    public void onListItemClick (ListView listView, View view, int position, long id) {
        TextView rowIdTextView = (TextView) view.findViewById(R.id.rowid);
        int rowId = Integer.parseInt(String.valueOf(rowIdTextView.getText()));
        Log.d(LOGTAG, "RawStatisticsFragment : onListItemClick : Position " + position + ", row ID " + rowId);

        Bundle statViewData = new Bundle();
        statViewData.putInt("rowId", rowId);

        SherlockFragment statEditorFragment = new StatEditorFragment();
        statEditorFragment.setArguments(statViewData);

        FragmentTransaction ft = getSherlockActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if(mDualPane) { // Use Pane 2
            ft.replace(R.id.pane2_fragment, statEditorFragment);
        } else {
            ft.replace(R.id.pane1_fragment, statEditorFragment);
        }
        ft.addToBackStack(null);
        ft.commit(); // Required for ABS
    }
}
