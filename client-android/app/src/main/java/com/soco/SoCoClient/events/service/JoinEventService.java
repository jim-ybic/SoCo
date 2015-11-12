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
import com.soco.SoCoClient.events.common.JoinEventActivity;
import com.soco.SoCoClient.events.model.Event;

import org.json.JSONObject;

public class JoinEventService extends IntentService {

    static final String tag = "JoinEventService";

    static SocoApp socoApp;

    public JoinEventService() {
        super("JoinEventService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        socoApp = (SocoApp) getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(tag, "join event service, handle intent:" + intent);

        Log.v(tag, "validate data");
        if(socoApp.user_id.isEmpty() || socoApp.token.isEmpty()){
            Log.e(tag, "user id or token or event is not available");
            return;
        }
//        if(socoApp.newEvent == null){
//            Log.e(tag, "new event is not available");
//            return;
//        }
        String event_id = intent.getStringExtra(Event.EVENT_ID);
        String phone = intent.getStringExtra(JoinEventActivity.PHONE);
        String email = intent.getStringExtra(JoinEventActivity.EMAIL);

        String url = UrlUtil.getJoinEventUrl();
        Object response = request(
                url,
                socoApp.user_id,
                socoApp.token,
                event_id,
                phone,
                email
        );

        Log.v(tag, "set response flag as true");
        socoApp.joinEventResponse = true;

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
            String event_id,
            String phone,
            String email
    ) {
        Log.v(tag, "join json request");

        JSONObject data = new JSONObject();
        try {
            data.put(JsonKeys.USER_ID, user_id);
            data.put(JsonKeys.TOKEN, token);
            data.put(JsonKeys.EVENT_ID, event_id);
            if(!StringUtil.isEmptyString(phone)) {
                data.put(JsonKeys.PHONE, phone);
            }
            if(!StringUtil.isEmptyString(email)) {
                data.put(JsonKeys.EMAIL, email);
            }

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
                Log.d(tag, "join event success, retrieve event id");
//                String event_id = json.getString(JsonKeys.EVENT_ID);
//                Log.d(tag, "create event success, " +
//                        "event id: " + event_id
//                );
                socoApp.joinEventResult = true;
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "join event fail, " +
                        "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
                socoApp.error_message=message;
                socoApp.joinEventResult = false;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            socoApp.joinEventResult = false;
            return false;
        }

        return true;
    }

}
