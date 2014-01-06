package net.cmikavac.autowol;

import java.util.ArrayList;
import java.util.List;

import net.cmikavac.autowol.adapters.DeviceListAdapter;
import net.cmikavac.autowol.models.DeviceModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
    private List<DeviceModel> mDevices = new ArrayList<DeviceModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_help).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
                intent.putExtra("deviceObject", new DeviceModel());
                startActivity(intent);
                break;
        }
        return true;
    }

    public void populateListView() {
        mDevices = mDbProvider.getAllDevices();
        ArrayAdapter<DeviceModel> adapter = new DeviceListAdapter(this, mDevices);
        ListView list = (ListView)findViewById(R.id.device_list_view);
        list.setAdapter(adapter);
        setEmptyListNotificationVisibility();
    }

    private void setEmptyListNotificationVisibility() {
        TextView notification = (TextView)findViewById(R.id.text_empty_list);
        if (!mDevices.isEmpty()) {
            notification.setVisibility(View.GONE);
        } else {
            notification.setVisibility(View.VISIBLE);
        }
    }
}