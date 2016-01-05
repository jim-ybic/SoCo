package com.soco.SoCoClient.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.soco.SoCoClient.buddies.allbuddies.ui.MyBuddiesFragment;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.buddies.suggested.SuggestedBuddiesFragment;
import com.soco.SoCoClient.events.allevents.AllEventsFragment;
import com.soco.SoCoClient.events.suggested.SuggestedEventsFragment;
import com.soco.SoCoClient.topics.AllTopicsFragment;

public class DashboardTabsAdapter extends FragmentPagerAdapter {

    String tag = "DashboardTabsAdapter";

    public DashboardTabsAdapter(FragmentManager fm) {
        super(fm);
        Log.v(tag, "DashboardTabsAdapter init");
    }

    @Override
    public Fragment getItem(int index) {
        Log.v(tag, "get item from index: " + index);
        switch (index) {
            case Config.DASHBOARD_TAB_INDEX_EVENTS:
//                return new SuggestedEventsFragment();
                return new AllEventsFragment();
//            case Config.DASHBOARD_TAB_INDEX_FRIENDS:
//                return new SuggestedBuddiesFragment();
//                return new MyBuddiesFragment();
            case Config.DASHBOARD_TAB_INDEX_TOPICS:
//                return new SuggestedEventsFragment();
                return new AllTopicsFragment();             //todo
            case Config.DASHBOARD_TAB_INDEX_POSTS:
//                return new SuggestedEventsFragment();
                return new AllTopicsFragment();             //todo

        }

        return null;
    }

    @Override
    public int getCount() {
        return Config.DASHBOARD_TAB_COUNT;
    }

}