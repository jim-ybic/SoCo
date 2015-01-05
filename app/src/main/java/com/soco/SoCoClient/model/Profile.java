package com.soco.SoCoClient.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Profile {
    // Config file
    public static String PROFILE_FILENAME = "SoCo.config";
    public static String PROFILE_EMAIL = "email";
    public static String PROFILE_NICKNAME = "nickname";
    public static String PROFILE_PHONE = "phone";
    public static String PROFILE_WECHAT = "wechat";
    public String email;
    public String nickname;
    public String phone;
    public String wechat;

    public Profile() {}

    public Profile(Context context) {
        Log.i("profile", "Create profile from " + PROFILE_FILENAME);
        SharedPreferences settings = context.getSharedPreferences(PROFILE_FILENAME, 0);

        this.email = settings.getString(PROFILE_EMAIL,"");
        this.nickname = settings.getString(PROFILE_NICKNAME,"");
        this.phone = settings.getString(PROFILE_PHONE,"");
        this.wechat = settings.getString(PROFILE_WECHAT,"");
    }
}
