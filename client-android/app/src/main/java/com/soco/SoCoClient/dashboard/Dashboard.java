package com.soco.SoCoClient.dashboard;

//import info.androidhive.tabsswipe.contactsAdapter.TabsPagerAdapter;
//import info.androidhive.tabsswipe.R;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.events.allevents.AllEventsActivity;
import com.soco.SoCoClient.events.comments.EventCommentsActivity;
import com.soco.SoCoClient.events.common.EventBuddiesActivity;
import com.soco.SoCoClient.events.common.EventDetailsActivity;
import com.soco.SoCoClient.events.common.EventOrganizersActivity;
import com.soco.SoCoClient.events.photos.EventPhotosActivity;
import com.soco.SoCoClient.friends.common.CommonFriendsActivity;
import com.soco.SoCoClient.friends.common.CommonGroupsActivity;
import com.soco.SoCoClient.userprofile.UserEventsActivity;

public class Dashboard extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener {

    String tag = "Dashboard";

    private ViewPager viewPager;
    private DashboardTabsAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

    // Tab titles
    private String[] tabs = {
            "Events",
            "Buddies",
//            "Stream",
//            "Messages"
    };

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
//        setSupportActionBar(toolbar);

        //use custom toolbar
//        toolbar = (Toolbar) findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);

        //set background color
//        viewPager.setBackgroundColor(Color.WHITE);

        actionBar = getSupportActionBar();
        if(actionBar == null){
            Log.e(tag, "Cannot get action bar object");
            return;
        }

        mAdapter = new DashboardTabsAdapter(getSupportFragmentManager());

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

    public void eventphotos (View view){
        Log.d(tag, "show all event photos");
        Intent i = new Intent(getApplicationContext(), EventPhotosActivity.class);
        startActivity(i);
    }

    public void eventcomments (View view){
        Log.d(tag, "show all event comments");
        Intent i = new Intent(getApplicationContext(), EventCommentsActivity.class);
        startActivity(i);
    }

    public void eventorganizers (View view){
        Log.d(tag, "show all event organizers");
        Intent i = new Intent(getApplicationContext(), EventOrganizersActivity.class);
        startActivity(i);

    }

    public void eventfriends (View view){
        Log.d(tag, "show all event friends");
        Intent i = new Intent(getApplicationContext(), EventBuddiesActivity.class);
        startActivity(i);

    }

    public void commongroups (View view){
        Log.d(tag, "show all common groups");
        Intent i = new Intent(getApplicationContext(), CommonGroupsActivity.class);
        startActivity(i);
    }

    public void commonfriends (View view){
        Log.d(tag, "show all common friends");
        Intent i = new Intent(getApplicationContext(), CommonFriendsActivity.class);
        startActivity(i);
    }

    public void personevents (View view){
        Log.d(tag, "show the person's upcoming events");
        Intent i = new Intent(getApplicationContext(), UserEventsActivity.class);
        startActivity(i);
    }

    public void eventdetails (View view){
        Log.d(tag, "event detail");
        Intent i = new Intent(getApplicationContext(), EventDetailsActivity.class);
        startActivity(i);
    }

    public void allevents(View view){
        Log.v(tag, "show all events");
        Intent i = new Intent(getApplicationContext(), AllEventsActivity.class);
        startActivity(i);
    }

}