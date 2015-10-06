package com.soco.SoCoClient.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.soco.SoCoClient.control.database.Config;
import com.soco.SoCoClient.view.friends.FragmentSuggestedFriends;
import com.soco.SoCoClient.view.events.FragmentSuggestedEvents;

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
            case Config.DASHBOARD_TAB_INDEX_EVENTS:
                return new FragmentSuggestedEvents();
            case Config.DASHBOARD_TAB_INDEX_FRIENDS:
//                return new FragmentContactsObs();
                return new FragmentSuggestedFriends();
//            case DataConfig.DASHBOARD_TAB_INDEX_STREAM:
//                return new FragmentStream();
//            case DataConfig.DASHBOARD_TAB_INDEX_MESSAGES:
//                return new FragmentMessages();
        }

        return null;
    }

    @Override
    public int getCount() {
        return Config.DASHBOARD_TAB_COUNT;
    }

}