package com.soco.SoCoClient.buddies.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.EventsResponseUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.userprofile.model.User;
import com.soco.SoCoClient.userprofile.model.UserBrief;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class DownloadSuggestedBuddiesTask extends AsyncTask<String, Void, Boolean> {

    String tag = "DownloadSuggestedBuddi";

    static final String PERFS_NAME = "EVENT_BUDDY_PERFS";
    static final String USER_ID = "user_id";
    static final String TOKEN = "token";

    Context context;
    SocoApp socoApp;
    TaskCallBack callBack;

    public DownloadSuggestedBuddiesTask(Context context, TaskCallBack cb) {
        this.context = context;
        this.socoApp = (SocoApp) context;
        this.callBack = cb;
    }

    protected Boolean doInBackground(String... params) {
        Log.d(tag, "download suggested buddies service,"
                        + "userid: " + socoApp.user_id + ", token: " + socoApp.token
        );

        Log.v(tag, "validate data");
//        if(!socoApp.SKIP_LOGIN &&
//                (socoApp.user_id == null || socoApp.user_id.isEmpty()
//                        || socoApp.token == null || socoApp.token.isEmpty())){
//            Log.e(tag, "user id or token or event is not available");
//            return;
//        }
        if (!socoApp.SKIP_LOGIN &&
                (socoApp.user_id == null || socoApp.user_id.isEmpty()
                        || socoApp.token == null || socoApp.token.isEmpty())) {
            Log.e(tag, "user id or token or event is not available in memory");

            SharedPreferences settings = context.getSharedPreferences(PERFS_NAME, 0);
            String userId = settings.getString(USER_ID, "");
            String token = settings.getString(TOKEN, "");
            Log.v(tag, "get stored userid/token: " + userId + ", " + token);
            if (token != null && !token.isEmpty()) {
                socoApp.user_id = userId;
                socoApp.token = token;
            } else {
                Log.e(tag, "cannot get userid/token from shared preference");
                socoApp.downloadSuggestedBuddiesResponse = true;
                socoApp.downloadSuggestedBuddiesResult = false;
                return false;
            }
        }

        String url = UrlUtil.getSuggestedBuddiessUrl();
        Object response = request(
                url,
                socoApp.user_id,
                socoApp.token
        );

        Log.v(tag, "set response flag as true");
        socoApp.downloadSuggestedBuddiesResponse = true;

        if (response != null) {
            Log.v(tag, "parse response");
            parse(response);
        } else {
            Log.e(tag, "response is null, cannot parse");
        }

        return true;
    }

    private Object request(
            String url,
            String user_id,
            String token
    ) {
        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<>();

        params.add(new BasicNameValuePair(JsonKeys.USER_ID, user_id));
        params.add(new BasicNameValuePair(JsonKeys.TOKEN, token));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        Log.d(tag, "request url: " + url);

        return HttpUtil.executeHttpGet(url);
    }

    private boolean parse(Object response) {
//        Log.d(tag, "parse response: " + response.getEntity().toString());

        Log.v(tag, "clear suggested buddies");
        socoApp.suggestedBuddies = new ArrayList<>();

        try {
            JSONObject json;
            json = new JSONObject(response.toString());
            Log.d(tag, "converted json: " + json);


            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                String allBuddiesString = json.getString(JsonKeys.BUDDIES);
                Log.v(tag, "all buddies string: " + allBuddiesString);
                JSONArray allBuddies = new JSONArray(allBuddiesString);
                Log.d(tag, "retrieve buddy list: " + allBuddies.length() + " users downloaded");

                for(int i=0; i<allBuddies.length(); i++){
                    User u = new User();
                    JSONObject obj = allBuddies.getJSONObject(i);
                    Log.v(tag, "current buddy json: " + obj.toString());
                    parseUserBasics(u, obj);
                    parseInterests(u, obj);
                    parseCommonEventsGroups(u, obj);
                    parseCommonUsers(u, obj);
                    socoApp.suggestedBuddies.add(u);
                }

                socoApp.downloadSuggestedBuddiesResult = true;
                socoApp.mappingSuggestedBuddyListToMap();
                Log.d(tag, socoApp.suggestedBuddies.size() + " buddies created from json response");
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "create buddy fail, " +
                                "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
                socoApp.downloadSuggestedBuddiesResult = false;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            socoApp.downloadSuggestedBuddiesResult = false;
            return false;
        }

        return true;
    }

    private void parseUserBasics(User u, JSONObject obj) throws JSONException {
        u.setUser_id(obj.getString(JsonKeys.USER_ID));
        u.setUser_name(obj.getString(JsonKeys.USER_NAME));
        u.setLocation(obj.getString(JsonKeys.LOCATION));
        Log.v(tag, "user id, name, location: " + u.getUser_id() + ", " + u.getUser_name() + ", " + u.getLocation());
    }

    private void parseCommonEventsGroups(User u, JSONObject obj) throws JSONException{
        Log.v(tag, "parse common event and groups ");

        if(obj.has(JsonKeys.NUMBER_OF_COMMON_EVENTS)){
            Log.v(tag, "found key for " + JsonKeys.NUMBER_OF_COMMON_EVENTS);
            u.setNumber_event(obj.getInt(JsonKeys.NUMBER_OF_COMMON_EVENTS));
            if(obj.has(JsonKeys.FIRST_COMMON_EVENT_NAME)) {
                u.setEvent_name(obj.getString(JsonKeys.FIRST_COMMON_EVENT_NAME));
                Log.v(tag, "first common event: " + u.getEvent_name());
            }
            else
                Log.w(tag, "no user common event");
        }
        else{
            Log.v(tag, "no key for " + JsonKeys.NUMBER_OF_COMMON_EVENTS);
            u.setNumber_event(obj.getInt(JsonKeys.NUMBER_OF_EVENTS));
            if(obj.has(JsonKeys.FIRST_JOINED_EVENT_NAME)) {
                u.setEvent_name(obj.getString(JsonKeys.FIRST_JOINED_EVENT_NAME));
                Log.v(tag, "first joiend event: " + u.getEvent_name());
            }
            else
                Log.w(tag, "no user joined event");
        }

        if(obj.has(JsonKeys.NUMBER_OF_COMMON_GROUPS)){
            Log.v(tag, "found key for " + JsonKeys.NUMBER_OF_COMMON_GROUPS);
            u.setNumber_group(obj.getInt(JsonKeys.NUMBER_OF_COMMON_GROUPS));
            if(obj.has(JsonKeys.FIRST_COMMON_GROUP_NAME)) {
                u.setGroup_name(obj.getString(JsonKeys.FIRST_COMMON_GROUP_NAME));
                Log.v(tag, "first common group: " + u.getGroup_name());
            }
            else
                Log.w(tag, "no user common group");
        }
        else{
            Log.v(tag, "no key for " + JsonKeys.NUMBER_OF_COMMON_GROUPS);
            u.setNumber_group(obj.getInt(JsonKeys.NUMBER_OF_GROUPS));
            if(obj.has(JsonKeys.FIRST_JOINED_GROUP_NAME)) {
                u.setGroup_name(obj.getString(JsonKeys.FIRST_JOINED_GROUP_NAME));
                Log.v(tag, "first joined group: " + u.getGroup_name());
            }
            else
                Log.w(tag, "no user joined group");
        }
    }

    private void parseInterests(User u, JSONObject obj) throws JSONException {
        Log.v(tag, "parse interests");
        if(obj.has(JsonKeys.INTERESTS)) {
            String allInterestsString = obj.getString(JsonKeys.INTERESTS);
            JSONArray allInterests = new JSONArray(allInterestsString);
            Log.v(tag, allInterests.length() + " interests found: " + allInterests);
            for (int j = 0; j < allInterests.length(); j++) {
                JSONObject cat = allInterests.getJSONObject(j);
                String interest = cat.getString(JsonKeys.INTEREST);
                u.addInterest(interest);
            }
        }
        else
            Log.v(tag, "no interest info found in json");
    }

    private void parseCommonUsers(User u, JSONObject obj) throws JSONException {
        u.setNumber_common_buddy(obj.getInt(JsonKeys.NUMBER_OF_COMMON_BUDDIES));
        Log.v(tag, "number of common buddy: " + u.getNumber_common_buddy());

        Log.v(tag, "parse common users");
        if(obj.has(JsonKeys.COMMON_BUDDIES)) {
            String allCommonUsersString = obj.getString(JsonKeys.COMMON_BUDDIES);
            JSONArray allCommonUsers = new JSONArray(allCommonUsersString);
            Log.v(tag, allCommonUsers.length() + " common users found: " + allCommonUsers);
            for (int j = 0; j < allCommonUsers.length(); j++) {
                JSONObject userJO = allCommonUsers.getJSONObject(j);
                String user_id = userJO.getString(JsonKeys.USER_ID);
                String user_name = userJO.getString(JsonKeys.USER_NAME);
                String user_icon_url = userJO.getString(JsonKeys.USER_ICON_URL);
                UserBrief ub = new UserBrief(user_id,user_name,user_icon_url);
                u.addCommon_buddy(ub);
            }
        }
        else
            Log.v(tag, "no interest info found in json");
    }

    protected void onPostExecute(Boolean result){
        Log.v(tag, "post execute");
        callBack.doneTask(null);
    }
}
