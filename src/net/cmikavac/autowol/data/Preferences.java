package net.cmikavac.autowol.data;

import java.util.ArrayList;

import net.cmikavac.autowol.models.*;

public class Preferences {
	public static ArrayList<Device> getStoredDevices()
	{
		ArrayList<Device> devices = new ArrayList<Device>();
		devices.add(new Device("Kanta", "192.168.1.5", "12:23:34:45:56:67"));
		devices.add(new Device("R2D2", "192.168.1.15", "12:23:34:45:56:67"));
		devices.add(new Device("Tabla", "192.168.1.25", "12:23:34:45:56:67"));
		return devices;
	}
}
