/**
 * The base Activity that starts the application
 */

package com.shankarlabs.carlog.ui;

import android.content.Context;
import android.os.Bundle;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setSupportProgressBarIndeterminate(true);

        setContentView(R.layout.main);

        // Figure out if we have dual panes, and then initialize the second pane
        View pane2 = findViewById(R.id.pane2_fragment);
        mDualPane = pane2 != null &&  pane2.getVisibility() == View.VISIBLE;

        if(!mDualPane) { // we have just one pane. Setup spinner in actionbar
            Log.d(LOGTAG, "CarLogActivity : onCreate : We have just one pane. Setting up Spinner.");
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
                            Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 1. Oil Changes.");
                            SherlockFragment oilChangeFragment = new OilChangeFragment();
                            ft.replace(R.id.pane1_fragment, oilChangeFragment);
                            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            // ft.addToBackStack(null); // Dont commit because there's nothing to go back to
                            ft.commit();
                            return true;  // We handled the callback; We don't need anyone else to handle it
                        case 2: // Maintenance
                            Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 2. Maintenance.");
                            return true;  // We handled the callback; We don't need anyone else to handle it
                        case 3: // Manage Vehicles
                            Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option 3. Manage Vehicles.");
                            return true;  // We handled the callback; We don't need anyone else to handle it
                        default: // Safe fallback
                            Log.d(LOGTAG, "spinnerNavigationListener : onNavigationItemSelected : Option " + itemPosition + ". Unknown option");
                            return false;  // We don't know what it is; So we pass it along.
                    }
                }
            };

            actionBar.setListNavigationCallbacks(mSpinnerAdapter, spinnerNavigationListener);
        }

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

        // This is a good time to figure out if we have dual panes
        // TODO Figure out if mDualpane needs detection at a later point of time
        // View pane2 = findViewById(R.id.pane2_fragment);
        // mDualPane = pane2 != null &&  pane2.getVisibility() == View.VISIBLE;

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
