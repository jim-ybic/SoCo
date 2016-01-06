package com.soco.SoCoClient.events.service;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.EventsResponseUtil;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.posts.Post;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class EventPostsTask extends AsyncTask<String, Void, ArrayList<Post>> {

    String tag = "EventPostsTask";
    String user_id;
    String token;
    TaskCallBack callBack;

    public EventPostsTask(String user_id, String token, TaskCallBack cb){
        Log.v(tag, "event posts task: " + user_id);
        this.user_id=user_id;
        this.token=token;
        callBack = cb;
    }

    protected ArrayList<Post> doInBackground(String... params) {
        String url = UrlUtil.getEventPostsUrl();
        Object response = request(
                url,
                user_id,
                token,
                params[0]   //eventid
        );

        if (response != null) {
            Log.v(tag, "parse response");
            return parse(response);
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
            String event_id){
        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair(JsonKeys.USER_ID, user_id));
        params.add(new BasicNameValuePair(JsonKeys.TOKEN, token));
        params.add(new BasicNameValuePair(JsonKeys.EVENT_ID, event_id));
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        Log.d(tag, "request url: " + url);

        return HttpUtil.executeHttpGet(url);
    }

    private ArrayList<Post> parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());
        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                String s = json.getString(JsonKeys.POSTS);
                JSONArray array = new JSONArray(s);
                return parsePosts(array);
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
            return null;
        }

        return null;
    }

    private ArrayList<Post> parsePosts(JSONArray array){
        ArrayList<Post> posts = new ArrayList<>();
        for(int i=0; i<array.length(); i++){
            try {
                JSONObject o = array.getJSONObject(i);
                //todo parse posts from json
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e(tag, "error parse event post: " + e.toString());
                return null;
            }
        }

        return posts;
    }

    protected void onPostExecute(Event result) {
        callBack.doneTask(result);
    }
}
