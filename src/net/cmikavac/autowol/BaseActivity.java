package net.cmikavac.autowol;

import net.cmikavac.autowol.data.DbProvider;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {
    protected DbProvider mDbProvider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openDb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDb();
    }

    private void openDb() {
        mDbProvider = new DbProvider(this);
        mDbProvider.open();
    }

    private void closeDb() {
        mDbProvider.close();
    }
}