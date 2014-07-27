package net.cmikavac.autowol.services;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import net.cmikavac.autowol.models.DeviceModel;

import net.cmikavac.autowol.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class WolService extends AsyncTask<DeviceModel, Void, String> {
    private Context mContext = null;
    private Boolean mNotify = false;
    
    /**
     * Constructor.
     * @param context   Context entity.
     */
    public WolService(Context context, Boolean notify) {
        mContext = context;
        mNotify = notify;
    }

    /* (non-Javadoc)
     * Wakes the device asynchronously.
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected String doInBackground(DeviceModel... devices) {
        DeviceModel device = devices[0];
        return Wake(device.getName(), device.getBroadcast(), device.getMac(), device.getPort());
    }

    /* (non-Javadoc)
     * Creates a notification or toast message after device WOL attempt.
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(String message) {
        super.onPostExecute(message);
        if (mNotify)
            createNotification(message);
        else
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Creates a WOL network packet and sends it to the network.
     * @param name          WOL device name.
     * @param broadcast     Access point broadcast IP/FQDN.
     * @param mac           MAC address of the device to wake.
     * @param port          WOL port to be used.
     * @return              Success/exception message.
     */
    private String Wake(String name, String broadcast, String mac, int port) {
        try {
            byte[] macBytes = getMacBytes(mac);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }

            InetAddress address = InetAddress.getByName(broadcast);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();

            return "WOL packet sent to " + name + ".";
        }
        catch (Exception e) {
            return  "Failed to send WOL packet: " + e.getMessage();
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

    /**
     * Creates a new Android notification to let the user know Auto-WOL packet has been sent.
     * @param message       Message to display.
     */
    private void createNotification(String message) {
        Notification notification = new NotificationCompat.Builder(mContext)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("Auto WOL")
            .setContentText(message)
            .build();

        NotificationManager mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(new Random(System.currentTimeMillis()).nextInt() , notification);
    }
}