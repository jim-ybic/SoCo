package com.soco.SoCoClient.topics;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.allevents.SimpleEventCardAdapter;
import com.soco.SoCoClient.events.model.Event;

import java.util.ArrayList;
import java.util.List;


public class AllTopicsFragment extends Fragment
        implements View.OnClickListener, TaskCallBack {

    static String tag = "AllTopicsFragment";

    View rootView;
    Context context;
    SocoApp socoApp;
    ProgressDialog pd;

    SwipeRefreshLayoutBottom swipeContainer;
    RecyclerView mRecyclerView;
    TopicCardAdapter adapter;
    ArrayList<Topic> topics = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        socoApp = (SocoApp) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(tag, "on create view");
        rootView = inflater.inflate(R.layout.fragment_all_topics, container, false);

        swipeContainer = (SwipeRefreshLayoutBottom) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        topics = new ArrayList<>();
        topics.add(new Topic("sample topic #1"));
        topics.add(new Topic("sample topic #2"));
        topics.add(new Topic("sample topic #3"));

        adapter = new TopicCardAdapter(getActivity(), topics);
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }


    public void doneTask(Object o) {
        //todo
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}
