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

                Log.v(tag, "add dummy posts");
                counter++;
                posts.add(new Post("user" + Integer.toString(counter), "user's comment"));
                counter++;
                posts.add(new Post("user"+Integer.toString(counter), "user's comment"));
                counter++;
                posts.add(new Post("user"+Integer.toString(counter), "user's comment"));
                counter++;
                posts.add(new Post("user"+Integer.toString(counter), "user's comment"));
                adapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

//        Log.v(tag, "add dummy posts");
//        posts = new ArrayList<>();
//        posts.add(new Post("user1", "user1's comment"));
//        posts.add(new Post("user2", "user2's comment"));
//        posts.add(new Post("user3", "user3's comment"));

        adapter = new PostCardAdapter(getActivity(), posts);
        mRecyclerView.setAdapter(adapter);

        Log.v(tag, "show progress dialog, start downloading event details");
        pd = ProgressDialog.show(getActivity(),
                context.getString(R.string.msg_downloading_event),
                context.getString(R.string.msg_pls_wait));
        new Thread(new Runnable(){
            public void run(){
                downloadEventPosts();
            }
        }).start();

        //todo: test below
//        Log.v(tag, "test photomanager");
//        PhotoManager manager = new PhotoManager();
//        manager.getBitmap("http://54.254.147.226:80/v1/image?image_path=images/events/2000101449419180409/image/8792531452149707670.jpg");


        return rootView;
    }

    private void downloadEventPosts(){
        String testeventid = "2000101449419180409";
        new EventPostsTask(SocoApp.user_id, SocoApp.token, this).execute(testeventid);  //todo
    }

    public void doneTask(Object o) {
        Log.v(tag, "donetask");
        if(o == null)
            Log.e(tag, "event posts task returns null");
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
