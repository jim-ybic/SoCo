package com.soco.SoCoClient.userprofile.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.soco.SoCoClient.common.database.Config;

public class UserProfileTabsAdapter extends FragmentPagerAdapter {

    String tag = "UserProfileTabsAdapter";

    public UserProfileTabsAdapter(FragmentManager fm) {
        super(fm);
        Log.v(tag, "UserProfileTabsAdapter init");
    }

    @Override
    public Fragment getItem(int index) {
        Log.v(tag, "get item from index: " + index);
        switch (index) {
//            case Config.EVENT_GROUPS_BUDDIES_TAB_INDEX_GROUPS:
//                return new EventGroupsFragment();
//            case Config.EVENT_GROUPS_BUDDIES_TAB_INDEX_BUDDIES:
//                return new EventBuddiesFragment();
            case Config.USER_PROFILE_TAB_INDEX_PROFILE:
                return new UserProfileFragment();
            case Config.USER_PROFILE_TAB_INDEX_GROUPS:
                return new UserGroupsFragment();
            case Config.USER_PROFILE_TAB_INDEX_EVENTS:
                return new UserEventsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return Config.USER_PROFILE_TAB_COUNT;
    }

}