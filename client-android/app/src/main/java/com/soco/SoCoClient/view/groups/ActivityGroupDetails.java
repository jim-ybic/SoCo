package com.soco.SoCoClient.view.groups;

import android.app.ActionBar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.view.DashboardTabsAdapter;

public class ActivityGroupDetails extends ActionBarActivity {

    static String tag = "GroupDetails";

//    private ViewPager viewPager;
//    private DashboardTabsAdapter mAdapter;
//    private android.support.v7.app.ActionBar actionBar;

    // Tab titles
//    private String[] tabs = {
//            "Upcoming Events",
//            "Past Events",
//            "Stream",
//            "Messages"
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

//        viewPager = (ViewPager) findViewById(R.id.pager);
//
//        actionBar = getSupportActionBar();
//        if(actionBar == null){
//            Log.e(tag, "Cannot get action bar object");
//            return;
//        }
//
//        mAdapter = new DashboardTabsAdapter(getSupportFragmentManager());
//
//        viewPager.setAdapter(mAdapter);
//        actionBar.setHomeButtonEnabled(false);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//        Log.d(tag, "Adding tabs");
//        for (String tab_name : tabs) {
//            actionBar.addTab(actionBar.newTab().setText(tab_name)
//                    .setTabListener(this));
//        }

//        Log.v(tag, "Set listener");
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                Log.v(tag, "position is " + position);
//                actionBar.setSelectedNavigationItem(position);
//            }
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_group_details, menu);
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

//    @Override
//    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab,
//                              android.support.v4.app.FragmentTransaction fragmentTransaction) {
//        Log.d(tag, "get position: " + tab.getPosition());
//        viewPager.setCurrentItem(tab.getPosition());
//    }
//
//    @Override
//    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab,
//                                android.support.v4.app.FragmentTransaction fragmentTransaction) {
//
//    }
//
//    @Override
//    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab,
//                                android.support.v4.app.FragmentTransaction fragmentTransaction) {
//
//    }
}
