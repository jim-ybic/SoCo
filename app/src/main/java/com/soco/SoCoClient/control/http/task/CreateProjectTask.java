package com.soco.SoCoClient.control.http.task;

import android.util.Log;

import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.http.HttpUtil;

import org.json.JSONObject;

public class CreateProjectTask {

    public static String tag = "CreateProjectTask";

    static String TEST_STR_TO_REMOVE = "test string - to be removed";

    public static void execute(String url, String pname){
        Object response = request(url, pname);
        if (response != null)
            parse(response);
    }

    public static Object request(String url, String pname) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfig.JSON_KEY_PROJECT_NAME, pname);
            data.put(HttpConfig.JSON_KEY_PROJECT_TYPE, TEST_STR_TO_REMOVE);
            data.put(HttpConfig.JSON_KEY_PROJECT_TAG, TEST_STR_TO_REMOVE);
            data.put(HttpConfig.JSON_KEY_PROJECT_SIGNATURE, TEST_STR_TO_REMOVE);
            Log.i(tag, "Create project Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public static boolean parse(Object response) {
        try {
            String str = response.toString();
            Log.i(tag, "Create project parse string: " + str);
            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfig.JSON_KEY_RESPONSE_STATUS);
            if(isSuccess.equals(HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Create project server parse: " + HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS);
                return true;
            }
            else {
                Log.e(tag, "Cannot receive create project status parse from server");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
