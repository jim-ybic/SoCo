package com.soco.SoCoClient.groups.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.userprofile.ui.EventCardListFragment;

public class GroupDetailsTabsAdapter extends FragmentPagerAdapter {

    String tag = "GroupDetailsTabsAdapter";
    String groupId;
    public GroupDetailsTabsAdapter(FragmentManager fm, String g) {
        super(fm);
        this.groupId=g;
        Log.v(tag, "GroupDetailsTabsAdapter init");
    }

    @Override
    public Fragment getItem(int index) {
        Log.v(tag, "get item from index: " + index);
        switch (index) {
            case Config.GROUP_DETAILS_TAB_INDEX_UPCOMINGEVENTS: {
                Fragment f1 = new EventCardListFragment();
                Bundle b1 = new Bundle();
                b1.putInt(EventCardListFragment.TRIGGER_FROM,EventCardListFragment.GROUP_UPCOMINGEVENT);
                b1.putString(EventCardListFragment.GROUP_ID, groupId);
                f1.setArguments(b1);
                 return f1;
            }
            case Config.GROUP_DETAILS_TAB_INDEX_PASTEVENTS: {
                Fragment f2 = new EventCardListFragment();
                Bundle b2 = new Bundle();
                b2.putInt(EventCardListFragment.TRIGGER_FROM,EventCardListFragment.GROUP_PASTEVENT);
                b2.putString(EventCardListFragment.GROUP_ID,groupId);
                f2.setArguments(b2);
                return f2;
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        return Config.GROUP_DETAILS_TAB_COUNT;
    }

}