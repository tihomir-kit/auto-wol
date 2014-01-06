package net.cmikavac.autowol.services;

import net.cmikavac.autowol.data.SharedPreferencesProvider;
import net.cmikavac.autowol.models.DeviceModel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver {
    private SharedPreferencesProvider mSharedPreferencesProvider = null;
    private WolService mWolService = null;
    private Context mContext = null;

    public WifiReceiver() {
    }

    private void setContext(Context context) {
        mContext = context;
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
    }
    
    private void wakeDevices(String ssid) {
        // TODO: wake all devices that match the ssid criteria 

        mWolService = new WolService(mContext);
        DeviceModel device = new DeviceModel();
        device.setIp("192.168.1.255");
        device.setMac("00:17:a4:d6:47:18");
        device.setPort(9);

        //mWolService.execute(device);
    }
}
