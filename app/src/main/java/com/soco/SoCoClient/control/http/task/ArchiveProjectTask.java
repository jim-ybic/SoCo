package com.soco.SoCoClient.control.http.task;

import android.util.Log;

import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.http.HttpUtil;

import org.json.JSONObject;

public class ArchiveProjectTask {

    public static String tag = "ArchiveProjectTask";

    public static void execute(String url, String pid){
        Object response = request(url, pid);
        if (response != null)
            parse(response);
    }

    public static Object request(String url, String pid) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfig.JSON_KEY_PROJECT_ID, pid);
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
