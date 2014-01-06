package net.cmikavac.autowol.services;

import net.cmikavac.autowol.models.DeviceModel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver {
    private WolService mWolService = null;

    public WifiReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION))
        {
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            NetworkInfo.State networkState = networkInfo.getState();

            if(networkState == NetworkInfo.State.CONNECTED)
            {
                String networkSsid = wifiManager.getConnectionInfo().getSSID().replace("\"", "");
                android.util.Log.d("CMIK", "Wifi is connected: " + networkSsid);
            }
            else if(networkState == NetworkInfo.State.DISCONNECTED)
            {
                String connectingToSsid = wifiManager.getConnectionInfo().getSSID().replace("\"", "");
                android.util.Log.d("CMIK", "Wifi is disconnected: " + connectingToSsid);
            }
        }

        mWolService = new WolService(context);
        DeviceModel device = new DeviceModel();
        device.setIp("192.168.1.255");
        device.setMac("00:17:a4:d6:47:18");
        device.setPort(9);

        //mWolService.execute(device);
    }

}
