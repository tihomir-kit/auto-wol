package net.cmikavac.autowol.adapters;

import java.util.List;

import net.cmikavac.autowol.DeviceActivity;
import net.cmikavac.autowol.R;
import net.cmikavac.autowol.data.DbProvider;
import net.cmikavac.autowol.models.DeviceModel;
import net.cmikavac.autowol.services.WolService;

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

    public DeviceListAdapter(Context context, List<DeviceModel> devices) {
        super(context, R.layout.device_item_view, devices);
        mContext = context;
        mDevices = devices;
        mDbProvider = new DbProvider(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ItemHolder itemHolder = new ItemHolder();
        
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.device_item_view, parent, false);
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

    private void setItemHolderTextValues(final int position, ItemHolder itemHolder) {
        DeviceModel device = mDevices.get(position);
        itemHolder.nameText.setText(device.getName());
        itemHolder.ipText.setText(device.getIp());
        itemHolder.macText.setText(device.getMac());
    }

    private void registerOnClickListener(final int position, View itemView) {
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wakeDevice(position);
            }
        });
    }

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
                        deleteDevice(position);
                        break;
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void wakeDevice(int position) {
        DeviceModel device = mDevices.get(position);
        new WolService(mContext).execute(device);
    }

    private void editDevice(int position) {
        DeviceModel device = mDevices.get(position);
        Intent intent = new Intent(mContext, DeviceActivity.class);
        intent.putExtra("deviceObject", device);
        mContext.startActivity(intent);
    }

    private void deleteDevice(int position) {
        mDbProvider.open();
        DeviceModel device = mDevices.get(position);
        mDbProvider.deleteDevice(device.getId());
        mDbProvider.close();

        mDevices.remove(position);
        notifyDataSetChanged();
    }

    private ItemHolder createItemHolder(View itemView) {
        ItemHolder itemHolder = new ItemHolder();
        itemHolder.nameText = (TextView)itemView.findViewById(R.id.device_item_text_name);
        itemHolder.ipText = (TextView)itemView.findViewById(R.id.device_item_txt_ip);
        itemHolder.macText = (TextView)itemView.findViewById(R.id.device_item_txt_mac);
        return itemHolder;
    }

    private class ItemHolder {
        TextView nameText;
        TextView ipText;
        TextView macText;
    }
}