package com.soco.SoCoClient.events.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.util.StringUtil;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

public class JoinEventTask extends AsyncTask<String, Void, Boolean> {

    private static final String tag = "JoinEventTask";

    SocoApp socoApp;
    String eventId;
    String phone;
    String email;
    TaskCallBack callback;

    public JoinEventTask(
            Context context, String eventId, String phone, String email, TaskCallBack cb
    ){

        this.socoApp = (SocoApp) context;
        this.eventId = eventId;
        this.phone = phone;
        this.email = email;
        this.callback = cb;
    }

    protected Boolean doInBackground(String... params) {
        Log.d(tag, "task begin");

        String url = UrlUtil.getJoinEventUrl();
        Object response = request(
                url,
                socoApp.user_id,
                socoApp.token,
                eventId,
                phone,
                email
        );

        Log.v(tag, "set response flag as true");
        socoApp.joinEventResponse = true;

        if (response != null) {
            Log.v(tag, "parse response");
            return parse(response);
        }
        else {
            Log.e(tag, "response is null, cannot parse");
            return false;
        }
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
                return true;
//                socoApp.joinEventResult = true;
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "join event fail, " +
                                "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
                return false;
//                socoApp.error_message=message;
//                socoApp.joinEventResult = false;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
//            socoApp.joinEventResult = false;
            return false;
        }
    }

    protected void onPostExecute(Boolean result){
        Log.v(tag, "post execute");
        if(result)
            callback.doneTask(true);
        else
            callback.doneTask(false);
    }

}
