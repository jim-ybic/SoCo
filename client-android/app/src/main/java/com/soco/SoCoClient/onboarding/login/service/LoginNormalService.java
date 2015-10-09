package com.soco.SoCoClient.onboarding.login.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.GraphResponse;
import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.ReturnCode;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.dashboard.Dashboard;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginNormalService extends IntentService {

    static final String tag = "LoginNormalService";

    static SocoApp socoApp;

    public LoginNormalService() {
        super("LoginNormalService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        socoApp = (SocoApp) getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(tag, "login normal, handle intent:" + intent
                        + ", login email " + socoApp.loginEmail
                        + ", logig password " + socoApp.loginPassword
        );

        Log.v(tag, "login to server");

        String url = UrlUtil.getLoginUrl();
        String name = "";   //not available from UI
        String email = socoApp.loginEmail;
        String phone = "";  //not available from UI
        String password = socoApp.loginPassword;
        Object response = request(
                url,
                name,
                email,
                phone,
                password
        );

        if (response != null)
            parse(response);

        return;
    }

    public static Object request(
            String url,
            String name,
            String email,
            String phone,
            String password
    ) {
        Log.v(tag, "create json request");

        JSONObject data = new JSONObject();
        try {
            if(!name.isEmpty())
                data.put(JsonKeys.NAME, name);
            data.put(JsonKeys.EMAIL, email);
            if(!phone.isEmpty())
                data.put(JsonKeys.PHONE, phone);
            data.put(JsonKeys.PASSWORD, password);
            Log.d(tag, "normal login request json: " + data);
        } catch (Exception e) {
            Log.e(tag, "cannot create json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public static int parse(Object response) {
        Log.d(tag, "parse register response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            String status = json.getString(JsonKeys.STATUS);
            String user_id = json.getString(JsonKeys.USER_ID);
            String verified = json.getString(JsonKeys.VERIFIED);
            String token = json.getString(JsonKeys.TOKEN);
            String error_code = json.getString(JsonKeys.ERROR_CODE);
            String property = json.getString(JsonKeys.PROPERTY);
            String message = json.getString(JsonKeys.MESSAGE);
            String more_info = json.getString(JsonKeys.MORE_INFO);
            Log.d(tag, "social login response, "
                    + "status: " + status + ", user_id: " + user_id
                    + ", verified: " + verified
                    + ", token: " + token
                    + ", error code: " + error_code + ", property: " + property
                    + ", message: " + message + ", more info: " + more_info);

            if(status.equals(HttpStatus.SUCCESS)){
                Log.d(tag, "login normal: SUCCESS, update status flag");
                socoApp.loginNormalStatus = true;
            }
            else {
                Log.d(tag, "login normal: FAIL, update status flag");
                socoApp.loginNormalStatus = false;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            return ReturnCode.JSON_PARSE_ERROR;
        }

        return ReturnCode.SUCCESS;
    }


}
