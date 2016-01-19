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

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.details.AddPostActivity;
import com.soco.SoCoClient.posts.AllPostsTask;
import com.soco.SoCoClient.posts.Post;
import com.soco.SoCoClient.posts.PostCardAdapter;

import java.util.ArrayList;
import java.util.List;

public class TopicPostsActivity extends ActionBarActivity
        implements TaskCallBack {

    static String tag = "TopicPostsFragment";
    public static String TOPIC_ID = "topic_id";

    Context context;
    SocoApp socoApp;
    ProgressDialog pd;
    static int counter=100000;

    SwipeRefreshLayoutBottom swipeContainer;
    RecyclerView mRecyclerView;
    PostCardAdapter adapter;
    List<Post> posts = new ArrayList<>();

    String topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_posts);

        Intent i = getIntent();
        topicId = i.getStringExtra(TOPIC_ID);
        Log.d(tag, "topicid: " + topicId);

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
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);


        adapter = new PostCardAdapter(this, posts);
        mRecyclerView.setAdapter(adapter);

        Log.v(tag, "show progress dialog, start downloading event details");
        pd = ProgressDialog.show(this,
                context.getString(R.string.msg_downloading_posts),
                context.getString(R.string.msg_pls_wait));
        new Thread(new Runnable(){
            public void run(){
                downloadTopicPosts();
            }
        }).start();
    }

    private void downloadTopicPosts(){
        new AllPostsTask(SocoApp.user_id, SocoApp.token, null, topicId, this).execute();
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

    public void post(View v){
        Log.v(tag, "tap post");
        Intent i = new Intent(this, AddPostActivity.class);
        i.putExtra(AddPostActivity.TOPIC_ID, String.valueOf(topicId));
        startActivity(i);
    }


}
