package com.soco.SoCoClient.common.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.model.Profile;

public class UrlUtil {
    static String tag = "UrlUtil";

    public static String getLoginUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_LOGIN;
        String url = "http://" + ip + ":" + port + path;
        Log.i(tag, "Login url: " + url);
        return url;
    }

    public static String getRegisterUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_REGISTER;
        String url = "http://" + ip + ":" + port + path;
        Log.i(tag, "Register url: " + url);
        return url;
    }

    public static String getCreateProjectUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_CREATE_PROJECT;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Create project url: " + url);
        return url;
    }

    public static String getArchiveProjectUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_ARCHIVE_PROJECT;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Archive project url: " + url);
        return url;
    }

    public static String getUpdateProjectNameUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_RENAME_PROJECT;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Rename project url: " + url);
        return url;
    }

    public static String getSetProjectAttributeUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_SET_PROJECT_ATTRIBUTE;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.i(tag, "Set project attribute url: " + url);
        return url;
    }

    public static String getHeartbeatUrl(Context context){
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_HEARTBEAT;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Heartbeat url: " + url);
        return url;
    }

    public static String getInviteProjectMemberUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_INVITE_PROJECT_MEMBER;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Invite project member url: " + url);
        return url;
    }

    public static String getJoinProjectByInviteUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_JOIN_PROJECT_BY_INVITE;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Join project by invite url: " + url);
        return url;
    }

    public static String getSendMessageUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_SEND_MESSAGE;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Join project by invite url: " + url);
        return url;
    }

    public static String getRetrieveMessageUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_RECEIVE_MESSAGE;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Receive message url: " + url);
        return url;
    }

    public static String getAckRetrieveMessageUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_ACK_RECEIVE_MESSAGE;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "Ack receive message url: " + url);
        return url;
    }

    public static String getAddFriendUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_ADD_FRIEND;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "add friend url: " + url);
        return url;
    }

    public static String getQueryUserUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_QUERY_USER;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "query user url: " + url);
        return url;
    }

    public static String getAddFileToActivityUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_ADD_FILE_TO_ACTIVITY;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "add file to activity url: " + url);
        return url;
    }

    public static String getGetActivityEventUrl(Context context) {
        String ip = getServerIp(context);
        String port = getServerPort(context);
        String path = HttpConfigV1.SERVER_PATH_GET_ACTIVITY_EVENT;
        String token = Profile.getLoginAccessToken(context);
        String url = "http://" + ip + ":" + port + path;
        url += HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;
        Log.d(tag, "get activity event url: " + url);
        return url;
    }

    public static String getServerIp(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        String ip = settings.getString(GeneralConfigV1.PROFILE_SERVER_IP, "");
        if(ip == null || ip.isEmpty()){
            Log.i(tag, "Server ip not found, use default: " + Config.DEFAULT_SERVER_IP);
            return Config.DEFAULT_SERVER_IP;
        }
        else
            return ip;
    }

    public static void setServerIp(Context context, String servIp){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfigV1.PROFILE_SERVER_IP, servIp);
        editor.commit();
    }

    public static String getServerPort(Context context){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        String port = settings.getString(GeneralConfigV1.PROFILE_SERVER_PORT, "");
        if (port == null || port.isEmpty()) {
            Log.i(tag, "Server port not found, use default: " + Config.DEFAULT_SERVER_PORT);
            return Config.DEFAULT_SERVER_PORT;
        }
        else
            return port;
    }

    public static void setServerPort(Context context, String servPort){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfigV1.PROFILE_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GeneralConfigV1.PROFILE_SERVER_PORT, servPort);
        editor.commit();
    }

    ////////////////////////////////////////////////////////////////////////////


    //http
    static final String URL_HEADER = "http://";
    static final String COLON = ":";
    static final String SERVER_IP = "54.254.147.226";
    static final String SERVER_PORT = "8090";
    static final String LOGIN_PATH = "/v1/login";
    static final String SOCIAL_LOGIN_PATH = "/v1/social_login";
    static final String REGISTER_PATH = "/v1/register";
    static final String CREATE_EVENT_PATH = "/v2/event";
    static final String SUGGESTED_EVENTS_PATH = "/v1/suggested_events";

    //sample: http://54.254.147.226:8090/v1/login
    public static String getLoginUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + LOGIN_PATH;
        Log.d(tag, "login url: " + url);
        return url;
    }

    //sample: http://54.254.147.226:8090/v1/social_login
    public static String getSocialLoginUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + SOCIAL_LOGIN_PATH;
        Log.d(tag, "social login url: " + url);
        return url;
    }

    //sample: http://54.254.147.226:8090/v1/register
    public static String getRegisterUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + REGISTER_PATH;
        Log.d(tag, "register url: " + url);
        return url;
    }

    public static String getCreateEventUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + CREATE_EVENT_PATH;
        Log.d(tag, "create event url: " + url);
        return url;
    }

    public static String getSuggestedEventsUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + SUGGESTED_EVENTS_PATH;
        Log.d(tag, "get suggested events url: " + url);
        return url;
    }

}
