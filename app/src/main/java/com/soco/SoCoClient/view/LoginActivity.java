package com.soco.SoCoClient.view;

import android.content.Intent;
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
    public static String SOCO_SERVER_IP = "192.168.43.240";
    public static String SOCO_SERVER_PORT = "8080";

    public static String LOGIN_PATH = "/socoserver/api/login";
    public static String REGISTER_PATH = "/socoserver/register/register";

    public static String LOGIN_SOCO_SERVER_URL = "http://"
            + SOCO_SERVER_IP + ":" + SOCO_SERVER_PORT + LOGIN_PATH;
    public static String REGISTER_SOCO_SERVER_URL = "http://"
            + SOCO_SERVER_IP + ":" + SOCO_SERVER_PORT + REGISTER_PATH;

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
        et_login_email.setText("jim.ybic@gmail.com");
        et_login_password.setText("Pass@123");

        //TEST
        SocoApp app = (SocoApp) getApplicationContext();
        app.setState("login");
    }

    private void findViewsById() {
        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
    }

    void updateProfile(String loginEmail) {
        ProfileUtil.ready(getApplicationContext(), loginEmail);
        nickname = ProfileUtil.getNickname(getApplicationContext(), loginEmail);
    }

    public void login (View view) {
        loginEmail = et_login_email.getText().toString();
        loginPassword = et_login_password.getText().toString();
        updateProfile(loginEmail);

        HttpTask loginTask = new HttpTask(LOGIN_SOCO_SERVER_URL, HttpTask.HTTP_TYPE_LOGIN,
                loginEmail, loginPassword);
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

        HttpTask registerTask = new HttpTask(REGISTER_SOCO_SERVER_URL, HttpTask.HTTP_TYPE_REGISTER,
                loginEmail, loginPassword);
        registerTask.execute();

        //TODO
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
