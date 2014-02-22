package net.cmikavac.autowol;

import java.util.ArrayList;
import java.util.List;

import net.cmikavac.autowol.models.DeviceModel;
import net.cmikavac.autowol.partials.DeviceListAdapter;

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

    /**
     * Upon activity creation populates the list view with Device entities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateListView();
    }

    /**
     * Re-populates the list view with Device entities
     * after Device deletion / creation / edit.
     */
    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    /**
     * Upon options menu creation inflates the menu and sets
     * button functionalities and visibility. 
     * @param menu      Menu entity to inflate.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_help).setVisible(false);
        return true;
    }

    /**
     * Routes to appropriate action upon clicking on an item from the actionBar menu.
     * @param item      Clicked item.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.action_new:
                Intent newDeviceIntent = new Intent(MainActivity.this, DeviceActivity.class);
                newDeviceIntent.putExtra("deviceObject", new DeviceModel());
                startActivity(newDeviceIntent);
                break;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
        return true;
    }

    /**
     * Fetches all Device entities from DB and binds them to an ArrayAdapter.
     */
    public void populateListView() {
        mDevices = mDbProvider.getAllDevices();
        ArrayAdapter<DeviceModel> adapter = new DeviceListAdapter(this, mDevices);
        ListView list = (ListView)findViewById(R.id.device_list_view);
        list.setAdapter(adapter);
        setEmptyListNotificationVisibility();
    }

    /**
     * If no Device entities exist yet, display an "empty list notification" instead.
     */
    private void setEmptyListNotificationVisibility() {
        TextView notification = (TextView)findViewById(R.id.text_empty_list);
        if (!mDevices.isEmpty()) {
            notification.setVisibility(View.GONE);
        } else {
            notification.setVisibility(View.VISIBLE);
        }
    }
}