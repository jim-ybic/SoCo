package com.soco.SoCoClient.onboarding.login;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.soco.SoCoClient.common.util.SocoApp;

public class LoginController {

    static final String tag = "LoginController";

    public static void requestFacebookUserInfo(Context context){
        Log.d(tag, "request facebook userinfo");

        final SocoApp socoApp = (SocoApp)context;

        socoApp.facebookUserinfoReady = false;

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
                        Log.d(tag, "/me response: " + response);
                        socoApp.facebookUserinfoReady = true;
                        socoApp.facebookUserinfoResponse = response;
                    }
                }
        ).executeAsync();
    }


}
