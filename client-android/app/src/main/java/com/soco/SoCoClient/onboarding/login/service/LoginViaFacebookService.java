package com.soco.SoCoClient.onboarding.login.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.facebook.GraphResponse;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginViaFacebookService extends IntentService {

    static final String tag = "LoginViaFacebookService";

    static final String FACEBOOK = "facebook";

    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 10;
    static final int THOUSAND = 1000;

    String userId;
    String userName;
    String userEmail;

    SocoApp socoApp;
    boolean requestStatus;
    GraphResponse requestResponse;

    public LoginViaFacebookService() {
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

        waitForFacebookResponse();

        if(requestStatus) {
            requestResponse = socoApp.facebookUserinfoResponse;
            retrieveUserinfo();
            loginToServer();
        }
        else{
            Log.e(tag, "cannot retrieve userinfo, skip login to server");
        }

        return;
    }

    private void waitForFacebookResponse() {
        Log.v(tag, "wait for response");

        int count = 0;
        requestStatus = socoApp.facebookUserinfoReady;
        requestResponse = null;

        while(!requestStatus && count < WAIT_ITERATION) {   //wait for 10s
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
            requestStatus = socoApp.facebookUserinfoReady;
        }

        return;
    }

    private void retrieveUserinfo() {
        Log.v(tag, "retrieve userinfo");

        JSONObject userinfo = requestResponse.getJSONObject();
        try {
            userId = userinfo.get(JsonKeys.ID).toString();
            Log.d(tag, "user id: " + userId);

            userEmail = userinfo.get(JsonKeys.EMAIL).toString();
            Log.d(tag, "user email: " + userEmail);

            userName = userinfo.get(JsonKeys.NAME).toString();
            Log.d(tag, "user name: " + userName);
        } catch (JSONException e) {
            Log.e(tag, "error retrieving facebook userinfo");
            e.printStackTrace();
        }

        return;
    }

    private void loginToServer(){
        Log.v(tag, "login to server");

        String url = UrlUtil.getSocialLoginUrl();
        Object response = request(
                url,
                FACEBOOK,
                userId,
                userName,
                userEmail
        );

        if (response != null)
            parse(response);

        return;
    }

    public Object request(
            String url,
            String type,
            String id,
            String name,
            String email
    ) {
        Log.v(tag, "create json request");

        JSONObject data = new JSONObject();
        try {
            data.put(JsonKeys.TYPE, type);
            data.put(JsonKeys.ID, id);
            data.put(JsonKeys.NAME, name);
            data.put(JsonKeys.EMAIL, email);
            Log.d(tag, "social login request json: " + data);
        } catch (Exception e) {
            Log.e(tag, "cannot create json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public boolean parse(Object response) {
        Log.d(tag, "parse social login response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            String status = json.getString(JsonKeys.STATUS);
            String user_id = json.getString(JsonKeys.USER_ID);
            Log.d(tag, "social login response, status: " + status + ", user_id: " + user_id);

            //todo
            //update login status flag

        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
