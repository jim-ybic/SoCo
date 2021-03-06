package com.soco.SoCoClient._ref;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.database._ref.DBManagerSoco;
//import com.soco.SoCoClient.obsolete.v1.control.http.service.HeartbeatService;
import com.soco.SoCoClient.common.http.task._ref.LoginTaskAsync;
import com.soco.SoCoClient.common.http.task._ref.RegisterTaskAsync;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.dashboard.Dashboard;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.model.Profile;

@Deprecated
public class LoginActivityV1 extends ActionBarActivity {

    public static String FLAG_EXIT = "exit";
    public static String tag = "Login";
    public String SOCO_SERVER_IP = "192.168.0.104";
    public String SOCO_SERVER_PORT = "8080";

    public String LOGIN_PATH = "/socoserver/api/login";
    public String REGISTER_PATH = "/socoserver/register/register";

    public String LOGIN_SOCO_SERVER_URL = "http://"
            + SOCO_SERVER_IP + ":" + SOCO_SERVER_PORT + LOGIN_PATH;
    public String REGISTER_SOCO_SERVER_URL = "http://"
            + SOCO_SERVER_IP + ":" + SOCO_SERVER_PORT + REGISTER_PATH;

//    public static String TEST_EMAIL = "jim.ybic@gmail.com";
//    public static String TEST_PASSWORD = "Pass@123";

    public static int REGISTER_RETRY = 10;
    public static int REGISTER_WAIT = 1000;    //ms

    // Local views
    EditText et_login_email;
    EditText et_login_password;

    // Local variables
    String loginEmail;
    String loginPassword;
    String nickname;
    SocoApp socoApp;
    Profile profile;
    DBManagerSoco dbmgrSoco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ref_activity_login_v1);

        socoApp = (SocoApp) getApplicationContext();

        profile = new Profile(getApplicationContext());
        socoApp.profile = profile;

        dbmgrSoco = new DBManagerSoco(getApplicationContext());
        dbmgrSoco.context = getApplicationContext();
        Log.i(tag, "login activity: 2 get application context " + dbmgrSoco.context);
        socoApp.dbManagerSoco = dbmgrSoco;

        findViewsById();

        if (getIntent().getBooleanExtra(FLAG_EXIT, false))
            finish();

        //check if login credential exists
        String savedLoginEmail = profile.getLoginEmail(this);
        String savedLoginPassword = profile.getLoginPassword(this);
        String savedLoginAccessToken = profile.getLoginAccessToken(this);
        Log.i(tag, "Get saved login email/password/token: "
                + savedLoginEmail + ", " + savedLoginPassword + ", " + savedLoginAccessToken);

        if(!savedLoginAccessToken.isEmpty()) {
            Log.i(tag, "Saved login access token can be used, skip login screen");
            et_login_email.setText(savedLoginEmail);
            et_login_password.setText(savedLoginPassword);
            login(null);
        }
    }

    private void findViewsById() {
        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
    }

//    public void serverConfig (View view) {
//        Log.i(tag, "serverConfig start");
//        Intent intent = new Intent(this, ServerConfigActivity.class);
//        startActivity(intent);
//    }

    public void login (View view) {
        loginEmail = et_login_email.getText().toString();
        loginPassword = et_login_password.getText().toString();

        profile.ready(getApplicationContext(), loginEmail);
        nickname = profile.getUsername(getApplicationContext(), loginEmail);
        Log.d(tag, "save current login email/password into profile: " +
                loginEmail + ", " + loginPassword);
        profile.setLoginEmail(this, loginEmail);
        profile.setLoginPassword(this, loginPassword);

        String access_token = profile.getLoginAccessToken(getApplicationContext());
        if (access_token != null && !access_token.isEmpty())
            Log.i(tag, "Load access token and skip login: " + access_token);
        else {
            Log.i(tag, "Cannot load access token, start login to server");
//            HttpTask loginTask = new HttpTask(
//                    profile.getLoginUrl(getApplicationContext()), HttpConfigV1.HTTP_TYPE_LOGIN,
//                    loginEmail, loginPassword, getApplicationContext(),
//                    null, null, null, null, null);
//            loginTask.execute();
            String url = UrlUtil.getLoginUrl(getApplicationContext());
            LoginTaskAsync task = new LoginTaskAsync(loginEmail, loginPassword, url,
                    getApplicationContext());
            task.execute();
        }

        boolean loginSuccess = validateLogin(loginEmail, loginPassword);
        if(loginSuccess) {
            profile.setLoginEmail(this, loginEmail);
            profile.setLoginPassword(this, loginPassword);
            Log.i(tag, "Save to profile login email/password: " + loginEmail + "/" + loginPassword);

            //start heartbeat service
//            Intent iHeartbeat = new Intent(this, HeartbeatService.class);
//            startService(iHeartbeat);

            Toast.makeText(getApplicationContext(), "Hello, " + nickname,
                    Toast.LENGTH_SHORT).show();

            //todo: testing script
//            Intent intent = new Intent(this, ShowActiveProjectsActivity.class);
            Intent intent = new Intent(this, Dashboard.class);

//            intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
//            intent.putExtra(Config.LOGIN_PASSWORD, loginPassword);

            //set global variable
            socoApp.loginEmail = loginEmail;
            socoApp.loginPassword = loginPassword;
            socoApp.profile = profile;
            socoApp.currentPath = GeneralConfigV1.PATH_ROOT;

            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Oops, login failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void register (View view) {
        loginEmail = et_login_email.getText().toString();
        loginPassword = et_login_password.getText().toString();

        profile.ready(getApplicationContext(), loginEmail);
        nickname = profile.getUsername(getApplicationContext(), loginEmail);

        //set initial status and start login
        socoApp.setRegistrationStatus(SocoApp.REGISTRATION_STATUS_START);

//        HttpTask registerTask = new HttpTask(
//                profile.getRegisterUrl(getApplicationContext()), HttpConfigV1.HTTP_TYPE_REGISTER,
//                loginEmail, loginPassword, getApplicationContext(),
//                null, null, null, null, null);
//        registerTask.execute();
        Context context = getApplicationContext();
        String url = UrlUtil.getRegisterUrl(context);
        RegisterTaskAsync task = new RegisterTaskAsync(loginEmail, loginPassword, url, context);
        task.execute();

        //wait and check login status
        boolean isSuccess = false;
        for (int i=1; i<= REGISTER_RETRY; i++) {
            Log.d(tag, "Wait for registration parse: " + i + "/" + REGISTER_RETRY);
            SystemClock.sleep(REGISTER_WAIT);;
            Log.d(tag, "Current registration status is: " + socoApp.getRegistationStatus());
            if(socoApp.getRegistationStatus().equals(SocoApp.REGISTRATION_STATUS_SUCCESS)) {
                isSuccess = true;
                break;
            }
            else if (socoApp.getRegistationStatus().equals(SocoApp.REGISTRATION_STATUS_FAIL)){
                isSuccess = false;
                break;
            }
        }

        if(isSuccess) {
            Log.i(tag, "Registration submitted success");
            new AlertDialog.Builder(this)
                    .setTitle("Registration submitted")
                    .setMessage("Check email to finish registration")
                    .setPositiveButton("OK", null)
                    .show();
        }
        else {
            Log.i(tag, "Registration failed");
            new AlertDialog.Builder(this)
                    .setTitle("Registration failed")
                    .setMessage("Review registration details and try again")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.mn_server_config) {
//            serverConfig(null);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(tag, "onResume");
        String savedLoginEmail = profile.getLoginEmail(this);
        String savedLoginPassword = profile.getLoginPassword(this);
        Log.d(tag, "load saved login email/password: "
                + savedLoginEmail + ", " + savedLoginPassword);
        et_login_email.setText(savedLoginEmail);
        et_login_password.setText(savedLoginPassword);
    }

    public boolean validateLogin(String loginEmail, String loginPassword) {
        Log.i("login", "Validate login for: " + loginEmail + "/" + loginPassword);

        //TODO: add login validation logic here

        return true;
    }


//    public static class ServerConfigActivity extends ActionBarActivity {
//
//        static String tag = "ServerConfig";
//        EditText et_servip, et_servport;
//    //    EditText et_regiaddr, et_loginaddr;
//    //    EditText et_cprojectaddr, et_aprojectaddr, et_rnprojectaddr, et_sprojattraddr;
//
//        SocoApp socoApp;
//        Profile profile;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_server_config_v1);
//
//            socoApp = (SocoApp)getApplicationContext();
//            profile = socoApp.profile;
//
//            findViewsById();
//            loadServerProperties();
//        }
//
//        private void findViewsById() {
//            et_servip = (EditText) findViewById(R.id.et_servip);
//            et_servport = (EditText) findViewById(R.id.et_servport);
//        }
//
//        private void loadServerProperties(){
//            et_servip.setText(UrlUtil.getServerIp(this));
//            et_servport.setText(UrlUtil.getServerPort(this));
//
//            Log.i(tag, "Load server config: "
//                    + et_servip.getText().toString() + ", "
//                    + et_servport.getText().toString() + ", "
//            );
//        }
//
//        public void cancel (View view){
//            finish();
//        }
//
//        public void save (View view){
//            UrlUtil.setServerIp(this, et_servip.getText().toString());
//            UrlUtil.setServerPort(this, et_servport.getText().toString());
//
//            Log.i(tag, "Save server config: "
//                    + et_servip.getText().toString() + ", "
//                    + et_servport.getText().toString() + ", "
//            );
//            finish();
//        }
//
//    //    @Override
//    //    public boolean onCreateOptionsMenu(Menu menu) {
//    //        // Inflate the menu; this adds items to the action bar if it is present.
//    //        getMenuInflater().inflate(R.menu.menu_server_config, menu);
//    //        return true;
//    //    }
//    //
//    //    @Override
//    //    public boolean onOptionsItemSelected(MenuItem item) {
//    //        // Handle action bar item clicks here. The action bar will
//    //        // automatically handle clicks on the Home/Up button, so long
//    //        // as you specify a parent activity in AndroidManifest.xml.
//    //        int id = item.getItemId();
//    //
//    //        //noinspection SimplifiableIfStatement
//    //        if (id == R.id.action_settings) {
//    //            return true;
//    //        }
//    //
//    //        return super.onOptionsItemSelected(item);
//    //    }
//
//    }
}
