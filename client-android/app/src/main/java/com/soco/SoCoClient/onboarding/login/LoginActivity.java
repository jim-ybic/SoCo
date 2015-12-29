package com.soco.SoCoClient.onboarding.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.RequestCode;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.dashboard.Dashboard;
import com.soco.SoCoClient.onboarding.forgotpassword.ForgotPasswordActivity;
import com.soco.SoCoClient.onboarding.login.service.LoginNormalService;
import com.soco.SoCoClient.onboarding.login.service.LoginNormalTask;
import com.soco.SoCoClient.onboarding.login.service.LoginViaFacebookService;
import com.soco.SoCoClient.onboarding.register.RegisterActivity;
import com.soco.SoCoClient.userprofile.SettingsActivity;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;



public class LoginActivity //extends ActionBarActivity
    extends Activity
    implements TaskCallBack
{
    public static String tag = "LoginActivity";

    static final String PERFS_NAME = "EVENT_BUDDY_PERFS";
    static final String LOGIN_EMAIL = "login_email";
    static final String LOGIN_PASSWORD = "login_password";

    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 10;
    static final int THOUSAND = 1000;

    EditText et_login_email;
    EditText et_login_password;


    CallbackManager callbackManager;
    LoginController controller;

    Context context;
    SocoApp socoApp;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_activity);

        findViews();
        setText();
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
        callbackManager = CallbackManager.Factory.create();
        return;
    }

    private void setText(){
        ((TextView) findViewById(R.id.event)).setText(R.string.event);
        ((TextView) findViewById(R.id.buddy)).setText(R.string.buddy);
        ((Button) findViewById(R.id.signintxt)).setText(R.string.signin);
        ((TextView) findViewById(R.id.registertxt)).setText(R.string.register_bold);
        ((TextView) findViewById(R.id.forgotpasswordtxt)).setText(R.string.forgotpassword_bold);
        ((EditText) findViewById(R.id.et_login_email)).setHint(R.string.email);
        ((EditText) findViewById(R.id.et_login_password)).setHint(R.string.password);
    }

    private void findViews() {
        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
    }

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
                onResume();
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

        Log.v(tag, "show progress dialog, start login normal in background");
        pd = ProgressDialog.show(this, "Login in progress", "Please wait...");
        hideViews();
        new Thread(new Runnable(){
            public void run(){
                loginNormalInBackground();
            }
        }).start();
    }

    boolean validateInput(){
        socoApp.loginEmail = et_login_email.getText().toString();
        socoApp.loginPassword = et_login_password.getText().toString();
        return true;
    }

    void loginNormalInBackground() {
        socoApp.loginNormalResponse = false;
        new LoginNormalTask(context, this).execute();
    }

    public void doneTask(Object o){
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
            onResume();
        }
        pd.dismiss();
    }

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

        Log.d(tag, "on activity result: " + requestCode + ", " + resultCode);

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

    @Override
    public void onResume() {
        super.onResume();
        showViews();
    }

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
