package com.soco.SoCoClient.view;

//import info.androidhive.tabsswipe.adapter.TabsPagerAdapter;
//import info.androidhive.tabsswipe.R;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.view.ui.tab.TabsPagerAdapter;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class ShowSingleProjectActivity extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Details" , "Updates"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_single_project_new);


        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);

//        actionBar = getActionBar();
        actionBar = getSupportActionBar();
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);

        if(actionBar == null){
            Log.e("TEST", "Cannot get action bar object");
            return;
        }
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        Log.i("TEST", "Get mAdapter");

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        Log.i("TEST", "Adding tabs");
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
                Log.i("TEST", "position is " + position);
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
        Log.i("TEST", "get position: " + tab.getPosition());
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