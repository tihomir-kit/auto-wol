package net.cmikavac.autowol.data;

import java.util.List;

import net.cmikavac.autowol.models.DeviceModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbProvider extends DbConfiguration {
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private DbMapper mDbMapper;

    /**
     * Constructor.
     * @param context       Context entity.
     */
    public DbProvider(Context context) {
        mDbHelper = new DbHelper(context);
        mDbMapper = new DbMapper();
    }


    /**
     * Opens the database connection.
     * @return      DbProvider entity.
     */
    public DbProvider open() {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * Maps content values from DeviceModel entity and inserts a new device record into DB.
     * @param device        DeviceModel entity to insert.
     * @return              Row Id of the newly inserted record.
     */
    public long insertDevice(DeviceModel device) {
        ContentValues values = setContentValues(device);
        return mDb.insert(DATABASE_TABLE, null, values);
    }

    /**
     * Gets a device record from DB and maps it to DeviceModel entity.
     * @param id        Device record Id of the record to fetch.
     * @return          DeviceModel entity.
     */
    public DeviceModel getDevice(long id) {
        String where = KEY_ROWID + "=" + id;
        Cursor cursor = mDb.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        DeviceModel device = mDbMapper.mapDevice(cursor); 
        cursor.close();
        return device;
    }

    /**
     * Gets all device records from DB and maps them to a list of DeviceModel entities.
     * @return      A list of DeviceModel entities.
     */
    public List<DeviceModel> getAllDevices() {
        Cursor cursor =  mDb.query(true, DATABASE_TABLE, ALL_KEYS, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<DeviceModel> devices = mDbMapper.mapDevices(cursor);
        cursor.close();
        return devices;
    }

    /**
     * Gets device records from DB by SSID and maps them to a list of DeviceModel entities.
     * @param ssid      Device SSID for filtering.
     * @return          A list of DeviceModel entities.
     */
    public List<DeviceModel> getDevicesBySSID(String ssid) {
        String where = KEY_SSID + "='" + ssid + "'";
        Cursor cursor =  mDb.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<DeviceModel> devices = mDbMapper.mapDevices(cursor);
        cursor.close();
        return devices;
    }

    /**
     * Updates a device record in DB.
     * @param device        DeviceModel entitiy to update.
     * @return              Device updated?
     */
    public boolean updateDevice(DeviceModel device) {
        String where = KEY_ROWID + "=" + device.getId();
        ContentValues newValues = setContentValues(device);
        return mDb.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    /**
     * Updates device records last_disconnected fields filtered by SSID.
     * @param ssid                      Device SSID for filtering.
     * @param currentTimeInMillis       Current time in milliseconds (value to be updated).
     * @return                          Number of rows updated.
     */
    public int updateDevicesLastDisconnected(String ssid, Long currentTimeInMillis) {
        String where = KEY_SSID + "='" + ssid + "'";
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_LAST_DISCONNECTED, currentTimeInMillis);
        return mDb.update(DATABASE_TABLE, newValues, where, null);
    }

    /**
     * Removes device record from DB.
     * @param rowId     Id of the record to remove.
     * @return          Record removed?
     */
    public boolean deleteDevice(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return mDb.delete(DATABASE_TABLE, where, null) != 0;
    }

    /**
     * Sets content values for DeviceModel entity.
     * @param device        DeviceModel entity to map to ContentValues.
     * @return              ConentValues entity.
     */
    private ContentValues setContentValues(DeviceModel device) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, device.getName());
        values.put(KEY_MAC, device.getMac());
        values.put(KEY_HOST, device.getHost());
        values.put(KEY_BROADCAST, device.getBroadcast());
        values.put(KEY_PORT, device.getPort());
        values.put(KEY_SSID, device.getSSID());
        values.put(KEY_QUIET_FROM, device.getQuietHoursFrom());
        values.put(KEY_QUIET_TO, device.getQuietHoursTo());
        values.put(KEY_IDLE_TIME, device.getIdleTime());
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

        /**
         * Array of patches to be applied on DB upgrade.
         */
        private static final Patch[] PATCHES = new Patch[] {
            new Patch() { public void apply(SQLiteDatabase db) { 
                Log.d("DB UPDATE", "Applying DB v1.1 patch"); 
                db.execSQL(DATABASE_PATCH_SQL_V1_1); 
            }},
            new Patch() { public void apply(SQLiteDatabase db) {
                Log.d("DB UPDATE", "Beginning transaction");
                db.beginTransaction();

                Log.d("DB UPDATE", "Applying DB v2.1 patch");
                db.execSQL(DATABASE_PATCH_SQL_V2_1);

                Log.d("DB UPDATE", "Applying DB v2.2 patch"); 
                db.execSQL(DATABASE_PATCH_SQL_V2_2);

                Log.d("DB UPDATE", "Applying DB v2.3 patch"); 
                db.execSQL(DATABASE_PATCH_SQL_V2_3);

                Log.d("DB UPDATE", "Applying DB v2.4 patch"); 
                db.execSQL(DATABASE_PATCH_SQL_V2_4);

                Log.d("DB UPDATE", "Transaction successful");
                db.setTransactionSuccessful();
                Log.d("DB UPDATE", "Ending transaction");
                db.endTransaction();
            }}
        };

        /**
         * Creates an empty SQL table for the application.
         * @param db        SQLiteDatabase entity.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_SQL);
        }

        /**
         * On DB version changed, drops existing table and creates a new one.
         * @param db                SQLiteDatabase entity.
         * @param oldVersion        Old DB version number.
         * @param newVersion        New DB version number.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "Upgrading application's database from version " + oldVersion + " to " + newVersion);

            for (int i = oldVersion; i < newVersion; i++) {
                PATCHES[i].apply(db);
            }
        }

        /**
         * @see http://www.greenmoonsoftware.com/2012/02/sqlite-schema-migration-in-android/
         */
        private static class Patch {
            public void apply(SQLiteDatabase db) {}
            //public void revert(SQLiteDatabase db) {} // not needed ATM
        }
    }
}