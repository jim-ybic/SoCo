package com.soco.SoCoClient.events._ref;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient._ref.GroupDetailsActivityV1;

@Deprecated
public class EventOrganizersActivity extends ActionBarActivity {

    static String tag = "EventOrganizers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_groups);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fragment_event_groups, menu);
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

    public void groupdetails(View view){
        Log.d(tag, "tap on group details");
        Intent i = new Intent(getApplicationContext(), GroupDetailsActivityV1.class);
        startActivity(i);
    }
}
