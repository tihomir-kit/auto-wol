package net.cmikavac.autowol.adapters;

import java.util.List;

import net.cmikavac.autowol.DeviceActivity;
import net.cmikavac.autowol.R;
import net.cmikavac.autowol.models.Device;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceListAdapter extends ArrayAdapter<Device> {
	private Context mContext = null;
	private List<Device> mDevices = null;

	public DeviceListAdapter(Context context, List<Device> devices) {
		super(context, R.layout.device_item_view, devices);
		mContext = context;
		mDevices = devices;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		ItemHolder itemHolder = new ItemHolder();
		
		if (itemView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = inflater.inflate(R.layout.device_item_view, parent, false);						
			itemHolder = setHolderItemViews(itemView);			
			itemView.setTag(itemHolder);
		} else {
			itemHolder = (ItemHolder)itemView.getTag();
		}

		setItemTextValues(position, itemHolder);
		registerOnClickListener(position, itemView);		
		registerOnLongClickListener(position, itemView);		

		return itemView;
	}

	private ItemHolder setHolderItemViews(View itemView) {
		ItemHolder itemHolder = new ItemHolder();
		itemHolder.nameText = (TextView)itemView.findViewById(R.id.device_item_text_name);
		itemHolder.ipText = (TextView)itemView.findViewById(R.id.device_item_txt_ip);
		itemHolder.macText = (TextView)itemView.findViewById(R.id.device_item_txt_mac);
		return itemHolder;
	}

	private void setItemTextValues(final int position, ItemHolder itemHolder) {
		Device device = mDevices.get(position);
		itemHolder.nameText.setText(device.getName());
		itemHolder.ipText.setText(device.getIp());
		itemHolder.macText.setText(device.getMac());
	}

	private void registerOnClickListener(final int position, View itemView) {
		itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: implement WOL call here
				Device device = mDevices.get(position); 
				String message = "You clicked position " + position
					+ " which is device " + device.getName();
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void registerOnLongClickListener(final int position, View itemView) {
		itemView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
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
		    	Device device = mDevices.get(position);
				Intent intent = new Intent(mContext, DeviceActivity.class);
				intent.putExtra("deviceObject", device);
				mContext.startActivity(intent);		
		    }
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}	

	private class ItemHolder {
		TextView nameText;
		TextView ipText;
		TextView macText;
	}
}
