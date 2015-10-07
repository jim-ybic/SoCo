package com.soco.SoCoClient.login.sociallogin;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.GraphResponse;
import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.dropbox._ref.DropboxUtilV1;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.http._ref.HttpUtilV1;
import com.soco.SoCoClient.common.http.task._ref.AddFileToActivityTaskAsync;
import com.soco.SoCoClient.common.util.ActivityUtil;
import com.soco.SoCoClient.common.util.FileUtils;
import com.soco.SoCoClient.common.util.SocoApp;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginViaFacebook extends IntentService {

    static final String tag = "LoginViaFacebook";

    static final String TYPE = "type";
    static final String FACEBOOK = "facebook";
    static final String FACEBOOK_FIELD_ID = "id";
    static final String FACEBOOK_FIELD_NAME = "name";
    static final String FACEBOOK_FIELD_EMAIL = "email";

    static final String STATUS = "status";
    static final String USER_ID = "user_id";

    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 10;
    static final int THOUSAND = 1000;

    String userId;
    String userName;
    String userEmail;

    SocoApp socoApp;
    boolean mRequestStatus;
    GraphResponse mRequestResponse;

    public LoginViaFacebook() {
        super("LoginViaFacebook");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        socoApp = (SocoApp)getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(tag, "login via facebook, handle intent:" + intent);

        waitForResponse();

        if(mRequestStatus) {
            mRequestResponse = socoApp.facebookUserinfoResponse;
            retrieveUserinfo();
            loginToServer();
        }
        else{
            Log.e(tag, "cannot retrieve userinfo, skip login to server");
        }

        return;
    }

    private void waitForResponse() {
        Log.v(tag, "wait for response");

        int count = 0;
        mRequestStatus = socoApp.facebookUserinfoReady;
        mRequestResponse = null;

        while(!mRequestStatus && count < WAIT_ITERATION) {   //wait for 10s
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
            mRequestStatus = socoApp.facebookUserinfoReady;
        }

        return;
    }

    private void retrieveUserinfo() {
        Log.d(tag, "retrieve userinfo");

        JSONObject userinfo = mRequestResponse.getJSONObject();
        try {
            userId = userinfo.get(FACEBOOK_FIELD_ID).toString();
            Log.d(tag, "user id: " + userId);

            userEmail = userinfo.get(FACEBOOK_FIELD_EMAIL).toString();
            Log.d(tag, "user email: " + userEmail);

            userName = userinfo.get(FACEBOOK_FIELD_NAME).toString();
            Log.d(tag, "user name: " + userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return;
    }

    private void loginToServer(){
        Log.d(tag, "login to server");

        String url = UrlUtil.getSocialLoginUrl();
        Object response = request(
                url,
                FACEBOOK,
                userId,
                userName,
                userEmail
//                loginEmail, loginPassword,
//                url
        );
        if (response != null)
            parse(response);
    }

    public Object request(
//            String loginEmail, String loginPassword,
            String url,
            String type,
            String id,
            String name,
            String email
    ) {
        JSONObject data = new JSONObject();
        try {
//            data.put(HttpConfigV1.JSON_KEY_USERNAME, loginEmail);
//            data.put(HttpConfigV1.JSON_KEY_PASSWORD, loginPassword);
            data.put(TYPE, type);
            data.put(FACEBOOK_FIELD_ID, id);
            data.put(FACEBOOK_FIELD_NAME, name);
            data.put(FACEBOOK_FIELD_EMAIL, email);
            Log.i(tag, "created json: " + data);
        } catch (Exception e) {
            Log.e(tag, "cannot create Login Json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public boolean parse(Object response) {
        Log.d(tag, "parse social login response: " + response.toString());
        try {
            JSONObject json = new JSONObject(response.toString());
//            String access_token = json.getString(HttpConfigV1.JSON_KEY_ACCESS_TOKEN);
            String status = json.getString(STATUS);
            String user_id = json.getString(USER_ID);
            Log.i(tag, "social login response, status: " + status + ", user_id: " + user_id);

//            profile.saveLoginAccessToken(context, access_token);
//            String now = SignatureUtil.now();
//            profile.setLastLoginTimestamp(context, now);
            return true;
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
