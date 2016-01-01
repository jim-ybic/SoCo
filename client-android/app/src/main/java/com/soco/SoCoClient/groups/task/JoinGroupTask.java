package com.soco.SoCoClient.groups.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.groups.model.Group;

import org.json.JSONObject;

public class JoinGroupTask extends AsyncTask<Void, Void, Boolean> {

    String tag = "JoinGroupTask";

    Context context;
    SocoApp socoApp;
    Group group;

    TaskCallBack callBack;

    public JoinGroupTask(Context c, Group g, TaskCallBack cb){
        Log.v(tag, "join group task: ");
        context = c;
        socoApp = (SocoApp) context;
        group = g;
        callBack = cb;

        socoApp.joinGroupResult = false;
    }


    protected Boolean doInBackground(Void... params) {
        Log.v(tag, "validate data");
        if(!socoApp.SKIP_LOGIN && (socoApp.user_id.isEmpty() || socoApp.token.isEmpty())){
            Log.e(tag, "user id or token or event is not available");
            return false;
        }

        String url = UrlUtil.getJoinGroupUrl();
        Object response = request(
                url,
                socoApp.user_id,
                socoApp.token,
                group
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

    private Object request(
            String url,
            String user_id,
            String token,
            Group g){
        Log.v(tag, "create json request");

        JSONObject data = new JSONObject();
        try {
            data.put(JsonKeys.USER_ID, user_id);
            data.put(JsonKeys.TOKEN, token);
            data.put(JsonKeys.GROUP_ID, g.getGroup_id());
            Log.d(tag, "created json: " + data);
        } catch (Exception e) {
            Log.e(tag, "cannot create json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    private boolean parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                Log.d(tag, "joingroup success, update flag");
                socoApp.joinGroupResult = true;
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "create group fail, "
                                + "error code: " + error_code
                                + ", message: " + message
                                + ", more info: " + more_info
                );
                socoApp.joinGroupResult = false;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            socoApp.joinGroupResult = false;
            return false;
        }

        return true;
    }

    protected void onPostExecute(Boolean result) {
        callBack.doneTask(socoApp.joinGroupResult);
    }

}
