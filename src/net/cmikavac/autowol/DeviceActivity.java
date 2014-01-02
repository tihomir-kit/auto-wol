package net.cmikavac.autowol;

import net.cmikavac.autowol.models.Device;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

public class DeviceActivity extends BaseActivity {
	private Device mDevice = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);

		setDevice();
		setViewValues();
		registerSwitchCallbacks();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		menu.findItem(R.id.layout_auto_wake).setVisible(false);

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

	private void registerSwitchCallbacks() {
		final CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				switch(buttonView.getId()) {
					case R.id.switch_auto_wake:
						toggleLinearLayoutVisibility(R.id.auto_wake_layout, isChecked);
						break;
					case R.id.switch_quiet_hours:
						toggleLinearLayoutVisibility(R.id.quiet_hours_layout, isChecked);
						break;
					case R.id.switch_idle_before_auto_wake:
						toggleLinearLayoutVisibility(R.id.idle_before_auto_wake_layout, isChecked);
						break;
				}
			}
		};

		Switch autoWakeSwitch = (Switch)findViewById(R.id.switch_auto_wake);
		autoWakeSwitch.setOnCheckedChangeListener(listener);
		
		Switch quietHoursSwitch = (Switch)findViewById(R.id.switch_quiet_hours);
		quietHoursSwitch.setOnCheckedChangeListener(listener);
		
		Switch idleSwitch = (Switch)findViewById(R.id.switch_idle_before_auto_wake);
		idleSwitch.setOnCheckedChangeListener(listener);
	}

	private void toggleLinearLayoutVisibility(int id, boolean isChecked) {
		LinearLayout linearLayout = (LinearLayout)findViewById(id);
		if (isChecked == true)
			linearLayout.setVisibility(LinearLayout.VISIBLE);
		else
			linearLayout.setVisibility(LinearLayout.GONE);
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