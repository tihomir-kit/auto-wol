package net.cmikavac.autowol.adapters;

import java.util.List;

import net.cmikavac.autowol.R;
import net.cmikavac.autowol.models.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DeviceListAdapter extends ArrayAdapter<Device> {
	private Context mContext = null;
	private List<Device> mDevices = null;
	
	public DeviceListAdapter(Context context, List<Device> devices) {
		super(context, R.layout.device_item_view, devices);
		mContext = context;
		mDevices = devices;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		if (itemView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);			
			itemView = inflater.inflate(R.layout.device_item_view, parent, false);
		}
	
		Device device = mDevices.get(position);
		
		TextView nameText = (TextView)itemView.findViewById(R.id.device_item_text_name);
		nameText.setText(device.getName());
		
		TextView ipText = (TextView)itemView.findViewById(R.id.device_item_txt_ip);
		ipText.setText(device.getIp());
		
		TextView macText = (TextView)itemView.findViewById(R.id.device_item_txt_mac);
		macText.setText(device.getMac());
		
		return itemView;
	}
}
