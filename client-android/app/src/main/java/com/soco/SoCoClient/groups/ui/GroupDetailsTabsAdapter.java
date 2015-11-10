package com.soco.SoCoClient.groups.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.userprofile.ui.UserEventsFragment;
import com.soco.SoCoClient.userprofile.ui.UserGroupsFragment;
import com.soco.SoCoClient.userprofile.ui.UserProfileFragment;

public class GroupDetailsTabsAdapter extends FragmentPagerAdapter {

    String tag = "GroupDetailsTabsAdapter";

    public GroupDetailsTabsAdapter(FragmentManager fm) {
        super(fm);
        Log.v(tag, "GroupDetailsTabsAdapter init");
    }

    @Override
    public Fragment getItem(int index) {
        Log.v(tag, "get item from index: " + index);
        switch (index) {
//            case Config.EVENT_GROUPS_BUDDIES_TAB_INDEX_GROUPS:
//                return new EventGroupsFragment();
//            case Config.EVENT_GROUPS_BUDDIES_TAB_INDEX_BUDDIES:
//                return new EventBuddiesFragment();

            //todo: create fragment classes for upcoming events and past events

            case Config.GROUP_DETAILS_TAB_INDEX_UPCOMINGEVENTS:
                return new UserEventsFragment();
            case Config.GROUP_DETAILS_TAB_INDEX_PASTEVENTS:
                return new UserEventsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return Config.GROUP_DETAILS_TAB_COUNT;
    }

}