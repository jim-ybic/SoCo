package com.soco.SoCoClient.events.common;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.ActionBar.LayoutParams;
import android.widget.PopupWindow;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;

public class EventGroupsBuddiesActivity extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener{

    String tag = "EventGroupsBuddiesActivity";

    private ViewPager viewPager;
    private EventGroupsBuddiesTabsAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

    private String[] tabs = {
            "Groups",
            "Buddies",
//            "Stream",
//            "Messages"
    };

    SocoApp socoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_groups_buddies);

        socoApp = (SocoApp) getApplicationContext();

        Log.v(tag, "set activity title as event title");
        if(socoApp.OFFLINE_MODE)
            setTitle("Sample Event Title");
        else
            setTitle(socoApp.suggestedEvents.get(socoApp.currentEventIndex).getTitle());

        viewPager = (ViewPager) findViewById(R.id.pager);

        actionBar = getSupportActionBar();
        if(actionBar == null){
            Log.e(tag, "Cannot get action bar object");
            return;
        }

        mAdapter = new EventGroupsBuddiesTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        Log.v(tag, "set actionbar custom view");
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowCustomEnabled(true);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_eventgroupsbuddies, null);
        LayoutParams layout = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, layout);
        Toolbar parent = (Toolbar) customView.getParent();
        Log.v(tag, "remove margin in actionbar area");
        parent.setContentInsetsAbsolute(0, 0);

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_event_groups_buddies, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
////        else if (id == R.id.close){
////            finish();
////        }

//        return super.onOptionsItemSelected(item);
//    }

    public void comments(View view){
        Log.v(tag, "tap on event comments");

        //todo
    }

    public void more(View view){
        Log.v(tag, "tap on overlay");

//        PopupWindow window = new PopupWindow()
        //todo
    }

    public void close(View view){
        Log.v(tag, "tap on close");
        finish();
    }

}
