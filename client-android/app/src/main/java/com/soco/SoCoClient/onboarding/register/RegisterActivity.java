package com.soco.SoCoClient.onboarding.register;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.soco.SoCoClient.R;

public class RegisterActivity extends ActionBarActivity {

    static final String tag = "RegisterActivity";

    EditText cName;
    EditText cEmail;
    EditText cAreacode;
    EditText cPhone;
    EditText cPassword;

//    Context context;
    RegisterController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        findViews();
//        context = getApplicationContext();

        Log.v(tag, "create controller");
        controller = new RegisterController();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_activity_register, menu);
//        return true;
//    }

    private void findViews(){
        cName = (EditText) findViewById(R.id.name);
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
        Log.d(tag, "start register");

        //todo
        //call controller.registeronserver
        //check return value
        //if success, continue to login and dashboard
    }
}
