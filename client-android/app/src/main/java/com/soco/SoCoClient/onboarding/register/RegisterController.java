package com.soco.SoCoClient.onboarding.register;

import android.util.Log;

import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.UrlUtil;

import org.json.JSONObject;

public class RegisterController {

    static final String tag = "RegisterController";

    public static void registerOnServer(){
        Log.d(tag, "register on server");

        String url = UrlUtil.getRegisterUrl();
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

        //todo
        //get url
        //build json
        //send register request
        //parse response
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
            data.put(TYPE, type);
            data.put(FACEBOOK_FIELD_ID, id);
            data.put(FACEBOOK_FIELD_NAME, name);
            data.put(FACEBOOK_FIELD_EMAIL, email);
            Log.d(tag, "social login request json: " + data);
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
            String status = json.getString(STATUS);
            String user_id = json.getString(USER_ID);
            Log.d(tag, "social login response, status: " + status + ", user_id: " + user_id);
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }


}
