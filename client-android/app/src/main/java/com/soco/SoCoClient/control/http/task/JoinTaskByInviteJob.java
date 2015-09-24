package com.soco.SoCoClient.control.http.task;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control.config._ref.HttpConfigV1;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.config.GeneralConfig;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.http.HttpUtil;
import com.soco.SoCoClient.model.Attribute;
import com.soco.SoCoClient.model.Task;

import org.json.JSONArray;
import org.json.JSONObject;

public class JoinTaskByInviteJob extends AsyncTask<Void, Void, Boolean>{

    String tag = "JoinTaskByInviteJob";

    Context context;
    Task task;

    public JoinTaskByInviteJob(Context context, Task task){
        Log.v(tag, "join task by invite: " + task.toString());
        this.context = context;
        this.task = task;
    }

    protected Boolean doInBackground(Void... params) {
        String url = getUrl();
        JSONObject data = getJsonData();
        Object response = HttpUtil.executeHttpPost(url, data);
        if(response != null)
            parse(response);
        return null;
    }

    String getUrl(){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        String ip = settings.getString(HttpConfig.PROFILE_SERVER_IP, "");
        String port = settings.getString(HttpConfig.PROFILE_SERVER_PORT, "");
        String token = settings.getString(HttpConfig.PROFILE_LOGIN_ACCESS_TOKEN, "");
        if(ip.isEmpty() || port.isEmpty() || token.isEmpty()) {
            Log.e(tag, "cannot load ip/port/token from shared preference");
            return "";
        }

        String path = HttpConfig.SERVER_PATH_JOIN_TASK_BY_INVITE;
        String url = "http://" + ip + ":" + port + path + "?"
                + HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;

        Log.d(tag, "get url [JoinTaskByInviteJob]: " + url);
        return url;
    }

    JSONObject getJsonData(){
        JSONObject data = new JSONObject();
        try{
            data.put(HttpConfig.JSON_KEY_ACTIVITY, task.getTaskIdServer());
        }catch(Exception e){
            Log.e(tag, "cannot create json data: " + e);
            e.printStackTrace();
        }

        Log.d(tag, "get json data: " + data);
        return data;
    }

    boolean parse(Object response){
        Log.d(tag, "parse server response: " + response);
        try {
            JSONObject data = new JSONObject(response.toString());
            String isSuccess = data.getString(HttpConfig.JSON_KEY_STATUS);
            if(isSuccess.equals(HttpConfig.JSON_VALUE_SUCCESS)){
                Log.v(tag, "server response success");
                if(data.has(HttpConfig.JSON_KEY_ACTIVITY)){
                    JSONObject obj = new JSONObject(data.getString(HttpConfig.JSON_KEY_ACTIVITY));
                    String taskName = obj.getString(HttpConfig.JSON_KEY_NAME);
                    String taskTag = obj.getString(HttpConfig.JSON_KEY_TAG);
                    String taskSignature = obj.getString(HttpConfig.JSON_KEY_SIGNATURE);
                    String taskType = obj.getString(HttpConfig.JSON_KEY_TYPE);
                    task.setTaskName(taskName);
                    //todo: handle other task attributes
                    task.save();
                    Log.d(tag, "saved task with server udpates: " + task.toString());
                }
                if(data.has(HttpConfig.JSON_KEY_ATTRIBUTES)){
                    JSONArray attributes = new JSONArray(data.getString(HttpConfig.JSON_KEY_ATTRIBUTES));
                    for(int i=0; i<attributes.length(); i++){
                        JSONObject attribute = attributes.getJSONObject(i);
                        String attrName = attribute.getString(HttpConfig.JSON_KEY_NAME);
                        String attrType = attribute.getString(HttpConfig.JSON_KEY_TYPE);
                        String attrValue = attribute.getString(HttpConfig.JSON_KEY_VALUE);
                        Attribute attr = new Attribute(context, DataConfig.ACTIVITY_TYPE_TASK,
                                task.getTaskIdLocal(), task.getTaskIdServer());
                        attr.setAttrName(attrName);
                        attr.setAttrValue(attrValue);;
                        //todo: handle other attributes
                        attr.save();
                        Log.d(tag, "new attributes saved to database: " + attr.toString());
                    }
                }
            }else {
                Log.e(tag, "cannot receive success response from server");
                return false;
            }
        }catch (Exception e){
            Log.e(tag, "cannot create json data: " + e);
            e.printStackTrace();
        }
        return true;
    }
}
