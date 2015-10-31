package com.soco.SoCoClient.userprofile;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

        if(socoApp.OFFLINE_MODE)
            ((CheckBox)findViewById(R.id.offline_mode)).setChecked(true);
        else
            ((CheckBox)findViewById(R.id.offline_mode)).setChecked(false);
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

    public void settext(View view){
        Log.d(tag, "set text");
        Button p1_button = (Button)findViewById(R.id.button);
        p1_button.setText("No like");

    }

}
