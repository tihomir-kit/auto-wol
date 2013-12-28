package net.cmikavac.autowol;

import net.cmikavac.autowol.data.DbProvider;
import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
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
