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
import com.soco.SoCoClient.posts.Post;

import java.util.ArrayList;


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
    TextView newTopics, hotTopics, specialTopics;


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

//                Log.v(tag, "add dummy data");
//                Topic p1 = new Topic(); p1.setTitle("sample topic #1");
//                Topic p2 = new Topic(); p2.setTitle("sample topic #2");
//                Topic p3 = new Topic(); p3.setTitle("sample topic #3");
//                topics.add(p1);
//                topics.add(p2);
//                topics.add(p3);

                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

//        topics = new ArrayList<>();
//        Log.v(tag, "add dummy data");
//        Topic p1 = new Topic(); p1.setTitle("sample topic #1");
//        Topic p2 = new Topic(); p2.setTitle("sample topic #2");
//        Topic p3 = new Topic(); p3.setTitle("sample topic #3");
//        topics.add(p1);
//        topics.add(p2);
//        topics.add(p3);

        Log.d(tag, "download all topics task");
        new AllTopicsTask(SocoApp.user_id, SocoApp.token, this).execute();

        adapter = new TopicCardAdapter(getActivity(), topics);
        mRecyclerView.setAdapter(adapter);

        setOnclickListener();
        return rootView;
    }


    public void doneTask(Object o) {
        Log.v(tag, "donetask");
        if(o == null)
            Log.e(tag, "all topics task returns null");
        else{
            ArrayList<Topic> newTopics = (ArrayList<Topic>) o;
            Log.v(tag, newTopics.size() + " topics found");
            for(Topic p : newTopics){
                topics.add(p);
                Log.v(tag, "add new topic: " + p.toString());
            }
        }
        adapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }


    private void setOnclickListener(){
        newTopics = (TextView) rootView.findViewById(R.id.newEvents);
        newTopics.setTypeface(null, Typeface.BOLD); //default
        newTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(tag, "tap new");
                highlightCategory(getString(R.string.event_category_new));
                //todo: load required items
            }
        });

        hotTopics = (TextView) rootView.findViewById(R.id.hotEvents);
        hotTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(tag, "tap hot");
                highlightCategory(getString(R.string.event_category_hot));
                //todo: load required items
            }
        });

        specialTopics = (TextView) rootView.findViewById(R.id.specialEvents);
        specialTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(tag, "tap special");
                highlightCategory(getString(R.string.event_category_special));
                //todo: load required items
            }
        });
    }


    private void highlightCategory(String cat){
        if(cat.equals(getString(R.string.event_category_new))){
            newTopics.setTypeface(null, Typeface.BOLD);
            hotTopics.setTypeface(null, Typeface.NORMAL);
            specialTopics.setTypeface(null, Typeface.NORMAL);
        }
        else if(cat.equals(getString(R.string.event_category_hot))){
            newTopics.setTypeface(null, Typeface.NORMAL);
            hotTopics.setTypeface(null, Typeface.BOLD);
            specialTopics.setTypeface(null, Typeface.NORMAL);
        }
        else if(cat.equals(getString(R.string.event_category_special))){
            newTopics.setTypeface(null, Typeface.NORMAL);
            hotTopics.setTypeface(null, Typeface.NORMAL);
            specialTopics.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}
