package com.soco.SoCoClient.control.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.soco.SoCoClient.control.Config;

public class ProfileUtil {

    public static String tag = "ProfileUtil";

    public static void ready(Context context, String loginEmail) {
        Log.i(tag, "Check if profile is ready for: " + loginEmail);

        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        String email = settings.getString(Config.PROFILE_EMAIL, "");
        if(email.isEmpty()) {
            Log.i(tag, "Create new profile, " + Config.PROFILE_EMAIL + ":" + loginEmail);
            editor.putString(Config.PROFILE_EMAIL, loginEmail);
            editor.commit();
        } else
            Log.i(tag, "Profile is ready for: " + loginEmail);
    }

    public static String getNickname(Context context, String loginEmail) {
        Log.i(tag, "Get nickname for: " + loginEmail);

        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        String n = settings.getString(Config.PROFILE_NICKNAME, "");
        String nickname;
        if (n.isEmpty())
            nickname =  loginEmail;
        else
            nickname = n;

        Log.i("profile", "Found nickname: " + nickname);
        return nickname;
    }

    public static void save(Context context, String nickname, String phone, String wechat){
        Log.i(tag, "Save profile to " + Config.PROFILE_FILENAME);

        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(Config.PROFILE_NICKNAME, nickname);
        editor.putString(Config.PROFILE_PHONE, phone);
        editor.putString(Config.PROFILE_WECHAT, wechat);
        editor.commit();
        Toast.makeText(context.getApplicationContext(), "Profile saved.",
                Toast.LENGTH_SHORT).show();
    }

    public static void saveLoginAccessToken(Context context, String token){
        Log.i(tag, "Save login access token: " + token);
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_LOGIN_ACCESS_TOKEN, token);
        editor.commit();
        Log.i(tag, "Save complete");
    }
}
