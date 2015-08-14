package com.soco.SoCoClient.v2.view.event;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.v2.control.config.DataConfig;
import com.soco.SoCoClient.v2.control.database.DataLoader;
import com.soco.SoCoClient.v2.model.Event;

public class EventDetail extends ActionBarActivity {

    static String tag = "EventDetail";

    DataLoader dataLoader;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_event_details);

        dataLoader = new DataLoader(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int seq = extras.getInt(DataConfig.EXTRA_EVENT_SEQ);
            Log.d(tag, "extra has seq " + seq);
            event = dataLoader.loadEvent(seq);
            Log.d(tag, "loaded event: " + event.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_event_detail, menu);
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
}
