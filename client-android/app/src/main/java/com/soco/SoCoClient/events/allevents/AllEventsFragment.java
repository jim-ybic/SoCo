package com.soco.SoCoClient.events.allevents;

import android.support.v4.app.Fragment;
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
import android.view.LayoutInflater;
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

public class AllEventsFragment extends Fragment
        implements TaskCallBack {

    static final String tag = "AllEventsActivity";

    private SwipeRefreshLayoutBottom swipeContainer;

    RecyclerView mRecyclerView;
    SimpleEventCardAdapter simpleEventCardAdapter;
    List<Event> events = new ArrayList<>();
    //    private final int MAX_EVENTS_TOSHOW=50;
    android.support.v7.app.ActionBar actionBar;
    View actionbarView;

    SocoApp socoApp;
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_all_events);

        socoApp = (SocoApp) getActivity().getApplicationContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(tag, "create fragment view.....");
        rootView = inflater.inflate(R.layout.activity_all_events, container, false);

        swipeContainer = (SwipeRefreshLayoutBottom) rootView.findViewById(R.id.swipeContainer);
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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        simpleEventCardAdapter = new SimpleEventCardAdapter(getActivity(), events);
        mRecyclerView.setAdapter(simpleEventCardAdapter);
        startTask();

        return rootView;
    }

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
        Intent i = new Intent(getActivity(), CreateEventActivity.class);
        startActivity(i);
    }

    public void eventdetails(View view) {
        Log.v(tag, "check event details");
        Intent i = new Intent(getActivity(), EventDetailsActivity.class);
        Long id = (Long) view.getTag();
        i.putExtra(EventDetailsActivity.EVENT_ID, id);
        startActivity(i);
    }

    public void myevents(View view) {
        Log.v(tag, "tap show my events");
        String myUserid = socoApp.user_id;
        Log.v(tag, "my userid: " + myUserid);

        Intent i = new Intent(getActivity(), UserProfileActivity.class);
        i.putExtra(User.USER_ID, myUserid);
        i.putExtra(Config.USER_PROFILE_TAB_INDEX, Config.USER_PROFILE_TAB_INDEX_EVENTS);
        startActivity(i);
    }
}
