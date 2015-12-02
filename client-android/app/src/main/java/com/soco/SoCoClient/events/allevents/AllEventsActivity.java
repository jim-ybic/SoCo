package com.soco.SoCoClient.events.allevents;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.CreateEventActivity;
import com.soco.SoCoClient.events.common.EventDetailsActivity;
import com.soco.SoCoClient.events.common.EventGroupsBuddiesActivity;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.userprofile.UserProfileActivity;
import com.soco.SoCoClient.userprofile.model.User;
import com.soco.SoCoClient.userprofile.task.UserEventTask;

import java.util.ArrayList;
import java.util.List;

public class AllEventsActivity extends ActionBarActivity implements TaskCallBack {

    static final String tag = "AllEventsActivity";

    RecyclerView mRecyclerView;
    SimpleEventCardAdapter simpleEventCardAdapter;
    List<Event> events = new ArrayList<>();

    android.support.v7.app.ActionBar actionBar;
    View actionbarView;

    SocoApp socoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        socoApp = (SocoApp) getApplicationContext();
//        events = socoApp.suggestedEvents;
//        if(events == null) {
//            Log.e(tag, "suggested events is not available");
//            Toast.makeText(getApplicationContext(), "Suggested events is not available.", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        else
//            Log.v(tag, events.size() + " events loaded");

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        simpleEventCardAdapter = new SimpleEventCardAdapter(this, events);
        mRecyclerView.setAdapter(simpleEventCardAdapter);


        UserEventTask uet = new UserEventTask(SocoApp.user_id,SocoApp.token,this);
        uet.execute();
//        setActionbar();

//        user = (ImageButton) findViewById(R.id.user);
//        testFacebookUserProfilePicture(user);
    }

    private void setActionbar() {
        //set background color
//        viewPager.setBackgroundColor(Color.WHITE);

        Log.v(tag, "set custom actionbar");

        actionBar = getSupportActionBar();
        if(actionBar == null){
            Log.e(tag, "Cannot get action bar object");
            return;
        }

//        mAdapter = new DashboardTabsAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(mAdapter);

        Log.v(tag, "set actionbar custom view");
        actionBar.setHomeButtonEnabled(false);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);  //comment out due to no tabs
        actionBar.setDisplayShowCustomEnabled(true);
        actionbarView = getLayoutInflater().inflate(R.layout.actionbar_allevents, null);

        android.support.v7.app.ActionBar.LayoutParams layout = new android.support.v7.app.ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionbarView, layout);
        Toolbar parent = (Toolbar) actionbarView.getParent();
        Log.v(tag, "remove margin in actionbar area");
        parent.setContentInsetsAbsolute(0, 0);

        Log.v(tag, "set actionbar background color");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

//    private void testFacebookUserProfilePicture(final ImageButton user) {
//        new Thread(){
//            public void run() {
////                super.run();
//                Log.d(tag, "test facebook user profile picture");
//                try {
//                    URL imageUrl = new URL("https://graph.facebook.com/" + "10153298013434285" + "/picture?type=large");
//                    bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
//                    Log.d(tag, "bitmap: " + bitmap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                handler.sendEmptyMessage(0);
//            }
//        }.start();
//    }
//
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            Log.d(tag, "handle message");
//            user.setImageBitmap(bitmap);
//        };
//    };
    public void doneTask(Object o){
        if(o==null){
            return;
        }
        events = (ArrayList<Event>) o;
        Log.v(tag, "done task: ");
        simpleEventCardAdapter = new SimpleEventCardAdapter(this, events);
        mRecyclerView.setAdapter(simpleEventCardAdapter);
    }
    public void createevent(View view){
        Log.v(tag, "create event");
        Intent i = new Intent(this, CreateEventActivity.class);
        startActivity(i);
    }

    public void eventdetails(View view){
        Log.v(tag, "check event details");
        Intent i = new Intent(this, EventDetailsActivity.class);
        Long id = (Long) view.getTag();
        i.putExtra(EventDetailsActivity.EVENT_ID,id);
        startActivity(i);
    }

    public void eventgroups(View view){
        Log.v(tag, "show all event groups");
        socoApp.eventGroupsBuddiesTabIndex = 0;
//        Intent i = new Intent(getApplicationContext(), EventOrganizersActivity.class);
        Intent i = new Intent(getApplicationContext(), EventGroupsBuddiesActivity.class);
        startActivity(i);

        //todo: passing event id to the new activity to get event details

    }

    public void eventbuddies(View view){
        Log.v(tag, "show all event buddies");
        socoApp.eventGroupsBuddiesTabIndex = 1;
        Intent i = new Intent(getApplicationContext(), EventGroupsBuddiesActivity.class);

        //todo: passing event id to the new activity to get event details

        startActivity(i);
    }

    public void myevents(View view){
        Log.v(tag, "tap show my events");
        String myUserid = socoApp.user_id;
        Log.v(tag, "my userid: " + myUserid);

        Intent i = new Intent(this, UserProfileActivity.class);
        i.putExtra(User.USER_ID, myUserid);
        i.putExtra(Config.USER_PROFILE_TAB_INDEX, Config.USER_PROFILE_TAB_INDEX_EVENTS);
        startActivity(i);
    }
}
