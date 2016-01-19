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
import com.soco.SoCoClient.events.service.EventPostsTask;

import java.util.ArrayList;
import java.util.List;


public class AllPostsFragment extends Fragment
        implements TaskCallBack {

    static String tag = "AllPostsFragment";

    View rootView;
    Context context;
    SocoApp socoApp;
    ProgressDialog pd;
    static int counter=100000;
    SwipeRefreshLayoutBottom swipeContainer;
    RecyclerView mRecyclerView;
    PostCardAdapter adapter;
    List<Post> posts = new ArrayList<>();

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
        rootView = inflater.inflate(R.layout.fragment_all_posts, container, false);

        swipeContainer = (SwipeRefreshLayoutBottom) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                downloadEventPosts();
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        adapter = new PostCardAdapter(getActivity(), posts);
        mRecyclerView.setAdapter(adapter);

        Log.v(tag, "show progress dialog, start downloading event details");
        pd = ProgressDialog.show(getActivity(),
                context.getString(R.string.msg_downloading_posts),
                context.getString(R.string.msg_pls_wait));
        new Thread(new Runnable(){
            public void run(){
                downloadEventPosts();
            }
        }).start();

        return rootView;
    }

    private void downloadEventPosts() {
        if (posts != null && posts.size() > 0) {
            Post lastPost = posts.get(posts.size()-1);
            String[] params = new String[1];
            params[0] = AllPostsTask.POST_ID;
            Log.v(tag, "load more posts from: " + lastPost.getId());
            new AllPostsTask(SocoApp.user_id, SocoApp.token, null, null, params, this).execute(lastPost.getId());
        }
        else {
            Log.v(tag, "first load posts");
            new AllPostsTask(SocoApp.user_id, SocoApp.token, null, null, null, this).execute();
        }
    }

    public void doneTask(Object o) {
        Log.v(tag, "donetask");
        if(o == null)
            Log.e(tag, "all posts task returns null");
        else{
            ArrayList<Post> newPosts = (ArrayList<Post>) o;
            Log.v(tag, newPosts.size() + " posts found");
            for(Post p : newPosts){
                posts.add(p);
                Log.v(tag, "add new post: " + p.toString());
            }
        }
        adapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
        pd.dismiss();
    }



}
