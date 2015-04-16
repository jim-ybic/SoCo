package com.soco.SoCoClient.control.http.task;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.http.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SetProjectAttributeTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "SetProjectAttributeTaskAsync";

    String url;
    String pid_onserver;
    HashMap<String, String> attrMap;

    public SetProjectAttributeTaskAsync(
            String url,
            String pid_onserver,
            HashMap<String, String> attrMap
    ){
        Log.i(tag, "Create new HttpTask: " + url);
        this.url = url;
        this.pid_onserver = pid_onserver;
        this.attrMap = attrMap;

        //todo: remove testing script
        if(pid_onserver == null){
            Log.e(tag, "pid_onserver is null");
            this.pid_onserver = "1";
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty()){
            Log.e(tag, "Cannot get url/type");
            return false;
        }

        execute(url, pid_onserver, attrMap);
        return true;
    }

    public static void execute(String url, String pid_onserver,
                               HashMap<String, String> attrMap){
        Log.i(tag, "Execute: " + url + ", " + pid_onserver + ", "
                + attrMap);
        Object response = request(url, pid_onserver, attrMap);
        if (response != null)
            parse(response);
    }

    public static Object request(String url, String pid_onserver,
                                 HashMap<String, String> attrMap) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfig.JSON_KEY_PROJECT_ID, Integer.parseInt(pid_onserver));

            JSONArray attrArray = new JSONArray();
            for(HashMap.Entry<String, String> e : attrMap.entrySet()){
                JSONObject attr = new JSONObject();
                attr.put(HttpConfig.JSON_KEY_ATTRIBUTE_NAME, e.getKey());
//                attr.put(HttpConfig.JSON_KEY_ATTRIBUTE_INDEX, DataConfig.INT_INDEX_1);
                attr.put(HttpConfig.JSON_KEY_ATTRIBUTE_TYPE, DataConfig.TYPE_STRING);
                attr.put(HttpConfig.JSON_KEY_ATTRIBUTE_VALUE, e.getValue());
                attrArray.put(attr);
            }
            data.put("attribute", attrArray);

            Log.i(tag, "Set project attribute Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public static boolean parse(Object response) {
        try {
            String str = response.toString();
            Log.i(tag, "Set project attribute parse string: " + str);

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
