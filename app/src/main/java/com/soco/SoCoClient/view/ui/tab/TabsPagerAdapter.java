package com.soco.SoCoClient.view.ui.tab;

//import info.androidhive.tabsswipe.GamesFragment;
//import info.androidhive.tabsswipe.MoviesFragment;
//import info.androidhive.tabsswipe.TopRatedFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    String tag = "TabsPagerAdapter";

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        Log.i(tag, "adapter init");
//        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        Log.i(tag, "get item from index " + index);
        switch (index) {
//            case 0:
                // Top Rated fragment activity
//                return new TopRatedFragment();
            case 0:
//                Games fragment activity
//                return new GamesFragment();
                return new ProjectDetailsFragment();
            case 1:
//                Movies fragment activity
//                return new MoviesFragment();
                return new ProjectUpdatesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}