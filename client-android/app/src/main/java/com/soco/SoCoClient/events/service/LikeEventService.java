package com.soco.SoCoClient.events.service;


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

public class LikeEventService extends IntentService {
    private final String EVENT_ID="EVENT_ID";
    static final String tag = "LikeEventService";

    static SocoApp socoApp;

    public LikeEventService() {
        super("LikeEventService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        socoApp = (SocoApp) getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Log.d(tag, "like event service, handle intent:" + intent);

        Log.v(tag, "validate data");
        if(socoApp.user_id.isEmpty() || socoApp.token.isEmpty()){
            Log.e(tag, "user id or token or event is not available");
            return;
        }
//        if(socoApp.newEvent == null){
//            Log.e(tag, "new event is not available");
//            return;
//        }
        double event_id = intent.getDoubleExtra(EVENT_ID,Double.NaN);
        if(Double.isNaN(event_id)) {
            Event event = socoApp.getCurrentSuggestedEvent();
            event_id = event.getId();
        }

        String url = UrlUtil.getLikeEventUrl();
        Object response = request(
                url,
                socoApp.user_id,
                socoApp.token,
                event_id
        );

        Log.v(tag, "set response flag as true");
        socoApp.likeEventResponse = true;

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
            double event_id
    ) {
        Log.v(tag, "create json request");

        JSONObject data = new JSONObject();
        try {
            data.put(JsonKeys.USER_ID, user_id);
            data.put(JsonKeys.TOKEN, token);
            data.put(JsonKeys.EVENT_ID, event_id);
            Log.d(tag, "create event json: " + data);
        } catch (Exception e) {
            Log.e(tag, "cannot create json post data");
            e.printStackTrace();
        }
        Log.e(tag,url);
        Log.e(tag,data.toString());
        return HttpUtil.executeHttpPost(url, data);
    }

    public static boolean parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                Log.d(tag, "like event success, retrieve event id");
                socoApp.likeEventResult = true;
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "like event fail, " +
                        "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
                socoApp.likeEventResult = false;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            socoApp.createEventResult = false;
            return false;
        }

        return true;
    }

}
