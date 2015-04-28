package com.soco.SoCoClient.view.config;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.GeneralConfig;
import com.soco.SoCoClient.model.Profile;
import com.soco.SoCoClient.view.LoginActivity;


public class ProfileActivity extends ActionBarActivity {
    static String tag = "ProfileActivity";
//    static String EMPTY_STRING = "";

    // Local views
//    TextView tv_profile_email;
    EditText et_profile_email, et_profile_password;
    EditText et_profile_username, et_profile_phone, et_profile_wechat;
    TextView tv_lastLoginTimestamp;

    String loginEmail, loginPassword;
    SocoApp socoApp;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        socoApp = (SocoApp)getApplicationContext();
        profile = socoApp.profile;
        
        findViewsById();

//        Intent intent = getIntent();
        loginEmail = socoApp.loginEmail;
        loginPassword = socoApp.loginPassword;
//        loginEmail = intent.getStringExtra(GeneralConfig.LOGIN_EMAIL);
//        loginPassword = intent.getStringExtra(GeneralConfig.LOGIN_PASSWORD);

//        profile = loadProfile();
        showProfile(profile);
    }

    private void findViewsById() {
        et_profile_email = (EditText) findViewById(R.id.profile_email);
        et_profile_password = (EditText) findViewById(R.id.profile_password);
        et_profile_username = (EditText) findViewById(R.id.profile_username);
        et_profile_phone = (EditText) findViewById(R.id.profile_phone);
        et_profile_wechat = (EditText) findViewById(R.id.profile_wechat);
        tv_lastLoginTimestamp = (TextView) findViewById(R.id.tv_last_login_timestamp);
    }

//    Profile loadProfile() {
//        Log.i(tag, "Load profile from " + GeneralConfig.PROFILE_FILENAME);
//        return new Profile(getApplicationContext());
//    }

    public void saveProfile(View view) {
        Log.i(tag, "Save profile to " + GeneralConfig.PROFILE_FILENAME);
        profile.save(
                getApplicationContext(),
                et_profile_username.getText().toString(),
                et_profile_phone.getText().toString(),
                et_profile_wechat.getText().toString());
        profile.setLoginEmail(this, et_profile_email.getText().toString());
        profile.setLoginPassword(this, et_profile_password.getText().toString());

        String name = et_profile_username.getText().toString();
        if(!name.isEmpty())
            socoApp.username = name;
    }

    public void logout(View view) {
//        finish();
        Log.i(tag, "Logout from current user");
        profile.logout(this);
        Toast.makeText(getApplicationContext(), "Logout complete.", Toast.LENGTH_SHORT).show();

//        Log.d(tag, "clear UI");
//        et_profile_email.setText(EMPTY_STRING);
//        et_profile_password.setText(EMPTY_STRING);
//        et_profile_username.setText(EMPTY_STRING);
//        et_profile_phone.setText(EMPTY_STRING);
//        et_profile_wechat.setText(EMPTY_STRING);
//        tv_lastLoginTimestamp.setText(EMPTY_STRING);

        Log.d(tag, "return to login screen");
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void showProfile(Profile profile){
        et_profile_email.setText(profile.email);
        et_profile_password.setText(profile.password);
        et_profile_username.setText(profile.username);
        et_profile_phone.setText(profile.phone);
        et_profile_wechat.setText(profile.wechat);
        tv_lastLoginTimestamp.setText(profile.lastLoginTimestamp);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}
