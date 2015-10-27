package com.soco.SoCoClient.onboarding.register.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.JsonSimulator;
import com.soco.SoCoClient.common.util.SocoApp;

import org.json.JSONObject;

public class RegisterService extends IntentService {

    static final String tag = "RegisterService";


    static SocoApp socoApp;

    public RegisterService() {
        super("RegisterService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        socoApp = (SocoApp) getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(tag, "register service, handle intent:" + intent
                        + ", register name: " + socoApp.registerName
                        + ", register email: " + socoApp.registerEmail
                        + ", register phone: " + socoApp.registerPhone
                        + ", register password: " + socoApp.registerPassword
                        + ", register location: " + socoApp.registerLocation
        );


        String url = UrlUtil.getRegisterUrl();
        Object response = request(
                url,
                socoApp.registerName,
                socoApp.registerEmail,
                socoApp.registerPhone,
                socoApp.registerPassword,
                socoApp.registerLocation
        );


        if (response != null) {
            Log.v(tag, "set register response flag as true");
            socoApp.registerResponse = true;

            Log.v(tag, "parse response");
            parse(response);
        }
        else {
            Log.e(tag, "response is null, cannot parse");

            if(socoApp.USE_SIMILATOR_REGISTER){
                Log.w(tag, "testing mode: use simulator for json response");

                Log.v(tag, "set register response flag as true");
                socoApp.registerResponse = true;

                Log.v(tag, "parse simulated response");
                parse(JsonSimulator.RegisterSuccessResponse());
            }
        }

        return;
    }

    public static Object request(
            String url,
            String name,
            String email,
            String phone,
            String password,
            String location
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
            if(!location.isEmpty())
                data.put(JsonKeys.LOCATION, location);

            Log.d(tag, "register request json: " + data);
        } catch (Exception e) {
            Log.e(tag, "cannot create json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public static boolean parse(Object response) {
        Log.d(tag, "parse register response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                Log.v(tag, "register success, retrieve user id and token");
                String user_id = json.getString(JsonKeys.USER_ID);
                String token = json.getString(JsonKeys.TOKEN);
                Log.d(tag, "register success, " +
                        "user id: " + user_id + ", token: " + token
                );
                socoApp.registerResult = true;
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "register fail, " +
                        "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
                socoApp.registerResult = false;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            socoApp.registerResult = false;
            return false;
        }

        return true;
    }

}
