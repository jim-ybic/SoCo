package com.soco.SoCoClient.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.soco.SoCoClient.common.util.SocoApp;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginController {

    static final String tag = "LoginController";

    static final String TYPE = "facebook";

//    Activity mActivity;
//    Context mContext;
//    String mId;
//    String mName;

//    public void loginToServerViaFacebook(
//            Activity activity,
//            Context context,
//            String id,
//            String name
//    ){
//        Log.d(tag, "login to server");
//
//        this.activity = activity;
//        this.context = context;
//        this.id = id;
//        this.name = name;
//
//        requestFacebookUserInfo();

//        String loginEmail = "";
//        String loginPassword = "";
//        String url = UrlUtil.getLoginUrl(context);
//        SocialLoginTask task = new SocialLoginTask(loginEmail, loginPassword, url, context);
//        task.execute();
//    }

//    private void wait2s(){
//        long endTime = System.currentTimeMillis() + 2*1000;
//        while (System.currentTimeMillis() < endTime) {
//            synchronized (this) {
//                try {
//                    wait(endTime - System.currentTimeMillis());
//                } catch (Exception e) {
//                    Log.e(tag, "Error in waiting");
//                }
//            }
//        }
//    }

    SocoApp socoApp;

    public void requestFacebookUserInfo(Context context){
        Log.d(tag, "request facebook userinfo");

        socoApp = (SocoApp)context;
//        mActivity = activity;
//        mContext = context;
//        mId = id;
//        mName = name;

//        int count = 0;
//        boolean wait = true;
//        while (wait && count <= 5) {
//            Log.d(tag, "wait for " + count*2 + "s");
//            wait2s();
//            count ++;

        ((SocoApp)context).facebookUserinfoReady = false;

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,about,bio,birthday,email,first_name,gender,locale,timezone");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        Log.d(tag, "me response: " + response);
                        JSONObject obj = response.getJSONObject();
//                            try {
//                                String email = obj.get("email").toString();
//                                Log.d(tag, "user email: " + email);

                        socoApp.facebookUserinfoReady = true;
                        socoApp.facebookUserinfoResponse = response;
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                    }
                }
        ).executeAsync();
//        }

//        if(activity == null)
//            Log.e(tag, "activity is null");
//        else
//            Log.d(tag, "acitivty: " +activity);


    }
}
