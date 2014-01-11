package net.cmikavac.autowol.data;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import net.cmikavac.autowol.models.DeviceModel;

public class DbMapper extends DbConfiguration {
    public List<DeviceModel> mapDevices(Cursor cursor) {
        List<DeviceModel> devices = new ArrayList<DeviceModel>();
        
        if (cursor.moveToFirst()) {
            do {
                devices.add(mapDevice(cursor));
            } while(cursor.moveToNext());
        }
        
        return devices;
    }
    
    public DeviceModel mapDevice(Cursor cursor) {
        DeviceModel device = null;

        if (cursor != null) {
            device = new DeviceModel(
                cursor.getLong(COL_ROWID),
                cursor.getString(COL_NAME),
                cursor.getString(COL_MAC),
                cursor.getString(COL_IP),
                cursor.getInt(COL_PORT),
                cursor.getString(COL_SSID),
                cursor.isNull(COL_QUIET_FROM) ? null : cursor.getLong(COL_QUIET_FROM),
                cursor.isNull(COL_QUIET_TO) ? null : cursor.getLong(COL_QUIET_TO),
                cursor.isNull(COL_IDLE_TIME) ? null : cursor.getInt(COL_IDLE_TIME),
                cursor.isNull(COL_LAST_DISCONNECTED) ? null : cursor.getLong(COL_LAST_DISCONNECTED)
            );
        }

        return device;
    }
}