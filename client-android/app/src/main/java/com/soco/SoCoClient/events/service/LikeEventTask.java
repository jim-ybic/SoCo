package com.soco.SoCoClient.events.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.LikeUtil;
import com.soco.SoCoClient.events.model.Event;

import org.json.JSONObject;

/**
 * Created by David_WANG on 12/07/2015.
 */
public class LikeEventTask extends AsyncTask<String, Void, Boolean> {

    private static String tag = "LikeEventTask";
    String user_id;
    String token;
    Event event;
    Button button;
    boolean isLiked;
    static String action;
    public LikeEventTask(String user_id, String token, Event e, Button b, boolean isLiked) {
        Log.v(tag, "event details task: " + user_id);
        this.user_id = user_id;
        this.token = token;
        this.event = e;
        this.button = b;
        this.isLiked = isLiked;
        this.action=isLiked?"Revert Like ":"Like ";
    }


    protected Boolean doInBackground(String... params) {
        if(event==null){
            return false;
        }
        Log.v(tag, "validate data");
        String url = isLiked?UrlUtil.getRevertLikeEventUrl():UrlUtil.getLikeEventUrl();
        Log.v(tag, "Applying action to "+action+"event id: "+ event.getId());

        Object response = request(
                url,
                user_id,
                token,
                event.getId()
        );

        if (response != null) {
            Log.v(tag, "parse response");
            return parse(response);
        } else {
            Log.e(tag, "response is null, cannot parse");
        }
        return false;
    }

    public static Object request(
            String url,
            String user_id,
            String token,
            long event_id
    ) {
        Log.v(tag, "create json request");

        JSONObject data = new JSONObject();
        try {
            data.put(JsonKeys.USER_ID, user_id);
            data.put(JsonKeys.TOKEN, token);
            data.put(JsonKeys.EVENT_ID, Long.toString(event_id));
            Log.d(tag, "create event json: " + data);
        } catch (Exception e) {
            Log.e(tag, "cannot create json post data");
            e.printStackTrace();
        }
        Log.d(tag, url);
        Log.d(tag, data.toString());
        return HttpUtil.executeHttpPost(url, data);
    }

    public static boolean parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if (status == HttpStatus.SUCCESS) {
                Log.d(tag, action+" success");
                return true;
            } else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, action +" event fail, " +
                                "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    protected void onPostExecute(Boolean result) {
        if(result) {
            Log.d(tag, action+" success, trying to update the button");
            //trigger the cancel action for previous like
            //isLiked, means here to revert the like
            //is not liked, means here to like
            LikeUtil.updateLikeButtonStatus(button, event, !isLiked);
        }
    }
}

