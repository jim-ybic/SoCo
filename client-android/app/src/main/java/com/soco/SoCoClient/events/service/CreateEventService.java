package com.soco.SoCoClient.events.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.util.StringUtil;
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

        Log.v(tag, "set response flag as true");
        socoApp.createEventResponse = true;

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
            JSONObject venue = new JSONObject();
            if(!event.getAddress().isEmpty()) {
                venue.put(JsonKeys.ADDRESS, event.getAddress());
            }

            data.put(JsonKeys.VENUE, venue);
            JSONObject dateObj = new JSONObject();
            if(!event.getStart_date().isEmpty())
                dateObj.put(JsonKeys.START_DATE, event.getStart_date());
            if(!event.getStart_date().isEmpty())
                dateObj.put(JsonKeys.END_DATE, event.getEnd_date());
            if(!event.getStart_time().isEmpty())
                dateObj.put(JsonKeys.START_TIME, event.getStart_time());
            if(!event.getStart_time().isEmpty())
                dateObj.put(JsonKeys.END_TIME, event.getEnd_time());
            data.put(JsonKeys.TIMEDATE, dateObj);

            if(!event.getIntroduction().isEmpty())
                data.put(JsonKeys.DESCRIPTION, event.getIntroduction());


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
                String event_id = json.getString(JsonKeys.EVENT_ID);
                if(!StringUtil.isEmptyString(event_id)){
                    socoApp.newEvent.setId(Long.parseLong(event_id));
                }
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
