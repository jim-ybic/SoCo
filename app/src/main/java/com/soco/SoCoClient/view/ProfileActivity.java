package com.soco.SoCoClient.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.config.Config;
import com.soco.SoCoClient.control.util.ProfileUtil;
import com.soco.SoCoClient.model.Profile;


public class ProfileActivity extends ActionBarActivity {
    static String tag = "ProfileActivity";

    // Local views
//    TextView tv_profile_email;
    EditText et_profile_email, et_profile_password;
    EditText et_profile_nickname, et_profile_phone, et_profile_wechat;

    String loginEmail, loginPassword;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewsById();;

        Intent intent = getIntent();
        loginEmail = intent.getStringExtra(Config.LOGIN_EMAIL);
        loginPassword = intent.getStringExtra(Config.LOGIN_PASSWORD);

        profile = loadProfile();
        showProfile(profile);
    }

    private void findViewsById() {
//        tv_profile_email = (TextView) findViewById(R.id.profile_email);
        et_profile_email = (EditText) findViewById(R.id.profile_email);
        et_profile_password = (EditText) findViewById(R.id.profile_password);
        et_profile_nickname = (EditText) findViewById(R.id.profile_nickname);
        et_profile_phone = (EditText) findViewById(R.id.profile_phone);
        et_profile_wechat = (EditText) findViewById(R.id.profile_wechat);
    }

    Profile loadProfile() {
        Log.i(tag, "Load profile from " + Config.PROFILE_FILENAME);
        return new Profile(getApplicationContext());
    }

    public void saveProfile(View view) {
        Log.i(tag, "Save profile to " + Config.PROFILE_FILENAME);
        ProfileUtil.save(getApplicationContext(),
                et_profile_nickname.getText().toString(),
                et_profile_phone.getText().toString(),
                et_profile_wechat.getText().toString());
        ProfileUtil.setLoginEmail(this, et_profile_email.getText().toString());
        ProfileUtil.setLoginPassword(this, et_profile_password.getText().toString());
    }

    public void logout(View view) {
//        finish();
        Log.i(tag, "Logout from current user");
        ProfileUtil.clearUserInfo(this);
    }

    public void showProfile(Profile profile){
        et_profile_email.setText(profile.email, TextView.BufferType.EDITABLE);
        et_profile_password.setText(profile.password, TextView.BufferType.EDITABLE);
        et_profile_nickname.setText(profile.nickname, TextView.BufferType.EDITABLE);
//        tv_profile_email.setText(profile.email, TextView.BufferType.EDITABLE);
        et_profile_phone.setText(profile.phone, TextView.BufferType.EDITABLE);
        et_profile_wechat.setText(profile.wechat, TextView.BufferType.EDITABLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
