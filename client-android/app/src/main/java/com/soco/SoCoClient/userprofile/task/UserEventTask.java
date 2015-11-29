package com.soco.SoCoClient.userprofile.task;


import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.service.DownloadSuggestedEventsService;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserEventTask extends AsyncTask<String, Void, ArrayList<Event>>{

    String tag = "UserEventTask";
    String user_id;
    String token;
    TaskCallBack callBack;

    public UserEventTask(String user_id, String token, TaskCallBack cb){
        Log.v(tag, "user event task: " + user_id);
        this.user_id=user_id;
        this.token=token;
        callBack = cb;
    }


    protected ArrayList<Event> doInBackground(String... params) {
        Log.v(tag, "validate data");

        String url = UrlUtil.getUserEventUrl();
        Object response = request(
                url,
                user_id,
                token,
                params[0]
        );

        if (response != null) {
            Log.v(tag, "parse response");
            ArrayList<Event> event = parse(response);
            return event;
        }
        else {
            Log.e(tag, "response is null, cannot parse");
        }
        return null;
    }

    Object request(
            String url,
            String user_id,
            String token,
            String buddy_id){
        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair(JsonKeys.USER_ID, user_id));
        params.add(new BasicNameValuePair(JsonKeys.TOKEN, token));
        params.add(new BasicNameValuePair(JsonKeys.BUDDY_USER_ID, buddy_id));
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        Log.d(tag, "request url: " + url);

        return HttpUtil.executeHttpGet(url);
    }

     ArrayList<Event> parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                ArrayList<Event> result = new ArrayList<>();
                String eventStr = json.getString(JsonKeys.EVENTS);
                JSONArray allEvents = new JSONArray(eventStr);
                for(int i=0;i<allEvents.length();i++){
                    Event e = new Event();
                    JSONObject obj = allEvents.getJSONObject(i);
                    Log.v(tag, "current event json: " + obj.toString());

                    DownloadSuggestedEventsService.parseEventBasics(e, obj);
                    DownloadSuggestedEventsService.parseTimedate(e, obj);
                    DownloadSuggestedEventsService.parseCategories(e, obj);
                    DownloadSuggestedEventsService.parseOrganizer(obj, e);
                    DownloadSuggestedEventsService.parseBuddies(obj, e);
                    result.add(e);
                }
                return result;
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "request fail, "
                                + "error code: " + error_code
                                + ", message: " + message
                                + ", more info: " + more_info
                );
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(ArrayList<Event> result) {
        callBack.doneTask(result);
    }
}
