package com.soco.SoCoClient.obsolete.v1.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.soco.SoCoClient.obsolete.v1.control.config.GeneralConfig;
import com.soco.SoCoClient.obsolete.v1.control.config.HttpConfig;

public class Profile {
    private static final String NICKNAME_NOT_SET = "NICKNAME NOT SET";
    static String tag = "Profile";

    public String email, password;
    public String username;
    public String phone, wechat, lastLoginTimestamp;

    public Profile(Context context) {
        Log.i(tag, "Create profile from " + GeneralConfig.PROFILE_FILENAME);
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);

        this.email = settings.getString(GeneralConfig.PROFILE_LOGIN_EMAIL,"");
        this.password = settings.getString(GeneralConfig.PROFILE_LOGIN_PASSWORD,"");
        this.username = settings.getString(GeneralConfig.PROFILE_USERNAME,"");
        this.phone = settings.getString(GeneralConfig.PROFILE_PHONE,"");
        this.wechat = settings.getString(GeneralConfig.PROFILE_WECHAT,"");
        this.lastLoginTimestamp = settings.getString(GeneralConfig.PROFILE_LAST_LOGIN_TIMESTAMP,"");
    }

    public void ready(Context context, String loginEmail) {
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

    public String getUsername(Context context, String loginEmail) {
        Log.i(tag, "Get username for: " + loginEmail);

        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        String n = settings.getString(GeneralConfig.PROFILE_USERNAME, "");
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
        Log.i(tag, "Save profile to " + GeneralConfig.PROFILE_FILENAME);

        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(GeneralConfig.PROFILE_USERNAME, username);
        editor.putString(GeneralConfig.PROFILE_PHONE, phone);
        editor.putString(GeneralConfig.PROFILE_WECHAT, wechat);
        editor.commit();
        Toast.makeText(context.getApplicationContext(), "Profile saved.",
                Toast.LENGTH_SHORT).show();

        this.username = username;
        this.phone = phone;
        this.wechat = wechat;
    }

    public void saveLoginAccessToken(Context context, String token){
        Log.i(tag, "Save login access token: " + token);
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfig.PROFILE_LOGIN_ACCESS_TOKEN, token);
        editor.commit();
        Log.i(tag, "Save complete");
    }

    public String getLoginAccessToken(Context context){
        Log.d(tag, "Get login access token");
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_LOGIN_ACCESS_TOKEN, "");
    }

    public String getServerIp(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_SERVER_IP, "");
    }

    public void setServerIp(Context context, String servIp){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfig.PROFILE_SERVER_IP, servIp);
        editor.commit();
    }

    public String getServerPort(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_SERVER_PORT, "");
    }

    public void setServerPort(Context context, String servPort){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfig.PROFILE_SERVER_PORT, servPort);
        editor.commit();
    }

    public String getLoginUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_LOGIN;
        String url = "http://" + ip + ":" + port + path;
        Log.i(tag, "Login url: " + url);
        return url;
    }

    public String getRegisterUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_REGISTER;
        String url = "http://" + ip + ":" + port + path;
        Log.i(tag, "Register url: " + url);
        return url;
    }

    public String getCreateProjectUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_CREATE_PROJECT;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Create project url: " + url);
        return url;
    }

    public String getArchiveProjectUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_ARCHIVE_PROJECT;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Archive project url: " + url);
        return url;
    }

    public String getUpdateProjectNameUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_RENAME_PROJECT;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Rename project url: " + url);
        return url;
    }

    public String getSetProjectAttributeUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_SET_PROJECT_ATTRIBUTE;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Set project attribute url: " + url);
        return url;
    }

    public String getHeartbeatUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_HEARTBEAT;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Heartbeat url: " + url);
        return url;
    }

    public String getInviteProjectMemberUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_INVITE_PROJECT_MEMBER;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Invite project member url: " + url);
        return url;
    }

    public String getLoginEmail(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_LOGIN_EMAIL, "");
    }

    public String getNickname(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        String name = settings.getString(GeneralConfig.PROFILE_USERNAME, "");
        if(name == null || name.isEmpty())
            name = NICKNAME_NOT_SET;
        return name;
    }

    public void setLoginEmail(Context context, String loginEmail){
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

    public String getLoginPassword(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_LOGIN_PASSWORD, "");
    }

    public void setLoginPassword(Context context, String loginPassword){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfig.PROFILE_LOGIN_PASSWORD, loginPassword);
        editor.commit();
    }

    public String getLastLoginTimestamp(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        return settings.getString(GeneralConfig.PROFILE_LAST_LOGIN_TIMESTAMP, "");
    }

    public void setLastLoginTimestamp(Context context, String timestamp){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfig.PROFILE_LAST_LOGIN_TIMESTAMP, timestamp);
        editor.commit();
    }

    public void logout(Context context){
        Log.d(tag, "delete user login access token");
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
//        editor.putString(GeneralConfig.PROFILE_LOGIN_EMAIL, "");
//        editor.putString(GeneralConfig.PROFILE_LOGIN_PASSWORD, "");
//        editor.putString(GeneralConfig.PROFILE_USERNAME, "");
//        editor.putString(GeneralConfig.PROFILE_PHONE, "");
//        editor.putString(GeneralConfig.PROFILE_WECHAT, "");
//        editor.putString(GeneralConfig.PROFILE_LOGIN_ACCESS_TOKEN, "");
//        editor.remove(GeneralConfig.PROFILE_LOGIN_EMAIL);
//        editor.remove(GeneralConfig.PROFILE_LOGIN_PASSWORD);
//        editor.remove(GeneralConfig.PROFILE_USERNAME);
//        editor.remove(GeneralConfig.PROFILE_PHONE);
//        editor.remove(GeneralConfig.PROFILE_WECHAT);
        editor.remove(GeneralConfig.PROFILE_LOGIN_ACCESS_TOKEN);
        editor.commit();
    }

    public String getJoinProjectByInviteUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_JOIN_PROJECT_BY_INVITE;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Join project by invite url: " + url);
        return url;
    }

    public String getSendMessageUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_SEND_MESSAGE;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Join project by invite url: " + url);
        return url;
    }

    public String getRetrieveMessageUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_RECEIVE_MESSAGE;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Receive message url: " + url);
        return url;
    }

    public String getAckRetrieveMessageUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_ACK_RECEIVE_MESSAGE;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Ack receive message url: " + url);
        return url;
    }

    public String getAddFriendUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_ADD_FRIEND;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "add friend url: " + url);
        return url;
    }

    public String getQueryUserUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_QUERY_USER;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "query user url: " + url);
        return url;
    }

    public String getAddFileToActivityUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_ADD_FILE_TO_ACTIVITY;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "add file to activity url: " + url);
        return url;
    }

    public String getGetActivityEventUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfig.SERVER_PATH_GET_ACTIVITY_EVENT;
        String token = getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfig.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "get activity event url: " + url);
        return url;
    }
}
