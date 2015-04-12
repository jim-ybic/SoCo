package com.soco.SoCoClient.control.http.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.http.HttpUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class InviteProjectMemberTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "InviteProjectMemberTask";

    String url;
    String pid_onserver;
    String inviteEmail;

    public InviteProjectMemberTaskAsync(
            String url,
            String pid_onserver,
            String inviteEmail
    ){
        Log.i(tag, "Create new HttpTask: "
                + url + ", " + pid_onserver + ", " + inviteEmail);
        this.url = url;
        this.pid_onserver = pid_onserver;
        this.inviteEmail = inviteEmail;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() || pid_onserver == null || inviteEmail.isEmpty()){
            Log.e(tag, "Cannot get url/type");
            return false;
        }

        execute(url, pid_onserver, inviteEmail);
        return true;
    }

    public static void execute(String url, String pid_onserver, String inviteEmail){
        Object response = request(url, pid_onserver, inviteEmail);
        if (response != null)
            parse(response);
    }

    public static Object request(String url, String pid_onserver, String inviteEmail) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfig.JSON_KEY_PROJECT_ID, Integer.parseInt(pid_onserver));
            data.put(HttpConfig.JSON_KEY_EMAIL, inviteEmail);
            Log.i(tag, "Post Json: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public static boolean parse(Object response) {
        try {
            String str = response.toString();
            Log.i(tag, "Server response string: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfig.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Server parse: " + HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS);
                return true;
            }
            else {
                Log.e(tag, "Cannot receive parse from server");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

}
