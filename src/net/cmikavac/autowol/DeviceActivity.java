package net.cmikavac.autowol;

import net.cmikavac.autowol.models.Device;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class DeviceActivity extends BaseActivity {
	private Device mDevice = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);

		setDevice();
		setViewValues();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		menu.findItem(R.id.action_new).setVisible(false);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
			case android.R.id.home:
				this.finish();
				break;
			case R.id.action_save:
				getViewValues();
				saveDeviceToDb();
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

	private void saveDeviceToDb() {
		if (mDevice.getId() == -1)
			mDbProvider.insertDevice(mDevice);
		else
			mDbProvider.updateDevice(mDevice);
	}

	private void setViewValues() {
		EditText nameText = (EditText)findViewById(R.id.edit_name);
		EditText ipText = (EditText)findViewById(R.id.edit_ip);
		EditText macText = (EditText)findViewById(R.id.edit_mac);
		
		nameText.setText(mDevice.getName());
		ipText.setText(mDevice.getIp());
		macText.setText(mDevice.getMac());
	}
	
	private void getViewValues() {
		EditText nameText = (EditText)findViewById(R.id.edit_name);
		EditText ipText = (EditText)findViewById(R.id.edit_ip);
		EditText macText = (EditText)findViewById(R.id.edit_mac);

		mDevice.setName(nameText.getText().toString());
		mDevice.setIp(ipText.getText().toString());
		mDevice.setMac(macText.getText().toString());
	}
}