package com.soco.SoCoClient.common.http.task;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.profile.Config;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.secondary.chat.model.Message;

import org.json.JSONArray;
import org.json.JSONObject;

@Deprecated
public class AckReceivedMessageJob extends AsyncTask<Void, Void, Boolean>{

    String tag = "AckReceivedMessageJob";

    Context context;
    Message message;

    public AckReceivedMessageJob(Context context, Message message){
        Log.v(tag, "ack received message: " + message.toString());
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
        String ip = settings.getString(com.soco.SoCoClient.common.http.Config.PROFILE_SERVER_IP, "");
        String port = settings.getString(com.soco.SoCoClient.common.http.Config.PROFILE_SERVER_PORT, "");
        String token = settings.getString(com.soco.SoCoClient.common.http.Config.PROFILE_LOGIN_ACCESS_TOKEN, "");
        if(ip.isEmpty() || port.isEmpty() || token.isEmpty()) {
            Log.e(tag, "cannot load ip/port/token from shared preference");
            return "";
        }

        String path = com.soco.SoCoClient.common.http.Config.SERVER_PATH_ACK_RECEIVE_MESSAGE;
        String url = "http://" + ip + ":" + port + path + "?"
                + HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;

        Log.d(tag, "get url [AckReceivedMessageJob]: " + url);
        return url;
    }

    JSONObject getJsonData(){
        JSONObject data = new JSONObject();
        try{
            JSONObject obj = new JSONObject();
            obj.put(com.soco.SoCoClient.common.http.Config.JSON_KEY_SIGNATURE, message.getSignature());
            JSONArray array = new JSONArray();
            array.put(obj);
            data.put(com.soco.SoCoClient.common.http.Config.JSON_KEY_ACK, array);
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
                message.setStatus(com.soco.SoCoClient.common.database.Config.MESSAGE_STATUS_RECEIVED);
                message.setReceiveTimestamp(TimeUtil.now());
                message.save();
                Log.d(tag, "updated message details with server response: " + message.toString());
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
