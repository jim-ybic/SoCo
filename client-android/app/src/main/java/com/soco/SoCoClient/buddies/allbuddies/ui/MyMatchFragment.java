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
import com.soco.SoCoClient.buddies.service.DownloadMyMatchTask;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;

import java.util.ArrayList;


public class MyMatchFragment extends Fragment implements View.OnClickListener,TaskCallBack {

    static String tag = "MyMatchFragment";

    static final int LOAD_BATCH_SIZE = 10;

    View rootView;
    Context context;
    SocoApp socoApp;
    MyMatchListAdapter  adapter;
    RecyclerView mRecyclerView;
    ArrayList<MyMatchListEntryItem> matchList = new ArrayList<>();
    private SwipeRefreshLayoutBottom swipeContainer;
    private int start_index=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();
        socoApp = (SocoApp)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_match, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.friends);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        adapter = new MyMatchListAdapter(context, matchList);
        mRecyclerView.setAdapter(adapter);

        startTask();
        swipeContainer = (SwipeRefreshLayoutBottom) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

    private void startTask(){
        DownloadMyMatchTask dmmt = new DownloadMyMatchTask(SocoApp.user_id,SocoApp.token,this);
        if(start_index!=0){
            dmmt.execute(Integer.toString(start_index));
        }else{
            dmmt.execute();
        }
    }
    public void doneTask(Object o){
        if(o!=null && o instanceof ArrayList) {
            ArrayList<MyMatchListEntryItem> result = (ArrayList<MyMatchListEntryItem>) o;
//todo temporary for testing, only show 2 at one time. later can use the commented method
//            for(MyMatchListEntryItem e:result){
//                matchList.add(e);
//            }
            for(int i=0; i<LOAD_BATCH_SIZE && i<result.size(); i++){
                matchList.add(result.get(i));
            }
            if(matchList!=null) {
                start_index = matchList.size();
            }
            Log.v(tag, "done task: ");
            adapter.notifyDataSetChanged();
        }
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }
}
