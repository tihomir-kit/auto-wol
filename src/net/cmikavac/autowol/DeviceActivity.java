package net.cmikavac.autowol;

import net.cmikavac.autowol.models.Device;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceActivity extends Activity {
	private Device mDevice = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);
		
		setDevice();
		setViewValues();
		
		Toast.makeText(DeviceActivity.this, mDevice.getName(), Toast.LENGTH_SHORT).show();
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
			case android.R.id.home:
				this.finish();
				break;
		}
		return true;
	}
	
	private void setDevice() {
		Intent intent = getIntent();
		Device device = (Device)intent.getSerializableExtra("deviceObject");
		
		if (device == null)
			mDevice = new Device();
		else
			mDevice = device;
	}	
	
	private void setViewValues() {
		TextView nameText = (TextView)findViewById(R.id.txt_name);
		nameText.setText(mDevice.getName());		
	}	
}
