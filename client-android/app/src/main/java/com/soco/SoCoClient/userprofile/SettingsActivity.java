package com.soco.SoCoClient.userprofile;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;

@Deprecated
public class SettingsActivity extends ActionBarActivity {

    static final String tag = "SettingsActivity";

    SocoApp socoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        socoApp = (SocoApp) getApplicationContext();
    }


}
