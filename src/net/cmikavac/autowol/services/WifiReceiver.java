package net.cmikavac.autowol.services;

import java.util.Calendar;
import java.util.List;

import net.cmikavac.autowol.data.DbProvider;
import net.cmikavac.autowol.data.SharedPreferencesProvider;
import net.cmikavac.autowol.models.DeviceModel;
import net.cmikavac.autowol.utils.TimeUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver {
    private SharedPreferencesProvider mSharedPreferencesProvider = null;
    private DbProvider mDbProvider = null;
    private Context mContext = null;

    /**
     * Obligatory empty constructor.
     */
    public WifiReceiver() {
    }

    /**
     * Sets context and instantiates DbProvider and SharedPreferencesProvider.
     * @param context       Context entity.
     */
    private void setContext(Context context) {
        mContext = context;
        mDbProvider = new DbProvider(context);
        mDbProvider.open();
        mSharedPreferencesProvider = new SharedPreferencesProvider(context);
    }

    /* (non-Javadoc)
     * Sets context and calls handleNetworkStateChange on broadcast received.
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        setContext(context);
        handleNetworkStateChange(intent);
    }

    /**
     * Gets network state on networkStateChange and delegates the logic further based
     * on whether the device just got connected or disconnected from Wi-Fi.
     * @param intent        Intent entity.
     */
    private void handleNetworkStateChange(Intent intent) {
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION))
        {
            WifiManager wifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            NetworkInfo.State networkState = networkInfo.getState();

            if(networkState == NetworkInfo.State.CONNECTED) {
                onWifiConnected(wifiManager);
            } else if (networkState == NetworkInfo.State.DISCONNECTED) {
                onWifiDisconnected();
            }
        }
    }

    /**
     * On Wi-Fi connected gets current SSID, saves its name to shared preferences,
     * and auto-wakes all the devices for that SSID that pass the auto-wake rules. 
     * @param wifiManager       WifiManager entity.
     */
    private void onWifiConnected(WifiManager wifiManager) {
        String ssid = wifiManager.getConnectionInfo().getSSID().replace("\"", "");
        mSharedPreferencesProvider.setLastSSID(ssid);
        wakeDevices(ssid);
    }

    /**
     * On Wi-Fi disconnected gets the SSID of the network from shared preferences
     * and updates last_disconnected field in DB for all devices matched by SSID. 
     */
    private void onWifiDisconnected() {
        String ssid = mSharedPreferencesProvider.getLastSSID();
        mDbProvider.updateDevicesLastDisconnected(ssid, Calendar.getInstance().getTimeInMillis());
        mDbProvider.close();
    }

    /**
     * Gets all devices from DB by SSID and wakes all that pass the auto-wake rules.
     * @param ssid      SSID name to filter device records.
     */
    private void wakeDevices(String ssid) {
        List<DeviceModel> devices = mDbProvider.getDevicesBySSID(ssid);

        for (DeviceModel device : devices) {
            wakeDevice(device);
        }

        mDbProvider.close();
    }
    
    /**
     * Checks if the device passes all the auto-wake rules, and if it does, it wakes it up.
     * @param device
     */
    private void wakeDevice(DeviceModel device) {
        Boolean isNowBetweenQuietHours = false;
        Boolean hasIdleTimePassed = true;

        if (device.getQuietHoursFrom() != null) {
            isNowBetweenQuietHours = TimeUtil.isNowBetweenQuietHours(device.getQuietHoursFrom(), device.getQuietHoursTo());
        }
        
        if (device.getIdleTime() != null) {
            hasIdleTimePassed = TimeUtil.hasIdleTimePassed(device.getIdleTime(), device.getLastDisconnected());
        }

        SharedPreferencesProvider provider = new SharedPreferencesProvider(mContext);
        Boolean showNotifications = provider.getShowNotifications();

        if (device.getQuietHoursFrom() != null) {
            if (!isNowBetweenQuietHours && hasIdleTimePassed) {
                new WolService(mContext, showNotifications).execute(device);
            }
        } else if (hasIdleTimePassed) {
            new WolService(mContext, showNotifications).execute(device);
        }
    }
}
