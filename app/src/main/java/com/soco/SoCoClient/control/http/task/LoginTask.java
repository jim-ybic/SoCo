package com.soco.SoCoClient.control.http.task;

import android.content.Context;
import android.util.Log;

import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.http.HttpUtil;
import com.soco.SoCoClient.control.util.ProfileUtil;
import com.soco.SoCoClient.control.util.SignatureUtil;

import org.json.JSONObject;


public class LoginTask {

    public static String tag = "LoginTask";

    public static void execute(String loginEmail, String loginPassword,
                               String url, Context context) {
        Object response = request(loginEmail, loginPassword, url);
        if (response != null)
            parse(response, context);
    }

    public static Object request(String loginEmail, String loginPassword, String url) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfig.JSON_KEY_USERNAME, loginEmail);
            data.put(HttpConfig.JSON_KEY_PASSWORD, loginPassword);
            Log.i(tag, "Login Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create Login Json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public static boolean parse(Object response, Context context) {
        Log.d(tag, "Process login parse: " + response.toString());
        try {
            JSONObject json = new JSONObject(response.toString());
            String access_token = json.getString(HttpConfig.JSON_KEY_ACCESS_TOKEN);
            Log.i(tag, "Login success. Get access token: " + access_token);
            ProfileUtil.saveLoginAccessToken(context, access_token);
            String now = SignatureUtil.now();
            ProfileUtil.setLastLoginTimestamp(context, now);
            return true;
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
