package com.shankarlabs.carlog.ui;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shankarlabs.carlog.R;
import com.shankarlabs.carlog.core.FillupDBHelper;
import com.shankarlabs.carlog.core.VehicleDBHelper;

public class StatEditorFragment extends SherlockFragment {
    private final String LOGTAG = "CarLog";
    private Context mContext;
    private int rowId;
    private FillupDBHelper fillupDBHelper;
    private VehicleDBHelper vehicleDBHelper;
    private TextView vehicleName;
    private EditText volume, price, distance, date, time, comments;
    private Switch resetLocation;
    private Button saveEditedData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle editorData = getArguments();
        rowId = editorData.getInt("rowId", -1);

        mContext = getSherlockActivity().getApplicationContext();

        fillupDBHelper = new FillupDBHelper(mContext);
        vehicleDBHelper = new VehicleDBHelper(mContext);

        Log.d(LOGTAG, "StatEditorFragment : onCreate : Editing data with row ID " + rowId);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);

        return layoutInflater.inflate(R.layout.stateditor, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vehicleName = (TextView) getSherlockActivity().findViewById(R.id.editfillupfor);
        volume = (EditText) getSherlockActivity().findViewById(R.id.editvolume);
        price = (EditText) getSherlockActivity().findViewById(R.id.editprice);
        distance = (EditText) getSherlockActivity().findViewById(R.id.editdistance);
        date = (EditText) getSherlockActivity().findViewById(R.id.editdate);
        time = (EditText) getSherlockActivity().findViewById(R.id.edittime);
        comments = (EditText) getSherlockActivity().findViewById(R.id.editcomments);
        resetLocation = (Switch) getSherlockActivity().findViewById(R.id.clearlocationswitch);
        saveEditedData = (Button) getSherlockActivity().findViewById(R.id.editstatsave);

        Cursor cursor = fillupDBHelper.getRowData(rowId);

        if(cursor == null) {
            Log.e(LOGTAG, "StatEditorFragment : onActivityCreated : Search failed for ID " + rowId);
        } else if(cursor.getCount() == 0) {
            Log.e(LOGTAG, "StatEditorFragment : onActivityCreated : No data found for ID " + rowId);
        } else {
            cursor.moveToFirst();
            int vehicleCode = cursor.getInt(6);
            String tVehicleName = vehicleDBHelper.getVehicleName(vehicleCode);
            vehicleName.setText(tVehicleName);

            volume.setText(cursor.getString(1));
            price.setText(cursor.getString(3));
            distance.setText(cursor.getString(2));
            String tempdate = cursor.getString(4);
            date.setText(tempdate.split(" ")[0]);
            time.setText(tempdate.split(" ")[1]);
            comments.setText(cursor.getString(9));
            if(cursor.getString(7).length() > 4 && cursor.getString(8).length() > 4) { // We have Lat/Lng data
                resetLocation.setChecked(true);
            } else {
                resetLocation.setChecked(false);
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.clear(); // First clear out all the elements in the menu
        getSherlockActivity().onCreateOptionsMenu(menu); // Add the Activity's options menu
        menuInflater.inflate(R.menu.stateditor, menu);

        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.stateditorsave :
                Log.d(LOGTAG, "StatEditorFragment : onOptionsItemSelected : Saving changes");
                break;
            case R.id.editstat :
                Log.d(LOGTAG, "StatEditorFragment : onOptionsItemSelected : Editing data");
                enableAllFields();
                break;
            case android.R.id.home :
                Log.d(LOGTAG, "StatEditorFragment : onOptionsItemSelected : Going Home");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.pane2_fragment, new StatisticsFragment());
                // ft.addToBackStack(null);
                ft.commit();
                break;
            default :
                Log.w(LOGTAG, "StatEditorFragment : onOptionsItemSelected : Unknown Option");
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void enableAllFields() {
        volume.setEnabled(true);
        price.setEnabled(true);
        distance.setEnabled(true);
        comments.setEnabled(true);
        resetLocation.setEnabled(true);
        date.setClickable(true);
        time.setClickable(true);
        saveEditedData.setVisibility(View.VISIBLE);
    }

    private void disableAllFields() {
        volume.setEnabled(false);
        price.setEnabled(false);
        distance.setEnabled(false);
        comments.setEnabled(false);
        resetLocation.setEnabled(false);
        date.setClickable(false);
        time.setClickable(false);
    }
}
