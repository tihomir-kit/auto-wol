package net.cmikavac.autowol;

import net.cmikavac.autowol.models.Device;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
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
			case R.id.action_new:
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
					case R.id.switch_idle_hours:
						toggleLinearLayoutVisibility(R.id.idle_hours_layout, isChecked);
						break;
				}
			}
		};

		Switch autoWakeSwitch = (Switch)findViewById(R.id.switch_auto_wake);
		autoWakeSwitch.setOnCheckedChangeListener(listener);
		
		Switch quietHoursSwitch = (Switch)findViewById(R.id.switch_quiet_hours);
		quietHoursSwitch.setOnCheckedChangeListener(listener);
		
		Switch idleSwitch = (Switch)findViewById(R.id.switch_idle_hours);
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
		ItemHolder itemHolder = createItemHolder();

		itemHolder.nameEdit.setText(mDevice.getName());
		itemHolder.ipEdit.setText(mDevice.getIp());
		itemHolder.macEdit.setText(mDevice.getMac());

		if (mDevice.getSSID() != null) {
			itemHolder.autoWakeSwitch.setChecked(true);
		}
		else {
			itemHolder.autoWakeLayout.setVisibility(LinearLayout.GONE);
		}

		if (mDevice.getQuietHoursStart() != null) {
			itemHolder.quietHoursSwitch.setChecked(true);
		}
		else {
			itemHolder.quietHoursLayout.setVisibility(LinearLayout.GONE);
		}

		if (mDevice.getIdleHours() != null) {
			itemHolder.idleHoursSwitch.setChecked(true);
		}
		else {
			itemHolder.idleHoursLayout.setVisibility(LinearLayout.GONE);
		}
	}

	private void getViewValues() {
		ItemHolder itemHolder = createItemHolder();

		mDevice.setName(itemHolder.nameEdit.getText().toString());
		mDevice.setIp(itemHolder.ipEdit.getText().toString());
		mDevice.setMac(itemHolder.macEdit.getText().toString());
	}

	private ItemHolder createItemHolder() {
		ItemHolder itemHolder = new ItemHolder();
		itemHolder.nameEdit = (EditText)findViewById(R.id.edit_name);
		itemHolder.ipEdit = (EditText)findViewById(R.id.edit_ip);
		itemHolder.macEdit = (EditText)findViewById(R.id.edit_mac);

		itemHolder.autoWakeLayout = (LinearLayout)findViewById(R.id.auto_wake_layout);
		itemHolder.quietHoursLayout = (LinearLayout)findViewById(R.id.quiet_hours_layout);
		itemHolder.idleHoursLayout = (LinearLayout)findViewById(R.id.idle_hours_layout);

		itemHolder.autoWakeSwitch = (Switch)findViewById(R.id.switch_auto_wake);
		itemHolder.quietHoursSwitch = (Switch)findViewById(R.id.switch_quiet_hours);
		itemHolder.idleHoursSwitch = (Switch)findViewById(R.id.switch_idle_hours);
		return itemHolder;
	}

	private class ItemHolder {
		EditText nameEdit;
		EditText ipEdit;
		EditText macEdit;

		LinearLayout autoWakeLayout;
		LinearLayout quietHoursLayout;
		LinearLayout idleHoursLayout;

		Switch autoWakeSwitch;
		Switch quietHoursSwitch;
		Switch idleHoursSwitch;
	}
}