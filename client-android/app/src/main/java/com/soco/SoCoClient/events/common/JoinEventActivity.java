package com.soco.SoCoClient.events.common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.service.EventDetailsTask;
import com.soco.SoCoClient.events.service.JoinEventService;

public class JoinEventActivity extends ActionBarActivity implements TaskCallBack {
    public static final String PHONE="PHONE";
    public static final String EMAIL="EMAIL";
    private static final String tag="JoinEventActivity";
    Event event;
    ProgressDialog pd;
    SocoApp socoApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);
        Intent intent = getIntent();
        socoApp = (SocoApp) getApplicationContext();

        getSupportActionBar().hide();
        String event_id = intent.getStringExtra(Event.EVENT_ID);

        EventDetailsTask edt = new EventDetailsTask(SocoApp.user_id,SocoApp.token,this);
        edt.execute(event_id);

    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_activity_join_event, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void close(View view){
        finish();
    }

    private void setViewFromEvent(Event event,String email) {
        if(event==null){
            return;
        }
        ((TextView) this.findViewById(R.id.address)).setText(event.getAddress());
        ((TextView) this.findViewById(R.id.textTitle)).setText(event.getTitle());
//        ((TextView) this.findViewById(R.id.edit_areacode)).setText("+852");
        //date time
        if (!StringUtil.isEmptyString(event.getStart_date())) {
            ((TextView) this.findViewById(R.id.textStartDate)).setText(TimeUtil.getTextDate(event.getStart_date(), "dd-MMM-yyyy"));
            ((TextView) this.findViewById(R.id.textStartDayOfWeek)).setText(TimeUtil.getDayOfStartDate(event.getStart_date()));
        }
        if (!StringUtil.isEmptyString(event.getStart_time()) || StringUtil.isEmptyString(event.getEnd_time())) {
            ((TextView) this.findViewById(R.id.textStartEndTime)).setText(TimeUtil.getTextStartEndTime(event));
        }
        if (!StringUtil.isEmptyString(email)) {
            ((TextView) this.findViewById(R.id.edit_email)).setText(email);
        }
    }
    public void joinRequest(View view){
        Log.v(tag, "start join event service at back end");

        Log.v(tag, "show progress dialog, start joining event");
        pd = ProgressDialog.show(this, "Sending join event request...", "Please wait...");
        new Thread(new Runnable(){
            public void run(){
                joinEventRequestInBackground();
                joinEventHandler.sendEmptyMessage(0);
            }
        }).start();
    }


    private void joinEventRequestInBackground() {

        Log.v(tag, "start join event service at back end");
        Intent i = new Intent(this, JoinEventService.class);
        i.putExtra(Event.EVENT_ID, Long.toString(event.getId()));
        String areacode = StringUtil.isEmptyString(((TextView) findViewById(R.id.edit_areacode)).getText().toString())?"+852":((TextView)findViewById(R.id.edit_areacode)).getText().toString();
        String phone = StringUtil.isEmptyString(((TextView) findViewById(R.id.edit_mobile)).getText().toString())?"":((TextView)findViewById(R.id.edit_mobile)).getText().toString();
       String email =  ((TextView)findViewById(R.id.edit_email)).getText().toString();
        if(!StringUtil.isEmptyString(phone)) {
            StringBuffer phoneSb = new StringBuffer();
            phoneSb.append(areacode);
            phoneSb.append(phone);
            i.putExtra(PHONE, phoneSb.toString());
        }
        if(!StringUtil.isEmptyString(email)) {
            i.putExtra(EMAIL, email);
        }
        startService(i);

        Log.v(tag, "set join event response flag as false");
        socoApp.addBuddyResponse = false;

        Log.v(tag, "set join response flag as false");
        socoApp.joinEventResponse = false;
        Log.v(tag, "wait and check status");
        int count = 0;
        while(!socoApp.joinEventResponse && count < 10) {   //wait for 10s
            Log.d(tag, "wait for response: " + count * 1 + "s");
            long endTime = System.currentTimeMillis() + 1*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                        Log.e(tag, "Error in waiting");
                    }
                }
            }
            count++;
        }
    }

    Handler joinEventHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");

            if(socoApp.joinEventResponse && socoApp.joinEventResult){
                Log.d(tag, "join event success");
                Toast.makeText(getApplicationContext(), "Join event suceess.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Log.e(tag, "join event fail, notify user");
                if(socoApp.error_message != null && !socoApp.error_message.isEmpty()) {
                    Toast.makeText(getApplicationContext(), socoApp.error_message, Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "Network error, please try again later.", Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();
        }
    };

    public void doneTask(Object o){
        if(o==null){
            return;
        }
        event = (Event) o;
        setViewFromEvent(event, socoApp.loginEmail);
    }

}
