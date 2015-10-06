package com.soco.SoCoClient.control.http.task;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control._ref.HttpConfigV1;
import com.soco.SoCoClient.control.profile.Config;
import com.soco.SoCoClient.control.http.HttpUtil;
import com.soco.SoCoClient.control.common.TimeUtil;
import com.soco.SoCoClient.model.Message;

import org.json.JSONObject;

public class SendMessageJob extends AsyncTask<Void, Void, Boolean>{

    String tag = "SendMessageJob";

    Context context;
    Message message;

    public SendMessageJob(Context context, Message message){
        Log.v(tag, "send message: " + message.toString());
        this.context = context;
        this.message = message;
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

        String path = com.soco.SoCoClient.control.http.Config.SERVER_PATH_SEND_MESSAGE;
        String url = "http://" + ip + ":" + port + path + "?"
                + HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;

        Log.d(tag, "get url [SendMessageJob]: " + url);
        return url;
    }

    JSONObject getJsonData(){
        JSONObject data = new JSONObject();
        try{
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_FROM_TYPE, message.getFromType());
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_FROM_ID, message.getFromId());
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_TO_TYPE, message.getToType());
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_TO_ID, message.getToId());
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_SEND_DATE_TIME, message.getCreateTimestamp());
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_FROM_DEVICE, message.getFromDevice());
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_CONTENT_TYPE, message.getContentType());
            data.put(com.soco.SoCoClient.control.http.Config.JSON_KEY_CONTENT, message.getContent());
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
                Log.v(tag, "server response success, update message status");
                message.setStatus(com.soco.SoCoClient.control.database.Config.MESSAGE_STATUS_SENT);
                message.setSendTimestamp(TimeUtil.now());
                message.save();
                Log.d(tag, "udpated message details with server response: " + message.toString());
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
