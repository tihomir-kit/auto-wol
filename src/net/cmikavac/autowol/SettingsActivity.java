package net.cmikavac.autowol;

import net.cmikavac.autowol.data.SharedPreferencesProvider;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;

public class SettingsActivity extends BaseActivity {
    Context mContext = null;

    /**
     * Upon activity creation initializes form items and registers callbacks.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;

        setShowNotifications();
        registerShowNotificationsCallback();
    }

    /**
     * Upon options menu creation inflates the menu and sets
     * button functionalities and visibility. 
     * @param menu      Menu entity to inflate.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        menu.findItem(R.id.action_new).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
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
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }

    /**
     * Sets initial "show notifications" preference value.
     */
    private void setShowNotifications() {
        SharedPreferencesProvider provider = new SharedPreferencesProvider(mContext);
        Boolean showNotifications = provider.getShowNotifications();
        final CheckedTextView chkNotification = (CheckedTextView)findViewById(R.id.chk_notification);
        chkNotification.setChecked(showNotifications);
    }

    /**
     * Registers show notifications callback. (Toggles checkbox 
     * on click and updates SharedPreferences value)
     */
    private void registerShowNotificationsCallback() {
        final CheckedTextView chkNotification = (CheckedTextView)findViewById(R.id.chk_notification);
        chkNotification.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesProvider provider = new SharedPreferencesProvider(mContext);
                Boolean showNotifications = provider.getShowNotifications();
                provider.setShowNotifications(!showNotifications);
                chkNotification.toggle();
            }
        });
    }
}
