/**
 * The base Activity that starts the application
 */

package com.shankarlabs.carlog.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.shankarlabs.carlog.R;

public class CarLogActivity extends SherlockFragmentActivity {

    private static final String LOGTAG = "CarLog";
    private boolean mDualPane; // Flag to indicate dual pane setup
    private Context mContext;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPreferenceEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setSupportProgressBarIndeterminate(true);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPreferenceEditor = sharedPrefs.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Figure out if we have dual panes, and then initialize the second pane
        View pane2 = findViewById(R.id.pane2_fragment);
        mDualPane = pane2 != null &&  pane2.getVisibility() == View.VISIBLE;
        sharedPreferenceEditor.putBoolean("mDualPane", mDualPane);
        sharedPreferenceEditor.commit();

        if(!mDualPane) { // we have just one pane. Setup spinner in actionbar
            Log.d(LOGTAG, "CarLogActivity : onCreate : We have just one pane. Setting up Spinner.");
            setTitle("");

            // Get the Action Bar ready to hold the Spinner
            ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

            SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.spinner_main, android.R.layout.simple_spinner_dropdown_item);

            ActionBar.OnNavigationListener spinnerNavigationListener = new ActionBar.OnNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    // Handle the item selected
                    switch(itemPosition) { // Assuming the callback is a zero-based position system
                        case 0: // Fill Up
                            Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 0. Fill Up.");
                            SherlockFragment fillUpFragment = new FillUpFragment();
                            ft.replace(R.id.pane1_fragment, fillUpFragment);
                            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                            ft.commit();
                            return true;  // We handled the callback; We don't need anyone else to handle it
                        case 1: // Oil Changes
                            Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 1. Maintenance.");
                            SherlockFragment maintenanceFragment = new MaintenanceFragment();
                            ft.replace(R.id.pane1_fragment, maintenanceFragment);
                            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                            ft.commit();
                            return true;  // We handled the callback; We don't need anyone else to handle it
                        case 2: // Vehicle Types
                            Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 2. Vehicle Types.");
                            SherlockFragment vehicleTypesFragment = new VehiclesFragment();
                            ft.replace(R.id.pane1_fragment, vehicleTypesFragment);
                            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                            ft.commit();
                            return true;  // We handled the callback; We don't need anyone else to handle it
                        case 3: // Maintenance Types
                            Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 3. Maintenance Types.");
                            SherlockFragment maintenanceTypesFragment = new MaintenanceTypesFragment();
                            ft.replace(R.id.pane1_fragment, maintenanceTypesFragment);
                            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                            ft.commit();
                            return true;  // We handled the callback; We don't need anyone else to handle it
                        case 4: // Statistics
                            Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 3. Statistics.");
                            SherlockFragment statisticsFragment = new StatisticsFragment();
                            ft.replace(R.id.pane1_fragment, statisticsFragment);
                            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                            ft.commit();
                            return true;  // We handled the callback; We don't need anyone else to handle it
                        default: // Safe fallback
                            Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Default : " + itemPosition + ". Unknown option");
                            return false;  // We don't know what it is; So we pass it along.
                    }
                }
            };

            actionBar.setListNavigationCallbacks(mSpinnerAdapter, spinnerNavigationListener);
        } else { // We have two panes. Populate the second pane
            Log.d(LOGTAG, "CarLogActivity : onCreate : We have two panes. Showing FillupFragment.");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            SherlockFragment fillUpFragment = new FillUpFragment();
            ft.replace(R.id.pane2_fragment, fillUpFragment);
            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
            ft.commit();
        }

        // Initialize the DB Objects so the DB gets populated
        // VehicleDBHelper vehicleDBHelper = new VehicleDBHelper(mContext);
        // MaintTypeDBHelper maintTypeDBHelper = new MaintTypeDBHelper(mContext);

        // vehicleDBHelper = null;
        // maintTypeDBHelper = null;

        // Get the location of the device, but in a thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Get the locaction of the device and store it in Prefs
            }
        }, "locationThread").run();

        Log.d(LOGTAG, "CarLogActivity : onCreate : Main screen ready");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        // We handle only Settings in the Main menu; The rest of the options are added by the Fragments. We let them handle those.
        switch (itemId) {
            case R.id.settings:
                Log.d(LOGTAG, "CarLogActivity : onOptionsItemSelected : Settings coming right up");
                return super.onOptionsItemSelected(menuItem);
            default:
                return false;
        }
    }
}
