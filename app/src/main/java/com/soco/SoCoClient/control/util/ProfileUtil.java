package com.soco.SoCoClient.control.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.soco.SoCoClient.control.config.GeneralConfig;
import com.soco.SoCoClient.control.config.HttpConfig;

public class ProfileUtil {

    public static String tag = "ProfileUtil";

    public static void ready(Context context, String loginEmail) {
        Log.i(tag, "Check if profile is ready for: " + loginEmail);

        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        String email = settings.getString(GeneralConfig.PROFILE_EMAIL, "");
        if(email.isEmpty()) {
            Log.i(tag, "Create new profile, " + GeneralConfig.PROFILE_EMAIL + ":" + loginEmail);
            editor.putString(GeneralConfig.PROFILE_EMAIL, loginEmail);
            editor.commit();
        } else
            Log.i(tag, "Profile is ready for: " + loginEmail);
    }

    public static String getNickname(Context context, String loginEmail) {
        Log.i(tag, "Get nickname for: " + loginEmail);

        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        String n = settings.getString(GeneralConfig.PROFILE_NICKNAME, "");
        String nickname;
        if (n.isEmpty())
            nickname =  loginEmail;
        else
            nickname = n;

        Log.i("profile", "Found nickname: " + nickname);
        return nickname;
    }

    public static void save(Context context, String nickname, String phone, String wechat){
        Log.i(tag, "Save profile to " + GeneralConfig.PROFILE_FILENAME);

        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(GeneralConfig.PROFILE_NICKNAME, nickname);
        editor.putString(GeneralConfig.PROFILE_PHONE, phone);
        editor.putString(GeneralConfig.PROFILE_WECHAT, wechat);
        editor.commit();
        Toast.makeText(context.getApplicationContext(), "Profile saved.",
                Toast.LENGTH_SHORT).show();
    }

    public static void saveLoginAccessToken(Context context, String token){
        Log.i(tag, "Save login access token: " + token);
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfig.PROFILE_LOGIN_ACCESS_TOKEN, token);
        editor.commit();
        Log.i(tag, "Save complete");
    }

    public static String getLoginAccessToken(Context context){
        Log.d(tag, "Get login access token");
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_LOGIN_ACCESS_TOKEN, "");
    }

    public static String getServerIp(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_SERVER_IP, "");
    }

    public static void setServerIp(Context context, String servIp){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfig.PROFILE_SERVER_IP, servIp);
        editor.commit();
    }

    public static String getServerPort(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_SERVER_PORT, "");
    }

    public static void setServerPort(Context context, String servPort){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfig.PROFILE_SERVER_PORT, servPort);
        editor.commit();
    }

    public static String getLoginUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_LOGIN;
        String url = "http://" + ip + ":" + port + path;
        Log.i(tag, "Login url: " + url);
        return url;
    }

    public static String getRegisterUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_REGISTER;
        String url = "http://" + ip + ":" + port + path;
        Log.i(tag, "Register url: " + url);
        return url;
    }

    public static String getCreateProjectUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_CREATE_PROJECT;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Create project url: " + url);
        return url;
    }

    public static String getArchiveProjectUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_ARCHIVE_PROJECT;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Archive project url: " + url);
        return url;
    }

    public static String getUpdateProjectNameUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_RENAME_PROJECT;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Rename project url: " + url);
        return url;
    }

    public static String getSetProjectAttributeUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_SET_PROJECT_ATTRIBUTE;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Set project attribute url: " + url);
        return url;
    }

    public static String getHeartbeatUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_HEARTBEAT;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Heartbeat url: " + url);
        return url;
    }

    public static String getInviteProjectMemberUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_INVITE_PROJECT_MEMBER;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Invite project member url: " + url);
        return url;
    }

    public static String getLoginEmail(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_LOGIN_EMAIL, "");
    }

    public static void setLoginEmail(Context context, String loginEmail){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfig.PROFILE_LOGIN_EMAIL, loginEmail);
        editor.commit();
    }

//    public static String getLastLoginEmail(Context context){
//        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
//        return settings.getString(GeneralConfig.PROFILE_LAST_LOGIN_EMAIL, "");
//    }
//
//    public static void setLastLoginEmail(Context context, String loginEmail){
//        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString(GeneralConfig.PROFILE_LAST_LOGIN_EMAIL, loginEmail);
//        editor.commit();
//    }

//    public static String getLastLoginPassword(Context context){
//        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
//        return settings.getString(GeneralConfig.PROFILE_LAST_LOGIN_PASSWORD, "");
//    }
//
//    public static void setLASTLoginPassword(Context context, String loginPassword){
//        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString(GeneralConfig.PROFILE_LAST_LOGIN_PASSWORD, loginPassword);
//        editor.commit();
//    }

    public static String getLoginPassword(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_LOGIN_PASSWORD, "");
    }

    public static void setLoginPassword(Context context, String loginPassword){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfig.PROFILE_LOGIN_PASSWORD, loginPassword);
        editor.commit();
    }

    public static String getLastLoginTimestamp(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_LAST_LOGIN_TIMESTAMP, "");
    }

    public static void setLastLoginTimestamp(Context context, String timestamp){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfig.PROFILE_LAST_LOGIN_TIMESTAMP, timestamp);
        editor.commit();
    }

    public static void logout(Context context){
        Log.d(tag, "delete user login access token");
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
//        editor.putString(GeneralConfig.PROFILE_LOGIN_EMAIL, "");
//        editor.putString(GeneralConfig.PROFILE_LOGIN_PASSWORD, "");
//        editor.putString(GeneralConfig.PROFILE_NICKNAME, "");
//        editor.putString(GeneralConfig.PROFILE_PHONE, "");
//        editor.putString(GeneralConfig.PROFILE_WECHAT, "");
//        editor.putString(GeneralConfig.PROFILE_LOGIN_ACCESS_TOKEN, "");
//        editor.remove(GeneralConfig.PROFILE_LOGIN_EMAIL);
//        editor.remove(GeneralConfig.PROFILE_LOGIN_PASSWORD);
//        editor.remove(GeneralConfig.PROFILE_NICKNAME);
//        editor.remove(GeneralConfig.PROFILE_PHONE);
//        editor.remove(GeneralConfig.PROFILE_WECHAT);
        editor.remove(GeneralConfig.PROFILE_LOGIN_ACCESS_TOKEN);
        editor.commit();
    }

    public static String getJoinProjectByInviteUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_JOIN_PROJECT_BY_INVITE;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Join project by invite url: " + url);
        return url;
    }

    public static String getSendMessageUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_SEND_MESSAGE;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Join project by invite url: " + url);
        return url;
    }


    public static String getRetrieveMessageUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_RECEIVE_MESSAGE;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Receive message url: " + url);
        return url;
    }

    public static String getAckRetrieveMessageUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_ACK_RECEIVE_MESSAGE;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Ack receive message url: " + url);
        return url;
    }
}
