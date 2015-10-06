package com.soco.SoCoClient.common.http.task;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.profile.Config;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.events.model.Attribute;
import com.soco.SoCoClient.common.model.Task;

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
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        String ip = settings.getString(com.soco.SoCoClient.common.http.Config.PROFILE_SERVER_IP, "");
        String port = settings.getString(com.soco.SoCoClient.common.http.Config.PROFILE_SERVER_PORT, "");
        String token = settings.getString(com.soco.SoCoClient.common.http.Config.PROFILE_LOGIN_ACCESS_TOKEN, "");
        if(ip.isEmpty() || port.isEmpty() || token.isEmpty()) {
            Log.e(tag, "cannot load ip/port/token from shared preference");
            return "";
        }

        String path = com.soco.SoCoClient.common.http.Config.SERVER_PATH_JOIN_TASK_BY_INVITE;
        String url = "http://" + ip + ":" + port + path + "?"
                + HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;

        Log.d(tag, "get url [JoinTaskByInviteJob]: " + url);
        return url;
    }

    JSONObject getJsonData(){
        JSONObject data = new JSONObject();
        try{
            data.put(com.soco.SoCoClient.common.http.Config.JSON_KEY_ACTIVITY, task.getTaskIdServer());
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
            String isSuccess = data.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_STATUS);
            if(isSuccess.equals(com.soco.SoCoClient.common.http.Config.JSON_VALUE_SUCCESS)){
                Log.v(tag, "server response success");
                if(data.has(com.soco.SoCoClient.common.http.Config.JSON_KEY_ACTIVITY)){
                    JSONObject obj = new JSONObject(data.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_ACTIVITY));
                    String taskName = obj.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_NAME);
                    String taskTag = obj.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_TAG);
                    String taskSignature = obj.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_SIGNATURE);
                    String taskType = obj.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_TYPE);
                    task.setTaskName(taskName);
                    //todo: handle other task attributes
                    task.save();
                    Log.d(tag, "saved task with server udpates: " + task.toString());
                }
                if(data.has(com.soco.SoCoClient.common.http.Config.JSON_KEY_ATTRIBUTES)){
                    JSONArray attributes = new JSONArray(data.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_ATTRIBUTES));
                    for(int i=0; i<attributes.length(); i++){
                        JSONObject attribute = attributes.getJSONObject(i);
                        String attrName = attribute.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_NAME);
                        String attrType = attribute.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_TYPE);
                        String attrValue = attribute.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_VALUE);
                        Attribute attr = new Attribute(context, com.soco.SoCoClient.common.database.Config.ACTIVITY_TYPE_TASK,
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
