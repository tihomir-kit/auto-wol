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
	public static final String KEY_IP = "ip";
	public static final String KEY_MAC = "mac";

	// "device" table field numbers
	public static final int COL_NAME = 1;
	public static final int COL_IP = 2;
	public static final int COL_MAC = 3;

	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_NAME, KEY_IP, KEY_MAC};

	protected static final String DATABASE_CREATE_SQL = 
		"create table " + DATABASE_TABLE 
		+ " (" + KEY_ROWID + " integer primary key autoincrement, "
		+ KEY_NAME + " string not null, "
		+ KEY_IP + " string not null, "
		+ KEY_MAC + " string not null"
		+ ");";
}
