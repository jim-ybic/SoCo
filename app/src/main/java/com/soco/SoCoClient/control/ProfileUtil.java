package com.soco.SoCoClient.control;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.soco.SoCoClient.model.Profile;

public class ProfileUtil {

    public static void ready(Context context, String loginEmail) {
        Log.i("profile", "Check if profile is ready for: " + loginEmail);

        SharedPreferences settings = context.getSharedPreferences(Profile.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        String email = settings.getString(Profile.PROFILE_EMAIL, "");
        if(email.isEmpty()) {
            Log.i("profile", "Create new profile, " + Profile.PROFILE_EMAIL + ":" + loginEmail);
            editor.putString(Profile.PROFILE_EMAIL, loginEmail);
            editor.commit();
        } else
            Log.i("profile", "Profile is ready for: " + loginEmail);
    }

    public static String getNickname(Context context, String loginEmail) {
        Log.i("profile", "Get nickname for: " + loginEmail);

        SharedPreferences settings = context.getSharedPreferences(Profile.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        String n = settings.getString(Profile.PROFILE_NICKNAME, "");
        String nickname;
        if (n.isEmpty())
            nickname =  loginEmail;
        else
            nickname = n;

        Log.i("profile", "Found nickname: " + nickname);
        return nickname;
    }

    public static void save(Context context, String nickname, String phone, String wechat){
        Log.i("profile", "Save profile to " + Profile.PROFILE_FILENAME);

        SharedPreferences settings = context.getSharedPreferences(Profile.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(Profile.PROFILE_NICKNAME, nickname);
        editor.putString(Profile.PROFILE_PHONE, phone);
        editor.putString(Profile.PROFILE_WECHAT, wechat);
        editor.commit();
        Toast.makeText(context.getApplicationContext(), "Profile saved.",
                Toast.LENGTH_SHORT).show();
    }
}
