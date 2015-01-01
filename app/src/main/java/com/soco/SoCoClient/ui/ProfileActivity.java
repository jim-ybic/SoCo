package com.soco.SoCoClient.ui;

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

import com.soco.SoCoClient.config.Config;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.datamodel.Profile;


public class ProfileActivity extends ActionBarActivity {

    String loginEmail;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        loginEmail = intent.getStringExtra(Config.LOGIN_EMAIL);

        profile = loadProfile();
        showProfile(profile);
    }

    Profile loadProfile() {
        Log.i("profile", "Load profile from " + Config.PROFILE_FILENAME);
        SharedPreferences settings = getSharedPreferences(Config.PROFILE_FILENAME, 0);

        Profile profile = new Profile();
        profile.email = settings.getString(Config.PROFILE_EMAIL,"");
        profile.nickname = settings.getString(Config.PROFILE_NICKNAME,"");
        profile.phone = settings.getString(Config.PROFILE_PHONE,"");
        profile.wechat = settings.getString(Config.PROFILE__WECHAT,"");

        return profile;
    }

    public void saveProfile(View view) {
        Log.i("profile", "Save profile to " + Config.PROFILE_FILENAME);
        SharedPreferences settings = getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(Config.PROFILE_NICKNAME,
                ((TextView) findViewById(R.id.profile_nickname)).getText().toString());
        editor.putString(Config.PROFILE_PHONE,
                ((TextView) findViewById(R.id.profile_phone)).getText().toString());
        editor.putString(Config.PROFILE__WECHAT,
                ((TextView) findViewById(R.id.profile_wechat)).getText().toString());
        editor.commit();
        Toast.makeText(getApplicationContext(), "Profile saved.", Toast.LENGTH_SHORT).show();
    }

    public void showProfile(Profile profile){
        ((EditText) findViewById(R.id.profile_nickname)).setText(profile.nickname,
                TextView.BufferType.EDITABLE);
        ((TextView) findViewById(R.id.profile_email)).setText(profile.email,
                TextView.BufferType.EDITABLE);
        ((EditText) findViewById(R.id.profile_phone)).setText(profile.phone,
                TextView.BufferType.EDITABLE);
        ((EditText) findViewById(R.id.profile_wechat)).setText(profile.wechat,
                TextView.BufferType.EDITABLE);
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
