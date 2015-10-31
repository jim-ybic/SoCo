package com.soco.SoCoClient.events.allevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.events.CreateEventActivity;

public class AllEventsActivity extends ActionBarActivity {

    static final String tag = "AllEventsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);
    }

    public void createevent(View view){
        Log.v(tag, "create event");
        Intent i = new Intent(this, CreateEventActivity.class);
        startActivity(i);
    }

}
