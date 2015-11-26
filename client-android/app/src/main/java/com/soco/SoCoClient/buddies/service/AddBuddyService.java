package com.soco.SoCoClient.buddies.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.model.Event;

import org.json.JSONObject;

public class AddBuddyService extends IntentService {

    static final String tag = "AddBuddyService";

    static SocoApp socoApp;

    public AddBuddyService() {
        super("AddBuddyService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        socoApp = (SocoApp) getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(tag, "start service, handle intent:" + intent
        );

        Log.v(tag, "validate data");
        if(!socoApp.SKIP_LOGIN && (socoApp.user_id.isEmpty() || socoApp.token.isEmpty())){
            Log.e(tag, "user id or token is not available");
            return;
        }

        String currentBuddyId = socoApp.getCurrentSuggestedBuddy().getUser_id();
        Log.v(tag, "current suggested buddy id: " + currentBuddyId);
        if(socoApp.BUDDY_INTERFACE_READY && currentBuddyId.isEmpty()){
            Log.e(tag, "buddy id is not available, current buddy id: " + socoApp.currentBuddyId);
            return;
        }

        String url = UrlUtil.getAddBuddyUrl();
        Object response = request(
                url,
                socoApp.user_id,
                socoApp.token,
                currentBuddyId
        );

        Log.v(tag, "set response flag as true");
        socoApp.addBuddyResponse = true;

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
            String token,
            String currentBuddyId
    ) {
        Log.v(tag, "create json request");

        JSONObject data = new JSONObject();
        try {
            if(socoApp.SKIP_LOGIN) {
                data.put(JsonKeys.USER_ID, JsonKeys.TEST_USER_ID);
                data.put(JsonKeys.TOKEN, JsonKeys.TEST_TOKEN);
            }
            else {
                data.put(JsonKeys.USER_ID, user_id);
                data.put(JsonKeys.TOKEN, token);
            }

            if(socoApp.BUDDY_INTERFACE_READY)
                data.put(JsonKeys.FRIEND_ID, currentBuddyId);
            else {
                Log.w(tag, "buddy interface not ready, use test user id: " + JsonKeys.TEST_USER_ID2);
                data.put(JsonKeys.FRIEND_ID, JsonKeys.TEST_USER_ID2);
            }

            Log.d(tag, "add buddy json: " + data);
        } catch (Exception e) {
            Log.e(tag, "cannot create json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public static boolean parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                Log.d(tag, "create event success, retrieve event id");
//                String event_id = json.getString(JsonKeys.EVENT_ID);
//                Log.d(tag, "create event success, " +
//                        "event id: " + event_id
//                );
                socoApp.addBuddyResult = true;
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.e(tag, "create event fail, " +
                        "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
                socoApp.addBuddyResult = false;
                socoApp.error_message = message;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            socoApp.addBuddyResult = false;
            return false;
        }

        return true;
    }

}
