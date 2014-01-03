package net.cmikavac.autowol.data;

import java.util.List;

import net.cmikavac.autowol.models.Device;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbProvider extends DbConfiguration {
    //private final Context mContext;
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private DbMapper mDbMapper;

    public DbProvider(Context context) {
        //mContext = context;
        mDbHelper = new DbHelper(context);
        mDbMapper = new DbMapper();
    }

    // Open the database connection
    public DbProvider open() {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection
    public void close() {
        mDbHelper.close();
    }

    // Inserts a new Device record into DB
    public long insertDevice(Device device) {
        ContentValues values = setContentValues(device);
        return mDb.insert(DATABASE_TABLE, null, values);
    }

    // Gets a Device record from DB by Device Id
    public Device getDevice(long id) {
        String where = KEY_ROWID + "=" + id;
        Cursor cursor = mDb.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Device device = mDbMapper.mapDevice(cursor); 
        cursor.close();
        return device;
    }

    // Gets all Device records from DB
    public List<Device> getAllDevices() {
        String where = null;
        Cursor cursor =  mDb.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<Device> devices = mDbMapper.mapDevices(cursor);
        cursor.close();
        return devices;
    }

    public boolean updateDevice(Device device) {
        String where = KEY_ROWID + "=" + device.getId();
        ContentValues newValues = setContentValues(device);
        return mDb.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    public boolean deleteDevice(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return mDb.delete(DATABASE_TABLE, where, null) != 0;
    }

    private ContentValues setContentValues(Device device) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, device.getName());
        values.put(KEY_IP, device.getIp());
        values.put(KEY_MAC, device.getMac());
        return values;
    }

    /////////////////////////////////////////////////////////////////////
    // DB Helper class
    /////////////////////////////////////////////////////////////////////
    /**
     * Handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DbHelper extends SQLiteOpenHelper {
        DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(db);
        }
    }
}