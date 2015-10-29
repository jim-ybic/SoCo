package com.soco.SoCoClient.onboarding.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.onboarding.register.service.RegisterService;

public class RegisterActivity extends ActionBarActivity {

    static final String tag = "RegisterActivity";

    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 10;
    static final int THOUSAND = 1000;

    EditText cFName, cLName;
    EditText cEmail;
    EditText cAreacode;
    EditText cPhone;
    EditText cPassword;

    Context context;
    SocoApp socoApp;
    RegisterController controller;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        findViews();
        context = getApplicationContext();
        socoApp = (SocoApp) context;

        Log.v(tag, "create controller");
        controller = new RegisterController();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_activity_register, menu);
//        return true;
//    }

    private void findViews(){
        cFName = (EditText) findViewById(R.id.fname);
        cLName = (EditText) findViewById(R.id.lname);
        cEmail = (EditText) findViewById(R.id.email);
        cAreacode = (EditText) findViewById(R.id.areacode);
        cPhone = (EditText) findViewById(R.id.phone);
        cPassword = (EditText) findViewById(R.id.password);
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

    public void register(View view){
        Log.v(tag, "tap on register, set global variables");
        if (!validateInput()) {
            Log.e(tag, "error validating error");
            return;
        }

        Log.v(tag, "show progress dialog, start register");
        pd = ProgressDialog.show(this, "Register in progress", "Please wait...");
        new Thread(new Runnable(){
            public void run(){
                registerInBackground();
                registerHandler.sendEmptyMessage(0);
            }
        }).start();

    }

    boolean validateInput() {
        Log.v(tag, "validate user input data");

        socoApp.registerName = cFName.getText().toString() + " " + cLName.getText().toString();
        socoApp.registerEmail = cEmail.getText().toString();
        socoApp.registerPhone = cAreacode.getText().toString() + cPhone.getText().toString();
        socoApp.registerPassword = cPassword.getText().toString();

        //todo
        //parse area code to get location
        socoApp.registerLocation = "";

        Log.v(tag, "data validation");
        if(socoApp.registerEmail.isEmpty()){
            Log.e(tag, "error: email empty");
            Toast.makeText(getApplicationContext(), "Email cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(socoApp.registerPassword.isEmpty()){
            Log.e(tag, "error: password empty");
            Toast.makeText(getApplicationContext(), "Password cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registerInBackground() {
        Log.v(tag, "start register service at back end");
        Intent i = new Intent(this, RegisterService.class);
        startService(i);

        Log.v(tag, "set register response flag as false");
        socoApp.registerResponse = false;

        Log.v(tag, "wait and check register status");
        int count = 0;
        while(!socoApp.registerResponse && count < WAIT_ITERATION) {   //wait for 10s
            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
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

    Handler registerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");

            if(socoApp.registerResult){
                Log.d(tag, "register success, finish this screen and login to dashboard");
                Toast.makeText(getApplicationContext(), "Suceess.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Log.e(tag, "register fail, notify user");
                Toast.makeText(getApplicationContext(), "Network error, please try again later.", Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();
        }
    };

}
