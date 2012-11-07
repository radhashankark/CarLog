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
            " NAME TEXT, " +
            " UNITSUSED INTEGER, " +
            " DESCRIPTION TEXT);";

    private final String INITIAL_DATA_NEW = "INSERT INTO " + DATABASE_NAME + " VALUES (, 0, 'New', 0, 'New Vehicle')";
    private final String INITIAL_DATA_DEFAULT = "INSERT INTO " + DATABASE_NAME + " VALUES (, 1, 'Default', 0, 'New Vehicle')";

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
        Log.d(LOGTAG, "VehicleDBHelper : onCreate : Created Database and inserted data");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Update this method when updating the DB
    }

    public Cursor getAllVehicleNames() {
        if(database == null)
            database = getWritableDatabase();

        Cursor cursor = database.rawQuery(SELECT_ALL_NAMES, null);

        return cursor;
    }

    public boolean saveVehicle(int code, String name, int units, String description) {
        if(database == null)
            database = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("CODE", code);
        values.put("NAME", name);
        values.put("UNITSUSED", units);
        values.put("DESCRIPTION", description);

        if(database.insert(DATABASE_NAME, null, values) != -1)
            return true;
        else
            return false;
    }

    public boolean updateVehicle(int code, String name, int units, String description) {
        if(database == null)
            database = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("CODE", code);
        values.put("NAME", name);
        values.put("UNITSUSED", units);
        values.put("DESCRIPTION", description);

        String whereClause = "code = ?";
        String[] whereArgs = {"" + code};

        if(database.update(DATABASE_NAME, values, whereClause, whereArgs) > 0)
            return true;
        else
            return false;
    }
}
