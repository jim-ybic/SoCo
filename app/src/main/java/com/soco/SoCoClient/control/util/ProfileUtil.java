package com.soco.SoCoClient.control.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.soco.SoCoClient.control.config.Config;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.http.HttpTask;
import com.soco.SoCoClient.view.LoginActivity;
import com.soco.SoCoClient.view.ShowActiveProjectsActivity;

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

    public static String getLoginAccessToken(Context context){
        Log.d(tag, "Get login access token");
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_LOGIN_ACCESS_TOKEN, "");
    }

    public static String getServerIp(Context context){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_SERVER_IP, "");
    }

    public static void setServerIp(Context context, String servIp){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_SERVER_IP, servIp);
        editor.commit();
    }

    public static String getServerPort(Context context){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_SERVER_PORT, "");
    }

    public static void setServerPort(Context context, String servPort){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_SERVER_PORT, servPort);
        editor.commit();
    }

    public static String getServerRegisterAddress(Context context){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_SERVER_REGISTER_ADDRESS, "");
    }

    public static void setServerRegisterAddress(Context context, String regiAddr){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_SERVER_REGISTER_ADDRESS, regiAddr);
        editor.commit();
    }

    public static String getServerLoginAddr(Context context){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_SERVER_LOGIN_ADDRESS, "");
    }

    public static void setServerLoginAddr(Context context, String loginAddr){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_SERVER_LOGIN_ADDRESS, loginAddr);
        editor.commit();
    }

    public static String getCreateProjectAddr(Context context){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_CREATE_PROJECT_ADDRESS, "");
    }

    public static void setCreateProjectAddr(Context context, String addr){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_CREATE_PROJECT_ADDRESS, addr);
        editor.commit();
    }

    public static String getArchiveProjectAddr(Context context){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_ARCHIVE_PROJECT_ADDRESS, "");
    }

    public static void setArchiveProjectAddr(Context context, String addr){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_ARCHIVE_PROJECT_ADDRESS, addr);
        editor.commit();
    }

    public static String getUpdateProjectNameAddr(Context context){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_UPDATE_PROJECT_NAME, "");
    }

    public static void setUpdateProjectNameAddr(Context context, String addr){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_UPDATE_PROJECT_NAME, addr);
        editor.commit();
    }

    public static String getSetProjectAttributeAddr(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_SET_PROJECT_ATTRIBUTE, "");
    }

    public static void setSetProjectAttributeAddr(Context context, String addr){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_SET_PROJECT_ATTRIBUTE, addr);
        editor.commit();
    }


    public static String getLoginUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = getServerLoginAddr(context);
        String url = "http://" + ip + ":" + port + path;
        Log.i(tag, "Login url: " + url);
        return url;
    }

    public static String getRegisterUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = getServerRegisterAddress(context);
        String url = "http://" + ip + ":" + port + path;
        Log.i(tag, "Register url: " + url);
        return url;
    }

    public static String getCreateProjectUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = getCreateProjectAddr(context);
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpTask.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Create project url: " + url);
        return url;
    }

    public static String getArchiveProjectUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = getArchiveProjectAddr(context);
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpTask.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Archive project url: " + url);
        return url;
    }

    public static String getUpdateProjectNameUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = getUpdateProjectNameAddr(context);
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpTask.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Rename project url: " + url);
        return url;
    }

    public static String getSetProjectAttributeUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = getSetProjectAttributeAddr(context);
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpTask.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Set project attribute url: " + url);
        return url;
    }

    public static String getHeartbeatUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_HEARTBEAT;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpTask.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Heartbeat url: " + url);
        return url;
    }


    public static String getLoginEmail(Context context){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_LOGIN_EMAIL, "");
    }

    public static void setLoginEmail(Context context, String loginEmail){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_LOGIN_EMAIL, loginEmail);
        editor.commit();
    }

    public static String getLoginPassword(Context context){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_LOGIN_PASSWORD, "");
    }

    public static void setLoginPassword(Context context, String loginPassword){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_LOGIN_PASSWORD, loginPassword);
        editor.commit();
    }

    public static String getLastLoginTimestamp(Context context){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        return settings.getString(Config.PROFILE_LAST_LOGIN_TIMESTAMP, "");
    }

    public static void setLastLoginTimestamp(Context context, String timestamp){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_LAST_LOGIN_TIMESTAMP, timestamp);
        editor.commit();
    }

    public static void clearUserInfo(Context context){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PROFILE_LOGIN_EMAIL, "");
        editor.putString(Config.PROFILE_LOGIN_PASSWORD, "");
        editor.putString(Config.PROFILE_NICKNAME, "");
        editor.putString(Config.PROFILE_PHONE, "");
        editor.putString(Config.PROFILE_WECHAT, "");
        editor.commit();
    }
}
