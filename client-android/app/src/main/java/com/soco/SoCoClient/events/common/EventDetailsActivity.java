package com.soco.SoCoClient.events.common;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.events.comments.EventCommentsActivity;
import com.soco.SoCoClient.events.photos.EventPhotosActivity;
import com.soco.SoCoClient.groups.GroupDetailsActivity;

public class EventDetailsActivity extends ActionBarActivity {

    static final String tag = "EventDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == R.id.close) {
            Log.d(tag, "tap menu item: close");
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void eventphotos (View view){
        Log.d(tag, "show all event photos");
        Intent i = new Intent(getApplicationContext(), EventPhotosActivity.class);
        startActivity(i);
    }

    public void eventcomments (View view){
        Log.d(tag, "show all event comments");
        Intent i = new Intent(getApplicationContext(), EventCommentsActivity.class);
        startActivity(i);
    }

    public void eventorganizers (View view){
        Log.d(tag, "show all event organizers");
        Intent i = new Intent(getApplicationContext(), EventOrganizersActivity.class);
        startActivity(i);

    }

    public void eventfriends (View view){
        Log.d(tag, "show all event friends");
        Intent i = new Intent(getApplicationContext(), EventBuddiesActivity.class);
        startActivity(i);

    }

    public void groupdetails (View view){
        Log.v(tag, "tap on group details");
        Intent i = new Intent(this, GroupDetailsActivity.class);
        startActivity(i);
    }
}
