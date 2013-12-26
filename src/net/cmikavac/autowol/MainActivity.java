package net.cmikavac.autowol;

import java.util.ArrayList;
import java.util.List;

import net.cmikavac.autowol.R;
import net.cmikavac.autowol.models.*;
import net.cmikavac.autowol.data.*;
import net.cmikavac.autowol.adapters.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private List<Device> mDevices = new ArrayList<Device>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mDevices = Preferences.getStoredDevices();
		populateListView();
		registerClickCallback();
		registerLongClickCallback();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void populateListView() {
		ArrayAdapter<Device> adapter = new DeviceListAdapter(this, mDevices);
		ListView list = (ListView) findViewById(R.id.device_list_view);
		list.setAdapter(adapter);		
	}
	
	private void registerClickCallback() {
		ListView list = (ListView)findViewById(R.id.device_list_view);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
				
				Device clickedDevice = mDevices.get(position);
				String message = "You clicked position " + position
						+ " which is device " + clickedDevice.getName();
				Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
			}						
		});		
	}	
	
	private void registerLongClickCallback() {
		ListView list = (ListView)findViewById(R.id.device_list_view);
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
				showDialog();
				return false;
			}
		});
	}	
	
	private void showDialog() {
		final CharSequence[] items = {"Wake", "Edit", "Delete"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose action");
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
