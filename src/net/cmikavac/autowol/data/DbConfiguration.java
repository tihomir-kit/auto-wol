package net.cmikavac.autowol.data;

public class DbConfiguration {
    // DB info
    public static final String DATABASE_NAME = "AutoWOLDB";
    public static final String DATABASE_TABLE = "device";
    public static final int DATABASE_VERSION = 1;

    // For logging
    protected static final String TAG = "DbProvider";
    
    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    // "device" table fields
    public static final String KEY_NAME = "name";
    public static final String KEY_MAC = "mac";
    public static final String KEY_IP = "ip";
    public static final String KEY_PORT = "port";
    public static final String KEY_SSID = "ssid";
    public static final String KEY_QUIET_FROM = "quiet_from";
    public static final String KEY_QUIET_TO = "quiet_to";
    public static final String KEY_IDLE_TIME = "idle_time";
    public static final String KEY_LAST_DISCONNECTED = "last_disconnected";

    // "device" table field numbers
    public static final int COL_NAME = 1;
    public static final int COL_MAC = 2;
    public static final int COL_IP = 3;
    public static final int COL_PORT = 4;
    public static final int COL_SSID = 5;
    public static final int COL_QUIET_FROM = 6;
    public static final int COL_QUIET_TO = 7;
    public static final int COL_IDLE_TIME = 8;
    public static final int COL_LAST_DISCONNECTED = 9;

    public static final String[] ALL_KEYS = new String[] {
        KEY_ROWID, KEY_NAME, KEY_MAC, KEY_IP, KEY_PORT, KEY_SSID, KEY_QUIET_FROM, KEY_QUIET_TO, KEY_IDLE_TIME, KEY_LAST_DISCONNECTED
    };

    protected static final String DATABASE_CREATE_SQL = 
        "create table " + DATABASE_TABLE + " (" 
        + KEY_ROWID + " integer primary key autoincrement, "
        + KEY_NAME + " string not null, "
        + KEY_MAC + " string not null, "
        + KEY_IP + " string not null, "
        + KEY_PORT + " integer not null, "
        + KEY_SSID + " string null, "
        + KEY_QUIET_FROM + " integer null, "
        + KEY_QUIET_TO + " integer null, "
        + KEY_IDLE_TIME + " integer null, "
        + KEY_LAST_DISCONNECTED + " integer null"
        + ");";
}