package com.soco.SoCoClient.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.soco.SoCoClient.control.config.GeneralConfig;

public class Profile {
    static String tag = "Profile";

    public String email, password;
    public String nickname;
    public String phone, wechat, lastLoginTimestamp;

    public Profile(Context context) {
        Log.i(tag, "Create profile from " + GeneralConfig.PROFILE_FILENAME);
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);

        this.email = settings.getString(GeneralConfig.PROFILE_LOGIN_EMAIL,"");
        this.password = settings.getString(GeneralConfig.PROFILE_LOGIN_PASSWORD,"");
        this.nickname = settings.getString(GeneralConfig.PROFILE_NICKNAME,"");
        this.phone = settings.getString(GeneralConfig.PROFILE_PHONE,"");
        this.wechat = settings.getString(GeneralConfig.PROFILE_WECHAT,"");
        this.lastLoginTimestamp = settings.getString(GeneralConfig.PROFILE_LAST_LOGIN_TIMESTAMP,"");
    }

}
