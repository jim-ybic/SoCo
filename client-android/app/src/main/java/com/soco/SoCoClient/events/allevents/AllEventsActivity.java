package com.soco.SoCoClient.events.allevents;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient._ref.Actor;
import com.soco.SoCoClient._ref.MyAdapter;
import com.soco.SoCoClient.events.CreateEventActivity;
import com.soco.SoCoClient.events.model.Event;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AllEventsActivity extends ActionBarActivity {

    static final String tag = "AllEventsActivity";

    RecyclerView mRecyclerView;
    SimpleEventCardAdapter simpleEventCardAdapter;
    List<Event> events = new ArrayList<>();

    Bitmap bitmap;
    ImageButton user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        generateDummyEvents();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        simpleEventCardAdapter = new SimpleEventCardAdapter(this, events);
        mRecyclerView.setAdapter(simpleEventCardAdapter);

//        user = (ImageButton) findViewById(R.id.user);
//        testFacebookUserProfilePicture(user);
    }

//    private void testFacebookUserProfilePicture(final ImageButton user) {
//        new Thread(){
//            public void run() {
////                super.run();
//                Log.d(tag, "test facebook user profile picture");
//                try {
//                    URL imageUrl = new URL("https://graph.facebook.com/" + "10153298013434285" + "/picture?type=large");
//                    bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
//                    Log.d(tag, "bitmap: " + bitmap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                handler.sendEmptyMessage(0);
//            }
//        }.start();
//    }
//
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            Log.d(tag, "handle message");
//            user.setImageBitmap(bitmap);
//        };
//    };

    private void generateDummyEvents() {
        Log.v(tag, "add 5 dummy events");
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
    }

    public void createevent(View view){
        Log.v(tag, "create event");
        Intent i = new Intent(this, CreateEventActivity.class);
        startActivity(i);
    }

}
