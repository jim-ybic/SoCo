package com.soco.SoCoClient.userprofile;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;

public class SettingsActivity extends ActionBarActivity {

    static final String tag = "SettingsActivity";

    SocoApp socoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        socoApp = (SocoApp) getApplicationContext();
    }

    public void clickOfflinemode(View view){
        Log.v(tag, "click offlinemode");

        boolean checked = ((CheckBox)view).isChecked();
        if(checked)
            socoApp.OFFLINE_MODE = true;
        else
            socoApp.OFFLINE_MODE = false;

        Log.v(tag, "offline mode: " + checked);
    }

}
