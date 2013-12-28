package net.cmikavac.autowol.data;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import net.cmikavac.autowol.models.Device;

public class DbMapper extends DbConfiguration {
	public List<Device> mapDevices(Cursor cursor) {
		List<Device> devices = new ArrayList<Device>();
		
		if (cursor.moveToFirst()) {
			do {
				devices.add(mapDevice(cursor));
			} while(cursor.moveToNext());
		}
		
		return devices;
	}
	
	public Device mapDevice(Cursor cursor) {
		Device device = null;

		if (cursor != null) {
			device = new Device(
				cursor.getInt(COL_ROWID),
				cursor.getString(COL_NAME),
				cursor.getString(COL_IP),
				cursor.getString(COL_MAC)
			);
		}

		return device;
	}
}
