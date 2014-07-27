package net.cmikavac.autowol.data;

public class DbConfiguration {
    // DB info
    public static final String DATABASE_NAME = "AutoWOLDB";
    public static final String DATABASE_TABLE = "device";
    public static final String DATABASE_TABLE_TMP = "tmp_device";
    public static final int DATABASE_VERSION = 2;

    // For logging
    protected static final String TAG = "DbProvider";
    
    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    // "device" table fields
    public static final String KEY_NAME = "name";
    public static final String KEY_MAC = "mac";
    public static final String KEY_HOST = "host";
    public static final String KEY_BROADCAST = "broadcast";
    public static final String KEY_PORT = "port";
    public static final String KEY_SSID = "ssid";
    public static final String KEY_QUIET_FROM = "quiet_from";
    public static final String KEY_QUIET_TO = "quiet_to";
    public static final String KEY_IDLE_TIME = "idle_time";
    public static final String KEY_LAST_DISCONNECTED = "last_disconnected";

    // "device" table fields (old)
    public static final String KEY_IP = "ip";

    // "device" table field numbers
    public static final int COL_NAME = 1;
    public static final int COL_MAC = 2;
    public static final int COL_HOST = 3;
    public static final int COL_BROADCAST = 4;
    public static final int COL_PORT = 5;
    public static final int COL_SSID = 6;
    public static final int COL_QUIET_FROM = 7;
    public static final int COL_QUIET_TO = 8;
    public static final int COL_IDLE_TIME = 9;
    public static final int COL_LAST_DISCONNECTED = 10;

    public static final String[] ALL_KEYS_V1 = new String[] {
        KEY_ROWID, KEY_NAME, KEY_MAC, KEY_IP, KEY_PORT, KEY_SSID, KEY_QUIET_FROM, KEY_QUIET_TO, KEY_IDLE_TIME, KEY_LAST_DISCONNECTED
    };

    public static final String[] ALL_KEYS_V2 = new String[] {
        KEY_ROWID, KEY_NAME, KEY_MAC, KEY_HOST, KEY_BROADCAST, KEY_PORT, KEY_SSID, KEY_QUIET_FROM, KEY_QUIET_TO, KEY_IDLE_TIME, KEY_LAST_DISCONNECTED
    };

    protected static final String DATABASE_CREATE_SQL_V1 = ""
        //+ "drop table if exists " + DATABASE_TABLE + "; "
        + "create table " + DATABASE_TABLE + " (" 
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
        + "); ";

    protected static final String DATABASE_CREATE_SQL_V2 = ""
        //+ "drop table if exists " + DATABASE_TABLE + "; "
        + "create table " + DATABASE_TABLE + " (" 
        + KEY_ROWID + " integer primary key autoincrement, "
        + KEY_NAME + " string not null, "
        + KEY_MAC + " string not null, "
        + KEY_HOST + " string null, "
        + KEY_BROADCAST + " string not null, "
        + KEY_PORT + " integer not null, "
        + KEY_SSID + " string null, "
        + KEY_QUIET_FROM + " integer null, "
        + KEY_QUIET_TO + " integer null, "
        + KEY_IDLE_TIME + " integer null, "
        + KEY_LAST_DISCONNECTED + " integer null"
        + "); ";

    // V1 - Adds default DB fields
    protected static final String DATABASE_PATCH_SQL_V1_1 = DATABASE_CREATE_SQL_V1;

    // V2 - Adds "HOST" field, and renames "IP" to "BROADCAST"
    protected static final String DATABASE_PATCH_SQL_V2_1 = "" 
        + "alter table " + DATABASE_TABLE + " rename to " + DATABASE_TABLE_TMP;

    protected static final String DATABASE_PATCH_SQL_V2_2 = "" 
        + DATABASE_CREATE_SQL_V2;

    protected static final String DATABASE_PATCH_SQL_V2_3 = "" 
        + "insert into " + DATABASE_TABLE + " ("
            + KEY_ROWID + ", "
            + KEY_NAME + ", "
            + KEY_MAC + ", "
            + KEY_BROADCAST + ", "
            + KEY_PORT + ", "
            + KEY_SSID + ", "
            + KEY_QUIET_FROM + ", "
            + KEY_QUIET_TO + ", "
            + KEY_IDLE_TIME + ", "
            + KEY_LAST_DISCONNECTED + ") "
        + "select "
            + KEY_ROWID + ", "
            + KEY_NAME + ", "
            + KEY_MAC + ", "
            + KEY_IP + ", "
            + KEY_PORT + ", "
            + KEY_SSID + ", "
            + KEY_QUIET_FROM + ", "
            + KEY_QUIET_TO + ", "
            + KEY_IDLE_TIME + ", "
            + KEY_LAST_DISCONNECTED + " "
        + "from " + DATABASE_TABLE_TMP;

    protected static final String DATABASE_PATCH_SQL_V2_4 = "" 
        + "drop table if exists " + DATABASE_TABLE_TMP;

    public static final String[] ALL_KEYS = ALL_KEYS_V2;
    protected static final String DATABASE_CREATE_SQL = DATABASE_CREATE_SQL_V2;
}