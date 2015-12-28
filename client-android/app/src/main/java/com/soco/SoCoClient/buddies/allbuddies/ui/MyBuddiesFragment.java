package com.soco.SoCoClient.buddies.allbuddies.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.buddies.service.DownloadMyBuddiesTask;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;

import java.util.ArrayList;


public class MyBuddiesFragment extends Fragment implements View.OnClickListener, TaskCallBack {

    static String tag = "MyBuddiesFragment";

    static final int LOAD_BATCH_SIZE = 20;

    View rootView;
    Context context;
    SocoApp socoApp;
    MyBuddiesListAdapter adapter;
    RecyclerView mRecyclerView;
    ArrayList<MyBuddiesListEntryItem> buddyList = new ArrayList<>();
    private SwipeRefreshLayoutBottom swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        socoApp = (SocoApp) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_my_buddies, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.friends);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        adapter = new MyBuddiesListAdapter(context, buddyList);
        mRecyclerView.setAdapter(adapter);
        startTask();
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

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
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
}
