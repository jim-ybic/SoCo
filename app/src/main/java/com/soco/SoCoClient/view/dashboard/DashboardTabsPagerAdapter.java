package com.soco.SoCoClient.view.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class DashboardTabsPagerAdapter extends FragmentPagerAdapter {

    String tag = "DashboardTabsPagerAdapter";

    public DashboardTabsPagerAdapter(FragmentManager fm) {
        super(fm);
        Log.i(tag, "contactsAdapter init");
    }

    @Override
    public Fragment getItem(int index) {
        Log.d(tag, "get item from index " + index);
        switch (index) {
            case 0:
                return new DashboardActivitiesFragment();
            case 1:
                return new DashboardContactsFragment();
            case 2:
                return new DashboardCalendarFragment();
            case 3:
                return new DashboardCalendarFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}