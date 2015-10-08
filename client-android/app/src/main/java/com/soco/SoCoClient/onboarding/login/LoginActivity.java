package com.soco.SoCoClient.onboarding.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.RequestCode;
import com.soco.SoCoClient.common.http.task._ref.LoginTaskAsync;
import com.soco.SoCoClient.common.http.UrlUtil;

import com.facebook.FacebookSdk;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.onboarding.forgotpassword.ForgotPasswordActivity;
import com.soco.SoCoClient.onboarding.login.service.LoginNormalService;
import com.soco.SoCoClient.onboarding.register.RegisterActivity;
import com.soco.SoCoClient.dashboard.Dashboard;
import com.soco.SoCoClient.onboarding.login.service.LoginViaFacebookService;

import java.util.Arrays;


public class LoginActivity extends ActionBarActivity {

    public static String tag = "LoginActivity";

//    public static String FLAG_EXIT = "exit";
//    public String SOCO_SERVER_IP = "192.168.0.104";
//    public String SOCO_SERVER_PORT = "8080";

//    public String LOGIN_PATH = "/socoserver/api/login";
//    public String REGISTER_PATH = "/socoserver/register/register";

//    public String LOGIN_SOCO_SERVER_URL = "http://"
//            + SOCO_SERVER_IP + ":" + SOCO_SERVER_PORT + LOGIN_PATH;
//    public String REGISTER_SOCO_SERVER_URL = "http://"
//            + SOCO_SERVER_IP + ":" + SOCO_SERVER_PORT + REGISTER_PATH;

//    public static String TEST_EMAIL = "jim.ybic@gmail.com";
//    public static String TEST_PASSWORD = "Pass@123";

//    public static int REGISTER_RETRY = 10;
//    public static int REGISTER_WAIT = 1000;    //ms

    // Local views
    EditText et_login_email;
    EditText et_login_password;

    // Local variables
//    String loginEmail;
//    String loginPassword;
    String nickname;
//    SocoApp socoApp;
//    Profile profile;
//    DBManagerSoco dbmgrSoco;

    //facebook
    CallbackManager callbackManager;
    LoginButton loginButton;

    //controller
    LoginController controller;

    Context context;
    SocoApp socoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_activity);

        findViews();
        context = getApplicationContext();
        socoApp = (SocoApp) context;

        Log.v(tag, "create controller");
        controller = new LoginController();

        Log.v(tag, "create facebook login button");
        initFacebook();


//        socoApp = (SocoApp) getApplicationContext();
//        profile = new Profile(getApplicationContext());
//        socoApp.profile = profile;

//        dbmgrSoco = new DBManagerSoco(getApplicationContext());
//        dbmgrSoco.context = getApplicationContext();
//        Log.i(tag, "login activity: 2 get application context " + dbmgrSoco.context);
//        socoApp.dbManagerSoco = dbmgrSoco;

        //test
//        et_login_email.setText(TEST_EMAIL);
//        et_login_password.setText(TEST_PASSWORD);

//        if (getIntent().getBooleanExtra(FLAG_EXIT, false))
//            finish();

        //check if login credential exists
//        String savedLoginEmail = profile.getLoginEmail(this);
//        String savedLoginPassword = profile.getLoginPassword(this);
//        String savedLoginAccessToken = profile.getLoginAccessToken(this);
//        Log.i(tag, "Get saved login email/password/token: "
//                + savedLoginEmail + ", " + savedLoginPassword + ", " + savedLoginAccessToken);

//        String savedLoginAccessToken = ""; //testing - used to bypass login screen

//        if(!savedLoginAccessToken.isEmpty()) {
//            Log.i(tag, "Saved login access token can be used, skip login screen");
//            et_login_email.setText(savedLoginEmail);
//            et_login_password.setText(savedLoginPassword);
//            loginNormal(null);  //comment out for testing
//        }

    }

    private void initFacebook() {
        Log.d(tag, "init facebook");

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.v(tag, "facebook access token: " + accessToken);

        if(accessToken != null) {
            Log.d(tag, "facebook token available: " + accessToken + ", proceed to app");
            loginViaFacebook();
        }
        else {
            Log.d(tag, "facebook token not available, prepare login facebook");

            callbackManager = CallbackManager.Factory.create();
            loginButton = (LoginButton) findViewById(R.id.login_button);

            Log.v(tag, "set permission");
            loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));

            Log.v(tag, "register callback");
            loginButton.registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.d(tag, "facebook login success, token: " + loginResult.getAccessToken());
//                            AccessToken accessToken = AccessToken.getCurrentAccessToken();
//                            Log.d(tag, "access token: " + accessToken);
                            loginViaFacebook();
                        }

                        @Override
                        public void onCancel() {
                            Log.d(tag, "facebook login cancel");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.d(tag, "facebook login error");
                        }
                    });
        }

        return;
    }

    private void loginViaFacebook(){
        Log.v(tag, "send login info to server");
        LoginController.requestFacebookUserInfo(context);

        Log.v(tag, "start login service - wait for response and login to server");
        Intent i = new Intent(this, LoginViaFacebookService.class);
        startService(i);

        Log.v(tag, "start dashboard");
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(tag, "on activity result: " + requestCode + ", " + resultCode + ", " + data.toString());

        if(callbackManager == null)
            Log.e(tag, "callbackmanager is null");
        else
            callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RequestCode.REGISTER){
            if(socoApp.registerStatus){
                //todo
                //login with newly registered email & password

            }

            //todo
            //handle register return value
        }

        return;
    }

    private void findViews() {
        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
    }

//    public void serverConfig (View view) {
//        Log.i(tag, "serverConfig start");
//        Intent intent = new Intent(this, LoginActivityV1.ServerConfigActivity.class);
//        startActivity(intent);
//    }


    public void loginNormal (View view) {
        socoApp.loginEmail = et_login_email.getText().toString();
        socoApp.loginPassword = et_login_password.getText().toString();

        Intent i = new Intent(this, LoginNormalService.class);
        startService(i);

        Log.v(tag, "start dashboard");
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);


//        profile.ready(getApplicationContext(), loginEmail);
//        nickname = profile.getUsername(getApplicationContext(), loginEmail);
//        Log.d(tag, "save current login email/password into profile: " +
//                loginEmail + ", " + loginPassword);
//        profile.setLoginEmail(this, loginEmail);
//        profile.setLoginPassword(this, loginPassword);

//        String access_token = profile.getLoginAccessToken(getApplicationContext());
//        if (access_token != null && !access_token.isEmpty())
//            Log.i(tag, "Load access token and skip login: " + access_token);
//        else {
//            Log.i(tag, "Cannot load access token, start login to server");
//            HttpTask loginTask = new HttpTask(
//                    profile.getLoginUrl(getApplicationContext()), HttpConfigV1.HTTP_TYPE_LOGIN,
//                    loginEmail, loginPassword, getApplicationContext(),
//                    null, null, null, null, null);
//            loginTask.execute();
//            String url = UrlUtil.getLoginUrl(getApplicationContext());
//            LoginTaskAsync task = new LoginTaskAsync(loginEmail, loginPassword, url,
//                    getApplicationContext());
//            task.execute();
//        }



//        boolean loginSuccess = validateLogin(loginEmail, loginPassword);
//        if(loginSuccess) {
//            profile.setLoginEmail(this, loginEmail);
//            profile.setLoginPassword(this, loginPassword);
//            Log.i(tag, "Save to profile login email/password: " + loginEmail + "/" + loginPassword);

        //start heartbeat service - comment for testing
//            Intent iHeartbeat = new Intent(this, HeartbeatService.class);
//            startService(iHeartbeat);

//            Toast.makeText(getApplicationContext(), "Hello, " + nickname,
//                    Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(this, ShowActiveProjectsActivity.class);
//            Intent intent = new Intent(this, Dashboard.class);

//            intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
//            intent.putExtra(Config.LOGIN_PASSWORD, loginPassword);

        //set global variable
//            socoApp.loginEmail = loginEmail;
//            socoApp.loginPassword = loginPassword;
//            socoApp.profile = profile;
//            socoApp.currentPath = GeneralConfigV1.PATH_ROOT;

//            startActivity(intent);
//        } else {
//            Toast.makeText(getApplicationContext(), "Oops, login failed.",
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    public void forgotpassword (View view) {
        Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(i);
    }

    public void register (View view) {
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult(i, RequestCode.REGISTER);
    }

//    public void registerV1 (View view) {
//        loginEmail = et_login_email.getText().toString();
//        loginPassword = et_login_password.getText().toString();
//
//        profile.ready(getApplicationContext(), loginEmail);
//        nickname = profile.getUsername(getApplicationContext(), loginEmail);
//
//        //set initial status and start login
//        socoApp.setRegistrationStatus(SocoApp.REGISTRATION_STATUS_START);
//
////        HttpTask registerTask = new HttpTask(
////                profile.getRegisterUrl(getApplicationContext()), HttpConfigV1.HTTP_TYPE_REGISTER,
////                loginEmail, loginPassword, getApplicationContext(),
////                null, null, null, null, null);
////        registerTask.execute();
//        Context context = getApplicationContext();
//        String url = UrlUtil.getRegisterUrl(context);
//        RegisterTaskAsync task = new RegisterTaskAsync(loginEmail, loginPassword, url, context);
//        task.execute();
//
//        //wait and check login status
//        boolean isSuccess = false;
//        for (int i=1; i<= REGISTER_RETRY; i++) {
//            Log.d(tag, "Wait for registration parse: " + i + "/" + REGISTER_RETRY);
//            SystemClock.sleep(REGISTER_WAIT);;
//            Log.d(tag, "Current registration status is: " + socoApp.getRegistationStatus());
//            if(socoApp.getRegistationStatus().equals(SocoApp.REGISTRATION_STATUS_SUCCESS)) {
//                isSuccess = true;
//                break;
//            }
//            else if (socoApp.getRegistationStatus().equals(SocoApp.REGISTRATION_STATUS_FAIL)){
//                isSuccess = false;
//                break;
//            }
//        }
//
//        if(isSuccess) {
//            Log.i(tag, "Registration submitted success");
//            new AlertDialog.Builder(this)
//                    .setTitle("Registration submitted")
//                    .setMessage("Check email to finish registration")
//                    .setPositiveButton("OK", null)
//                    .show();
//        }
//        else {
//            Log.i(tag, "Registration failed");
//            new AlertDialog.Builder(this)
//                    .setTitle("Registration failed")
//                    .setMessage("Review registration details and try again")
//                    .setPositiveButton("OK", null)
//                    .show();
//        }
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_login, menu);
//        return true;
//    }

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

//        Log.d(tag, "on resume");

//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        Log.d(tag, "facebook access token: " + accessToken);

        // Logs 'install' and 'app activate' App Events. - facebook
//        AppEventsLogger.activateApp(this);

//        String savedLoginEmail = profile.getLoginEmail(this);
//        String savedLoginPassword = profile.getLoginPassword(this);
//        Log.d(tag, "load saved login email/password: "
//                + savedLoginEmail + ", " + savedLoginPassword);
//        et_login_email.setText(savedLoginEmail);
//        et_login_password.setText(savedLoginPassword);
    }

//    public boolean validateLogin(String loginEmail, String loginPassword) {
//        Log.i("login", "Validate login for: " + loginEmail + "/" + loginPassword);
//
//        //TODO: add login validation logic here
//
//        return true;
//    }


}
