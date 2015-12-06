package com.soco.SoCoClient.buddies.service;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.buddies.allbuddies.ui.MyMatchListEntryItem;
import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by David_WANG on 12/06/2015.
 */
public class DownloadMyMatchTask extends AsyncTask<String, Void, ArrayList<MyMatchListEntryItem>> {

    String tag = "DownloadMyMatchTask";
    String user_id;
    String token;
//    String[] paramNames;
    TaskCallBack callBack;

    public DownloadMyMatchTask(String user_id, String token, TaskCallBack cb){
        Log.v(tag, "user event task: " + user_id);
        this.user_id=user_id;
        this.token=token;
        callBack = cb;
    }
    protected ArrayList<MyMatchListEntryItem> doInBackground(String... params) {
        Log.v(tag, "validate data");

        String url = UrlUtil.getMyMatchUrl();
        Object response = request(
                url,
                user_id,
                token,
                params
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

    Object request(
            String url,
            String user_id,
            String token,
            String... inputs){
        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair(JsonKeys.USER_ID, user_id));
        params.add(new BasicNameValuePair(JsonKeys.TOKEN, token));
        if(inputs!=null&&inputs.length>0) {
           params.add(new BasicNameValuePair(JsonKeys.START_INDEX, inputs[0]));
        }
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        Log.d(tag, "request url: " + url);

        return HttpUtil.executeHttpGet(url);
    }

    ArrayList<MyMatchListEntryItem> parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());
        Log.v(tag, "clear my match");
        try {
            JSONObject json;
            json = new JSONObject(response.toString());
            Log.d(tag, "converted json: " + json);


            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                String allBuddiesString = json.getString(JsonKeys.BUDDIES);
                Log.v(tag, "all buddies string: " + allBuddiesString);
                JSONArray allBuddies = new JSONArray(allBuddiesString);
                Log.d(tag, "retrieve buddy list: " + allBuddies.length() + " users downloaded");
                ArrayList<MyMatchListEntryItem> result = new ArrayList<>();
                for(int i=0; i<allBuddies.length(); i++){
                    MyMatchListEntryItem u = new MyMatchListEntryItem();
                    JSONObject obj = allBuddies.getJSONObject(i);
                    Log.v(tag, "current buddy json: " + obj.toString());
                    parseUserBasics(u, obj);
                    result.add(u);
                }
                return result;
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "create buddy fail, " +
                                "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
        }

        return null;
    }

    private void parseUserBasics(MyMatchListEntryItem u, JSONObject obj) throws JSONException {
        u.setUser_id(obj.getString(JsonKeys.USER_ID));
        u.setUser_name(obj.getString(JsonKeys.USER_NAME));
        u.setSuggest_reason(obj.getString(JsonKeys.HIGHLIGHT));
        Log.v(tag, "user id, name, reason: " + u.getUser_id() + ", " + u.getUser_name() + ", " + u.getSuggest_reason());
    }
    protected void onPostExecute(ArrayList<MyMatchListEntryItem> result) {
        callBack.doneTask(result);
    }
}
