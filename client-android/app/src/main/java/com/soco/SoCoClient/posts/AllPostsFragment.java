package com.soco.SoCoClient.posts;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.topics.Topic;
import com.soco.SoCoClient.topics.TopicCardAdapter;

import java.util.ArrayList;


public class AllPostsFragment extends Fragment
        implements View.OnClickListener, TaskCallBack {

    static String tag = "AllPostsFragment";

    View rootView;
    Context context;
    SocoApp socoApp;
    ProgressDialog pd;

    SwipeRefreshLayoutBottom swipeContainer;
    RecyclerView mRecyclerView;
    PostCardAdapter adapter;
    ArrayList<Post> posts = new ArrayList<>();

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

                Log.v(tag, "add dummy posts");
                posts.add(new Post("user1", "user1's comment"));
                posts.add(new Post("user2", "user2's comment"));
                posts.add(new Post("user3", "user3's comment"));
                adapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        Log.v(tag, "add dummy posts");
        posts = new ArrayList<>();
        posts.add(new Post("user1", "user1's comment"));
        posts.add(new Post("user2", "user2's comment"));
        posts.add(new Post("user3", "user3's comment"));

        adapter = new PostCardAdapter(getActivity(), posts);
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
