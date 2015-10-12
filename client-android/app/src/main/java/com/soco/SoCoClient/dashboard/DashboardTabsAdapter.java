package com.soco.SoCoClient.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.friends.suggested.FragmentSuggestedFriends;
import com.soco.SoCoClient.events.suggested.SuggestedEventsFragment;

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
                return new SuggestedEventsFragment();
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