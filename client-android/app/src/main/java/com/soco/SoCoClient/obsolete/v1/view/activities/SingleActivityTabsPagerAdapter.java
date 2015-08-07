package com.soco.SoCoClient.obsolete.v1.view.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class SingleActivityTabsPagerAdapter extends FragmentPagerAdapter {

    String tag = "SingleProjectTabsPagerAdapter";

    public SingleActivityTabsPagerAdapter(FragmentManager fm) {
        super(fm);
        Log.i(tag, "membersAdapter init");
    }

    @Override
    public Fragment getItem(int index) {
        Log.d(tag, "get item from index " + index);
        switch (index) {
            case 0:
                return new ActivityDetailsFragment();
            case 1:
                return new ActivityUpdatesFragment();
            case 2:
                return new ActivityMembersFragment();
            case 3:
                return new ActivityResourcesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}