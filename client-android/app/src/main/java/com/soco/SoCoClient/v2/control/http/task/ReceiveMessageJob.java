package com.soco.SoCoClient.v2.control.http.task;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.v2.control.config.GeneralConfig;
import com.soco.SoCoClient.v2.control.config.HttpConfig;
import com.soco.SoCoClient.v2.control.http.HttpUtil;
import com.soco.SoCoClient.v2.model.Message;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReceiveMessageJob extends AsyncTask<Void, Void, Boolean>{

    String tag = "ReceiveMessageJob";

    Context context;

    public ReceiveMessageJob(Context context){
        Log.v(tag, "receive message");
        this.context = context;
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

        String path = HttpConfig.SERVER_PATH_RECEIVE_MESSAGE;
        String url = "http://" + ip + ":" + port + path + "?"
                + com.soco.SoCoClient.obsolete.v1.control.config.HttpConfig.HTTP_TOKEN_TYPE + "=" + token;

        Log.d(tag, "get url [ReceiveMessageJob]: " + url);
        return url;
    }

    JSONObject getJsonData(){
        return new JSONObject();
    }

    boolean parse(Object response){
        Log.d(tag, "parse server response: " + response);
        try {
            JSONObject data = new JSONObject(response.toString());
            String isSuccess = data.getString(HttpConfig.JSON_KEY_STATUS);
            if(isSuccess.equals(HttpConfig.JSON_VALUE_SUCCESS)){
                Log.v(tag, "server response success, receive messages");
                JSONArray messages = new JSONArray(data.getString(HttpConfig.JSON_KEY_MESSAGE));
                for(int i=0; i<messages.length(); i++){
                    JSONObject obj = messages.getJSONObject(i);
                    Log.v(tag, "create message from json: " + obj.toString());

                    Message message = new Message(context);
                    message.setFromType(obj.getInt(HttpConfig.JSON_KEY_FROM_TYPE));
                    message.setFromId(obj.getString(HttpConfig.JSON_KEY_FROM_ID));
                    message.setToType(obj.getInt(HttpConfig.JSON_KEY_TO_TYPE));
                    message.setToId(obj.getString(HttpConfig.JSON_KEY_TO_ID));
                    message.setSendTimestamp(obj.getString(HttpConfig.JSON_KEY_SEND_DATE_TIME));
                    message.setContentType(obj.getInt(HttpConfig.JSON_KEY_CONTENT_TYPE));
                    message.setContent(obj.getString(HttpConfig.JSON_KEY_CONTENT));
                    message.setSignature(obj.getString(HttpConfig.JSON_KEY_SIGNATURE));
                    message.save();
                    Log.d(tag, "received message saved to database: " + message.toString());

                    AckReceivedMessageJob job = new AckReceivedMessageJob(context, message);
                    job.execute();
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