package net.cmikavac.autowol.services;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import net.cmikavac.autowol.models.DeviceModel;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class WolService extends AsyncTask<DeviceModel, Void, String> {
    private Context mContext = null;
    
    /**
     * Constructor.
     * @param context   Context entity.
     */
    public WolService(Context context) {
        mContext = context;
    }

    /* (non-Javadoc)
     * Wakes the device asynchronously.
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected String doInBackground(DeviceModel... devices) {
        DeviceModel device = devices[0];
        return Wake(device.getIp(), device.getMac(), device.getPort());
    }

    /* (non-Javadoc)
     * Creates a toast message after device WOL attempt.
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(String message) {
        super.onPostExecute(message);
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();;
    }

    /**
     * Creates a WOL network packet and sends it to the network.
     * @param ip        Access point broadcast IP.
     * @param mac       MAC address of the device to wake.
     * @param port      WOL port to be used.
     * @return          Success/exception message.
     */
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

    /**
     * Creates a byte array from MAC address.
     * @param mac       MAC address.
     * @return          Byte array representing MAC address.
     * @throws IllegalArgumentException
     */
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