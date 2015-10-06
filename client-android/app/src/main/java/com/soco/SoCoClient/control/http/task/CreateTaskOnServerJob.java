package com.soco.SoCoClient.control.http.task;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control._ref.HttpConfigV1;
import com.soco.SoCoClient.control.profile.Config;
import com.soco.SoCoClient.control.http.HttpUtil;
import com.soco.SoCoClient.model.Task;

import org.json.JSONObject;

public class CreateTaskOnServerJob extends AsyncTask<Void, Void, Boolean>{

    String tag = "CreateTaskOnServerJob";

    Context context;
    Task task;

    public CreateTaskOnServerJob(Context context, Task task){
        Log.v(tag, "create task on server: " + task.toString());
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
        String ip = settings.getString(com.soco.SoCoClient.control.http.Config.PROFILE_SERVER_IP, "");
        String port = settings.getString(com.soco.SoCoClient.control.http.Config.PROFILE_SERVER_PORT, "");
        String token = settings.getString(com.soco.SoCoClient.control.http.Config.PROFILE_LOGIN_ACCESS_TOKEN, "");
        if(ip.isEmpty() || port.isEmpty() || token.isEmpty()) {
            Log.e(tag, "cannot load ip/port/token from shared preference");
            return "";
        }

        String path = com.soco.SoCoClient.control.http.Config.SERVER_PATH_CREATE_TASK;
        String url = "http://" + ip + ":" + port + path + "?"
                + HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;

        Log.d(tag, "get url [CreateTaskOnServerJob]: " + url);
        return url;
    }

    JSONObject getJsonData(){
        JSONObject data = new JSONObject();
        try{
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_NAME, task.getTaskName());
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_SIGNATURE, com.soco.SoCoClient.control.database.Config.ENTITY_VALUE_EMPTY);
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_TYPE, com.soco.SoCoClient.control.database.Config.ENTITY_VALUE_EMPTY);
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_TAG, com.soco.SoCoClient.control.database.Config.ENTITY_VALUE_EMPTY);
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
            String isSuccess = data.getString(com.soco.SoCoClient.control.http.Config.JSON_KEY_STATUS);
            if(isSuccess.equals(com.soco.SoCoClient.control.http.Config.JSON_VALUE_SUCCESS)){
                int tidServer = Integer.parseInt(data.getString(com.soco.SoCoClient.control.http.Config.JSON_KEY_ID));
                task.refresh();
                task.setTaskIdServer(tidServer);
                task.save();
                Log.d(tag, "updated task id server: " + task.toString());
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
