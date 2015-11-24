package com.soco.SoCoClient.buddies.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.soco.SoCoClient.buddies.allbuddies.ui.MyBuddiesListEntryItem;
import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DownloadMyBuddiesService extends IntentService {

    static final String tag = "DownloadMyBuddies";

    static SocoApp socoApp;

    public DownloadMyBuddiesService() {
        super("DownloadMyBuddiesService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        socoApp = (SocoApp) getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(tag, "download my buddies service, handle intent:" + intent
                        + ", userid: " + socoApp.user_id + ", token: " + socoApp.token
        );

        Log.v(tag, "validate data");
        String url = UrlUtil.getMyBuddiesUrl();
        Object response = request(
                url,
                socoApp.user_id,
                socoApp.token
        );

        Log.v(tag, "set response flag as true");
        socoApp.downloadMyBuddiesResponse = true;

        if (response != null) {
            Log.v(tag, "parse response");
            parse(response);
        }
        else {
            Log.e(tag, "response is null, cannot parse");
        }

        return;
    }

    public static Object request(
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

    public boolean parse(Object response) {
        Log.v(tag, "clear my buddies");
        socoApp.myBuddies = new ArrayList<>();

        try {
            JSONObject json;
            json = new JSONObject(response.toString());
                Log.d(tag, "converted json: " + json);


            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                String allBuddiesString = json.getString(JsonKeys.USER);
                Log.v(tag, "all buddies string: " + allBuddiesString);
                JSONArray allBuddies = new JSONArray(allBuddiesString);
                Log.d(tag, "retrieve buddy list: " + allBuddies.length() + " users downloaded");

                for(int i=0; i<allBuddies.length(); i++){
                    MyBuddiesListEntryItem u = new MyBuddiesListEntryItem();
                    JSONObject obj = allBuddies.getJSONObject(i);
                    Log.v(tag, "current buddy json: " + obj.toString());
                    parseUserBasics(u, obj);
                    socoApp.myBuddies.add(u);
                }

                socoApp.downloadMyBuddiesResult = true;
                Log.d(tag, socoApp.myBuddies.size() + " buddies created from json response");
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "create buddy fail, " +
                        "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
                socoApp.downloadMyBuddiesResult = false;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            socoApp.downloadMyBuddiesResult = false;
            return false;
        }

        return true;
    }

    private void parseUserBasics(MyBuddiesListEntryItem u, JSONObject obj) throws JSONException {
        u.setUser_id(obj.getString(JsonKeys.USER_ID));
        u.setUser_name(obj.getString(JsonKeys.USER_NAME));
        u.setLocation(obj.getString(JsonKeys.LOCATION));
        Log.v(tag, "user id, name, location: " + u.getUser_id() + ", " + u.getUser_name() + ", " + u.getLocation());
    }
}
