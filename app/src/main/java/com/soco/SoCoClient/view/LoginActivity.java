package com.soco.SoCoClient.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.http.HttpTask;
import com.soco.SoCoClient.control.util.LoginUtil;
import com.soco.SoCoClient.control.util.ProfileUtil;
import com.soco.SoCoClient.control.SocoApp;


public class LoginActivity extends ActionBarActivity {

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

    public static String TEST_EMAIL = "jim.ybic@gmail.com";
    public static String TEST_PASSWORD = "Pass@123";

    public static int REGISTER_RETRY = 10;
    public static int REGISTER_WAIT = 1000;    //ms

    // Local views
    EditText et_login_email;
    EditText et_login_password;

    // Local variables
    String loginEmail;
    String loginPassword;
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewsById();

        if (getIntent().getBooleanExtra(FLAG_EXIT, false))
            finish();

        // Testing login
        et_login_email.setText(TEST_EMAIL);
        et_login_password.setText(TEST_PASSWORD);
    }

    private void findViewsById() {
        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
    }

    void updateProfile(String loginEmail) {
        ProfileUtil.ready(getApplicationContext(), loginEmail);
        nickname = ProfileUtil.getNickname(getApplicationContext(), loginEmail);
    }

    public void serverConfig (View view) {
        Log.i(tag, "serverConfig start");
        Intent intent = new Intent(this, ServerConfigActivity.class);
        startActivity(intent);

//        SOCO_SERVER_IP = ProfileUtil.getServerIp(this);
//        SOCO_SERVER_PORT = ProfileUtil.getServerPort(this);
//        REGISTER_PATH = ProfileUtil.getServerRegisterAddress(this);
//        LOGIN_PATH = ProfileUtil.getServerLoginAddr(this);
//        LOGIN_SOCO_SERVER_URL = "http://"
//                + SOCO_SERVER_IP + ":" + SOCO_SERVER_PORT + LOGIN_PATH;
//        REGISTER_SOCO_SERVER_URL = "http://"
//                + SOCO_SERVER_IP + ":" + SOCO_SERVER_PORT + REGISTER_PATH;
//
//        Log.i(tag, "Update server config: " + SOCO_SERVER_IP + ", " + SOCO_SERVER_PORT + ", "
//                    + REGISTER_PATH + ", " + LOGIN_PATH);
//        Log.i(tag, "Register url: " + REGISTER_SOCO_SERVER_URL);
//        Log.i(tag, "Login url: " + LOGIN_SOCO_SERVER_URL);
    }

    public String getLoginUrl(){
        String ip = ProfileUtil.getServerIp(this);
        String port = ProfileUtil.getServerPort(this);
        String path = ProfileUtil.getServerLoginAddr(this);
        String url = "http://" + ip + ":" + port + path;
        Log.i(tag, "Login url: " + url);
        return url;
    }

    public String getRegisterUrl(){
        String ip = ProfileUtil.getServerIp(this);
        String port = ProfileUtil.getServerPort(this);
        String path = ProfileUtil.getServerRegisterAddress(this);
        String url = "http://" + ip + ":" + port + path;
        Log.i(tag, "Register url: " + url);
        return url;
    }

    public void login (View view) {
        loginEmail = et_login_email.getText().toString();
        loginPassword = et_login_password.getText().toString();
        updateProfile(loginEmail);

        HttpTask loginTask = new HttpTask(getLoginUrl(), HttpTask.HTTP_TYPE_LOGIN,
                loginEmail, loginPassword, getApplicationContext());
        loginTask.execute();
        //TODO: check if login is success

        boolean loginSuccess = LoginUtil.validateLogin(loginEmail, loginPassword);
        if(loginSuccess) {
            Toast.makeText(getApplicationContext(), "Hello, " + nickname,
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ShowActiveProgramsActivity.class);
            intent.putExtra(com.soco.SoCoClient.control.Config.LOGIN_EMAIL, loginEmail);
            intent.putExtra(com.soco.SoCoClient.control.Config.LOGIN_PASSWORD, loginPassword);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Oops, login failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void register (View view) {
        loginEmail = et_login_email.getText().toString();
        loginPassword = et_login_password.getText().toString();
        updateProfile(loginEmail);

        //set initial status and start login
        final SocoApp app = (SocoApp) getApplicationContext();
        app.setRegistrationStatus(SocoApp.REGISTRATION_STATUS_START);

        HttpTask registerTask = new HttpTask(getRegisterUrl(), HttpTask.HTTP_TYPE_REGISTER,
                loginEmail, loginPassword, getApplicationContext());
        registerTask.execute();

        //wait and check login status
        boolean isSuccess = false;
        for (int i=1; i<= REGISTER_RETRY; i++) {
            Log.d(tag, "Wait for registration response: " + i + "/" + REGISTER_RETRY);
            SystemClock.sleep(REGISTER_WAIT);;
            Log.d(tag, "Current registration status is: " + app.getRegistationStatus());
            if(app.getRegistationStatus().equals(SocoApp.REGISTRATION_STATUS_SUCCESS)) {
                isSuccess = true;
                break;
            }
            else if (app.getRegistationStatus().equals(SocoApp.REGISTRATION_STATUS_FAIL)){
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
