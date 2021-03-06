package com.soco.SoCoClient.common.http.task._ref;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient._ref.HttpUtilV1;

import org.json.JSONArray;
import org.json.JSONObject;

@Deprecated
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
            signatureData.put(HttpConfigV1.JSON_KEY_PROJECT_SIGNATURE, signature);

            JSONArray array = new JSONArray();
            array.put(signatureData);

            ackData.put(HttpConfigV1.JSON_KEY_ACK, array);
        }
        catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }

        return HttpUtilV1.executeHttpPost(url, ackData);
    }

    public static boolean parse(Object response) {
        try {
            String str = response.toString();
            Log.i(tag, "Server response string: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfigV1.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
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
