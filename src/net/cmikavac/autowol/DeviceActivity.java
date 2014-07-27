package net.cmikavac.autowol;

import java.io.IOException;
import java.io.InputStream;

import net.cmikavac.autowol.models.DeviceModel;
import net.cmikavac.autowol.partials.TimePickerFragment;
import net.cmikavac.autowol.partials.TimePickerFragment.OnTimePickedListener;
import net.cmikavac.autowol.utils.CustomTextWatcher;
import net.cmikavac.autowol.utils.NetworkingUtil;
import net.cmikavac.autowol.utils.TimeUtil;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceActivity extends BaseActivity implements OnTimePickedListener {
    private DeviceModel mDevice = null;
    private FormItems mFormItems = null;

    /** 
     * Upon activity creation initializes form items, set mDevice to existing
     * or new device sets form value, and registers callbacks. 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        initializeFormItems();
        ensureDeviceExists();
        setFormValues();
        registerSwitchCallbacks();
        registerDiscoverSSIDCallback();
        registerLinearLayoutButtonsCallbacks();
        registerAfterTextChangedCallbacks();
        registerAfterMacTextChangedCallback();
    }

    /**
     * Upon options menu creation inflates the menu and sets
     * button functionalities and visibility. 
     * @param menu      Menu entity to inflate.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        menu.findItem(R.id.action_new).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);

        return true;
    }

    /**
     * Routes to appropriate action upon clicking on an item from the actionBar menu.
     * @param item      Clicked item.
     */
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
                if (validateFormValues()) {
                    getFormValues();
                    saveDeviceToDb();
                    this.finish();
                }
                break;
        }
        return true;
    }

    /**
     * Registers toggleLinearLayoutVisibility() method to auto-wake, quiet
     * hours and idle time switch onCheckChanged() listener callbacks.
     */
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

    /**
     * Registers discoverSSID() method to discover SSID button onClick() listener callback.
     */
    private void registerDiscoverSSIDCallback() {
        mFormItems.discoverSSIDButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverSSID();
            }
        });
    }

    /**
     * Registers showTimePickerDialog() method to quiet hours
     * layout onClick() listener callbacks.
     */
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

    /**
     * Registers new CustomTextWatcher() to all EditText fields listener callbacks. Used
     * to remove validation error when the user starts typing into an EditText field.
     */
    private void registerAfterTextChangedCallbacks() {
        mFormItems.nameEdit.addTextChangedListener(new CustomTextWatcher(mFormItems.nameEdit));
        mFormItems.macEdit.addTextChangedListener(new CustomTextWatcher(mFormItems.macEdit));
        mFormItems.broadcastEdit.addTextChangedListener(new CustomTextWatcher(mFormItems.broadcastEdit));
        mFormItems.portEdit.addTextChangedListener(new CustomTextWatcher(mFormItems.portEdit));
        mFormItems.ssidEdit.addTextChangedListener(new CustomTextWatcher(mFormItems.ssidEdit));
        mFormItems.idleTimeEdit.addTextChangedListener(new CustomTextWatcher(mFormItems.idleTimeEdit));
    }

    /**
     * Registers TextWatcher for MAC EditText field. Automatically adds colons,  
     * switches the MAC to upper case and handles the cursor position.
     */
    private void registerAfterMacTextChangedCallback() {
        mFormItems.macEdit.addTextChangedListener(new TextWatcher() {
            String mPreviousMac = null;

            /* (non-Javadoc)
             * Does nothing.
             * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
             */
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            /* (non-Javadoc)
             * Does nothing.
             * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
             */
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            /* (non-Javadoc)
             * Formats the MAC address and handles the cursor position.
             * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
             */
            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredMac = mFormItems.macEdit.getText().toString().toUpperCase();
                String cleanMac = clearNonMacCharacters(enteredMac);
                String formattedMac = formatMacAddress(cleanMac);

                int selectionStart = mFormItems.macEdit.getSelectionStart();
                formattedMac = handleColonDeletion(enteredMac, formattedMac, selectionStart);
                int lengthDiff = formattedMac.length() - enteredMac.length();

                setMacEdit(cleanMac, formattedMac, selectionStart, lengthDiff);
            }

            /**
             * Strips all characters from a string except A-F and 0-9.
             * @param mac       User input string.
             * @return          String containing MAC-allowed characters.
             */
            private String clearNonMacCharacters(String mac) {
                return mac.toString().replaceAll("[^A-Fa-f0-9]", "");
            }

            /**
             * Adds a colon character to an unformatted MAC address after
             * every second character (strips full MAC trailing colon) 
             * @param cleanMac      Unformatted MAC address.
             * @return              Properly formatted MAC address.
             */
            private String formatMacAddress(String cleanMac) {
                int grouppedCharacters = 0;
                String formattedMac = "";

                for (int i = 0; i < cleanMac.length(); ++i) {
                    formattedMac += cleanMac.charAt(i);
                    ++grouppedCharacters;

                    if (grouppedCharacters == 2) {
                        formattedMac += ":";
                        grouppedCharacters = 0;
                    }
                }

                // Removes trailing colon for complete MAC address
                if (cleanMac.length() == 12)
                    formattedMac = formattedMac.substring(0, formattedMac.length() - 1);

                return formattedMac;
            }

            /**
             * Upon users colon deletion, deletes MAC character preceding deleted colon as well.
             * @param enteredMac            User input MAC.
             * @param formattedMac          Formatted MAC address.
             * @param selectionStart        MAC EditText field cursor position.
             * @return                      Formatted MAC address.
             */
            private String handleColonDeletion(String enteredMac, String formattedMac, int selectionStart) {
                if (mPreviousMac != null && mPreviousMac.length() > 1) {
                    int previousColonCount = colonCount(mPreviousMac);
                    int currentColonCount = colonCount(enteredMac);

                    if (currentColonCount < previousColonCount) {
                        formattedMac = formattedMac.substring(0, selectionStart - 1) + formattedMac.substring(selectionStart);
                        String cleanMac = clearNonMacCharacters(formattedMac);
                        formattedMac = formatMacAddress(cleanMac);
                    }
                }
                return formattedMac;
            }

            /**
             * Gets MAC address current colon count.
             * @param formattedMac      Formatted MAC address.
             * @return                  Current number of colons in MAC address.
             */
            private int colonCount(String formattedMac) {
                return formattedMac.replaceAll("[^:]", "").length();
            }

            /**
             * Removes TextChange listener, sets MAC EditText field value, 
             * sets new cursor position and re-initiates the listener.
             * @param cleanMac          Clean MAC address.
             * @param formattedMac      Formatted MAC address.
             * @param selectionStart    MAC EditText field cursor position.
             * @param lengthDiff        Formatted/Entered MAC number of characters difference.
             */
            private void setMacEdit(String cleanMac, String formattedMac, int selectionStart, int lengthDiff) {
                mFormItems.macEdit.removeTextChangedListener(this);
                if (cleanMac.length() <= 12) {
                    mFormItems.macEdit.setText(formattedMac);
                    mFormItems.macEdit.setSelection(selectionStart + lengthDiff);
                    mPreviousMac = formattedMac;
                } else {
                    mFormItems.macEdit.setText(mPreviousMac);
                    mFormItems.macEdit.setSelection(mPreviousMac.length());
                }
                mFormItems.macEdit.addTextChangedListener(this);
            }
        });
    }
    
    /**
     * Toggles LinearLayout visibility on and off based on isChecked param. Layouts with 
     * their corresponding switches checked are shown, and unchecked ones are hidden.
     * @param layoutId      Id of the layout for which to toggle visibility.
     * @param isChecked     Is layouts switch checked? 
     */
    private void toggleLinearLayoutVisibility(int layoutId, boolean isChecked) {
        LinearLayout linearLayout = (LinearLayout)findViewById(layoutId);
        if (isChecked == true) {
            linearLayout.setVisibility(LinearLayout.VISIBLE);
        } else {
            linearLayout.setVisibility(LinearLayout.GONE);
        }
    }

    /**
     * Discovers current SSID and prompts the user to potentially use it with Auto-Wake.
     */
    private void discoverSSID() {
        String ssid = NetworkingUtil.getCurrentSSID(this);

        AlertDialog.Builder builder = ssid == null ? createSSIDEmptyDialog() : createSSIDExistsDialog(ssid);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Creates a notification dialog for null SSID.
     * @return      AlertDialog Builder entity.
     */
    private Builder createSSIDEmptyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discover SSID")
            .setMessage("Could not obtain current SSID. Please check your Wi-Fi connection.")
            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }
        );
        return builder;
    }

    /**
     * Creates a dialog prompt to use current SSID with Auto-Wake. 
     * @param ssid      Current Wi-Fi SSID.
     * @return          AlertDialog Builder entity.
     */
    private Builder createSSIDExistsDialog(final String ssid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discover SSID")
            .setMessage("You are currently connected to \"" + ssid + "\". Would you like to use that SSID with Auto-Wake?")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mFormItems.ssidEdit.setText(ssid);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }
        );
        return builder;
    }

    /**
     * Gets initial hour and minute values to be set on TimePicker by default, bundles
     * that data together with layoutId and passes it to a new TimePicker fragment.
     * Pops-up a TimePicker fragment.
     * @param layoutId      QuietHoursFrom or QuietHoursTo layout Id.
     */
    public void showTimePickerDialog(int layoutId) {
        Integer hour = getQuietHoursHour(layoutId);
        Integer minute = getQuietHoursMinute(layoutId);

        Bundle bundle = createTimePickerBundle(layoutId, hour, minute);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(), Integer.toString(layoutId));
    }

    /**
     * Gets quiet hour based on "from" and "to" layout Ids. Gets the DB value
     * or the default value (0 or 7) if the DB value is not already set.
     * @param layoutId      QuietHoursFrom or QuietHoursTo layout Id.
     * @return              DB quiet hours value or the default value.
     */
    public Integer getQuietHoursHour(int layoutId) {
        Integer hour = layoutId  == R.id.layout_quiet_hours_from ? 0 : 7;

        switch (layoutId) {
            case R.id.layout_quiet_hours_from:
                if (mDevice.getQuietHoursFrom() != null)
                    hour = TimeUtil.getHourFromMilliseconds(mDevice.getQuietHoursFrom());
                break;
            case R.id.layout_quiet_hours_to:
                if (mDevice.getQuietHoursTo() != null)
                    hour = TimeUtil.getHourFromMilliseconds(mDevice.getQuietHoursTo());
                break;
        }

        return hour;
    }

    /**
     * Gets quiet minute based on "from" and "to" layout Ids. Gets the DB value
     * or the default value (0) if the DB value is not already set.
     * @param layoutId      QuietHoursFrom or QuietHoursTo layout Id.
     * @return              DB quiet minutes value or the default value.
     */
    public Integer getQuietHoursMinute(int layoutId) {
        Integer minute = 0;

        switch (layoutId) {
            case R.id.layout_quiet_hours_from:
                if (mDevice.getQuietHoursFrom() != null)
                    minute = TimeUtil.getMinuteFromMilliseconds(mDevice.getQuietHoursFrom());
                break;
            case R.id.layout_quiet_hours_to:
                if (mDevice.getQuietHoursTo() != null)
                    minute = TimeUtil.getMinuteFromMilliseconds(mDevice.getQuietHoursTo());
                break;
        }

        return minute;
    }

    /**
     * Creates a data bundle for TimePicker.
     * @param layoutId      QuietHoursFrom or QuietHoursTo layout Id.
     * @param hour          Initial hour value.
     * @param minute        Initial minutes value.
     * @return              Bundle entity.
     */
    public Bundle createTimePickerBundle(int layoutId, int hour, int minute) {
        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", layoutId);
        bundle.putInt("hour", hour);
        bundle.putInt("minute", minute);
        return bundle;
    }

    /**
     * On time picked event, converts hour and minutes values to milliseconds
     * milliseconds and sets a new value for the layout in the activity.
     * @see net.cmikavac.autowol.partials.TimePickerFragment.OnTimePickedListener#onTimePicked(int, int, int)
     * @param layoutId      QuietHoursFrom or QuietHoursTo layout Id.
     * @param hour          Hour value.
     * @param minute        Minutes value.
     */
    @Override
    public void onTimePicked(int layoutId, int hour, int minute) {
        Long timeInMillis = TimeUtil.getTimeInMilliseconds(hour, minute);
        setQuietHoursValues(layoutId, timeInMillis);
    }

    /**
     * Sets Device QuietHours property value and sets the formatted
     * time value in QuietHours layout in the activity.  
     * @param layoutId          QuietHoursFrom or QuietHoursTo layout Id.
     * @param timeInMillis      Time in milliseconds.
     */
    private void setQuietHoursValues(int layoutId, Long timeInMillis) {
        switch (layoutId) {
            case R.id.layout_quiet_hours_from:
                mDevice.setQuietHoursFrom(timeInMillis);
                mFormItems.quietHoursFromText.setText(TimeUtil.getFormatedTime(timeInMillis, this));
                break;
            case R.id.layout_quiet_hours_to:
                mDevice.setQuietHoursTo(timeInMillis);
                mFormItems.quietHoursToText.setText(TimeUtil.getFormatedTime(timeInMillis, this));
                break;
        }
    }

    
    /**
     * Sets the mDevice entity. If the the serializable deviceObject
     * is null, creates a new mDevice entity.
     */
    private void ensureDeviceExists() {
        Intent intent = getIntent();
        DeviceModel device = (DeviceModel)intent.getSerializableExtra("deviceObject");
        
        if (device == null) {
            mDevice = new DeviceModel();
        } else {
            mDevice = device;
        }
    }

    /**
     * Saves mDevice object values to DB.
     */
    private void saveDeviceToDb() {
        if (mDevice.getId() == -1) {
            mDbProvider.insertDevice(mDevice);
        } else {
            mDbProvider.updateDevice(mDevice);
        }
    }

    /**
     * Reads mDevice entity property values and sets them as activity form 
     * values. Also ensures that form switches are checked or unchecked 
     * depending on whether their nested values are set or not.
     */
    private void setFormValues() {
        mFormItems.nameEdit.setText(mDevice.getName());
        mFormItems.macEdit.setText(mDevice.getMac());

        if (mDevice.getBroadcast() != null) {
            mFormItems.broadcastEdit.setText(mDevice.getBroadcast());
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
            mFormItems.quietHoursFromText.setText(TimeUtil.getFormatedTime(mDevice.getQuietHoursFrom(), this));
            mFormItems.quietHoursToText.setText(TimeUtil.getFormatedTime(mDevice.getQuietHoursTo(), this));
            mFormItems.quietHoursSwitch.setChecked(true);
        }
        else {
            Long millisFrom = TimeUtil.getTimeInMilliseconds(getQuietHoursHour(R.id.layout_quiet_hours_from), 0);
            Long millisTo = TimeUtil.getTimeInMilliseconds(getQuietHoursHour(R.id.layout_quiet_hours_to), 0);
            mDevice.setQuietHoursFrom(millisFrom);
            mDevice.setQuietHoursTo(millisTo);
            mFormItems.quietHoursFromText.setText(TimeUtil.getFormatedTime(millisFrom, this));
            mFormItems.quietHoursToText.setText(TimeUtil.getFormatedTime(millisTo, this));
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

    /**
     * Gets values from activity form and maps them to mDevice entity.
     * "Nulls" the values for unchecked switches.
     */
    private void getFormValues() {
        mDevice.setName(mFormItems.nameEdit.getText().toString());
        mDevice.setMac(mFormItems.macEdit.getText().toString());
        mDevice.setBroadcast(mFormItems.broadcastEdit.getText().toString());
        mDevice.setPort(Integer.parseInt(mFormItems.portEdit.getText().toString()));

        if (mFormItems.autoWakeSwitch.isChecked()) {
            mDevice.setSSID(mFormItems.ssidEdit.getText().toString());
    
            if (!mFormItems.quietHoursSwitch.isChecked()) {
                mDevice.setQuietHoursFrom(null);
                mDevice.setQuietHoursTo(null);
            }

            if (mFormItems.idleTimeSwitch.isChecked()) {
                mDevice.setIdleTime(Integer.parseInt(mFormItems.idleTimeEdit.getText().toString()));
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

    /**
     * Validates form values and sets their error messages if the validation failed.
     * @return      Is form valid?
     */
    private Boolean validateFormValues() {
        Boolean isValid = true;
        resetFormErrors();

        // Name
        Editable name = mFormItems.nameEdit.getText(); 
        if (name == null || name.toString().isEmpty()) {
            mFormItems.nameEdit.setError("Name is required");
            isValid = false;
        }

        // Mac
        Editable mac = mFormItems.macEdit.getText();
        if (mac == null || mac.toString().isEmpty()) {
            mFormItems.macEdit.setError("MAC address is required");
            isValid = false;
        } else if (mac.toString().length() != 17) {
            // Since MAC is auto-formatted, it is already of right format, so only its  
            // length needs to be validated to check if it's a valid MAC address
            mFormItems.macEdit.setError("Invalid MAC address");
            isValid = false;
        }

        // Ip
        Editable ip = mFormItems.broadcastEdit.getText();
        if (ip == null || ip.toString().isEmpty()) {
            mFormItems.broadcastEdit.setError("Broadcast IP/FQDN is required");
            isValid = false;
        }

        // Port
        Editable port = mFormItems.portEdit.getText();
        if (port == null || port.toString().isEmpty()) {
            mFormItems.portEdit.setError("Port is required");
            isValid = false;
        }

        if (mFormItems.autoWakeSwitch.isChecked()) {
            // SSID
            Editable ssid = mFormItems.ssidEdit.getText();
            if (ssid == null || ssid.toString().isEmpty()) {
                mFormItems.ssidEdit.setError("SSID is required");
                isValid = false;
            }

            // Idle time
            if (mFormItems.idleTimeSwitch.isChecked()) {
                Editable idleTime = mFormItems.idleTimeEdit.getText();
                if (idleTime == null || idleTime.toString().isEmpty()) {
                    mFormItems.idleTimeEdit.setError("Idle time is required");
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    /**
     * Clears all form errors.
     */
    private void resetFormErrors() {
        mFormItems.nameEdit.setError(null);
        mFormItems.macEdit.setError(null);
        mFormItems.broadcastEdit.setError(null);
        mFormItems.portEdit.setError(null);
        mFormItems.nameEdit.setError(null);
    }

    /**
     * Displays the help dialog containing assets/Help.html data.
     */
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

        TextView textView = (TextView)alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(14);
    }

    /**
     * Reads assets/Help.html file data as a String.  
     * @return      Help.html data as a string.
     */
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

    /**
     * Populates mFormItems entity with new FormItems entity.
     */
    private void initializeFormItems() {
        mFormItems = createFormItems();
    }
    
    /**
     * Creates a new FormItems entity containing form view entities found 
     * by their R.Ids and cast to appropriate types.
     * @return      FormItems entity.
     */
    private FormItems createFormItems() {
        FormItems formItems = new FormItems();

        // EditText
        formItems.nameEdit = (EditText)findViewById(R.id.edit_name);
        formItems.broadcastEdit = (EditText)findViewById(R.id.edit_broadcast);
        formItems.macEdit = (EditText)findViewById(R.id.edit_mac);
        formItems.portEdit = (EditText)findViewById(R.id.edit_port);
        formItems.ssidEdit = (EditText)findViewById(R.id.edit_ssid);
        formItems.idleTimeEdit = (EditText)findViewById(R.id.edit_idle_time);

        // TextView
        formItems.quietHoursFromText = (TextView)findViewById(R.id.text_quiet_hours_from);
        formItems.quietHoursToText = (TextView)findViewById(R.id.text_quiet_hours_to);

        // Button
        formItems.discoverSSIDButton = (ImageButton)findViewById(R.id.btn_discover_ssid);

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

    /**
     * FormItems class containing definitions of all needed view elements. Used in parent class
     * to access view elements more easily without the need to find them by Id each time.
     */
    private class FormItems {
        // EditText
        EditText nameEdit;
        EditText broadcastEdit;
        EditText macEdit;
        EditText portEdit;
        EditText ssidEdit;
        EditText idleTimeEdit;

        // TextView
        TextView quietHoursFromText;
        TextView quietHoursToText;

        // Button
        ImageButton discoverSSIDButton;

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