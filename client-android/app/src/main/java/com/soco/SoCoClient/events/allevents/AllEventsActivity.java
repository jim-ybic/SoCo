package com.soco.SoCoClient.events.allevents;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient._ref.Actor;
import com.soco.SoCoClient._ref.MyAdapter;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.dashboard.DashboardTabsAdapter;
import com.soco.SoCoClient.events.CreateEventActivity;
import com.soco.SoCoClient.events.common.EventDetailsActivity;
import com.soco.SoCoClient.events.model.Event;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AllEventsActivity extends ActionBarActivity {

    static final String tag = "AllEventsActivity";

    RecyclerView mRecyclerView;
    SimpleEventCardAdapter simpleEventCardAdapter;
    List<Event> events = new ArrayList<>();

    android.support.v7.app.ActionBar actionBar;
    View actionbarView;

//    Bitmap bitmap;
//    ImageButton user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

       // generateDummyEvents();
        SocoApp socoApp = (SocoApp) getApplicationContext();
        events = socoApp.suggestedEvents;

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        simpleEventCardAdapter = new SimpleEventCardAdapter(this, events);
        mRecyclerView.setAdapter(simpleEventCardAdapter);

        setActionbar();

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
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
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

    private void generateDummyEvents() {
        Log.v(tag, "add 5 dummy events");
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
    }

    public void createevent(View view){
        Log.v(tag, "create event");
        Intent i = new Intent(this, CreateEventActivity.class);
        startActivity(i);
    }
    public void eventdetails(View view){

        Log.v(tag, "check event details");
        Intent i = new Intent(this, EventDetailsActivity.class);
        double id = (double) view.getTag();
        i.putExtra(EventDetailsActivity.EVENT_ID,id);
        startActivity(i);
    }
}
