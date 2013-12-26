package net.cmikavac.autowol;

import java.util.ArrayList;
import java.util.List;

import net.cmikavac.autowol.R;
import net.cmikavac.autowol.models.*;
import net.cmikavac.autowol.data.*;
import net.cmikavac.autowol.adapters.*;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	private List<Device> devices = new ArrayList<Device>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		devices = Preferences.getStoredDevices();
		populateListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void populateListView() {
		ArrayAdapter<Device> adapter = new DeviceListAdapter(this, devices);
		ListView list = (ListView) findViewById(R.id.deviceListView);
		list.setAdapter(adapter);		
	}
}
