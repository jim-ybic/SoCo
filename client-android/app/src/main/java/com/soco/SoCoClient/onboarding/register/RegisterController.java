package com.soco.SoCoClient.onboarding.register;

import android.util.Log;

import com.soco.SoCoClient.common.ReturnCode;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;

import org.json.JSONObject;

public class RegisterController {

    static final String tag = "RegisterController";

    public static int registerOnServer(
            String name,
            String email,
            String phone,
            String password,
            String location
    ){
        Log.d(tag, "register on server");

        if(email.isEmpty() || password.isEmpty()){
            Log.e(tag, "error: email and password cannot be empty");
            return ReturnCode.EMAIL_OR_PASSWORD_EMPTY;
        }

        String url = UrlUtil.getRegisterUrl();
        Object response = request(
                url,
                name,
                email,
                phone,
                password,
                location
        );

        if (response != null)
            return parse(response);
        else
            return ReturnCode.SERVER_RESPONSE_NULL;
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

    public static int parse(Object response) {
        Log.d(tag, "parse register response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            String status = json.getString(JsonKeys.STATUS);
            String user_id = json.getString(JsonKeys.USER_ID);
            String token = json.getString(JsonKeys.TOKEN);
            String error_code = json.getString(JsonKeys.ERROR_CODE);
            String property = json.getString(JsonKeys.PROPERTY);
            String message = json.getString(JsonKeys.MESSAGE);
            String more_info = json.getString(JsonKeys.MORE_INFO);
            Log.d(tag, "social login response, "
                    + "status: " + status + ", user_id: " + user_id + ", token: " + token
                    + ", error code: " + error_code + ", property: " + property
                    + ", message: " + message + ", more info: " + more_info);

            //todo
            //update register status flag

        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            return ReturnCode.JSON_PARSE_ERROR;
        }

        return ReturnCode.SUCCESS;
    }




}
