package net.cmikavac.autowol.adapters;

import java.util.List;

import net.cmikavac.autowol.DeviceActivity;
import net.cmikavac.autowol.R;
import net.cmikavac.autowol.models.Device;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
		Holder holder = new Holder();
		
		if (itemView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = inflater.inflate(R.layout.device_item_view, parent,
					false);
			
			holder.nameText = (TextView) itemView.findViewById(R.id.device_item_text_name);
			holder.ipText = (TextView) itemView.findViewById(R.id.device_item_txt_ip);
			holder.macText = (TextView)itemView.findViewById(R.id.device_item_txt_mac);
			
			itemView.setTag(holder);
		}else{
			holder = (Holder)itemView.getTag();
		}

		Device device = mDevices.get(position);

		holder.nameText.setText(device.getName());
		holder.ipText.setText(device.getIp());
		holder.macText.setText(device.getMac());

		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, DeviceActivity.class);
				intent.putExtra("device", mDevices.get(position));
				mContext.startActivity(intent);

				String message = "You clicked position " + position
						+ " which is device "
						+ mDevices.get(position).getName();
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
			}

		});

		return itemView;
	}
	
//koristi se ovaj holder za smooth scroling da se ne mora svaki put raditi referenca na elemente layouta
	private class Holder{
		TextView nameText;
		TextView ipText;
		TextView macText;
	}
}
