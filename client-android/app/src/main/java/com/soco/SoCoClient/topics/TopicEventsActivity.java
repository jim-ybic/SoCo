package com.soco.SoCoClient.topics;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.allevents.SimpleEventCardAdapter;
import com.soco.SoCoClient.events.details.EventDetailsActivity;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.userprofile.task.DownloadEventsTask;

import java.util.ArrayList;
import java.util.List;

public class TopicEventsActivity extends ActionBarActivity
        implements TaskCallBack {

    static final String tag = "TopicEventsFragment";
    public static final String TOPIC_ID = "topic_id";

    SwipeRefreshLayoutBottom swipeContainer;
    RecyclerView mRecyclerView;
    SimpleEventCardAdapter simpleEventCardAdapter;
    List<Event> events = new ArrayList<>();
    //    private final int MAX_EVENTS_TOSHOW=50;
    android.support.v7.app.ActionBar actionBar;
    View actionbarView;

    Context context;
    SocoApp socoApp;
    ProgressDialog pd;

    String topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_events);

        Intent i = getIntent();
        topicId = i.getStringExtra(TOPIC_ID);
        Log.d(tag, "get topicid: " + topicId);

        context = getApplicationContext();
        socoApp = (SocoApp) context;

        swipeContainer = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                downloadEventsInBackgroud();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        simpleEventCardAdapter = new SimpleEventCardAdapter(this, events);
        mRecyclerView.setAdapter(simpleEventCardAdapter);

        Log.v(tag, "show progress dialog, fetch events from server");
        pd = ProgressDialog.show(this,
                context.getString(R.string.msg_downloading_events),
                context.getString(R.string.msg_pls_wait));
        new Thread(new Runnable(){
            public void run(){
                downloadEventsInBackgroud();
            }
        }).start();
    }

    private void downloadEventsInBackgroud() {
        Log.v(tag, "load events for topic: " + topicId);
        new DownloadEventsTask(SocoApp.user_id, SocoApp.token, topicId, this).execute();
    }

    public void doneTask(Object o) {
        if (o != null && o instanceof ArrayList) {
            ArrayList<Event> result = (ArrayList<Event>) o;
            for (Event e : result) {
                events.add(e);
            }
            Log.v(tag, "done task: ");
            simpleEventCardAdapter.notifyDataSetChanged();
        }
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
        pd.dismiss();
    }

    public void eventdetails(View view) {
        Intent i = new Intent(this, EventDetailsActivity.class);
        Long id = (Long) view.getTag();
        Log.v(tag, "check event details: " + id);
        i.putExtra(EventDetailsActivity.EVENT_ID, id);
        startActivity(i);
    }

}
