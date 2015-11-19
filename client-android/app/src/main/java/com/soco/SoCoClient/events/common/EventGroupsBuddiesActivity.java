package com.soco.SoCoClient.events.common;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.ActionBar.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.service.EventGroupsBuddiesService;
import com.soco.SoCoClient.groups.GroupDetailsActivity;
import com.soco.SoCoClient.onboarding.register.service.RegisterService;
import com.soco.SoCoClient.userprofile.UserProfileActivity;

public class EventGroupsBuddiesActivity extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener{

    String tag = "EventGroupsBuddiesActivity";

    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 10;
    static final int THOUSAND = 1000;

    public static final String EVENT_ID = "event_id";

    private ViewPager viewPager;
    private EventGroupsBuddiesTabsAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

    static final String GROUPS = "Groups";
    static final String BUDDIES = "Buddies";

    SocoApp socoApp;
    ProgressDialog pd;
    long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_groups_buddies);

        socoApp = (SocoApp) getApplicationContext();

        Intent i = getIntent();
        eventId = i.getLongExtra(EVENT_ID, 0);
        Log.d(tag, "get event id: " + eventId);

        //todo: send request to server - event groups and buddies
        Log.v(tag, "show progress dialog");
        pd = ProgressDialog.show(this, "Downloading data", "Please wait...");
        new Thread(new Runnable(){
            public void run(){
                eventGBInBackground();
                eventGBhandler.sendEmptyMessage(0);
            }
        }).start();



    }

    private void eventGBInBackground() {
        Log.v(tag, "start service at back end");
        Intent i = new Intent(this, EventGroupsBuddiesService.class);
//        Event event = socoApp.getCurrentSuggestedEvent();
//        Log.v(tag, "current event id: " + event.getId());
        i.putExtra(EventGroupsBuddiesService.EVENT_ID, eventId);
        startService(i);

        Log.v(tag, "set response flag as false");
        socoApp.eventGroupsBuddiesResponse = false;

        Log.v(tag, "wait and check register status");
        int count = 0;
        while(!socoApp.eventGroupsBuddiesResponse && count < WAIT_ITERATION) {   //wait for 10s
            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                        Log.e(tag, "Error in waiting");
                    }
                }
            }
            count++;
        }
    }

    Handler eventGBhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");

            if(socoApp.eventGroupsBuddiesResponse && socoApp.eventGroupsBuddiesResult){
                Log.d(tag, "eventGB: success");
                Toast.makeText(getApplicationContext(), "Suceess.", Toast.LENGTH_SHORT).show();
                setActionbar();
            }
            else{
                Log.e(tag, "event GB fail, notify user");
                Toast.makeText(getApplicationContext(), "Network error, please try again later.", Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();
        }
    };

    private void setActionbar() {
        Log.v(tag, "customize actionbar");

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

        TextView eventtitle= (TextView) customView.findViewById(R.id.title);
        eventtitle.setText(socoApp.getCurrentSuggestedEvent().getTitle());
        Log.v(tag, "set toolbar event title: " + socoApp.getCurrentSuggestedEvent().getTitle());

//        Log.v(tag, "set actionbar background color");
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));
//        actionBar.setBackgroundDrawable(colorDrawable);

        Log.v(tag, "Adding tabs");
        android.support.v7.app.ActionBar.Tab tabGroups = actionBar.newTab().setText(GROUPS).setTabListener(this);
        actionBar.addTab(tabGroups);
        android.support.v7.app.ActionBar.Tab tabBuddies= actionBar.newTab().setText(BUDDIES).setTabListener(this);
        actionBar.addTab(tabBuddies);

        Log.v(tag, "set starting tab");
        if(socoApp.eventGroupsBuddiesTabIndex == 0)
            actionBar.selectTab(tabGroups);
        else if(socoApp.eventGroupsBuddiesTabIndex == 1)
            actionBar.selectTab(tabBuddies);

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

    public void groupdetails(View view){
        Log.v(tag, "tap on a single group, show details");
        Intent i = new Intent(this, GroupDetailsActivity.class);

        //todo: pass group id as parameter (currently only testing ui)

        startActivity(i);
    }

    public void userprofile(View view){
        Log.v(tag, "tap on single user, show user profile");
        Intent i = new Intent(this, UserProfileActivity.class);

        //todo: pass user id as parameter

        startActivity(i);
    }

}
