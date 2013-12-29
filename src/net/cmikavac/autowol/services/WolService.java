package net.cmikavac.autowol.services;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import net.cmikavac.autowol.models.Device;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class WolService extends AsyncTask<Device, Void, String> {
	private Context mContext = null;
	
	public WolService(Context context) {
		mContext = context;
	}

	@Override
	protected String doInBackground(Device... devices) {
		Device device = devices[0];
		return Wake(device.getIp(), device.getMac(), 9);
	}

	@Override
	protected void onPostExecute(String message) {
		super.onPostExecute(message);
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();;
	}

	private String Wake(String ip, String mac, int port) {
		try {
			byte[] macBytes = getMacBytes(mac);
			byte[] bytes = new byte[6 + 16 * macBytes.length];
			for (int i = 0; i < 6; i++) {
				bytes[i] = (byte) 0xff;
			}
			for (int i = 6; i < bytes.length; i += macBytes.length) {
				System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
			}

			InetAddress address = InetAddress.getByName(ip);
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
			DatagramSocket socket = new DatagramSocket();
			socket.send(packet);
			socket.close();

			return "Wake-on-Lan packet sent.";
		}
		catch (Exception e) {
			return  "Failed to send Wake-on-LAN packet: " + e.getMessage();
		}
	}

	private static byte[] getMacBytes(String mac) throws IllegalArgumentException {
		byte[] bytes = new byte[6];
		String[] hex = mac.split("(\\:|\\-)");
		
		if (hex.length != 6) {
			throw new IllegalArgumentException("Invalid MAC address.");
		}

		try {
			for (int i = 0; i < 6; i++) {
				bytes[i] = (byte) Integer.parseInt(hex[i], 16);
			}
		}
		catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid hex digit in MAC address.");
		}

		return bytes;
	}
}
