package com.soco.SoCoClient.onboarding.login.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.GraphResponse;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.dashboard.Dashboard;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginNormalService extends IntentService {

    static final String tag = "LoginNormalService";

    public LoginNormalService() {
        super("LoginNormalService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(tag, "login normal, handle intent:" + intent);

        //todo
        //login steps
        //set socoapp.loginnormalstatus
        //if success...
        //if fail...

        return;
    }

}
