package com.soco.SoCoClient.onboarding.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.RequestCode;

import com.facebook.FacebookSdk;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.onboarding.forgotpassword.ForgotPasswordActivity;
import com.soco.SoCoClient.onboarding.login.service.LoginNormalService;
import com.soco.SoCoClient.onboarding.register.RegisterActivity;
import com.soco.SoCoClient.dashboard.Dashboard;
import com.soco.SoCoClient.onboarding.login.service.LoginViaFacebookService;
import com.soco.SoCoClient.userprofile.SettingsActivity;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.security.Signature;
import android.content.pm.Signature;
import java.util.Arrays;
import java.util.List;


public class LoginActivity //extends ActionBarActivity
    extends Activity
{
    public static String tag = "LoginActivity";

    static final String PERFS_NAME = "EVENT_BUDDY_PERFS";
    static final String LOGIN_EMAIL = "login_email";
    static final String LOGIN_PASSWORD = "login_password";

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


    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 5;
    static final int THOUSAND = 1000;

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
//    LoginButton loginButton;

    //controller
    LoginController controller;

    Context context;
    SocoApp socoApp;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Remove title bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.login_activity);

        findViews();
        context = getApplicationContext();
        socoApp = (SocoApp) context;


//        //initial screen resolution to app
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
////        IconUrlUtil = size.x;
//        display.getRealSize(size);
//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        // Use 1/8th of the available memory for this memory cache.
//        final int cacheSize = maxMemory / 8;
//        Log.v(tag, "init icon downloader: " + size + ", " + cacheSize);
//        IconUrlUtil.initialForIconDownloader(Math.min(size.x, size.y), cacheSize);

//        Log.v(tag, "save screensize and cachesize to shared preference: " + Math.min(size.x, size.y) + ", " + cacheSize);
//        SharedPreferences settings = context.getSharedPreferences(PERFS_NAME, 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString(SCREEN_SIZE, String.valueOf(Math.min(size.x, size.y)));
//        editor.putString(CACHE_SIZE, String.valueOf(cacheSize));
//        editor.commit();

        Log.v(tag, "create controller");
        controller = new LoginController();

        Log.v(tag, "create facebook login button");
        initFacebook();

        if(SocoApp.SKIP_LOGIN){ //for testing
            Log.w(tag, "[testing] skip login, goto dashboard");
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
        }

        keyHashTest();

        Log.v(tag, "check if have saved login detail");
        SharedPreferences settings = context.getSharedPreferences(PERFS_NAME, 0);
        String loginEmail = settings.getString(LOGIN_EMAIL, "");
        String loingPassword = settings.getString(LOGIN_PASSWORD, "");
        Log.v(tag, "get stored login detail: " + loginEmail + ", " + loingPassword);
        if(loginEmail != null && !loginEmail.isEmpty()){
            ((TextView) findViewById(R.id.et_login_email)).setText(loginEmail);
            ((TextView) findViewById(R.id.et_login_password)).setText(loingPassword);
            loginNormal(null);
        }

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

    private void keyHashTest() {
        Log.v(tag, "KeyHash start");
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.soco.SoCoClient",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(tag, "KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(tag, "keyhash error");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {

        }
        Log.v(tag, "KeyHash end");
    }

    private void initFacebook() {
        Log.d(tag, "init facebook");

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.v(tag, "facebook access token: " + accessToken);

        //todo: auto login via facebook when facebook token available
//        if(accessToken != null) {
//            Log.d(tag, "facebook token available: " + accessToken + ", proceed to app");
//            loginViaFacebook();
//        }
//        else {
//            Log.d(tag, "facebook token not available, facebook loginbutton");

        callbackManager = CallbackManager.Factory.create();

//            loginButton = (LoginButton) findViewById(R.id.login_button);
//
//            Log.v(tag, "set permission");
//            loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));
//
//            Log.v(tag, "register callback");
//            loginButton.registerCallback(callbackManager,
//                    new FacebookCallback<LoginResult>() {
//                        @Override
//                        public void onSuccess(LoginResult loginResult) {
//                            Log.d(tag, "facebook login success, token: " + loginResult.getAccessToken());
////                            AccessToken accessToken = AccessToken.getCurrentAccessToken();
////                            Log.d(tag, "access token: " + accessToken);
//                            loginViaFacebook();
//                        }
//
//                        @Override
//                        public void onCancel() {
//                            if(SocoApp.CAN_SKIP_FACEBOOK_LOGIN) {
//                                Log.e(tag, "facebook login errors, skip for testing mode");
//                                loginViaFacebook();
//                            }
//                            else {
//                                Toast.makeText(getApplicationContext(), "facebook login errors, skip for testing mode", Toast.LENGTH_SHORT).show();
//                                Log.e(tag, "facebook login error");
//                            }
//                        }
//
//                        @Override
//                        public void onError(FacebookException exception) {
//                            if(SocoApp.CAN_SKIP_FACEBOOK_LOGIN) {
//                                Log.e(tag, "testing mode enabled, skipping facebook login errors");
//                                loginViaFacebook();
//                            }
//                            else {
//                                Toast.makeText(getApplicationContext(), "Error login facebook, please try again later.", Toast.LENGTH_SHORT).show();
//                                Log.e(tag, "facebook login error");
//                            }
//                        }
//                    });
//        }
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

    private void loginViaFacebook() {
        Log.v(tag, "request facebook userinfo");
        LoginController.requestFacebookUserInfo(context);

        Log.v(tag, "show progress dialog, start login via facebook");
        pd = ProgressDialog.show(this, "Login via Facebook in progress", "Please wait...");
        new Thread(new Runnable(){
            public void run(){
                loginViaFacebookInBackground();
                loginViaFacebookHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    void loginViaFacebookInBackground(){
        Log.v(tag, "set response flag as false");
        socoApp.loginViaFacebookResponse = false;

        if(socoApp.OFFLINE_MODE){
            Log.w(tag, "offline mode: bypass login via facebook");
            socoApp.loginViaFacebookResult = true;
            return;
        }

        Log.v(tag, "in background: start login service - wait for response and login to server");
        Intent i = new Intent(this, LoginViaFacebookService.class);
        startService(i);

        Log.v(tag, "wait and check response status");
        int count = 0;
        while(!socoApp.loginViaFacebookResponse && count < WAIT_ITERATION) {   //wait for 10s
            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                        Log.e(tag, "Error in waiting");
                    }
                }
            }
            count++;
        }
    }

    Handler loginViaFacebookHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");

            if(socoApp.loginViaFacebookResponse && socoApp.loginViaFacebookResult){
                Log.d(tag, "login via facebook success, finish this screen and login to dashboard");
                Toast.makeText(getApplicationContext(), "Login via Facebook suceess.", Toast.LENGTH_SHORT).show();

                Log.v(tag, "start dashboard");
                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                startActivity(intent);
            }
            else{
                Log.e(tag, "login via facebook fail, notify user");
                Toast.makeText(getApplicationContext(), "Login via Facebook fail, please check password and try again.", Toast.LENGTH_SHORT).show();
            }

//            pd.dismiss();
        }
    };

    public void loginNormal (View view) {
        Log.v(tag, "tap login normal");
        if(!validateInput()){
            Log.e(tag, "error validating error");
            return;
        }

        Log.v(tag, "saved login details");
        SharedPreferences settings = context.getSharedPreferences(PERFS_NAME, 0);
        String loginEmail = ((TextView) findViewById(R.id.et_login_email)).getText().toString();
        String loginPassword = ((TextView) findViewById(R.id.et_login_password)).getText().toString();
        Log.v(tag, "save login detail: " + loginEmail + ", " + loginPassword);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LOGIN_EMAIL, loginEmail);
        editor.putString(LOGIN_PASSWORD, loginPassword);
        editor.commit();

        Log.v(tag, "show progress dialog, start register");
//        pd = ProgressDialog.show(this, "Login in progress", "Please wait...");
        hideViews();
        new Thread(new Runnable(){
            public void run(){
                loginNormalInBackground();
                loginNormalHandler.sendEmptyMessage(0);
            }
        }).start();


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

    boolean validateInput(){
        socoApp.loginEmail = et_login_email.getText().toString();
        socoApp.loginPassword = et_login_password.getText().toString();

        //todo
        //validate details

        return true;
    }

    void loginNormalInBackground() {
        Log.v(tag, "set response flag as false");
        socoApp.loginNormalResponse = false;

        if(socoApp.OFFLINE_MODE){
            Log.w(tag, "offline mode: bypass login normal");
            socoApp.loginNormalResponse = true;
            socoApp.loginNormalResult = true;
            return;
        }

        Log.v(tag, "start login normal service, login email " + et_login_email + ", logig password " + et_login_password);
        Intent i = new Intent(this, LoginNormalService.class);
        startService(i);

        Log.v(tag, "wait and check response status");
        int count = 0;
        while(!socoApp.loginNormalResponse && count < WAIT_ITERATION) {   //wait for 10s
            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                        Log.e(tag, "Error in waiting");
                    }
                }
            }
            count++;
        }
    }

    Handler loginNormalHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");

            if(socoApp.loginNormalResponse && socoApp.loginNormalResult){
                Log.d(tag, "login normal success, finish this screen and login to dashboard");
                Toast.makeText(getApplicationContext(), "Login suceess.", Toast.LENGTH_SHORT).show();

                Log.v(tag, "start dashboard");
                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                startActivity(intent);
            }
            else{
                Log.e(tag, "login normal fail, notify user");
                Toast.makeText(getApplicationContext(), "Login fail, please check password and try again.", Toast.LENGTH_SHORT).show();
            }

//            pd.dismiss();
        }
    };

    public void forgotpassword (View view) {
        Log.v(tag, "tap on forgot password");
        Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(i);
    }

    public void register (View view) {
        Log.v(tag, "tap on register");
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult(i, RequestCode.REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(tag, "on activity result: " + requestCode + ", " + resultCode
//                        + ", " + data.toString()
        );

        if(callbackManager == null) {
            Log.e(tag, "callbackmanager is null, recreate callbackmanager");
            callbackManager = CallbackManager.Factory.create();
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RequestCode.REGISTER && socoApp.registerResult){
            Log.v(tag, "register success - continue to login and goto dashboard");

            socoApp.loginEmail = socoApp.registerEmail;
            socoApp.loginPassword = socoApp.registerPassword;

            Log.v(tag, "start login normal service, login email " + et_login_email + ", logig password " + et_login_password);
            Intent i = new Intent(this, LoginNormalService.class);
            startService(i);

            Log.v(tag, "start dashboard");
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
        }

        return;
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
        showViews();

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

    public void facebooklogin(View view){
        Log.d(tag, "tap facebook login");

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.v(tag, "facebook access token: " + accessToken);

        if(accessToken != null) {
            Log.d(tag, "facebook token available: " + accessToken + ", proceed to app");
            loginViaFacebook();
            return;
        }

        Log.d(tag, "facebook token not available, prepare login facebook");
        List<String> permissionNeeds= Arrays.asList("email", "public_profile", "user_friends");

//        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(
                this,
                permissionNeeds);
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResults) {
                        Log.d(tag, "facebook login success, token: " + loginResults.getAccessToken());

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResults.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.v(tag, "me response: " + response.toString());
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                        loginViaFacebook();
                    }
                    @Override
                    public void onCancel() {
                        if(SocoApp.CAN_SKIP_FACEBOOK_LOGIN) {
                            Log.e(tag, "facebook login errors, skip for testing mode");
                            loginViaFacebook();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "facebook login errors, skip for testing mode", Toast.LENGTH_SHORT).show();
                            Log.e(tag, "facebook login error");
                        }
                    }

                    @Override
                    public void onError(FacebookException e) {
                        if(SocoApp.CAN_SKIP_FACEBOOK_LOGIN) {
                            Log.e(tag, "testing mode enabled, skipping facebook login errors");
                            loginViaFacebook();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error login facebook, please try again later.", Toast.LENGTH_SHORT).show();
                            Log.e(tag, "facebook login error");
                        }
                    }
                });
    }

    public void settings(View view){
        Log.v(tag, "tap setting");
        Intent i = new Intent (this, SettingsActivity.class);
        startActivity(i);
    }
    public void hideViews(){
        findViewById(R.id.sociallogin).setVisibility(View.INVISIBLE);
        findViewById(R.id.actions).setVisibility(View.INVISIBLE);
    }
    public void showViews(){
        findViewById(R.id.sociallogin).setVisibility(View.VISIBLE);
        findViewById(R.id.actions).setVisibility(View.VISIBLE);
    }
}
