package net.cmikavac.autowol;

import java.io.IOException;
import java.io.InputStream;

import net.cmikavac.autowol.TimePickerFragment.OnTimePickedListener;
import net.cmikavac.autowol.models.Device;
import net.cmikavac.autowol.utils.TimeConverter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceActivity extends BaseActivity implements OnTimePickedListener {
    private Device mDevice = null;
    private FormItems mFormItems = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        initializeFormItems();
        setDevice();
        setFormValues();
        registerSwitchCallbacks();
        registerLinearLayoutButtonsCallbacks();
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
            case R.id.action_help:
                displayHelpDialog();
                break;
            case R.id.action_save:
                getFormValues();
                saveDeviceToDb();
                this.finish();
                break;
        }
        return true;
    }

    private void registerSwitchCallbacks() {
        final CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                switch (button.getId()) {
                    case R.id.switch_auto_wake:
                        toggleLinearLayoutVisibility(R.id.layout_auto_wake, isChecked);
                        break;
                    case R.id.switch_quiet_hours:
                        toggleLinearLayoutVisibility(R.id.layout_quiet_hours, isChecked);
                        break;
                    case R.id.switch_idle_time:
                        toggleLinearLayoutVisibility(R.id.layout_idle_time, isChecked);
                        break;
                }
            }
        };

        mFormItems.autoWakeSwitch.setOnCheckedChangeListener(listener);
        mFormItems.quietHoursSwitch.setOnCheckedChangeListener(listener);
        mFormItems.idleTimeSwitch.setOnCheckedChangeListener(listener);
    }

    private void registerLinearLayoutButtonsCallbacks() {
        final OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view.getId());
            }
        };

        mFormItems.quietHoursFromLayout.setOnClickListener(listener);
        mFormItems.quietHoursToLayout.setOnClickListener(listener);
    }

    private void toggleLinearLayoutVisibility(int layoutId, boolean isChecked) {
        LinearLayout linearLayout = (LinearLayout)findViewById(layoutId);
        if (isChecked == true) {
            linearLayout.setVisibility(LinearLayout.VISIBLE);
        }
        else {
            linearLayout.setVisibility(LinearLayout.GONE);
        }
    }

    public void showTimePickerDialog(int layoutId) {
        Integer hour = getQuietHoursHour(layoutId);
        Integer minute = getQuietHoursMinute(layoutId);

        Bundle bundle = createTimePickerBundle(layoutId, hour, minute);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(), Integer.toString(layoutId));
    }

    public Integer getQuietHoursHour(int layoutId) {
        Integer hour = layoutId  == R.id.layout_quiet_hours_from ? 0 : 7;

        switch (layoutId) {
            case R.id.layout_quiet_hours_from:
                if (mDevice.getQuietHoursFrom() != null)
                    hour = TimeConverter.getHourFromMilliseconds(mDevice.getQuietHoursFrom());
                break;
            case R.id.layout_quiet_hours_to:
                if (mDevice.getQuietHoursTo() != null)
                    hour = TimeConverter.getHourFromMilliseconds(mDevice.getQuietHoursTo());
                break;
        }

        return hour;
    }

    public Integer getQuietHoursMinute(int layoutId) {
        Integer minute = 0;

        switch (layoutId) {
            case R.id.layout_quiet_hours_from:
                if (mDevice.getQuietHoursFrom() != null)
                    minute = TimeConverter.getMinuteFromMilliseconds(mDevice.getQuietHoursFrom());
                break;
            case R.id.layout_quiet_hours_to:
                if (mDevice.getQuietHoursTo() != null)
                    minute = TimeConverter.getMinuteFromMilliseconds(mDevice.getQuietHoursTo());
                break;
        }

        return minute;
    }

    public Bundle createTimePickerBundle(int layoutId, int hour, int minute) {
        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", layoutId);
        bundle.putInt("hour", hour);
        bundle.putInt("minute", minute);
        return bundle;
    }

    @Override
    public void onTimePicked(int layoutId, int hour, int minute) {
        Long timeInMillis = TimeConverter.getTimeInMilliseconds(hour, minute);
        setQuietHoursValues(layoutId, timeInMillis);
    }

    private void setQuietHoursValues(int layoutId, Long timeInMillis) {
        switch (layoutId) {
            case R.id.layout_quiet_hours_from:
                mDevice.setQuietHoursFrom(timeInMillis);
                mFormItems.quietHoursFromText.setText(TimeConverter.getFormatedTime(timeInMillis, this));
                break;
            case R.id.layout_quiet_hours_to:
                mDevice.setQuietHoursTo(timeInMillis);
                mFormItems.quietHoursToText.setText(TimeConverter.getFormatedTime(timeInMillis, this));
                break;
        }
    }

    private void setDevice() {
        Intent intent = getIntent();
        Device device = (Device)intent.getSerializableExtra("deviceObject");
        
        if (device == null) {
            mDevice = new Device();
        }
        else {
            mDevice = device;
        }
    }

    private void saveDeviceToDb() {
        if (mDevice.getId() == -1) {
            mDbProvider.insertDevice(mDevice);
        }
        else {
            mDbProvider.updateDevice(mDevice);
        }
    }

    private void setFormValues() {
        mFormItems.nameEdit.setText(mDevice.getName());
        mFormItems.macEdit.setText(mDevice.getMac());

        if (mDevice.getIp() != null) {
            mFormItems.ipEdit.setText(mDevice.getIp());
        }

        if (mDevice.getPort() != null) {
            mFormItems.portEdit.setText(mDevice.getPort().toString());
        }

        if (mDevice.getSSID() != null) {
            mFormItems.autoWakeSwitch.setChecked(true);
            mFormItems.ssidEdit.setText(mDevice.getSSID());
        }
        else {
            mFormItems.autoWakeLayout.setVisibility(LinearLayout.GONE);
        }

        if (mDevice.getQuietHoursFrom() != null) {
            mFormItems.quietHoursFromText.setText(TimeConverter.getFormatedTime(mDevice.getQuietHoursFrom(), this));
            mFormItems.quietHoursToText.setText(TimeConverter.getFormatedTime(mDevice.getQuietHoursTo(), this));
            mFormItems.quietHoursSwitch.setChecked(true);
        }
        else {
            Long millisFrom = TimeConverter.getTimeInMilliseconds(getQuietHoursHour(R.id.layout_quiet_hours_from), 0);
            Long millisTo = TimeConverter.getTimeInMilliseconds(getQuietHoursHour(R.id.layout_quiet_hours_from), 0);
            mFormItems.quietHoursFromText.setText(TimeConverter.getFormatedTime(millisFrom, this));
            mFormItems.quietHoursToText.setText(TimeConverter.getFormatedTime(millisTo, this));
            mFormItems.quietHoursLayout.setVisibility(LinearLayout.GONE);
        }

        if (mDevice.getIdleTime() != null) {
            mFormItems.idleTimeEdit.setText(mDevice.getIdleTime().toString());
            mFormItems.idleTimeSwitch.setChecked(true);
        }
        else { 
            mFormItems.idleTimeLayout.setVisibility(LinearLayout.GONE);
        }
    }

    private void getFormValues() {
        mDevice.setName(mFormItems.nameEdit.getText().toString());
        mDevice.setMac(mFormItems.macEdit.getText().toString());
        mDevice.setIp(mFormItems.ipEdit.getText().toString());
        mDevice.setPort(Integer.parseInt(mFormItems.portEdit.getText().toString()));
        
        if (mFormItems.autoWakeSwitch.isChecked()) {
            mDevice.setSSID(mFormItems.ssidEdit.getText().toString());
    
            if (!mFormItems.quietHoursSwitch.isChecked()) {
                mDevice.setQuietHoursFrom(null);
                mDevice.setQuietHoursTo(null);
            }
            
            if (mFormItems.idleTimeSwitch.isChecked()) {
                String idleTime = mFormItems.idleTimeEdit.getText().toString(); 
                if (idleTime != null && !idleTime.isEmpty())
                    mDevice.setIdleTime(Integer.parseInt(idleTime));
            } else {
                mDevice.setIdleTime(null);
            }
        } else {
            mDevice.setSSID(null);
            mDevice.setQuietHoursFrom(null);
            mDevice.setQuietHoursTo(null);
            mDevice.setIdleTime(null);
        }
    }

    private void displayHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help")
            .setMessage(getHelpHtml())
            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }
        );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private CharSequence getHelpHtml() {
        InputStream inputStream;
        String html = null; 

        try {
            inputStream = getAssets().open("Help.html");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            html = new String(buffer);
            inputStream.close();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return Html.fromHtml(html);
    }

    private void initializeFormItems() {
        mFormItems = createFormItems();
    }
    
    private FormItems createFormItems() {
        FormItems formItems = new FormItems();
        
        // EditText
        formItems.nameEdit = (EditText)findViewById(R.id.edit_name);
        formItems.ipEdit = (EditText)findViewById(R.id.edit_ip);
        formItems.macEdit = (EditText)findViewById(R.id.edit_mac);
        formItems.portEdit = (EditText)findViewById(R.id.edit_port);
        formItems.ssidEdit = (EditText)findViewById(R.id.edit_ssid);
        formItems.idleTimeEdit = (EditText)findViewById(R.id.edit_idle_time);
        
        // TextView
        formItems.quietHoursFromText = (TextView)findViewById(R.id.text_quiet_hours_from);
        formItems.quietHoursToText = (TextView)findViewById(R.id.text_quiet_hours_to);
        
        // LinearLayout
        formItems.autoWakeLayout = (LinearLayout)findViewById(R.id.layout_auto_wake);
        formItems.quietHoursLayout = (LinearLayout)findViewById(R.id.layout_quiet_hours);
        formItems.quietHoursFromLayout = (LinearLayout)findViewById(R.id.layout_quiet_hours_from);
        formItems.quietHoursToLayout = (LinearLayout)findViewById(R.id.layout_quiet_hours_to);
        formItems.idleTimeLayout = (LinearLayout)findViewById(R.id.layout_idle_time);
        
        // Switch
        formItems.autoWakeSwitch = (Switch)findViewById(R.id.switch_auto_wake);
        formItems.quietHoursSwitch = (Switch)findViewById(R.id.switch_quiet_hours);
        formItems.idleTimeSwitch = (Switch)findViewById(R.id.switch_idle_time);

        return formItems;
    }

    private class FormItems {
        // EditText
        EditText nameEdit;
        EditText ipEdit;
        EditText macEdit;
        EditText portEdit;
        EditText ssidEdit;
        EditText idleTimeEdit;

        // TextView
        TextView quietHoursFromText;
        TextView quietHoursToText;

        // LinearLayout
        LinearLayout autoWakeLayout;
        LinearLayout quietHoursLayout;
        LinearLayout quietHoursFromLayout;
        LinearLayout quietHoursToLayout;
        LinearLayout idleTimeLayout;

        // Switch
        Switch autoWakeSwitch;
        Switch quietHoursSwitch;
        Switch idleTimeSwitch;
    }
}