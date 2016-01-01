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
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.CreateEventActivity;
import com.soco.SoCoClient.events.details.EventDetailsActivity;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.userprofile.UserProfileActivity;
import com.soco.SoCoClient.userprofile.model.User;
import com.soco.SoCoClient.userprofile.task.UserEventTask;

import java.util.ArrayList;
import java.util.List;

public class AllEventsActivity extends ActionBarActivity implements TaskCallBack {

    static final String tag = "AllEventsActivity";

    private SwipeRefreshLayoutBottom swipeContainer;

    RecyclerView mRecyclerView;
    SimpleEventCardAdapter simpleEventCardAdapter;
    List<Event> events = new ArrayList<>();
    //    private final int MAX_EVENTS_TOSHOW=50;
    android.support.v7.app.ActionBar actionBar;
    View actionbarView;

    SocoApp socoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        socoApp = (SocoApp) getApplicationContext();

        swipeContainer = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                startTask();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        simpleEventCardAdapter = new SimpleEventCardAdapter(this, events);
        mRecyclerView.setAdapter(simpleEventCardAdapter);
        startTask();
    }

    private void setActionbar() {
        //set background color
//        viewPager.setBackgroundColor(Color.WHITE);

        Log.v(tag, "set custom actionbar");

        actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.e(tag, "Cannot get action bar object");
            return;
        }

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
    private void startTask() {
        if (events != null && events.size() > 0) {
            Event lastEvent = events.get(events.size() - 1);
            String[] params = new String[1];
            params[0] = UserEventTask.START_EVENT_ID;
            UserEventTask uet = new UserEventTask(SocoApp.user_id, SocoApp.token, params, this);
            uet.execute(Long.toString(lastEvent.getId()));
        } else {
            UserEventTask uet = new UserEventTask(SocoApp.user_id, SocoApp.token, this);
            uet.execute();
        }
    }

    public void doneTask(Object o) {
        if (o != null && o instanceof ArrayList) {
            ArrayList<Event> result = (ArrayList<Event>) o;
            for (Event e : result) {
                events.add(e);
            }
//            int currentSize = events.size();
//            if(currentSize>MAX_EVENTS_TOSHOW){
//                int toRemove = currentSize-MAX_EVENTS_TOSHOW;
//                if(toRemove>0){
//                    for(int i=0;i<toRemove;i++){
//                        events.remove(0);
//                    }
//                }
//            }
            Log.v(tag, "done task: ");
            simpleEventCardAdapter.notifyDataSetChanged();
        }
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);

    }

    public void createevent(View view) {
        Log.v(tag, "create event");
        Intent i = new Intent(this, CreateEventActivity.class);
        startActivity(i);
    }

    public void eventdetails(View view) {
        Log.v(tag, "check event details");
        Intent i = new Intent(this, EventDetailsActivity.class);
        Long id = (Long) view.getTag();
        i.putExtra(EventDetailsActivity.EVENT_ID, id);
        startActivity(i);
    }

    public void myevents(View view) {
        Log.v(tag, "tap show my events");
        String myUserid = socoApp.user_id;
        Log.v(tag, "my userid: " + myUserid);

        Intent i = new Intent(this, UserProfileActivity.class);
        i.putExtra(User.USER_ID, myUserid);
        i.putExtra(Config.USER_PROFILE_TAB_INDEX, Config.USER_PROFILE_TAB_INDEX_EVENTS);
        startActivity(i);
    }
}
