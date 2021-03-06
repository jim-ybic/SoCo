package com.soco.SoCoClient.userprofile.task;


import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.EventsResponseUtil;
import com.soco.SoCoClient.events.model.Event;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DownloadEventsTask extends AsyncTask<String, Void, ArrayList<Event>>{

    public static String BUDDY_USER_ID =JsonKeys.BUDDY_USER_ID;
    public static String START_EVENT_ID=JsonKeys.START_EVENT_ID;
//    public static String KEYWORD="";

    String tag = "DownloadEventsTask";
    String user_id;
    String token;
    String topicId;
    String[] paramNames;
    TaskCallBack callBack;

    public DownloadEventsTask(String user_id, String token, TaskCallBack cb){
        Log.v(tag, "user event task: " + user_id);
        this.user_id=user_id;
        this.token=token;
        this.callBack = cb;
    }

    public DownloadEventsTask(String user_id, String token, String topicId, TaskCallBack cb){
        Log.v(tag, "user event task: " + user_id);
        this.user_id=user_id;
        this.token=token;
        this.topicId = topicId;
        this.callBack = cb;
    }

    public DownloadEventsTask(String user_id, String token, String[] paramNames, TaskCallBack cb){
        Log.v(tag, "user event task: " + user_id);
        this.user_id=user_id;
        this.token=token;
        this.paramNames=paramNames;
        this.callBack = cb;
    }

    protected ArrayList<Event> doInBackground(String... params) {
        Log.v(tag, "validate data");

        String url = UrlUtil.getEventsUrl();
        Object response = request(
                url,
                user_id,
                token,
                params
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

    private Object request(
            String url,
            String user_id,
            String token,
            String... inputs){
        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair(JsonKeys.USER_ID, user_id));
        params.add(new BasicNameValuePair(JsonKeys.TOKEN, token));

        if(paramNames!=null&&inputs!=null&&paramNames.length>0&&inputs.length>0) {  //loading more
            for(int i = 0; i<paramNames.length&&i<inputs.length;i++){
                params.add(new BasicNameValuePair(paramNames[i], inputs[i]));
            }
        }

        if(topicId != null && !topicId.isEmpty())
            params.add(new BasicNameValuePair(JsonKeys.TOPIC_ID, topicId));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        Log.d(tag, "request url: " + url);

        return HttpUtil.executeHttpGet(url);
    }

     ArrayList<Event> parse(Object response) {
        Log.v(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                String eventStr = json.getString(JsonKeys.EVENTS);
                JSONArray allEvents = new JSONArray(eventStr);
                return EventsResponseUtil.parseEventsFromJSONArray(allEvents);
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
