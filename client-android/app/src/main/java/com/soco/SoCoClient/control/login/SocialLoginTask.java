package com.soco.SoCoClient.control.login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control._ref.HttpConfigV1;
import com.soco.SoCoClient.control.common.SignatureUtil;
import com.soco.SoCoClient.control.common.SocoApp;
import com.soco.SoCoClient.control.http._ref.HttpUtilV1;
import com.soco.SoCoClient.model.Profile;

import org.json.JSONObject;

public class SocialLoginTask extends AsyncTask<Void, Void, Boolean> {

    static String tag = "SocialLoginTask";

//    String loginEmail, loginPassword;
    String type;
    String id;
    String name;

    String url;
    Context context;
//    Profile profile;

    public SocialLoginTask(
            String loginEmail,
            String loginPassword,
            String url,
            Context context
    ){
        Log.i(tag, "Create new HttpTask: " + url);
//        this.loginEmail = loginEmail;
//        this.loginPassword = loginPassword;
        this.url = url;
        this.context = context;

//        profile = ((SocoApp)context).profile;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty()){
            Log.e(tag, "Cannot get url/type");
            return false;
        }

        execute(
//                loginEmail, loginPassword,
                url, context);
        return true;
    }

    public void execute(
//            String loginEmail, String loginPassword,
            String url, Context context
    ) {
        Object response = request(
//                loginEmail, loginPassword,
                url);
        if (response != null)
            parse(response, context);
    }

    public Object request(
//            String loginEmail, String loginPassword,
            String url) {
        JSONObject data = new JSONObject();
        try {
//            data.put(HttpConfigV1.JSON_KEY_USERNAME, loginEmail);
//            data.put(HttpConfigV1.JSON_KEY_PASSWORD, loginPassword);
            Log.i(tag, "Login Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create Login Json post data");
            e.printStackTrace();
        }

        return HttpUtilV1.executeHttpPost(url, data);
    }

    public boolean parse(Object response, Context context) {
        Log.d(tag, "Process login parse: " + response.toString());
        try {
            JSONObject json = new JSONObject(response.toString());
            String access_token = json.getString(HttpConfigV1.JSON_KEY_ACCESS_TOKEN);
            Log.i(tag, "Login success. Get access token: " + access_token);

//            profile.saveLoginAccessToken(context, access_token);
//            String now = SignatureUtil.now();
//            profile.setLastLoginTimestamp(context, now);
            return true;
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

}
