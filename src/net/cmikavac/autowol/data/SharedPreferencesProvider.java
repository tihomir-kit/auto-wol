package net.cmikavac.autowol.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesProvider {
    private SharedPreferences mPreferences = null;
    private Context mContext = null;

    // Last SSID device has been connected to.
    // Used for on device disconnected DB records timestamp updates. 
    public static final String PREF_LAST_SSID = "LastSSID";

    /**
     * Sets application context and instantiates PreferencesManager.
     * @param context       Context entity.
     */
    public SharedPreferencesProvider(Context context) {
        mContext = context;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    /**
     * Gets PREF_LAST_SSID value from PreferenceManager.
     * @return
     */
    public String getLastSSID() {
        return mPreferences.getString(PREF_LAST_SSID, "No saved SSID's exist yet.");
    }

    /**
     * Sets PreferenceManager PREF_LAST_SSID value.
     * @param ssid      SSID to set.
     */
    public void setLastSSID(String ssid) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREF_LAST_SSID, ssid);
        editor.commit();
    }
}
