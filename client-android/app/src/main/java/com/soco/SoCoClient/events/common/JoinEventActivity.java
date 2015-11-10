package com.soco.SoCoClient.events.common;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events.model.Event;

public class JoinEventActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);
        Intent intent = getIntent();
        SocoApp socoApp = (SocoApp) getApplicationContext();
        double event_id = intent.getDoubleExtra(Event.EVENT_ID,Double.NaN);
        Event event;
        if(Double.isNaN(event_id)){
            event = socoApp.getCurrentSuggestedEvent();
        }else{
            event = socoApp.suggestedEventsMap.get(event_id);
        }

        setViewFromEvent(event, socoApp.loginEmail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_join_event, menu);
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
    private void setViewFromEvent(Event event,String email) {

        ((TextView) this.findViewById(R.id.address)).setText(event.getAddress());
        ((TextView) this.findViewById(R.id.textTitle)).setText(event.getTitle());
        ((TextView) this.findViewById(R.id.edit_areacode)).setText("+852");
        //date time
        if (!StringUtil.isEmptyString(event.getStart_date())) {
            ((TextView) this.findViewById(R.id.textStartDate)).setText(TimeUtil.getTextDate(event.getStart_date(), "dd-MMM"));
            ((TextView) this.findViewById(R.id.textStartDayOfWeek)).setText(TimeUtil.getDayOfStartDate(event.getStart_date()));
        }
        if (!StringUtil.isEmptyString(event.getStart_time()) || StringUtil.isEmptyString(event.getEnd_time())) {
            ((TextView) this.findViewById(R.id.textStartEndTime)).setText(TimeUtil.getTextStartEndTime(event));
        }
        if (!StringUtil.isEmptyString(email)) {
            ((TextView) this.findViewById(R.id.edit_email)).setText(email);
        }
    }


    }
