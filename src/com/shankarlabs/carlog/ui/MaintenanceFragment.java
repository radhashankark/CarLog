package com.shankarlabs.carlog.ui;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shankarlabs.carlog.R;

public class MaintenanceFragment extends SherlockFragment {

    private static boolean mDualPane;
    private static final String LOGTAG = "CarLog";
    private static Context mContext;

    public MaintenanceFragment () {
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
        View fragmentView = inflater.inflate(R.layout.maintenance, container, false);
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
            getSherlockActivity().getActionBar().setSelectedNavigationItem(1);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // MenuInflater menuInflater = getSherlockActivity().getSupportMenuInflater();
        menu.clear(); // First clear out all the elements in the menu
        getSherlockActivity().onCreateOptionsMenu(menu); // Add the Activity's options menu
        menuInflater.inflate(R.menu.maintenance, menu);

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
            case R.id.savemaintenance:
                Log.d(LOGTAG, "MaintenanceFragment : onOptionsItemSelected : Saving all data");
                Toast.makeText(mContext, "Saving", Toast.LENGTH_SHORT).show();
                // saveMaintenance();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
