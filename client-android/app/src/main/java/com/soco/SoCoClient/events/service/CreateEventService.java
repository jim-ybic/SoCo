package com.soco.SoCoClient.events.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.JsonSimulator;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.model.Event;

import org.json.JSONObject;

public class CreateEventService extends IntentService {

    static final String tag = "CreateEventService";

    static SocoApp socoApp;

    public CreateEventService() {
        super("CreateEventService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        socoApp = (SocoApp) getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(tag, "register service, handle intent:" + intent
                        + ", event: " + socoApp.newEvent.toString()
        );

        Log.v(tag, "validate data");
        if(!socoApp.SKIP_LOGIN && (socoApp.user_id.isEmpty() || socoApp.token.isEmpty())){
            Log.e(tag, "user id or token or event is not available");
            return;
        }
        if(socoApp.newEvent == null){
            Log.e(tag, "new event is not available");
            return;
        }

        String url = UrlUtil.getCreateEventUrl();
        Object response = request(
                url,
                socoApp.user_id,
                socoApp.token,
                socoApp.newEvent
        );

        if (response != null) {
            Log.v(tag, "set response flag as true");
            socoApp.createEventResponse = true;

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
            Event event
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

            data.put(JsonKeys.NAME, event.getTitle());
            if(!event.getLocation().isEmpty())
                data.put(JsonKeys.LOCATION, event.getLocation());
            if(!event.getDate().isEmpty())
                data.put(JsonKeys.DATE, event.getDate());
            if(!event.getTime().isEmpty())
                data.put(JsonKeys.TIME, event.getTime());
            if(!event.getIntroduction().isEmpty())
                data.put(JsonKeys.INTRODUCTION, event.getIntroduction());

            Log.d(tag, "create event json: " + data);
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
                socoApp.createEventResult = true;
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "create event fail, " +
                        "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
                socoApp.createEventResult = false;
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
