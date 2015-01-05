package com.soco.SoCoClient.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.ProfileUtil;
import com.soco.SoCoClient.model.Profile;


public class ProfileActivity extends ActionBarActivity {

    // Local views
    TextView tv_profile_email;
    EditText et_profile_nickname;
    EditText et_profile_phone;
    EditText et_profile_wechat;

    String loginEmail;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewsById();;

        Intent intent = getIntent();
        loginEmail = intent.getStringExtra(LoginActivity.LOGIN_EMAIL);

        profile = loadProfile();
        showProfile(profile);
    }

    private void findViewsById() {
        tv_profile_email = (TextView) findViewById(R.id.profile_email);
        et_profile_nickname = (EditText) findViewById(R.id.profile_nickname);
        et_profile_phone = (EditText) findViewById(R.id.profile_phone);
        et_profile_wechat = (EditText) findViewById(R.id.profile_wechat);
    }

    Profile loadProfile() {
        Log.i("profile", "Load profile from " + ProfileUtil.PROFILE_FILENAME);
        SharedPreferences settings = getSharedPreferences(ProfileUtil.PROFILE_FILENAME, 0);

        Profile profile = new Profile();
        profile.email = settings.getString(ProfileUtil.PROFILE_EMAIL,"");
        profile.nickname = settings.getString(ProfileUtil.PROFILE_NICKNAME,"");
        profile.phone = settings.getString(ProfileUtil.PROFILE_PHONE,"");
        profile.wechat = settings.getString(ProfileUtil.PROFILE__WECHAT,"");

        return profile;
    }

    public void saveProfile(View view) {
        Log.i("profile", "Save profile to " + ProfileUtil.PROFILE_FILENAME);
        SharedPreferences settings = getSharedPreferences(ProfileUtil.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(ProfileUtil.PROFILE_NICKNAME, et_profile_nickname.getText().toString());
        editor.putString(ProfileUtil.PROFILE_PHONE, et_profile_phone.getText().toString());
        editor.putString(ProfileUtil.PROFILE__WECHAT, et_profile_wechat.getText().toString());
        editor.commit();
        Toast.makeText(getApplicationContext(), "Profile saved.", Toast.LENGTH_SHORT).show();
    }

    public void showProfile(Profile profile){
        et_profile_nickname.setText(profile.nickname, TextView.BufferType.EDITABLE);
        tv_profile_email.setText(profile.email, TextView.BufferType.EDITABLE);
        et_profile_phone.setText(profile.phone, TextView.BufferType.EDITABLE);
        et_profile_wechat.setText(profile.wechat, TextView.BufferType.EDITABLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
}
