package com.soco.SoCoClient.events.common;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.dashboard.DashboardTabsAdapter;

public class EventGroupsBuddiesActivity extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener{

    String tag = "EventGroupsBuddiesActivity";

    private ViewPager viewPager;
    private EventGroupsBuddiesTabsAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

    private String[] tabs = {
            "Events",
            "Buddies",
//            "Stream",
//            "Messages"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_groups_buddies);

        viewPager = (ViewPager) findViewById(R.id.pager);

        actionBar = getSupportActionBar();
        if(actionBar == null){
            Log.e(tag, "Cannot get action bar object");
            return;
        }

        mAdapter = new EventGroupsBuddiesTabsAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Log.v(tag, "set actionbar background color");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));
        actionBar.setBackgroundDrawable(colorDrawable);

        Log.v(tag, "Adding tabs");
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

//        Log.v(tag, "set action bar tab background color");
//        actionBar.setStackedBackgroundDrawable(colorDrawable);


        Log.v(tag, "Set listener");
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.v(tag, "position is " + position);
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
        Log.v(tag, "get position: " + tab.getPosition());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_groups_buddies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
