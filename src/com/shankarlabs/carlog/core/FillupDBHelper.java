package com.shankarlabs.carlog.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FillupDBHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final String DATABASE_NAME = "Fillup";
    private static int DATABASE_VERSION = 1;
    private SQLiteDatabase database = null;

    private static final String LOGTAG = "CarLog";
    private final String TABLE_CREATE = "CREATE TABLE " + DATABASE_NAME +
            " (_id INTEGER primary key autoincrement," +
            " QUANTITY REAL," +
            " DISTANCE TEXT, " +
            " PRICE REAL, " +
            " DATE TEXT, " +
            " ISPARTIAL INTEGER, " +
            " VEHICLE INTEGER, " +
            " LATITUDE TEXT, " +
            " LONGITUDE TEXT, " +
            " COMMENTS TEXT, " +
            " UNITS INTEGER, " +
            " RESETCALCULATIONS INTEGER, " +
            " IMAGELOCATION TEXT);";

    public FillupDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Add stuff to do when creating the DB. Called when the DB is created for the first time
        db.execSQL(TABLE_CREATE);
        Log.d(LOGTAG, "FillupDBHelper : onCreate : Created Database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Update this method when updating the DB
    }

    public boolean saveFillup(float quantity, String distance, float price, String date, boolean isPartial,
                              int vehicleCode, String latitude, String longitude, String comments, int unitsUsed,
                              int resetCalculations, String imageLocation) {
        if(database == null)
            database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_id", (Integer) null);
        values.put("QUANTITY", quantity);
        values.put("DISTANCE", distance);
        values.put("PRICE", price);
        values.put("DATE", date);
        values.put("ISPARTIAL", isPartial ? 1 : 0);
        values.put("VEHICLE", vehicleCode);
        values.put("LATITUDE", latitude);
        values.put("LONGITUDE", longitude);
        values.put("COMMENTS", comments);
        values.put("UNITS", unitsUsed);
        values.put("RESETCALCULATIONS", resetCalculations);
        values.put("IMAGELOCATION", imageLocation);

        if(database.insert(DATABASE_NAME, null, values) != -1)
            return true;
        else
            return false;
    }

}
