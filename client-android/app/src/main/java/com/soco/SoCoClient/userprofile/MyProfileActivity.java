package com.soco.SoCoClient.userprofile;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.events.CreateEventActivity;
import com.soco.SoCoClient.groups.CreateGroupActivity;

@Deprecated
public class MyProfileActivity extends ActionBarActivity {

    static final String tag = "MyProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createEvent(View view){
        Log.v(tag, "create event");

        Intent i = new Intent(this, CreateEventActivity.class);
        startActivity(i);
    }

    public void createGroup(View view){
        Log.v(tag, "create group");

        Intent i = new Intent(this, CreateGroupActivity.class);
        startActivity(i);
    }


}
