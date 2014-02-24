package net.cmikavac.autowol.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkingUtil {
    /**
     * Gets current SSID if connected to Wi-Fi, null if disconnected from Wi-Fi.
     * @param context       Context entity.
     * @return              Wi-Fi SSID string. 
     */
    public static String getCurrentSSID(Context context) {
        String ssid = null;

        ConnectivityManager connectionManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo.isConnected()) {
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();

            if (connectionInfo != null && connectionInfo.getSSID() != null)
                ssid = connectionInfo.getSSID();
        }

        return ssid != null ? ssid.replace("\"", "") : null;
    }
}
