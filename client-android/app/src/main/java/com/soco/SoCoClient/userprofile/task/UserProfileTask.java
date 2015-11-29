package com.soco.SoCoClient.userprofile.task;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.userprofile.model.User;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class UserProfileTask extends AsyncTask<Void, Void, Boolean>{

    String tag = "UserProfileTask";

    Context context;
    SocoApp socoApp;
//    Group group;
    User user;

    TaskCallBack callBack;

    public UserProfileTask(Context c, User u, TaskCallBack cb){
        Log.v(tag, "user profile task: " + u.toString());
        context = c;
        socoApp = (SocoApp) context;
        user = u;
        callBack = cb;

//        socoApp.createGroupResult = false;
    }


    protected Boolean doInBackground(Void... params) {
        Log.v(tag, "validate data");
        if(!socoApp.SKIP_LOGIN && (socoApp.user_id.isEmpty() || socoApp.token.isEmpty())){
            Log.e(tag, "user id or token or event is not available");
            return false;
        }

        String url = UrlUtil.getUserProfileUrl();
        Object response = request(
                url,
                socoApp.user_id,
                socoApp.token,
                user
        );

        if (response != null) {
            Log.v(tag, "parse response");
            parse(response);
        }
        else {
            Log.e(tag, "response is null, cannot parse");
        }

        return true;
    }

    Object request(
            String url,
            String user_id,
            String token,
            User u){
//        Log.v(tag, "create json request");

        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<>();
        if(socoApp.SKIP_LOGIN) {
            Log.v(tag, "test user id: " + JsonKeys.TEST_USER_ID + ", test token: " + JsonKeys.TEST_TOKEN);
            params.add(new BasicNameValuePair(JsonKeys.USER_ID, JsonKeys.TEST_USER_ID));
            params.add(new BasicNameValuePair(JsonKeys.TOKEN, JsonKeys.TEST_TOKEN));
        }
        else{
            params.add(new BasicNameValuePair(JsonKeys.USER_ID, user_id));
            params.add(new BasicNameValuePair(JsonKeys.TOKEN, token));
        }
        params.add(new BasicNameValuePair(JsonKeys.BUDDY_USER_ID, user.getUser_id()));
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        Log.d(tag, "request url: " + url);

        return HttpUtil.executeHttpGet(url);
    }

     boolean parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                String userStr = json.getString(JsonKeys.USER);
                JSONObject userObj = new JSONObject(userStr);

                String name = userObj.getString(JsonKeys.USER_NAME);
                Log.v(tag, "user name: " + name);
                user.setUser_name(name);

                user.setUser_icon_url(userObj.getString(JsonKeys.USER_ICON_URL));
                user.setLocation(userObj.getString(JsonKeys.LOCATION));

                String hometown = userObj.getString(JsonKeys.HOMETOWN);
                Log.v(tag, "hometown: " + hometown);
                user.setHometown(hometown);

                String bio = userObj.getString(JsonKeys.BIOGRAPHY);
                Log.v(tag, "bio: " + bio);
                user.setBiography(bio);

                String friendsStr = userObj.getString(JsonKeys.FRIENDS_LIST);
                Log.v(tag, "friends list: " + friendsStr);
                JSONArray allFriends = new JSONArray(friendsStr);
                for(int i=0; i<allFriends.length(); i++){
                    JSONObject friend = allFriends.getJSONObject(i);
                    User u = new User(friend);
                    user.addFriends_list(u);
                    Log.v(tag, "add new friend: " + u.getUser_id() + ", " + u.getUser_name());
                }
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
            return false;
        }

        return true;
    }

    protected void onPostExecute(Boolean result) {
        callBack.doneTask(null);
    }

}
