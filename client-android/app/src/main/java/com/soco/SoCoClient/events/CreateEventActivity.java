package com.soco.SoCoClient.events;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.service.CreateEventService;

public class CreateEventActivity extends ActionBarActivity {

    static final String tag = "CreateEventActivity";

    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 10;
    static final int THOUSAND = 1000;

    SocoApp socoApp;
    ProgressDialog pd;

    EditText mTitle;
    EditText mLocation;
    EditText mDate;
    EditText mTime;
    EditText mIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        socoApp = (SocoApp) getApplicationContext();

        findViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
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

    void findViews(){
        mTitle = (EditText)findViewById(R.id.title);
        mLocation = (EditText)findViewById(R.id.location);
        mDate = (EditText)findViewById(R.id.date);
        mTime = (EditText)findViewById(R.id.time);
        mIntroduction = (EditText)findViewById(R.id.introduction);
    }

    public void apply(View view){
        Log.v(tag, "tap on apply");

        if(!validateInput()){
            Log.e(tag, "error validating error");
            return;
        }

        Log.v(tag, "show progress dialog, start register");
        pd = ProgressDialog.show(this, "Saving event on server", "Please wait...");
        new Thread(new Runnable(){
            public void run(){
                createEventInBackground();
                createEventHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    boolean validateInput(){
        Log.v(tag, "validate user input");

        if(mTitle.getText().toString().isEmpty()){
            Log.e(tag, "title is empty");
            Toast.makeText(getApplicationContext(), "Title cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!socoApp.SKIP_LOGIN && (socoApp.user_id.isEmpty() || socoApp.token.isEmpty())){
            Log.e(tag, "user id or token is empty");
            Toast.makeText(getApplicationContext(), "Please re-login and try again.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    void createEventInBackground(){

        Log.v(tag, "create the event object");
        Event e = new Event();
        e.setTitle(mTitle.getText().toString());
        e.setLocation(mLocation.getText().toString());
        e.setDate(mDate.getText().toString());
        e.setTime(mTime.getText().toString());
        e.setIntroduction(mIntroduction.getText().toString());
        socoApp.newEvent = e;

        Log.v(tag, "start create event service");
        Intent i = new Intent(this, CreateEventService.class);
        startService(i);

        Log.v(tag, "set response flag false");
        socoApp.createEventResponse = false;

        Log.v(tag, "wait and check register status");
        int count = 0;
        while(!socoApp.createEventResponse && count < WAIT_ITERATION) {   //wait for 10s
            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e1) {
                        Log.e(tag, "Error in waiting: " + e1);
                        e1.printStackTrace();
                    }
                }
            }
            count++;
        }

    }

    Handler createEventHandler  = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");

            if(socoApp.createEventResponse && socoApp.createEventResult){
                Log.d(tag, "create event successfully on server");
                Toast.makeText(getApplicationContext(), "Event saved.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Log.e(tag, "create event on server failed");
                Toast.makeText(getApplicationContext(), "Event cannot save, please try again.", Toast.LENGTH_SHORT).show();
            }


            pd.dismiss();
        }
    };

}
