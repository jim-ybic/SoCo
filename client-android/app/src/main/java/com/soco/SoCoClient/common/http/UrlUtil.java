package com.soco.SoCoClient.common.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.model.Profile;
import com.soco.SoCoClient.common.util.SocoApp;

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
    static final String SERVER_PORT = "80";

    static final String LOGIN_PATH = "/v1/login";
    static final String SOCIAL_LOGIN_PATH = "/v1/social_login";
    static final String REGISTER_PATH = "/v1/register";
    static final String CREATE_EVENT_PATH = "/v1/event";
    static final String SUGGESTED_EVENTS_PATH = "/v1/suggested_events";
    static final String ADD_BUDDY_PATH = "/v1/add_buddy";
    static final String LIKE_EVENT_PATH = "/v1/like_event";
    static final String REVERT_LIKE_EVENT_PATH = "/v1/revert_like_event";
    static final String JOIN_EVENT_PATH = "/v1/join_event";
    static final String EVENT_GROUPS_BUDDIES_PATH = "/v1/event_groups_buddies";
    static final String SUGGESTED_BUDDIES_PATH = "/v1/suggested_buddies";
    static final String MY_BUDDIES_PATH = "/v1/my_buddies";
    static final String MY_MATCH_PATH = "/v1/matched_buddies";
    static final String USER_ICON="/v1/user_icon?";
    static final String GROUP_ICON="/v1/group_icon?";
    static final String CREATE_GROUP_PATH = "/v1/group";
    static final String USER_PROFILE_PATH = "/v1/user";
    static final String USER_EVENT_PATH = "/v1/events";
    static final String USER_GROUP_PATH = "/v1/user_groups";
    static final String GROUP_LIST_PATH = "/v1/groups";
    static final String GROUP_PATH = "/v1/group";
    static final String GROUP_MEMBER_PATH = "/v1/group_members";
    static final String JOIN_GROUP_PATH = "/v1/join_group";
    static final String USER_ICON_PATH = "/v1/user_icon";
    static final String EVENT_POST_PATH = "/v1/event_post";
    static final String EVENT_POSTS_PATH = "/v1/event_posts";

    public static String getLoginUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + LOGIN_PATH;
        Log.v(tag, "login url: " + url);
        return url;
    }

    public static String getSocialLoginUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + SOCIAL_LOGIN_PATH;
        Log.v(tag, "social login url: " + url);
        return url;
    }

    public static String getRegisterUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + REGISTER_PATH;
        Log.v(tag, "register url: " + url);
        return url;
    }

    public static String getCreateEventUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + CREATE_EVENT_PATH;
        Log.v(tag, "create event url: " + url);
        return url;
    }

    public static String getSuggestedEventsUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + SUGGESTED_EVENTS_PATH;
        Log.v(tag, "get suggested events url: " + url);
        return url;
    }
    public static String getSuggestedBuddiessUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + SUGGESTED_BUDDIES_PATH;
        Log.v(tag, "get suggested events url: " + url);
        return url;
    }

    public static String getAddBuddyUrl() {
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + ADD_BUDDY_PATH;
        Log.v(tag, "get add buddy url: " + url);
        return url;
    }

    public static String getLikeEventUrl() {
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + LIKE_EVENT_PATH;
        Log.v(tag, "get like event url: " + url);
        return url;
    }

    public static String getRevertLikeEventUrl() {
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + REVERT_LIKE_EVENT_PATH;
        Log.v(tag, "get revert like event url: " + url);
        return url;
    }
    public static String getJoinEventUrl() {
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + JOIN_EVENT_PATH;
        Log.v(tag, "get join event url: " + url);
        return url;
    }

    public static String getEventGroupsBuddiesUrl() {
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + EVENT_GROUPS_BUDDIES_PATH;
        Log.v(tag, "get event groups buddies url: " + url);
        return url;
    }

    public static String getUserIconUrlPrefix(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + USER_ICON;
//        Log.v(tag, "user icon url: " + url);
        return url;
    }

    public static String getGroupIconUrlPrefix(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + GROUP_ICON;
//        Log.v(tag, "user icon url: " + url);
        return url;
    }

    public static String getUserIconUrl(String user_id) {
        StringBuffer sb = new StringBuffer();
        sb.append(getUserIconUrlPrefix());
        sb.append(SocoApp.getCurrentUserTokenForUrl());
//        if(SocoApp.user_id!=null&&!SocoApp.user_id.equalsIgnoreCase(user_id)){
//            sb.append("&buddy_user_id=");
//            sb.append(user_id);
//        }
        //update: always append buddy_user_id, even for the user herself
        sb.append("&buddy_user_id=");
        sb.append(user_id);
        Log.v(tag, "user icon url: " + sb.toString());
        return sb.toString();
    }
    public static String getGroupIconUrl(String group_id) {
        StringBuffer sb = new StringBuffer();
        sb.append(getGroupIconUrlPrefix());
        sb.append(SocoApp.getCurrentUserTokenForUrl());
        sb.append("&group_id=");
        sb.append(group_id);
        return sb.toString();
    }

    public static String getCreateGroupUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + CREATE_GROUP_PATH;
        Log.v(tag, "create group url: " + url);
        return url;
    }

    public static String getUserProfileUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + USER_PROFILE_PATH;
        Log.v(tag, "user profile url: " + url);
        return url;
    }
    public static String getMyBuddiesUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + MY_BUDDIES_PATH;
        Log.v(tag, "get my buddies url: " + url);
        return url;
    }
    public static String getMyMatchUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + MY_MATCH_PATH;
        Log.v(tag, "get my match url: " + url);
        return url;
    }
    public static String getEventsUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + USER_EVENT_PATH;
        Log.v(tag, "get events url: " + url);
        return url;
    }
    public static String getUserGroupUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + USER_GROUP_PATH;
        Log.v(tag, "get user group url: " + url);
        return url;
    }
    public static String getEventUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + CREATE_EVENT_PATH;
        Log.v(tag, "event url: " + url);
        return url;
    }
    public static String getGroupsUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + GROUP_LIST_PATH;
        Log.v(tag, "groups url: " + url);
        return url;
    }

    public static String getGroupUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + GROUP_PATH;
        Log.v(tag, "single group url: " + url);
        return url;
    }

    public static String getGroupMembersUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + GROUP_MEMBER_PATH;
        Log.v(tag, "single group url: " + url);
        return url;
    }

    public static String getJoinGroupUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + JOIN_GROUP_PATH;
        Log.v(tag, "join group url: " + url);
        return url;
    }

    public static String getUserIconUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + USER_ICON_PATH;
        Log.v(tag, "user icon url: " + url);
        return url;
    }

    public static String getEventPostUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + EVENT_POST_PATH;
        Log.v(tag, "event post url: " + url);
        return url;
    }

    public static String getEventPostsUrl(){
        String url = URL_HEADER + SERVER_IP + COLON + SERVER_PORT + EVENT_POSTS_PATH;
        Log.v(tag, "event posts url: " + url);
        return url;
    }

}
