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

    // Show notifications on Auto-WOL?
    public static final String PREF_SHOW_NOTIFICATIONS = "ShowNotifications";

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
     * @return         Last SSID.
     */
    public String getLastSSID() {
        return mPreferences.getString(PREF_LAST_SSID, "MyLastSSID");
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

    /**
     * Gets PREF_SHOW_NOTIFICATIONS value from PreferenceManager.
     * @return        Show notifications or true if preference not set.
     */
    public Boolean getShowNotifications() {
        return mPreferences.getBoolean(PREF_SHOW_NOTIFICATIONS, true);
    }

    /**
     * Sets PreferenceManager PREF_SHOW_NOTIFICATIONS value.
     * @param ssid      Show notifications?
     */
    public void setShowNotifications(Boolean showNotifications) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(PREF_SHOW_NOTIFICATIONS, showNotifications);
        editor.commit();
    }
}
