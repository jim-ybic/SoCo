package com.soco.SoCoClient.v2.businesslogic.http.task;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.v2.businesslogic.config.DataConfig2;
import com.soco.SoCoClient.v2.businesslogic.config.GeneralConfig2;
import com.soco.SoCoClient.v2.businesslogic.config.HttpConfig2;
import com.soco.SoCoClient.v2.businesslogic.http.HttpUtil2;
import com.soco.SoCoClient.v2.datamodel.Task;
import com.soco.SoCoClient.v1.control.config.HttpConfig;

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
        Object response = HttpUtil2.executeHttpPost(url, data);
        if(response != null)
            parse(response);
        return null;
    }

    String getUrl(){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig2.PROFILE_FILENAME, 0);
        String ip = settings.getString(HttpConfig2.PROFILE_SERVER_IP, "");
        String port = settings.getString(HttpConfig2.PROFILE_SERVER_PORT, "");
        String token = settings.getString(HttpConfig2.PROFILE_LOGIN_ACCESS_TOKEN, "");
        if(ip.isEmpty() || port.isEmpty() || token.isEmpty()) {
            Log.e(tag, "cannot load ip/port/token from shared preference");
            return "";
        }

        String path = HttpConfig2.SERVER_PATH_CREATE_TASK;
        String url = "http://" + ip + ":" + port + path + "?"
                + HttpConfig.HTTP_TOKEN_TYPE + "=" + token;

        Log.d(tag, "get url: " + url);
        return url;
    }

    JSONObject getJsonData(){
        JSONObject data = new JSONObject();
        try{
            data.put(HttpConfig2.JSON_KEY_NAME, task.getTaskName());
            data.put(HttpConfig2.JSON_KEY_SIGNATURE, DataConfig2.ENTITY_VALUE_EMPTY);
            data.put(HttpConfig2.JSON_KEY_TYPE, DataConfig2.ENTITY_VALUE_EMPTY);
            data.put(HttpConfig2.JSON_KEY_TAG, DataConfig2.ENTITY_VALUE_EMPTY);
        }catch(Exception e){
            Log.e(tag, "cannot create json data: " + e);
            e.printStackTrace();
        }

        Log.d(tag, "get json data: " + data);
        return data;
    }

    boolean parse(Object response){
        Log.v(tag, "parse server response: " + response);
        try {
            JSONObject data = new JSONObject(response.toString());
            String isSuccess = data.getString(HttpConfig2.JSON_KEY_STATUS);
            if(isSuccess.equals(HttpConfig2.JSON_VALUE_SUCCESS)){
                int tidServer = Integer.parseInt(data.getString(HttpConfig2.JSON_KEY_ID));
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
