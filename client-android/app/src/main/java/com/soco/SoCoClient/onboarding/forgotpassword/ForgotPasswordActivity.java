package com.soco.SoCoClient.onboarding.forgotpassword;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.soco.SoCoClient.R;

public class ForgotPasswordActivity extends ActionBarActivity {

    static final String tag = "ForgotPasswordActivity";

    EditText cPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        findViews();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_activity_forgot_password, menu);
//        return true;
//    }

    private void findViews(){
        cPhone = (EditText) findViewById(R.id.phone);
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

    public void send(View view){
        Log.d(tag, "tap on send");

        //todo
        //call controller.sendrequest
        //check return value
    }
}
