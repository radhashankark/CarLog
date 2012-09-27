/**
 * The base Activity that starts the application
 */

package com.shankarlabs.carlog.ui;

import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.shankarlabs.carlog.R;

public class CarLogActivity extends SherlockFragmentActivity {

    private static final String LOGTAG = "CarLog";
    private boolean mDualPane; // Flag to indicate dual pane setup

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setSupportProgressBarIndeterminate(true);

        setContentView(R.layout.main);

        Log.d(LOGTAG, "CarLogActivity : onCreate : Main screen ready");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.dashboard, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        // This is a good time to figure out if we have dual panes
        // TODO Figure out if mDualpane needs detection at a later point of time
        // View pane2 = findViewById(R.id.pane2_fragment);
        // mDualPane = pane2 != null &&  pane2.getVisibility() == View.VISIBLE;

        switch (itemId) {
            case R.id.addfillup:
                Log.d(LOGTAG, "CarLogActivity : onCreate : Adding new fillup");
                break;
            case R.id.save:
                Log.d(LOGTAG, "CarLogActivity : onCreate : Saving all data");
                break;
            case R.id.exit:
                Log.d(LOGTAG, "CarLogActivity : onCreate : Exit requested. Exiting");
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
