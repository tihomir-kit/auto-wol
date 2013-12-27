package net.cmikavac.autowol;

import java.util.ArrayList;
import java.util.List;

import net.cmikavac.autowol.adapters.DeviceListAdapter;
import net.cmikavac.autowol.data.Preferences;
import net.cmikavac.autowol.models.Device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	private List<Device> mDevices = new ArrayList<Device>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mDevices = Preferences.getStoredDevices();
		populateListView();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
			case R.id.action_new:
				Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
				intent.putExtra("deviceObject", new Device());
				startActivity(intent);
				break;
		}
		return true;
	}

	private void populateListView() {
		ArrayAdapter<Device> adapter = new DeviceListAdapter(this, mDevices);
		ListView list = (ListView)findViewById(R.id.device_list_view);
		list.setAdapter(adapter);		
	}
}
