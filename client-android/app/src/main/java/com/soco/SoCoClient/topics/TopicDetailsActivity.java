package com.soco.SoCoClient.topics;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.allevents.SimpleEventCardAdapter;
import com.soco.SoCoClient.events.model.Event;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailsActivity extends ActionBarActivity
        implements TaskCallBack{

    static final String tag = "TopicDetailsActivity";
    public static final String TOPIC_ID = "topic_id";

    SwipeRefreshLayoutBottom swipeContainerEvents;
    RecyclerView mRecyclerViewEvents;
    SimpleEventCardAdapter simpleEventCardAdapter;
    List<Event> events = new ArrayList<>();

    Context context;
    SocoApp socoApp;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_details);

        Log.v(tag, "oncreate");
        context = getApplicationContext();
        socoApp = (SocoApp) context;

        swipeContainerEvents = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeContainer);
        if(swipeContainerEvents == null){
            Log.e(tag, "cannot find view");
            return;
        }
        // Setup refresh listener which triggers new data loading
        swipeContainerEvents.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            }
        });
        mRecyclerViewEvents = (RecyclerView) findViewById(R.id.list);
        if(mRecyclerViewEvents == null){
            Log.e(tag, "cannot find view");
            return;
        }
        mRecyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewEvents.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewEvents.setHasFixedSize(true);

        Log.v(tag, "adding sample events");
        Event e1 = new Event();
        e1.setTitle("sample event #1");
        events.add(e1);
        Event e2 = new Event();
        e2.setTitle("sample event #2");
        events.add(e2);
        simpleEventCardAdapter = new SimpleEventCardAdapter(this, events);

        mRecyclerViewEvents.setAdapter(simpleEventCardAdapter);
    }

    public void doneTask(Object o) {
        //todo
    }


}
