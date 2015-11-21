package com.soco.SoCoClient.buddies.allbuddies.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.soco.SoCoClient.common.database.Config;

public class AllBuddiesTabsAdapter extends FragmentPagerAdapter {

    String tag = "AllBuddiesTabsAdapter";

    public AllBuddiesTabsAdapter(FragmentManager fm) {
        super(fm);
        Log.v(tag, "AllBuddiesTabsAdapter init");
    }

    @Override
    public Fragment getItem(int index) {
        Log.v(tag, "get item from index: " + index);
        switch (index) {
//            case Config.EVENT_GROUPS_BUDDIES_TAB_INDEX_GROUPS:
//                return new EventGroupsFragment();
//            case Config.EVENT_GROUPS_BUDDIES_TAB_INDEX_BUDDIES:
//                return new EventBuddiesFragment();
            case Config.ALL_BUDDIES_TAB_INDEX_MYMATCH:
                return new MyMatchFragment();
            case Config.ALL_BUDDIES_TAB_INDEX_MYBUDDIES:
                return new MyBuddiesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return Config.ALL_BUDDIES_TAB_COUNT;
    }

}