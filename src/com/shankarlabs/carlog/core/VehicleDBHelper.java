package com.shankarlabs.carlog.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VehicleDBHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final String DATABASE_NAME = "VEHICLES";
    private static int DATABASE_VERSION = 1;
    private SQLiteDatabase database = null;

    private static final String LOGTAG = "CarLog";
    private final String TABLE_CREATE = "CREATE TABLE " + DATABASE_NAME +
            " (_id INTEGER primary key autoincrement," +
            " CODE INTEGER," +
            " NAME TEXT," +
            " ISDEFAULT INTEGER," +
            " UNITSUSED INTEGER," +
            " DESCRIPTION TEXT);";

    private final String INITIAL_DATA_NEW = "INSERT INTO " + DATABASE_NAME + " VALUES (null, 0, 'New', 0, 0, 'New Vehicle')";
    private final String INITIAL_DATA_DEFAULT = "INSERT INTO " + DATABASE_NAME + " VALUES (null, 1, 'Default', 1, 0, 'Default Vehicle')";
    private final String INITIAL_DATA_TEST1 = "INSERT INTO " + DATABASE_NAME + " VALUES (null, 12, 'Test Vehicle 1', 0, 1, 'Test Vehicle 1, using Metric system')";
    private final String INITIAL_DATA_TEST2 = "INSERT INTO " + DATABASE_NAME + " VALUES (null, 321, 'Test Vehicle 2', 0, 0, 'Test Vehicle 2 just because.')";

    private final String SELECT_ALL_QUERY = " SELECT * FROM " + DATABASE_NAME;
    private final String SELECT_ALL_NAMES = " SELECT NAME FROM " + DATABASE_NAME;

    public VehicleDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Add stuff to do when creating the DB. Called when the DB is created for the first time
        db.execSQL(TABLE_CREATE);
        db.execSQL(INITIAL_DATA_NEW);
        db.execSQL(INITIAL_DATA_DEFAULT);
        db.execSQL(INITIAL_DATA_TEST1);
        db.execSQL(INITIAL_DATA_TEST2);
        Log.d(LOGTAG, "VehicleDBHelper : onCreate : Created Database and inserted data");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Update this method when updating the DB
    }

    public Cursor getAllVehicles() {
        if(database == null)
            database = getWritableDatabase();

        Cursor cursor = database.rawQuery(SELECT_ALL_QUERY, null);

        return cursor;
    }

    public boolean saveVehicle(int code, String name, int isDefault, int units, String description) {
        if(database == null)
            database = getWritableDatabase();

        if(isDefault == 1) // There's a new default vehicle. Reset old default vehicle(s)
            resetDefaultVehicle();

        ContentValues values = new ContentValues();

        values.put("_id", (Integer) null);
        values.put("CODE", code);
        values.put("NAME", name);
        values.put("ISDEFAULT", isDefault);
        values.put("UNITSUSED", units);
        values.put("DESCRIPTION", description);

        if(database.insert(DATABASE_NAME, null, values) != -1)
            return true;
        else
            return false;
    }

    public boolean updateVehicle(int code, String name, int isDefault, int units, String description) {
        if(database == null)
            database = getWritableDatabase();

        if(isDefault == 1) // There's a new default vehicle. Reset old default vehicle(s)
            resetDefaultVehicle();

        ContentValues values = new ContentValues();

        // values.put("CODE", code);
        values.put("NAME", name);
        values.put("ISDEFAULT", isDefault);
        values.put("UNITSUSED", units);
        values.put("DESCRIPTION", description);

        String whereClause = "code = ?";
        String[] whereArgs = {"" + code};

        if(database.update(DATABASE_NAME, values, whereClause, whereArgs) > 0)
            return true;
        else
            return false;
    }

    public boolean deleteVehicle(int code) {
        if(database == null)
            database = getWritableDatabase();

        String whereClause = "code = ?";
        String[] whereArgs = {"" + code};

        if(database.delete(DATABASE_NAME, whereClause, whereArgs) > 0)
            return true;
        else
            return false;
    }

    public boolean resetDefaultVehicle() {
        Log.w(LOGTAG, "VehicleDBHelper : resetDefaultVehicle : Removing all default vehicle(s)");

        if(database == null)
            database = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("ISDEFAULT", 0);

        if(database.update(DATABASE_NAME, values, null, null) > 0)
            return true;
        else
            return false;
    }

    public int getDefaultVehicleId() {
        if(database == null)
            database = getWritableDatabase();

        String query = "SELECT _id from " + DATABASE_NAME + " WHERE ISDEFAULT = 1";
        Cursor cursor = database.rawQuery(query, null);

        if(cursor == null) {
            Log.w(LOGTAG, "VehicleDBHelper : getDefaultVehicleId : Cursor is null");
            return -1;
        } else if(cursor.getCount() == 0) {
            Log.w(LOGTAG, "VehicleDBHelper : getDefaultVehicleId : Zero results");
            return 0;
        } else {
            cursor.moveToFirst();
            return cursor.getInt(0); // Index 0 is the _id that we're looking for
        }
    }

    public String[] getVehicleData(int id) {
        if(database == null)
            database = getWritableDatabase();

        // String query = "SELECT * from " + DATABASE_NAME + " WHERE _id = '" + id + "'";
        Cursor cursor = database.rawQuery(SELECT_ALL_QUERY, null);

        if(cursor == null) {
            Log.w(LOGTAG, "VehicleDBHelper : getVehicleData : Cursor is null");
            return null;
        } else if(cursor.getCount() == 0) {
            Log.w(LOGTAG, "VehicleDBHelper : getVehicleData : Zero results");
            return null;
        } else {
            Log.d(LOGTAG, "VehicleDBHelper : getVehicleData : Moving Cursor to position " + id);
            cursor.moveToPosition(id);
            String[] vehicleData = new String[6];

            vehicleData[0] = "" + cursor.getInt(0);
            vehicleData[1] = "" + cursor.getInt(1);
            vehicleData[2] = cursor.getString(2);
            vehicleData[3] = "" + cursor.getInt(3);
            vehicleData[4] = "" + cursor.getInt(4);
            vehicleData[5] = cursor.getString(5);

            return vehicleData;
        }
    }
}
