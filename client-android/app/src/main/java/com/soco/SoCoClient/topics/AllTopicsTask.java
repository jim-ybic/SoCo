package com.soco.SoCoClient.topics;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.posts.Photo;
import com.soco.SoCoClient.posts.Post;
import com.soco.SoCoClient.userprofile.model.User;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class AllTopicsTask extends AsyncTask<String, Void, ArrayList<Topic>> {

    String tag = "AllTopicsTask";
    String user_id;
    String token;
    TaskCallBack callBack;

    public AllTopicsTask(String user_id, String token, TaskCallBack cb){
        Log.v(tag, "all topics task");
        this.user_id=user_id;
        this.token=token;
        callBack = cb;
    }

    protected ArrayList<Topic> doInBackground(String... params) {
        String url = UrlUtil.getTopicsUrl();
        Object response = request(
                url,
                user_id,
                token
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
            String token
    ){
        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair(JsonKeys.USER_ID, user_id));
        params.add(new BasicNameValuePair(JsonKeys.TOKEN, token));

        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;
        Log.d(tag, "request url: " + url);

        return HttpUtil.executeHttpGet(url);
    }

    private ArrayList<Topic> parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());
        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                String s = json.getString(JsonKeys.TOPICS);
                JSONArray array = new JSONArray(s);
                return parseTopics(array);
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

    private ArrayList<Topic> parseTopics(JSONArray array){
        ArrayList<Topic> topics = new ArrayList<>();
        for(int i=0; i<array.length(); i++){
            try {
                JSONObject o = array.getJSONObject(i);
                Topic p = parseTopic(o);
                topics.add(p);
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e(tag, "error parse event post: " + e.toString());
                return null;
            }
        }

        Log.v(tag, topics.size() + " posts downloaded");
        return topics;
    }

    private Topic parseTopic(JSONObject o){
        Log.v(tag, "parse topic from json: " + o);
        if(o == null) {
            Log.e(tag, "json is null");
            return null;
        }
        else{
            Topic p = new Topic();
            try {
                p.setId(o.getString(JsonKeys.ID));
                p.setTitle(o.getString(JsonKeys.TITLE));
                if(o.has(JsonKeys.INTRODUCTION))
                    p.setIntroduction(o.getString(JsonKeys.INTRODUCTION));

                p.setNumberPhotos(o.getInt(JsonKeys.NUMBER_OF_PHOTOS));
                p.setNumberEvents(o.getInt(JsonKeys.NUMBER_OF_EVENTS));
                p.setNumberPosts(o.getInt(JsonKeys.NUMBER_OF_POSTS));
                p.setNumberViews(o.getInt(JsonKeys.NUMBER_OF_VIEWS));

                Long time = Long.valueOf(o.getString(JsonKeys.CREATE_DATE));
                p.setCreateTimedate(TimeUtil.getDate(time, "HH:mm  dd/MM"));

                if(o.has(JsonKeys.BANNER_URL) && !o.getString(JsonKeys.BANNER_URL).isEmpty()){
                    p.setBanner_url(o.getString(JsonKeys.BANNER_URL));
                    Log.v(tag, "topic banner url: " + p.getBanner_url());
                }
                else{
                    Log.v(tag, "topic has no banner, use default banner: " + Config.DEFAULT_TOPIC_BANNER_URL);
                    p.setBanner_url(Config.DEFAULT_TOPIC_BANNER_URL);
                }

                if(o.has(JsonKeys.GROUP)) {
                    Group g = parseGroup(o.getJSONObject(JsonKeys.GROUP));
                    p.setGroup(g);
                }

                if(o.has(JsonKeys.CREATOR)) {
                    User u = parseUser(o.getJSONObject(JsonKeys.CREATOR));
                    p.setCreator(u);
                }

                Log.v(tag, "parsed topic: " + p.toString());
                return p;
            }
            catch(Exception e){
                Log.e(tag, "error parse topic from json: " + e.toString());
                e.printStackTrace();
                return null;
            }
        }
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

    private Group parseGroup(JSONObject o){
        Log.v(tag, "parse group from json: " + o);
        if(o == null){
            Log.e(tag, "json is null");
            return null;
        }
        else{
            Group u = new Group();
            try {
                u.setGroup_id(o.getString(JsonKeys.GID));
                u.setGroup_name(o.getString(JsonKeys.NAME));
            }
            catch (Exception e){
                Log.e(tag, "error parse user from json: " + e);
                e.printStackTrace();
                return null;
            }
            Log.d(tag, "get group: " + u.toString());
            return u;
        }
    }

    protected void onPostExecute(ArrayList<Topic> topics) {
        if(topics == null)
            Log.e(tag, "no topic received");
        else
            Log.v(tag, "post execute, size: " + topics.size());
        callBack.doneTask(topics);
    }
}
