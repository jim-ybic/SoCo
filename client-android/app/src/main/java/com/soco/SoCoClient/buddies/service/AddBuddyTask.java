package com.soco.SoCoClient.buddies.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;

import org.json.JSONObject;

public class AddBuddyTask extends AsyncTask<String, Void, Object> {
    private TextView tv;
    private static String tag="AddBuddyTask";
    public AddBuddyTask(TextView view){
        this.tv = view;
    }
    protected Object doInBackground(String... friends) {
        String url = UrlUtil.getAddBuddyUrl();
        JSONObject data = new JSONObject();
        Log.v(tag, "create json request");
        for(String id:friends) {
            try {
                data.put(JsonKeys.USER_ID, SocoApp.user_id);
                data.put(JsonKeys.TOKEN, SocoApp.token);
                data.put(JsonKeys.FRIEND_ID,id);
                Log.d(tag, "create event json: " + data);
            } catch (Exception e) {
                Log.e(tag, "cannot create json post data");
                e.printStackTrace();
            }
            Log.d(tag, url);
            Log.d(tag, data.toString());
        }
        return HttpUtil.executeHttpPost(url, data);
    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    protected void onPostExecute(Object response) {
        try{
            JSONObject json = new JSONObject(response.toString());
            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                tv.setText("Added");
                tv.setEnabled(false);
            }
         } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
        }

    }
}