package com.soco.SoCoClient.events.service;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.EventsResponseUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events.model.Event;
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
                Post p = parsePost(o);
                posts.add(p);
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e(tag, "error parse event post: " + e.toString());
                return null;
            }
        }

        Log.v(tag, posts.size() + " posts downloaded");
        return posts;
    }

    private Post parsePost(JSONObject o){
        Log.v(tag, "parse post from json: " + o);
        if(o == null) {
            Log.e(tag, "json is null");
            return null;
        }
        else{
            Post p = new Post();
            try {
                p.setId(o.getString(JsonKeys.ID));

                Long time = Long.valueOf(o.getString(JsonKeys.TIME));
                p.setTime(TimeUtil.getDate(time, "HH:mm  dd/MM"));
                p.setComment(o.getString(JsonKeys.COMMENT));

                User u = parseUser(o.getJSONObject(JsonKeys.USER));
                p.setUser(u);

                if(o.has(JsonKeys.PHOTOS)) {
                    ArrayList<Photo> photos = parsePhotos(o.getJSONArray(JsonKeys.PHOTOS));
                    p.setPhotos(photos);
                }

                Log.v(tag, "parsed post: " + p.toString());
                return p;
            }
            catch(Exception e){
                Log.e(tag, "error parse post from json: " + e.toString());
                e.printStackTrace();
                return null;
            }
        }
    }

    private ArrayList<Photo> parsePhotos(JSONArray array){
        if(array == null){
            Log.e(tag, "json is null");
            return null;
        }
        else{
            ArrayList<Photo> photos = new ArrayList<>();
            for(int i=0; i<array.length(); i++){
                try {
                    Photo p = parsePhoto(array.getJSONObject(i));
                    photos.add(p);
                }
                catch (Exception e){
                    Log.e(tag, "cannot get object from json array");
                    e.printStackTrace();
                }
            }

            Log.v(tag, photos.size() + " photos parsed");
            return photos;
        }
    }

    private Photo parsePhoto(JSONObject o){
        if(o == null){
            Log.e(tag, "json is null");
            return null;
        }
        else{
            Photo p = new Photo();
            try {
                p.setName(o.getString(JsonKeys.PHOTO_NAME));
                p.setUrl(o.getString(JsonKeys.PHOTO_URL));
                Log.v(tag, "parsed photo: " + p);
                return p;
            }
            catch(Exception e){
                Log.e(tag, "error parse photo from json: " + e);
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
                u.setUser_id(o.getString(JsonKeys.ID));
                u.setUser_name(o.getString(JsonKeys.NAME));
            }
            catch (Exception e){
                Log.e(tag, "error parse user from json: " + e);
                e.printStackTrace();
                return null;
            }
            return u;
        }
    }

    protected void onPostExecute(ArrayList<Post> posts) {
        Log.v(tag, "post execute, size: " + posts.size());
        callBack.doneTask(posts);
    }
}
