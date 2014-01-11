package net.cmikavac.autowol.services;

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

    public WifiReceiver() {
    }

    private void setContext(Context context) {
        mContext = context;
        mDbProvider = new DbProvider(context);
        mDbProvider.open();
        mSharedPreferencesProvider = new SharedPreferencesProvider(context);
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        setContext(context);
        handleNetworkStateChange(intent);
    }

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

    private void onWifiConnected(WifiManager wifiManager) {
        String ssid = wifiManager.getConnectionInfo().getSSID().replace("\"", "");
        mSharedPreferencesProvider.setLastSSID(ssid);
        wakeDevices(ssid);
    }

    private void onWifiDisconnected() {
        String ssid = mSharedPreferencesProvider.getLastSSID();
        // TODO: update db records for ssid (set last disconnected timestamp)

        mDbProvider.close();
    }
    
    private void wakeDevices(String ssid) {
        WolService wolService = new WolService(mContext);
        List<DeviceModel> devices = mDbProvider.getDevicesBySSID(ssid);

        for (DeviceModel device : devices) {
            wakeDevice(device, wolService);
        }

        mDbProvider.close();
    }
    
    private void wakeDevice(DeviceModel device, WolService wolService) {
        Boolean isNowBetweenQuietHours = TimeUtil.isNowBetweenQuietHours(device.getQuietHoursFrom(), device.getQuietHoursTo());
        if (device.getQuietHoursFrom() != null) {
            if (!isNowBetweenQuietHours) {
                wolService.execute(device);
            }
        }
    }
}
