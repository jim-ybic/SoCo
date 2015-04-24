package com.soco.SoCoClient.control.http.task;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.http.HttpUtil;

import org.json.JSONObject;

public class ArchiveActivityTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "ArchiveProjectTask";

    String url;
    String pid_onserver;

    public ArchiveActivityTaskAsync(
            String url,
            String pid_onserver
    ){
        Log.i(tag, "Create new HttpTask: "
                + url + ", " + pid_onserver);
        this.url = url;
        this.pid_onserver = pid_onserver;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() || pid_onserver == null){
            Log.e(tag, "Cannot get url/pid_onserver");
            return false;
        }

        execute(url, pid_onserver);
        return true;
    }

    public static void execute(String url, String pid_onserver){
        Object response = request(url, pid_onserver);
        if (response != null)
            parse(response);
    }

    public static Object request(String url, String pid_onserver) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfig.JSON_KEY_PROJECT_ID, Integer.parseInt(pid_onserver));
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
