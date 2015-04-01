package com.soco.SoCoClient.control.http.task;

import android.content.Context;
import android.util.Log;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.http.HttpTask;
import com.soco.SoCoClient.control.http.HttpUtil;

import org.json.JSONObject;

public class RegisterTask {

    public static String tag = "RegisterTask";

    public static void execute(String loginEmail, String loginPassword, String url,
                               Context context){
        Object response = request(loginEmail, loginPassword, url);
        if (response != null)
            parse(response, context);
    }

    public static Object request(String loginEmail, String loginPassword, String url) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfig.JSON_KEY_USERNAME, loginEmail);
            data.put(HttpConfig.JSON_KEY_EMAIL, loginEmail);
            data.put(HttpConfig.JSON_KEY_PASSWORD, loginPassword);
            data.put(HttpConfig.JSON_KEY_PASSWORD2, loginPassword);
            Log.i(tag, "Register Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create RegisterJson post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public static boolean parse(Object response, Context context) {
        Log.d(tag, "Process register parse: " + response.toString());
        try {
            String str = response.toString();
            if (str.contains(HttpTask.KEYWORD_REGISTRATION_SUBMITTED)){
                SocoApp app = (SocoApp) context;
                app.setRegistrationStatus(SocoApp.REGISTRATION_STATUS_SUCCESS);
                Log.i(tag, "Set registration status: success");
                return true;
            }
            else {
                SocoApp app = (SocoApp) context;
                app.setRegistrationStatus(SocoApp.REGISTRATION_STATUS_FAIL);
                Log.i(tag, "Set registration status: fail");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
