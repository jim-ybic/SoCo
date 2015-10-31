package com.soco.SoCoClient.events.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.soco.SoCoClient.common.database.Config;

public class EventGroupsBuddiesTabsAdapter extends FragmentPagerAdapter {

    String tag = "EventGroupsBuddiesTabsAdapter";

    public EventGroupsBuddiesTabsAdapter(FragmentManager fm) {
        super(fm);
        Log.v(tag, "EventGroupsBuddiesTabsAdapter init");
    }

    @Override
    public Fragment getItem(int index) {
        Log.v(tag, "get item from index: " + index);
        switch (index) {
            case Config.EVENT_GROUPS_BUDDIES_TAB_INDEX_GROUPS:
                return new EventGroupsFragment();
            case Config.EVENT_GROUPS_BUDDIES_TAB_INDEX_BUDDIES:
                return new EventBuddiesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return Config.EVENT_GROUPS_BUDDIES_TAB_COUNT;
    }

}