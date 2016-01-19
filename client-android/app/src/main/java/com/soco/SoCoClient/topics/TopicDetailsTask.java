package com.soco.SoCoClient.topics;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.EventsResponseUtil;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.userprofile.model.User;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class TopicDetailsTask extends AsyncTask<String, Void, Topic> {

    String tag = "TopicDetailsTask";
    String user_id;
    String token;
    String topicId;
    TaskCallBack callBack;

    public TopicDetailsTask(
            String user_id, String token,
            String topicId,
            TaskCallBack cb){
        Log.v(tag, "topic details task: " + topicId);
        this.user_id=user_id;
        this.token=token;
        this.topicId = topicId;
        this.callBack = cb;
    }

    protected Topic doInBackground(String... params) {
        Log.v(tag, "validate data");

        String url = UrlUtil.getTopicUrl();
        Object response = request(url);

        if (response != null) {
            Log.v(tag, "parse response");
            return parse(response);
        }
        else {
            Log.e(tag, "response is null, cannot parse");
        }
        return null;
    }

    Object request(String url){
        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair(JsonKeys.USER_ID, user_id));
        params.add(new BasicNameValuePair(JsonKeys.TOKEN, token));
        params.add(new BasicNameValuePair(JsonKeys.TOPIC_ID, topicId));
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        Log.d(tag, "request url: " + url);

        return HttpUtil.executeHttpGet(url);
    }

    private Topic parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                String s = json.getString(JsonKeys.TOPIC);
                JSONObject obj = new JSONObject(s);
                Log.v(tag, "current topic json: " + obj.toString());
                return parseTopic(obj);
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

    private Topic parseTopic(JSONObject o){
        if(o == null)
            return null;

        Topic t = new Topic();
        try {
            t.setId(o.getString(JsonKeys.ID));
            t.setTitle(o.getString(JsonKeys.TITLE));
            t.setIntroduction(o.getString(JsonKeys.INTRODUCTION));

            String sUser = o.getString(JsonKeys.CREATOR);
            JSONObject oUser = new JSONObject(sUser);
            User u = parseUser(oUser);
            t.setCreator(u);

            //todo: parse any other details
        }
        catch (Exception e){
            Log.e(tag, "cannot parse topic from json, return null: " + e);
            e.printStackTrace();
            return null;
        }

        Log.d(tag, "get topic from json: " + t.toString());
        return t;
    }

    private User parseUser(JSONObject o){
        Log.v(tag, "parse user from json: " + o);
        if(o == null){
            Log.e(tag, "json is null");
            return null;
        }
        else{
            User u = new User();
            try {
                u.setUser_id(o.getString(JsonKeys.USER_ID));
                u.setUser_name(o.getString(JsonKeys.USER_NAME));
            }
            catch (Exception e){
                Log.e(tag, "error parse user from json: " + e);
                e.printStackTrace();
                return null;
            }
            return u;
        }
    }

    protected void onPostExecute(Topic o) {
        callBack.doneTask(o);
    }
}
