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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.allevents.SimpleEventCardAdapter;
import com.soco.SoCoClient.events.model.Event;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailsActivity extends ActionBarActivity
        implements TaskCallBack{

    static final String tag = "TopicDetailsActivity";
    public static final String TOPIC_ID = "topic_id";

    Context context;
    SocoApp socoApp;
    ProgressDialog pd;
    Topic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_details);

        Log.v(tag, "oncreate");
        context = getApplicationContext();
        socoApp = (SocoApp) context;

        Log.v(tag, "hide actionbar");
        getSupportActionBar().hide();

        Log.d(tag, "get topic id");
        Intent i = getIntent();
        final String topicId = i.getStringExtra(TOPIC_ID);
        Log.d(tag, "topic id: " + topicId);

        Log.v(tag, "show progress dialog, start downloading details");
        pd = ProgressDialog.show(this,
                context.getString(R.string.msg_downloading_topic),
                context.getString(R.string.msg_pls_wait));
        new Thread(new Runnable(){
            public void run(){
                downloadTopicDetails(topicId);
            }
        }).start();
    }

    private void downloadTopicDetails(String topicId){
        Log.v(tag, "download topic details: " + topicId);
        new TopicDetailsTask(SocoApp.user_id, SocoApp.token, topicId, this).execute();
    }

    public void doneTask(Object o) {
        if(o == null){
            Log.e(tag, "topic details task return null");
            pd.dismiss();
            Toast.makeText(getApplicationContext(), R.string.msg_network_error, Toast.LENGTH_SHORT).show();
            return;
        }
        topic = (Topic) o;
        showDetails(topic);
        pd.dismiss();
        //todo
    }

    private void showDetails(Topic t){
        if(t == null){
            Log.e(tag, "show topic is null");
            return;
        }
        Log.d(tag, "show topic details: " + t.toString());

        ((TextView) findViewById(R.id.title)).setText(t.getTitle());
        ((TextView) findViewById(R.id.intro)).setText(t.getIntroduction());

        Log.v(tag, "set creator name and icon");
        ((TextView) findViewById(R.id.creatorname)).setText(t.getCreator().getUser_name());
        ImageButton ib = (ImageButton) findViewById(R.id.creatoricon);
        IconUrlUtil.setImageForButtonSmall(getResources(), ib, UrlUtil.getUserIconUrl(t.getCreator().getUser_id()));
    }

//    public void eventdetails(View view){
//        //todo
//    }

    public void close(View v){
        finish();
    }

    public void topicevents(View v){
        Log.d(tag, "view topic events");
        Intent i = new Intent(this, TopicEventsActivity.class);
        i.putExtra(TopicEventsActivity.TOPIC_ID, topic.getId());
        startActivity(i);
    }

    public void topicposts(View v){
        Log.d(tag, "view topic posts");
        Intent i = new Intent(this, TopicPostsActivity.class);
        i.putExtra(TopicPostsActivity.TOPIC_ID, topic.getId());
        startActivity(i);
    }

}
