package com.soco.SoCoClient.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.soco.SoCoClient.control.Config;

public class Profile {
    static String tag = "Profile";

    public String email;
    public String nickname;
    public String phone;
    public String wechat;

    public Profile(Context context) {
        Log.i(tag, "Create profile from " + Config.PROFILE_FILENAME);
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        this.email = settings.getString(Config.PROFILE_EMAIL,"");
        this.nickname = settings.getString(Config.PROFILE_NICKNAME,"");
        this.phone = settings.getString(Config.PROFILE_PHONE,"");
        this.wechat = settings.getString(Config.PROFILE_WECHAT,"");
    }


}
