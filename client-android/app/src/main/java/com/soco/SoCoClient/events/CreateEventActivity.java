package com.soco.SoCoClient.events;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;

public class CreateEventActivity extends ActionBarActivity {

    static final String tag = "CreateEventActivity";

    SocoApp socoApp;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        socoApp = (SocoApp) getApplicationContext();
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

    public void done(View view){
        Log.v(tag, "tap on done");

        if(!validateInput()){
            Log.e(tag, "error validating error");
            return;
        }

        Log.v(tag, "show progress dialog, start register");
        pd = ProgressDialog.show(this, "Register in progress", "Please wait...");
        new Thread(new Runnable(){
            public void run(){
                createEventInBackground();
                createEventHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    boolean validateInput(){

        //TODO
        //retrieve user input and validate
        //add to global variables

        return true;
    }

    void createEventInBackground(){

        //TODO
        //start service to create event
        //wait and check status
    }

    Handler createEventHandler  = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");

            //// TODO: 10/16/2015
            //check flag for result
            //inform user

            pd.dismiss();
        }
    };

}
