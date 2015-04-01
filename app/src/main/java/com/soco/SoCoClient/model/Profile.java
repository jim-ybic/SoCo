package com.soco.SoCoClient.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.soco.SoCoClient.control.config.Config;

public class Profile {
    static String tag = "Profile";

    public String email, password;
    public String nickname;
    public String phone, wechat, lastLoginTimestamp;

    public Profile(Context context) {
        Log.i(tag, "Create profile from " + Config.PROFILE_FILENAME);
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        this.email = settings.getString(Config.PROFILE_LOGIN_EMAIL,"");
        this.password = settings.getString(Config.PROFILE_LOGIN_PASSWORD,"");
        this.nickname = settings.getString(Config.PROFILE_NICKNAME,"");
        this.phone = settings.getString(Config.PROFILE_PHONE,"");
        this.wechat = settings.getString(Config.PROFILE_WECHAT,"");
        this.lastLoginTimestamp = settings.getString(Config.PROFILE_LAST_LOGIN_TIMESTAMP,"");
    }

}
