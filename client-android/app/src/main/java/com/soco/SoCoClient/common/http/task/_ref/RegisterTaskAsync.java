package com.soco.SoCoClient.common.http.task._ref;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.http._ref.HttpUtilV1;

import org.json.JSONObject;

public class RegisterTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "LoginTaskAsync";

    String loginEmail, loginPassword;
    String url;
    Context context;

    public RegisterTaskAsync(
            String loginEmail,
            String loginPassword,
            String url,
            Context context
    ){
        Log.i(tag, "Create new HttpTask: " + url);
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
        this.url = url;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty()){
            Log.e(tag, "Cannot get url/type");
            return false;
        }

        execute(loginEmail, loginPassword, url, context);
        return true;
    }


    public static void execute(String loginEmail, String loginPassword, String url,
                               Context context){
        Object response = request(loginEmail, loginPassword, url);
        if (response != null)
            parse(response, context);
    }

    public static Object request(String loginEmail, String loginPassword, String url) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfigV1.JSON_KEY_USERNAME, loginEmail);
            data.put(HttpConfigV1.JSON_KEY_EMAIL, loginEmail);
            data.put(HttpConfigV1.JSON_KEY_PASSWORD, loginPassword);
            data.put(HttpConfigV1.JSON_KEY_PASSWORD2, loginPassword);
            Log.i(tag, "Register Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create RegisterJson post data");
            e.printStackTrace();
        }

        return HttpUtilV1.executeHttpPost(url, data);
    }

    public static boolean parse(Object response, Context context) {
        Log.d(tag, "Process register parse: " + response.toString());
        try {
            String str = response.toString();
            if (str.contains(HttpConfigV1.KEYWORD_REGISTRATION_SUBMITTED)){
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
