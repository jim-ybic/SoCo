package com.soco.SoCoClient.events._ref;

//import info.androidhive.tabsswipe.membersAdapter.TabsPagerAdapter;
//import info.androidhive.tabsswipe.R;
import com.soco.SoCoClient.R;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

@Deprecated
public class SingleActivityActivity extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener {

    String tag = "ShowSingleProjectActivity";

    private ViewPager viewPager;
    private SingleActivityTabsPagerAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

//     Tab titles
    private String[] tabs = {"Details" , "Updates", "Members", "Resources"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_single_activity);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);

//        actionBar = getActionBar();
        actionBar = getSupportActionBar();
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);

        if(actionBar == null){
            Log.e(tag, "Cannot get action bar object");
            return;
        }
        mAdapter = new SingleActivityTabsPagerAdapter(getSupportFragmentManager());
        Log.i(tag, "Get mAdapter");

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        Log.d(tag, "Adding tabs");
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        Log.i("main", "Set listener");
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                Log.d(tag, "position is " + position);
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab,
                              android.support.v4.app.FragmentTransaction fragmentTransaction) {
        Log.d(tag, "get position: " + tab.getPosition());
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab,
                                android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab,
                                android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }
}