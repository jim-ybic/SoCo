package com.soco.SoCoClient.buddies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.buddies.allbuddies.ui.MyBuddiesListAdapter;
import com.soco.SoCoClient.buddies.allbuddies.ui.MyBuddiesListEntryItem;
import com.soco.SoCoClient.buddies.service.DownloadMyBuddiesTask;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.userprofile.UserProfileActivity;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class MyBuddiesActivity extends ActionBarActivity
        implements TaskCallBack {

    static String tag = "MyBuddiesActivity";

    static final int LOAD_BATCH_SIZE = 20;

    Context context;
    SocoApp socoApp;
    MyBuddiesListAdapter adapter;
    RecyclerView mRecyclerView;
    ArrayList<MyBuddiesListEntryItem> buddyList = new ArrayList<>();
    private SwipeRefreshLayoutBottom swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buddies);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();
        socoApp = (SocoApp) context;

        mRecyclerView = (RecyclerView) findViewById(R.id.friends);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        adapter = new MyBuddiesListAdapter(context, buddyList);
        mRecyclerView.setAdapter(adapter);
        startTask();
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
    }

    private void startTask() {
        DownloadMyBuddiesTask dmmt = new DownloadMyBuddiesTask(SocoApp.user_id, SocoApp.token, this);
        if (buddyList != null && buddyList.size() > 0) {
            dmmt.execute(buddyList.get(buddyList.size() - 1).getUser_id());
        } else {
            dmmt.execute();
        }
    }

    public void doneTask(Object o) {
        Log.v(tag, "done task: " + o.toString());
        if (o != null && o instanceof ArrayList) {
            ArrayList<MyBuddiesListEntryItem> result = (ArrayList<MyBuddiesListEntryItem>) o;
//            for(MyMatchListEntryItem e:result){
//                matchList.add(e);
//            }
            for (int i = 0; i < LOAD_BATCH_SIZE && i < result.size(); i++) {
                buddyList.add(result.get(i));
            }

//            if(firstTime){
//                adapter = new MyBuddiesListAdapter(getActivity(), buddyList);
//                list = (ListView) rootView.findViewById(R.id.friends);
//                list.setAdapter(adapter);
//                firstTime=false;
//            }else {
            adapter.notifyDataSetChanged();
//            }
        }
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

    public void buddydetails(View view) {
        Log.v(tag, "show buddy details");
        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
        String user_id = (String)view.getTag();
        i.putExtra(User.USER_ID, user_id);
        startActivity(i);
    }
}
