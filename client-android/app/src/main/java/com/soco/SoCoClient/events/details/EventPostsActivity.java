package com.soco.SoCoClient.events.details;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.CreateEventActivity;
import com.soco.SoCoClient.events.allevents.SimpleEventCardAdapter;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.service.EventPostsTask;
import com.soco.SoCoClient.posts.AllPostsTask;
import com.soco.SoCoClient.posts.Post;
import com.soco.SoCoClient.posts.PostCardAdapter;
import com.soco.SoCoClient.userprofile.UserProfileActivity;
import com.soco.SoCoClient.userprofile.model.User;
import com.soco.SoCoClient.userprofile.task.DownloadEventsTask;

import java.util.ArrayList;
import java.util.List;

public class EventPostsActivity extends ActionBarActivity implements TaskCallBack {

    static final String tag = "EventPostsActivity";
    static final String EVENTID = "eventid";

    String eventId;
    Context context;
    ProgressDialog pd;

    SwipeRefreshLayoutBottom swipeContainer;
    RecyclerView mRecyclerView;
    PostCardAdapter adapter;
    ArrayList<Post> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_posts);

        Intent i = getIntent();
        eventId = i.getStringExtra(EVENTID);
        Log.v(tag, "get eventid: " + eventId);

        context = getApplicationContext();

        swipeContainer = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //todo: download more posts
                swipeContainer.setRefreshing(false);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

//        Log.v(tag, "add dummy posts");
//        posts = new ArrayList<>();
//        posts.add(new Post("user1", "user1's comment"));
//        posts.add(new Post("user2", "user2's comment"));
//        posts.add(new Post("user3", "user3's comment"));

        adapter = new PostCardAdapter(this, posts);
        adapter.setIsSourceFromSingleEvent(true);
        mRecyclerView.setAdapter(adapter);

        Log.v(tag, "show progress dialog, start downloading event details");
        pd = ProgressDialog.show(this,
                context.getString(R.string.msg_downloading_event),
                context.getString(R.string.msg_pls_wait));
        new Thread(new Runnable(){
            public void run(){
                downloadEventPosts();
            }
        }).start();
    }

    private void downloadEventPosts(){
//        new EventPostsTask(SocoApp.user_id, SocoApp.token, this).execute(eventId);
        new AllPostsTask(SocoApp.user_id, SocoApp.token, eventId, null, null, this).execute();
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

    public void postoptions(View v){
        View popupView = getLayoutInflater().inflate(R.layout.popup_window_postoptions, null);
        PopupWindow window = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        window.showAsDropDown(v);
        setPostOptionsPopupListeners(window, popupView);
    }

    void setPostOptionsPopupListeners(final PopupWindow window, View view) {

        TextView edit = (TextView) view.findViewById(R.id.popup_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                //todo
            }
        });

        //todo: more options
    }


}
