package com.klimovgv.smsblock.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.klimovgv.smsblock.R;
import com.klimovgv.smsblock.db.SmsDataSource;
import com.klimovgv.smsblock.models.Sms;

import java.util.List;

public class MainActivity extends Activity {
    private SmsDataSource dataSource;
    private List<Sms> allSms;
    private SmsListArrayAdapter smsListArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        dataSource = new SmsDataSource(this);
        dataSource.open();

        allSms = dataSource.getAllSms();
        smsListArrayAdapter = new SmsListArrayAdapter(this, allSms);

        ListView list = (ListView)findViewById(R.id.sms_listview);
        list.setAdapter(smsListArrayAdapter);
        registerForContextMenu(list);
    }

    public void onRefreshClick(View view) {
        refreshSmsList();
    }

    public void onFiltersClick(View view) {
        Intent intent = new Intent(this, FiltersActivity.class);
        startActivity(intent);
    }

    private void refreshSmsList() {
        allSms = dataSource.getAllSms();

        smsListArrayAdapter.clear();
        smsListArrayAdapter.addAll(allSms);

        smsListArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.sms_listview) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("From: " + allSms.get(info.position).getPhoneNumber());

            String[] menuItems = getResources().getStringArray(R.array.sms_listview_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String[] menuItems = getResources().getStringArray(R.array.sms_listview_menu);

        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];
        Sms selectedSms = allSms.get(info.position);

        if ("delete".equalsIgnoreCase(menuItemName)) {
            this.dataSource.deleteSms(selectedSms);
            refreshSmsList();
        }

        return true;
    }

    @Override
    protected void onResume() {
        dataSource.open();
        refreshSmsList();

        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();

        super.onPause();
    }
}
