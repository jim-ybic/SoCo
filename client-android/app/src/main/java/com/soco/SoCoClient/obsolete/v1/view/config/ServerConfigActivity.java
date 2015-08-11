package com.soco.SoCoClient.obsolete.v1.view.config;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.obsolete.v1.control.SocoApp;
import com.soco.SoCoClient.v2.model.Profile;


public class ServerConfigActivity extends ActionBarActivity {

    static String tag = "ServerConfig";
    EditText et_servip, et_servport;
//    EditText et_regiaddr, et_loginaddr;
//    EditText et_cprojectaddr, et_aprojectaddr, et_rnprojectaddr, et_sprojattraddr;

    SocoApp socoApp;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v1_activity_server_config);

        socoApp = (SocoApp)getApplicationContext();
        profile = socoApp.profile;

        findViewsById();
        loadServerProperties();
    }

    private void findViewsById() {
        et_servip = (EditText) findViewById(R.id.et_servip);
        et_servport = (EditText) findViewById(R.id.et_servport);
    }

    private void loadServerProperties(){
        et_servip.setText(profile.getServerIp(this));
        et_servport.setText(profile.getServerPort(this));

        Log.i(tag, "Load server config: "
                + et_servip.getText().toString() + ", "
                + et_servport.getText().toString() + ", "
        );
    }

    public void cancel (View view){
        finish();
    }

    public void save (View view){
        profile.setServerIp(this, et_servip.getText().toString());
        profile.setServerPort(this, et_servport.getText().toString());

        Log.i(tag, "Save server config: "
                + et_servip.getText().toString() + ", "
                + et_servport.getText().toString() + ", "
        );
        finish();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_server_config, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
