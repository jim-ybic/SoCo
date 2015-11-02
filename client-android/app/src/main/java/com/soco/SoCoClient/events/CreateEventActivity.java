package com.soco.SoCoClient.events;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.service.CreateEventService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventActivity extends ActionBarActivity {

    static final String tag = "CreateEventActivity";

    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 10;
    static final int THOUSAND = 1000;

    SocoApp socoApp;
    ProgressDialog pd;

    EditText mTitle;
    EditText mLocation;
    EditText mStartdate, mEnddate;
    EditText mStarttime, mEndtime;
    EditText mIntroduction;

    DatePickerDialog startdatePicker, enddatePicker;
    TimePickerDialog starttimePicker, endtimePicker;
    SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        socoApp = (SocoApp) getApplicationContext();

        findViews();
        setTimedatePicker();
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
        mStartdate = (EditText)findViewById(R.id.startdate);
        mEnddate = (EditText)findViewById(R.id.enddate);
        mStarttime = (EditText)findViewById(R.id.starttime);
        mEndtime = (EditText)findViewById(R.id.endtime);
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
        e.setAddress(mLocation.getText().toString());
        e.setStart_date(mStartdate.getText().toString());
        e.setEnd_date(mEnddate.getText().toString());
        e.setStart_time(mStarttime.getText().toString());
        e.setEnd_time(mEndtime.getText().toString());
        e.setIntroduction(mIntroduction.getText().toString());
        Log.d(tag, "user created event: " + e.toString());
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


    void setTimedatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        int year = newCalendar.get(Calendar.YEAR);
        int month = newCalendar.get(Calendar.MONTH);
        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
        final int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);

        startdatePicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        mStartdate.setText(dateformatter.format(newDate.getTime()));
                    }
                }, year, month, day);

        enddatePicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        mEnddate.setText(dateformatter.format(newDate.getTime()));
                    }
                }, year, month, day);

        starttimePicker = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hourStr, minuteStr;
                        if(selectedHour<10)
                            hourStr = "0" + String.valueOf(selectedHour);
                        else
                            hourStr = String.valueOf(selectedHour);
                        if(selectedMinute<10)
                            minuteStr = "0" + String.valueOf(selectedMinute);
                        else
                            minuteStr = String.valueOf(selectedMinute);
                        mStarttime.setText( hourStr + ":" + minuteStr);
                    }
                }, hour, minute, true); //Yes 24 hour time

        endtimePicker = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hourStr, minuteStr;
                        if(selectedHour<10)
                            hourStr = "0" + String.valueOf(selectedHour);
                        else
                            hourStr = String.valueOf(selectedHour);
                        if(selectedMinute<10)
                            minuteStr = "0" + String.valueOf(selectedMinute);
                        else
                            minuteStr = String.valueOf(selectedMinute);
                        mEndtime.setText(hourStr + ":" + minuteStr);
                    }
                }, hour, minute, true); //Yes 24 hour time
    }

    public void startdatePick(View view){
        startdatePicker.show();
    }

    public void enddatePick(View view){
        enddatePicker.show();
    }

    public void starttimePick(View view){
        starttimePicker.show();
    }

    public void endtimePick(View view){
        endtimePicker.show();
    }

}
