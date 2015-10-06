package com.soco.SoCoClient.login;

import android.content.Context;
import android.util.Log;

import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.login.sociallogin.SocialLoginTask;

public class LoginController {

    static final String tag = "LoginController";

    public void loginToServer(Context context){
        Log.d(tag, "login to server");

        String loginEmail = "";
        String loginPassword = "";
        String url = UrlUtil.getLoginUrl(context);
        SocialLoginTask task = new SocialLoginTask(loginEmail, loginPassword, url, context);
        task.execute();
    }
}
