package com.soco.SoCoClient.events.common;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events._ref.EventBuddiesActivity;
import com.soco.SoCoClient.events._ref.EventOrganizersActivity;
import com.soco.SoCoClient.events.comments.EventCommentsActivity;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.photos.EventPhotosActivity;
import com.soco.SoCoClient.groups.GroupDetailsActivity;

public class EventDetailsActivity extends ActionBarActivity {

    static final String tag = "EventDetailsActivity";
    private long Current_Event_Id = 0;
    private SocoApp socoApp;
    public static final String EVENT_ID = "event_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent i = getIntent();

        socoApp = (SocoApp) getApplicationContext();
        Event event;
        long eventId = i.getLongExtra(EVENT_ID, 0);

        //if id has been passed from the intent, then get the event id to better locate the event.
        //else, taking from suggestedEvents (from the list, can only checking the pos, might need to further change)
        if(eventId!=0 && socoApp.suggestedEventsMap!=null && socoApp.suggestedEventsMap.containsKey(eventId)){
            event = socoApp.suggestedEventsMap.get(eventId);
        }else {
            event = socoApp.getCurrentSuggestedEvent();
        }
        Current_Event_Id = event.getId();
        showDetails(event);
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

        //todo: pass group id as parameters

        startActivity(i);
    }

    private void showDetails(Event event){
        Log.v(tag, "show event details: " + event.toString());

//        ((TextView)this.findViewById(R.id.textNoOfViews)).setText(Integer.toString(event.getNumber_of_views()));
//        ((TextView)this.findViewById(R.id.textNoOfLikes)).setText(Integer.toString(event.getNumber_of_likes()));
//        ((TextView)this.findViewById(R.id.textNoOfComments)).setText(Integer.toString(event.getNumber_of_comments()));

        Log.v(tag, "set address: " + event.getAddress());
        ((TextView)this.findViewById(R.id.address)).setText(event.getAddress());

        Log.v(tag, "set intro: " + event.getIntroduction());
        ((TextView)this.findViewById(R.id.textIntroduction)).setText(event.getIntroduction());

        Log.v(tag, "set title: " + event.getTitle());
        ((TextView)this.findViewById(R.id.textTitle)).setText(event.getTitle());

        Log.v(tag, "set datetime");
        if(!StringUtil.isEmptyString(event.getStart_date())) {
            ((TextView) this.findViewById(R.id.textStartDate)).setText(TimeUtil.getTextDate(event.getStart_date(), "dd-MMM"));
            ((TextView) this.findViewById(R.id.textStartDayOfWeek)).setText(TimeUtil.getDayOfStartDate(event.getStart_date()));
        }
        if(!StringUtil.isEmptyString(event.getStart_time())||StringUtil.isEmptyString(event.getEnd_time())) {
            ((TextView) this.findViewById(R.id.textStartEndTime)).setText(TimeUtil.getTextStartEndTime(event));
        }

        //todo: set categories

        //todo: set buddies

        //todo: set organizers

    }
}
