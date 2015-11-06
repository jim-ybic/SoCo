package com.soco.SoCoClient.common.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.soco.SoCoClient._ref.GeneralConfigV1;

@Deprecated
public class Profile {
    private static final String NICKNAME_NOT_SET = "NICKNAME NOT SET";
    static String tag = "Profile";

    public String email, password;
    public String username;
    public String phone, wechat, lastLoginTimestamp;

    public Profile(Context context) {
        Log.i(tag, "Create profile from " + GeneralConfigV1.PROFILE_FILENAME);
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);

        this.email = settings.getString(GeneralConfigV1.PROFILE_LOGIN_EMAIL,"");
        this.password = settings.getString(GeneralConfigV1.PROFILE_LOGIN_PASSWORD,"");
        this.username = settings.getString(GeneralConfigV1.PROFILE_USERNAME,"");
        this.phone = settings.getString(GeneralConfigV1.PROFILE_PHONE,"");
        this.wechat = settings.getString(GeneralConfigV1.PROFILE_WECHAT,"");
        this.lastLoginTimestamp = settings.getString(GeneralConfigV1.PROFILE_LAST_LOGIN_TIMESTAMP,"");
    }

    public void ready(Context context, String loginEmail) {
        Log.i(tag, "Check if profile is ready for: " + loginEmail);

        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        String email = settings.getString(GeneralConfigV1.PROFILE_EMAIL, "");
        if(email.isEmpty()) {
            Log.i(tag, "Create new profile, " + GeneralConfigV1.PROFILE_EMAIL + ":" + loginEmail);
            editor.putString(GeneralConfigV1.PROFILE_EMAIL, loginEmail);
            editor.commit();
        } else
            Log.i(tag, "Profile is ready for: " + loginEmail);
    }

    public String getUsername(Context context, String loginEmail) {
        Log.i(tag, "Get username for: " + loginEmail);

        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        String n = settings.getString(GeneralConfigV1.PROFILE_USERNAME, "");
        String username;
        if (n.isEmpty())
            username =  loginEmail;
        else
            username = n;

        Log.i(tag, "Found username: " + username);
        return username;
    }

    public void save(
            Context context,
            String username,
            String phone,
            String wechat
    ){
        Log.i(tag, "Save profile to " + GeneralConfigV1.PROFILE_FILENAME);

        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(GeneralConfigV1.PROFILE_USERNAME, username);
        editor.putString(GeneralConfigV1.PROFILE_PHONE, phone);
        editor.putString(GeneralConfigV1.PROFILE_WECHAT, wechat);
        editor.commit();
        Toast.makeText(context.getApplicationContext(), "Profile saved.",
                Toast.LENGTH_SHORT).show();

        this.username = username;
        this.phone = phone;
        this.wechat = wechat;
    }

    public void saveLoginAccessToken(Context context, String token){
        Log.i(tag, "Save login access token: " + token);
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfigV1.PROFILE_LOGIN_ACCESS_TOKEN, token);
        editor.commit();
        Log.i(tag, "Save complete");
    }

    public static String getLoginAccessToken(Context context){
        Log.d(tag, "Get login access token");
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfigV1.PROFILE_LOGIN_ACCESS_TOKEN, "");
    }

    public String getLoginEmail(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfigV1.PROFILE_LOGIN_EMAIL, "");
    }

    public String getNickname(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        String name = settings.getString(GeneralConfigV1.PROFILE_USERNAME, "");
        if(name == null || name.isEmpty())
            name = NICKNAME_NOT_SET;
        return name;
    }

    public void setLoginEmail(Context context, String loginEmail){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfigV1.PROFILE_LOGIN_EMAIL, loginEmail);
        editor.commit();
    }

//    public static String getLastLoginEmail(Context context){
//        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
//        return settings.getString(GeneralConfigV1.PROFILE_LAST_LOGIN_EMAIL, "");
//    }
//
//    public static void setLastLoginEmail(Context context, String loginEmail){
//        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString(GeneralConfigV1.PROFILE_LAST_LOGIN_EMAIL, loginEmail);
//        editor.commit();
//    }

//    public static String getLastLoginPassword(Context context){
//        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
//        return settings.getString(GeneralConfigV1.PROFILE_LAST_LOGIN_PASSWORD, "");
//    }
//
//    public static void setLASTLoginPassword(Context context, String loginPassword){
//        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString(GeneralConfigV1.PROFILE_LAST_LOGIN_PASSWORD, loginPassword);
//        editor.commit();
//    }

    public String getLoginPassword(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfigV1.PROFILE_LOGIN_PASSWORD, "");
    }

    public void setLoginPassword(Context context, String loginPassword){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfigV1.PROFILE_LOGIN_PASSWORD, loginPassword);
        editor.commit();
    }

    public String getLastLoginTimestamp(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfigV1.PROFILE_LAST_LOGIN_TIMESTAMP, "");
    }

    public void setLastLoginTimestamp(Context context, String timestamp){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfigV1.PROFILE_LAST_LOGIN_TIMESTAMP, timestamp);
        editor.commit();
    }

    public void logout(Context context){
        Log.d(tag, "delete user login access token");
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
//        editor.putString(GeneralConfigV1.PROFILE_LOGIN_EMAIL, "");
//        editor.putString(GeneralConfigV1.PROFILE_LOGIN_PASSWORD, "");
//        editor.putString(GeneralConfigV1.PROFILE_USERNAME, "");
//        editor.putString(GeneralConfigV1.PROFILE_PHONE, "");
//        editor.putString(GeneralConfigV1.PROFILE_WECHAT, "");
//        editor.putString(GeneralConfigV1.PROFILE_LOGIN_ACCESS_TOKEN, "");
//        editor.remove(GeneralConfigV1.PROFILE_LOGIN_EMAIL);
//        editor.remove(GeneralConfigV1.PROFILE_LOGIN_PASSWORD);
//        editor.remove(GeneralConfigV1.PROFILE_USERNAME);
//        editor.remove(GeneralConfigV1.PROFILE_PHONE);
//        editor.remove(GeneralConfigV1.PROFILE_WECHAT);
        editor.remove(GeneralConfigV1.PROFILE_LOGIN_ACCESS_TOKEN);
        editor.commit();
    }

}
