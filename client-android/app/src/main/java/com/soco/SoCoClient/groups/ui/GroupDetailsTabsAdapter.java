package com.soco.SoCoClient.groups.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.userprofile.ui.EventCardListFragment;

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
            case Config.GROUP_DETAILS_TAB_INDEX_UPCOMINGEVENTS: {
                //todo: set flag for showing upcoming events

                return new EventCardListFragment();
            }
            case Config.GROUP_DETAILS_TAB_INDEX_PASTEVENTS: {
                //todo: set flag for showing past events

                return new EventCardListFragment();
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        return Config.GROUP_DETAILS_TAB_COUNT;
    }

}