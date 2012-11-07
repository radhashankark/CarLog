package com.shankarlabs.carlog.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shankarlabs.carlog.R;
import com.shankarlabs.carlog.core.FillupDBHelper;
import com.shankarlabs.carlog.core.VehicleDBHelper;

public class FillUpFragment extends SherlockFragment {

    private static final String LOGTAG = "CarLog";
    private static Context mContext;
    private boolean mDualPane;
    private Spinner fillupFor;
    private EditText volumeEditText, priceEditText, distanceEditText, commentsEditText;
    private CheckBox saveLocation, partialFillup;
    private Button saveFillupButton;
    private VehicleDBHelper vehicleDBHelper;
    private FillupDBHelper fillupDBHelper;
    private SharedPreferences sharedPreferences;

    public FillUpFragment (Context context) {
        super();

        mContext = context;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View fragmentView = inflater.inflate(R.layout.fillup, container, false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get Pane2 status
        View pane2 = getSherlockActivity().findViewById(R.id.pane2_fragment);
        mDualPane = pane2 != null && pane2.getVisibility() == View.VISIBLE;

        vehicleDBHelper = new VehicleDBHelper(mContext);
        fillupDBHelper = new FillupDBHelper(mContext);

        fillupFor = (Spinner) getSherlockActivity().findViewById(R.id.fillupfor);
        volumeEditText = (EditText) getSherlockActivity().findViewById(R.id.volumeedittext);
        priceEditText = (EditText) getSherlockActivity().findViewById(R.id.priceedittext);
        distanceEditText = (EditText) getSherlockActivity().findViewById(R.id.distanceedittext);
        commentsEditText = (EditText) getSherlockActivity().findViewById(R.id.commentsedittext);
        saveLocation = (CheckBox) getSherlockActivity().findViewById(R.id.savelocationckbx);
        partialFillup = (CheckBox) getSherlockActivity().findViewById(R.id.partialfillupckbx);
        saveFillupButton = (Button) getSherlockActivity().findViewById(R.id.savefillupbtn);

        Cursor cursor = vehicleDBHelper.getAllVehicles();
        if(cursor == null) {
            Log.w(LOGTAG, "FillUpFragment : onActivityCreated : All Vehicle Names Cursor is null");
            Toast.makeText(mContext, "No vehicle names found", Toast.LENGTH_SHORT).show();
        } else if(cursor.getCount() == 0) {
            Log.w(LOGTAG, "FillUpFragment : onActivityCreated : Zero vehicles found");
            Toast.makeText(mContext, "No vehicles saved", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst(); // Move to first to start reading
            // cursor.moveToNext(); // Move to the next one to skip one default row
            Log.w(LOGTAG, "FillUpFragment : onActivityCreated : " + cursor.getCount() + " vehicles found");
            Toast.makeText(mContext, (cursor.getCount() - 1) + " vehicles found", Toast.LENGTH_SHORT).show();
        }

        String[] projection = new String[] {"NAME"};
        int[] mapTo = new int[] {android.R.id.text1};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(mContext,
                R.layout.cl_spinner_item,
                cursor,
                projection,
                mapTo,
                0);

        fillupFor.setAdapter(simpleCursorAdapter);

        fillupFor.setSelection(1);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // MenuInflater menuInflater = getSherlockActivity().getSupportMenuInflater();
        menu.clear(); // First clear out all the elements in the menu
        getSherlockActivity().onCreateOptionsMenu(menu); // Add the Activity's options menu
        menuInflater.inflate(R.menu.fillup, menu);

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
            case R.id.save:
                Log.d(LOGTAG, "FillUpFragment : onOptionsItemSelected : Saving all data");
                Toast.makeText(mContext, "Saving", Toast.LENGTH_SHORT).show();
                saveFillup();
                break;
            case R.id.camera:
                Log.d(LOGTAG, "FillUpFragment : onOptionsItemSelected : Starting to take a photo");
                Toast.makeText(mContext, "Starting to take a photo", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void saveFillup() {
        fillupDBHelper.saveFillup(
                Float.parseFloat(volumeEditText.getText().toString()),
                distanceEditText.getText().toString(),
                Float.parseFloat(priceEditText.getText().toString()),
                "date",
                partialFillup.isChecked(),
                Integer.parseInt(fillupFor.getSelectedItem().toString()),
                sharedPreferences.getString("latitude", "0.0000"),
                sharedPreferences.getString("longitude", "0.0000"),
                commentsEditText.getText().toString(),
                Integer.parseInt("Units for car"),
                0,
                null);

    }
}
