package com.soco.SoCoClient.onboarding.register;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.ReturnCode;
import com.soco.SoCoClient.common.util.SocoApp;

public class RegisterActivity extends ActionBarActivity {

    static final String tag = "RegisterActivity";

    EditText cName;
    EditText cEmail;
    EditText cAreacode;
    EditText cPhone;
    EditText cPassword;

    Context context;
    SocoApp socoApp;
    RegisterController controller;

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
        Log.v(tag, "tap on register");

        String name = cName.getText().toString();
        String email = cEmail.getText().toString();
        String phone = cAreacode.getText().toString() + cPhone.getText().toString();
        String password = cPassword.getText().toString();

        //todo
        //parse area code to get location
        String location = "";

        if(email.isEmpty()){
            Log.e(tag, "error: email empty");
            Toast.makeText(getApplicationContext(), "Email cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){
            Log.e(tag, "error: password empty");
            Toast.makeText(getApplicationContext(), "Password cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.v(tag, "start to register on server");
        RegisterController.registerOnServer(
                context,
                name,
                email,
                phone,
                password,
                location
        );

        Log.v(tag, "check register status");
        if(socoApp.registerStatus){
            Log.v(tag, "login success, finish this screen and login to dashboard");
            finish();
        }
        else{
            Log.e(tag, "login fail, notify user");
            Toast.makeText(getApplicationContext(), "Network error, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
