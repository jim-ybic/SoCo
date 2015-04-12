package com.soco.SoCoClient.view;

import android.app.AlertDialog;
import android.content.Context;
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
import com.soco.SoCoClient.control.http.service.HeartbeatService;
import com.soco.SoCoClient.control.http.task.LoginTaskAsync;
import com.soco.SoCoClient.control.http.task.RegisterTaskAsync;
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

        //check if login credential exists
        String savedLoginEmail = ProfileUtil.getLoginEmail(this);
        String savedLoginPassword = ProfileUtil.getLoginPassword(this);
        Log.i(tag, "Get saved login email/password: " + savedLoginEmail + "/" + savedLoginPassword);
        if(!savedLoginEmail.isEmpty())
            login(null);

    }

    private void findViewsById() {
        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
    }

    public void serverConfig (View view) {
        Log.i(tag, "serverConfig start");
        Intent intent = new Intent(this, ServerConfigActivity.class);
        startActivity(intent);
    }

    public void login (View view) {
        loginEmail = et_login_email.getText().toString();
        loginPassword = et_login_password.getText().toString();

        ProfileUtil.ready(getApplicationContext(), loginEmail);
        nickname = ProfileUtil.getNickname(getApplicationContext(), loginEmail);

        String access_token = ProfileUtil.getLoginAccessToken(getApplicationContext());
        if (access_token != null && !access_token.isEmpty())
            Log.i(tag, "Load access token and skip login: " + access_token);
        else {
            Log.i(tag, "Cannot load access token, start login to server");
//            HttpTask loginTask = new HttpTask(
//                    ProfileUtil.getLoginUrl(getApplicationContext()), HttpConfig.HTTP_TYPE_LOGIN,
//                    loginEmail, loginPassword, getApplicationContext(),
//                    null, null, null, null, null);
//            loginTask.execute();
            String url = ProfileUtil.getLoginUrl(getApplicationContext());
            LoginTaskAsync task = new LoginTaskAsync(loginEmail, loginPassword, url,
                    getApplicationContext());
            task.execute();
        }

        boolean loginSuccess = LoginUtil.validateLogin(loginEmail, loginPassword);
        if(loginSuccess) {
            ProfileUtil.setLoginEmail(this, loginEmail);
            ProfileUtil.setLoginPassword(this, loginPassword);
            Log.i(tag, "Save to profile login email/password: " + loginEmail + "/" + loginPassword);

            //start heartbeat service
            Intent iHeartbeat = new Intent(this, HeartbeatService.class);
            startService(iHeartbeat);

            Toast.makeText(getApplicationContext(), "Hello, " + nickname,
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ShowActiveProjectsActivity.class);
//            intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
//            intent.putExtra(Config.LOGIN_PASSWORD, loginPassword);
            ((SocoApp)getApplicationContext()).loginEmail = loginEmail;
            ((SocoApp)getApplicationContext()).loginPassword = loginPassword;
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Oops, login failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void register (View view) {
        loginEmail = et_login_email.getText().toString();
        loginPassword = et_login_password.getText().toString();

        ProfileUtil.ready(getApplicationContext(), loginEmail);
        nickname = ProfileUtil.getNickname(getApplicationContext(), loginEmail);

        //set initial status and start login
        final SocoApp app = (SocoApp) getApplicationContext();
        app.setRegistrationStatus(SocoApp.REGISTRATION_STATUS_START);

//        HttpTask registerTask = new HttpTask(
//                ProfileUtil.getRegisterUrl(getApplicationContext()), HttpConfig.HTTP_TYPE_REGISTER,
//                loginEmail, loginPassword, getApplicationContext(),
//                null, null, null, null, null);
//        registerTask.execute();
        Context context = getApplicationContext();
        String url = ProfileUtil.getRegisterUrl(context);
        RegisterTaskAsync task = new RegisterTaskAsync(loginEmail, loginPassword, url, context);
        task.execute();

        //wait and check login status
        boolean isSuccess = false;
        for (int i=1; i<= REGISTER_RETRY; i++) {
            Log.d(tag, "Wait for registration parse: " + i + "/" + REGISTER_RETRY);
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
        if (id == R.id.mn_server_config) {
            serverConfig(null);
        }
        return super.onOptionsItemSelected(item);
    }



}
