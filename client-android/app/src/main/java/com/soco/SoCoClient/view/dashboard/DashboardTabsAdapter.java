package com.soco.SoCoClient.view.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.view.chats.ChatsFragment;
import com.soco.SoCoClient.view.event.EventsFragment;

public class DashboardTabsAdapter extends FragmentPagerAdapter {

    String tag = "DashboardTabsAdapter";

    public DashboardTabsAdapter(FragmentManager fm) {
        super(fm);
        Log.v(tag, "contactsAdapter init");
    }

    @Override
    public Fragment getItem(int index) {
        Log.v(tag, "get item from index: " + index);
        switch (index) {
            case DataConfig.DASHBOARD_TAB_INDEX_EVENTS:
                return new EventsFragment();
            case DataConfig.DASHBOARD_TAB_INDEX_CHATS:
//                return new FragmentContactsObs();
                return new ChatsFragment();
            case DataConfig.DASHBOARD_TAB_INDEX_STREAM:
                return new FragmentStream();
            case DataConfig.DASHBOARD_TAB_INDEX_DISCOVERY:
                return new FragmentDiscovery();
        }

        return null;
    }

    @Override
    public int getCount() {
        return DataConfig.DASHBOARD_TAB_COUNT;
    }

}