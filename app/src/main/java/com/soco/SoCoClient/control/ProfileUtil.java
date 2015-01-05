package com.soco.SoCoClient.control;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ProfileUtil {

    // Config file
    public static String PROFILE_FILENAME = "SoCo.config";
    public static String PROFILE_EMAIL = "email";
    public static String PROFILE_NICKNAME = "nickname";
    public static String PROFILE_PHONE = "phone";
    public static String PROFILE__WECHAT = "wechat";

    public static void ready(Context context, String loginEmail) {
        Log.i("profile", "Check if profile is ready for: " + loginEmail);

        SharedPreferences settings = context.getSharedPreferences(PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        String email = settings.getString(PROFILE_EMAIL, "");
        if(email.isEmpty()) {
            Log.i("profile", "Create new profile, " + PROFILE_EMAIL + ":" + loginEmail);
            editor.putString(PROFILE_EMAIL, loginEmail);
            editor.commit();
        } else
            Log.i("profile", "Profile is ready for: " + loginEmail);
    }

    public static String getNickname(Context context, String loginEmail) {
        Log.i("profile", "Get nickname for: " + loginEmail);

        SharedPreferences settings = context.getSharedPreferences(PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        String n = settings.getString(PROFILE_NICKNAME, "");
        String nickname;
        if (n.isEmpty())
            nickname =  loginEmail;
        else
            nickname = n;

        Log.i("profile", "Found nickname: " + nickname);
        return nickname;
    }
}
