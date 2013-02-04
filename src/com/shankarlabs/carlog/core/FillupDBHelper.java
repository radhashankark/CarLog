package com.shankarlabs.carlog.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FillupDBHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final String DATABASE_NAME = "Fillup";
    private static int DATABASE_VERSION = 1;
    private SQLiteDatabase database = null;

    private static final String LOGTAG = "CarLog";
    // private final String SELECT_DATA_QUERY = "SELECT FROM " + DATABASE_NAME + " WHERE ";
    private final String TABLE_CREATE_QUERY = "CREATE TABLE " + DATABASE_NAME +
            " (_id INTEGER primary key autoincrement," +
            " QUANTITY TEXT," +
            " DISTANCE TEXT, " +
            " PRICE TEXT, " +
            " DATE TEXT, " +
            " ISPARTIAL INTEGER, " +
            " VEHICLE INTEGER, " +
            " LATITUDE TEXT, " +
            " LONGITUDE TEXT, " +
            " RESETCALCULATIONS INTEGER, " +
            " IMAGELOCATION TEXT, " +
            " COMMENTS TEXT);";

    public FillupDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Add stuff to do when creating the DB. Called when the DB is created for the first time
        db.execSQL(TABLE_CREATE_QUERY);
        Log.d(LOGTAG, "FillupDBHelper : onCreate : Created Database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Update this method when updating the DB
    }

    public boolean saveFillup(float quantity, String distance, float price, String date, boolean isPartial,
                              int vehicleCode, String latitude, String longitude, String comments,
                              int resetCalculations, String imageLocation) {
        if(database == null)
            database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_id", (Integer) null);
        values.put("QUANTITY", "" + quantity);
        values.put("DISTANCE", distance);
        values.put("PRICE", "" + price);
        values.put("DATE", date);
        values.put("ISPARTIAL", isPartial ? 1 : 0);
        values.put("VEHICLE", vehicleCode);
        values.put("LATITUDE", latitude);
        values.put("LONGITUDE", longitude);
        values.put("RESETCALCULATIONS", resetCalculations);
        values.put("IMAGELOCATION", imageLocation);
        values.put("COMMENTS", comments);

        if(database.insert(DATABASE_NAME, null, values) != -1)
            return true;
        else
            return false;
    }

    public Cursor getStatsFor(int vehicleCode) {
        if(database == null)
            database = getWritableDatabase();

        String[] columns = {"_id", "Quantity", "Price", "Date", "isPartial"};
        String selection = "Vehicle = '" + vehicleCode + "'";
        return database.query(DATABASE_NAME, columns, selection, null, null, null, null);
    }

    public Cursor getRowData(int rowId) {
        if(database == null)
            database = getWritableDatabase();

        String selection = "_id = '" + rowId + "'";
        return database.query(DATABASE_NAME, null, selection, null, null, null, null);
    }

    public void dumpDbToLogs() {
        if(database == null)
            database = getReadableDatabase();

        Cursor cursor = database.query(DATABASE_NAME, null, null, null, null, null, null);
        if(cursor == null) {
            Log.e(LOGTAG, "FillupDBHelper : dumpToLogs : Cursor is null");
        } else if(cursor.getCount() == 0) {
            Log.w(LOGTAG, "FillupDBHelper : dumpToLogs : No Data found in DB");
        } else {
            cursor.moveToFirst();
            Log.d(LOGTAG, "FillupDBHelper : dumpToLogs : \t" +
                    cursor.getColumnName(0) + "\t" +
                    cursor.getColumnName(1) + "\t" +
                    cursor.getColumnName(2) + "\t" +
                    cursor.getColumnName(3) + "\t" +
                    cursor.getColumnName(4) + "\t" +
                    cursor.getColumnName(5) + "\t" +
                    cursor.getColumnName(6) + "\t" +
                    cursor.getColumnName(7) + "\t" +
                    cursor.getColumnName(8) + "\t" +
                    cursor.getColumnName(9) + "\t" +
                    cursor.getColumnName(10) + "\t" +
                    cursor.getColumnName(11));

            //Show first row
            Log.d(LOGTAG, "FillupDBHelper : dumpToLogs : \t" +
                    cursor.getInt(0) + "\t" +
                    cursor.getString(1) + "\t" +
                    cursor.getString(2) + "\t" +
                    cursor.getString(3) + "\t" +
                    cursor.getString(4) + "\t" +
                    cursor.getInt(5) + "\t" +
                    cursor.getInt(6) + "\t" +
                    cursor.getString(7) + "\t" +
                    cursor.getString(8) + "\t" +
                    cursor.getInt(9) + "\t" +
                    cursor.getString(10) + "\t" +
                    cursor.getString(11));

            while(cursor.moveToNext()) {
                Log.d(LOGTAG, "FillupDBHelper : dumpToLogs : \t" +
                        cursor.getInt(0) + "\t" +
                        cursor.getString(1) + "\t" +
                        cursor.getString(2) + "\t" +
                        cursor.getString(3) + "\t" +
                        cursor.getString(4) + "\t" +
                        cursor.getInt(5) + "\t" +
                        cursor.getInt(6) + "\t" +
                        cursor.getString(7) + "\t" +
                        cursor.getString(8) + "\t" +
                        cursor.getInt(9) + "\t" +
                        cursor.getString(10) + "\t" +
                        cursor.getString(11));
            }
        }
    }
}
