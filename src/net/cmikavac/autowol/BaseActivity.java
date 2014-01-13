package net.cmikavac.autowol;

import net.cmikavac.autowol.data.DbProvider;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {
    protected DbProvider mDbProvider = null;

    /**
     * Upon activity creation opens a DB connection.
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openDb();
    }

    /**
     * Upon activity destruction closes DB connection. 
     * @see android.support.v4.app.FragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDb();
    }

    /**
     * Instantiates new DbProvider and opens a DB connection.
     */
    private void openDb() {
        mDbProvider = new DbProvider(this);
        mDbProvider.open();
    }

    /**
     * Closes DB connection.
     */
    private void closeDb() {
        mDbProvider.close();
    }
}