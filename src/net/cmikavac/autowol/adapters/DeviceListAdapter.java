package net.cmikavac.autowol.adapters;

import java.util.List;

import net.cmikavac.autowol.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import net.cmikavac.autowol.models.*;

public class DeviceListAdapter extends ArrayAdapter<Device> {
	LayoutInflater _inflater = null;
	List<Device> _devices = null;
	
	public DeviceListAdapter(Context context, List<Device> devices) {
		super(context, R.layout.device_item_view, devices);
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_devices = devices;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		if (itemView == null) {			
			itemView = _inflater.inflate(R.layout.device_item_view, parent, false);
		}
	
		Device device = _devices.get(position);
		
		TextView nameText = (TextView)itemView.findViewById(R.id.device_item_textName);
		nameText.setText(device.getName());
		
		TextView ipText = (TextView)itemView.findViewById(R.id.device_item_textIp);
		ipText.setText(device.getIp());
		
		TextView macText = (TextView)itemView.findViewById(R.id.device_item_textMac);
		macText.setText(device.getMac());
		
		return itemView;
	}
}
