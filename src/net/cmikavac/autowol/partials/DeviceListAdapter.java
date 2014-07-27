package net.cmikavac.autowol.partials;

import java.util.List;

import net.cmikavac.autowol.DeviceActivity;
import net.cmikavac.autowol.R;
import net.cmikavac.autowol.data.DbProvider;
import net.cmikavac.autowol.models.DeviceModel;
import net.cmikavac.autowol.services.WolService;
import net.cmikavac.autowol.utils.TimeUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DeviceListAdapter extends ArrayAdapter<DeviceModel> {
    private Context mContext = null;
    private List<DeviceModel> mDevices = null;
    private DbProvider mDbProvider = null;

    /**
     * Constructor.
     * @param context       Context entity.
     * @param devices       List of DeviceModel entities.
     */
    public DeviceListAdapter(Context context, List<DeviceModel> devices) {
        super(context, R.layout.device_item_view, devices);
        mContext = context;
        mDevices = devices;
        mDbProvider = new DbProvider(context);
    }

    /**
     * Binds data to current itemView and registers its click callbacks.
     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     * @param position          Current itemView position in list.
     * @param itemView          Current View item on which to bind data.
     * @param parentGroup       Parent ViewGroup entity.
     */
    @Override
    public View getView(final int position, View itemView, ViewGroup parentGroup) {
        ItemHolder itemHolder = new ItemHolder();

        // Inflates new items on scroll to improve performance.
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.device_item_view, parentGroup, false);
            itemHolder = createItemHolder(itemView);
            itemView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder)itemView.getTag();
        }

        setItemHolderTextValues(position, itemHolder);
        registerOnClickListener(position, itemView);
        registerOnLongClickListener(position, itemView);

        return itemView;
    }

    /**
     * Sets text values for ItemHolder entity from mDevice entity. 
     * Sets default values for null mDevice text values.
     * @param position      ItemHolder position in list.
     * @param itemHolder    ItemHolder entity for which to set text values.
     */
    private void setItemHolderTextValues(final int position, ItemHolder itemHolder) {
        DeviceModel device = mDevices.get(position);
        itemHolder.nameText.setText(device.getName());
        itemHolder.broadcastText.setText(device.getBroadcast());
        itemHolder.macText.setText(device.getMac());
        itemHolder.portText.setText("Port: " + device.getPort());

        if (device.getSSID() != null) {
            itemHolder.ssidText.setText("SSID: " + device.getSSID());
        }

        if (device.getQuietHoursFrom() != null) {
            String hoursFrom = TimeUtil.getFormatedTime(device.getQuietHoursFrom(), mContext);
            String hoursTo = TimeUtil.getFormatedTime(device.getQuietHoursTo(), mContext);
            String quietHours = "QH: " + hoursFrom + " to " + hoursTo;
            itemHolder.quietHoursText.setText(quietHours);
        }
        
        if (device.getIdleTime() != null) {
            itemHolder.idleTimeText.setText("Idle time: " + device.getIdleTime() + " (min)");
        }
    }

    /**
     * Registers wakeDevice onClick callback to itemView entity.
     * @param position      Item position in the list.
     * @param itemView      ItemView entity for which to set callback.
     */
    private void registerOnClickListener(final int position, View itemView) {
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wakeDevice(position);
            }
        });
    }

    /**
     * Registers showDialog onLongClick callback to itemView entity.
     * @param position      Item position in the list.
     * @param itemView      ItemView entity for which to set callback.
     */
    private void registerOnLongClickListener(final int position, View itemView) {
        itemView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);
                showDialog(position);
                return false;
            }
        });
    }

    /**
     * Builds and shows alert dialog with "2ake, edit and delete" options. 
     * @param position      Item position in the list.
     */
    private void showDialog(final int position) {
        final CharSequence[] dialogItems = {"Wake", "Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Choose action");
        builder.setItems(dialogItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item) {
                    case 0: // Wake
                        wakeDevice(position);
                        break;
                    case 1: // Edit
                        editDevice(position);
                        break;
                    case 2: // Delete
                        confirmDeviceDeletion(position);
                        break;
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Wakes the device.
     * @param position      Item position in the list.
     */
    private void wakeDevice(int position) {
        DeviceModel device = mDevices.get(position);
        new WolService(mContext, false).execute(device);
    }

    /**
     * Starts edit activity for selected device and passes the DeviceModel
     * entity as extended data (deviceObject).
     * @param position      Item position in the list.
     */
    private void editDevice(int position) {
        DeviceModel device = mDevices.get(position);
        Intent intent = new Intent(mContext, DeviceActivity.class);
        intent.putExtra("deviceObject", device);
        mContext.startActivity(intent);
    }

    /**
     * Prompts the user with a confirmation dialog for device deletion.
     * If deletion is confirmed, delete device from DB else do nothing.
     * @param position      Item position in the list.
     */
    private void confirmDeviceDeletion(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

        alert.setTitle("Confirm");
        alert.setMessage("Are you sure you wish to delete this device?");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteDevice(position);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                 // Deletion canceled. Do nothing.
            }
        });

        alert.show();
    }

    /**
     * Deletes selected device record from DB and removes it from the item list on main activity. 
     * @param position      Item position in the list.
     */
    private void deleteDevice(int position) {
        mDbProvider.open();
        DeviceModel device = mDevices.get(position);
        mDbProvider.deleteDevice(device.getId());
        mDbProvider.close();

        mDevices.remove(position);
        notifyDataSetChanged();
    }

    /**
     * Creates ItemHolder with all the view items view entities found 
     * by their R.Ids and cast to appropriate types.
     * @param itemView      ItemView entity for which to create ItemHolder entity. 
     * @return              New ItemHolder entity.
     */
    private ItemHolder createItemHolder(View itemView) {
        ItemHolder itemHolder = new ItemHolder();
        itemHolder.nameText = (TextView)itemView.findViewById(R.id.device_item_text_name);
        itemHolder.broadcastText = (TextView)itemView.findViewById(R.id.device_item_txt_broadcast);
        itemHolder.macText = (TextView)itemView.findViewById(R.id.device_item_txt_mac);
        itemHolder.portText = (TextView)itemView.findViewById(R.id.device_item_txt_port);
        itemHolder.ssidText = (TextView)itemView.findViewById(R.id.device_item_txt_ssid);
        itemHolder.quietHoursText = (TextView)itemView.findViewById(R.id.device_item_txt_quiet_hours);
        itemHolder.idleTimeText = (TextView)itemView.findViewById(R.id.device_item_txt_idle_time);
        return itemHolder;
    }

    /**
     * ItemHolder class containing definitions of all needed view elements. Used in parent class
     * to access view elements more easily without the need to find them by Id each time.
     */
    private class ItemHolder {
        TextView nameText;
        TextView broadcastText;
        TextView macText;
        TextView portText;
        TextView ssidText;
        TextView quietHoursText;
        TextView idleTimeText;
    }
}