package com.soco.SoCoClient.control.http.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.http.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class AckRetrieveMessageTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "AckReceiveMessageTask";

    String url;
    String signature;

    public AckRetrieveMessageTaskAsync(
            String url,
            String signature
    ){
        Log.i(tag, "Create new HttpTask: " + url);
        this.url = url;
        this.signature = signature;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() ){
            Log.e(tag, "Cannot get url");
            return false;
        }

        execute(url, signature);
        return true;
    }

    public void execute(String url, String signature){
        Object response = request(url, signature);
        if (response != null)
            parse(response);
    }

    public Object request(String url, String signature) {
        JSONObject ackData = new JSONObject();
        try {
            JSONObject signatureData = new JSONObject();
            signatureData.put(HttpConfig.JSON_KEY_PROJECT_SIGNATURE, signature);

            JSONArray array = new JSONArray();
            array.put(signatureData);

            ackData.put(HttpConfig.JSON_KEY_ACK, array);
        }
        catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }

        return HttpUtil.executeHttpPost(url, ackData);
    }

    public static boolean parse(Object response) {
        try {
            String str = response.toString();
            Log.i(tag, "Server response string: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfig.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Server response status success");
            }
            else {
                Log.e(tag, "Parse result not in success status");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
