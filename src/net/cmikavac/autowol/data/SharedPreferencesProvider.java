package net.cmikavac.autowol.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesProvider {
    private SharedPreferences mPreferences = null;
    private Context mContext = null;

    public static final String PREF_LAST_SSID = "LastSSID";

    public SharedPreferencesProvider(Context context) {
        mContext = context;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public String getLastSSID() {
        return mPreferences.getString(PREF_LAST_SSID, "No saved SSID's exist yet.");
    }

    public void setLastSSID(String ssid) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREF_LAST_SSID, ssid);
        editor.commit();
    }
}
