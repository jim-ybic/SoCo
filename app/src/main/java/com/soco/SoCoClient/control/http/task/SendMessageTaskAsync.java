package com.soco.SoCoClient.control.http.task;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.http.HttpUtil;

import org.json.JSONObject;

public class SendMessageTaskAsync extends AsyncTask<Void, Void, Boolean> {

    private static final String TEST_DEVICE_NAME = "TEST DEVICE NAME";
    static String tag = "SendMessageTask";

    String url;
    int from_type, to_type;
    String from_id, to_id;
    String timestamp, device;
    String content;
    int content_type;

    public SendMessageTaskAsync(
            String url,
            int from_type, String from_id,
            int to_type, String to_id,
            String timestamp, String device,
            int content_type, String content
    ){
        Log.i(tag, "Create new HttpTask: "
                + url + ", " + from_type + ", " + from_id + ", " + to_type + ", " + to_id
                + ", " + timestamp + ", " + device + ", " + content_type + ", " + content);
        this.url = url;
        this.from_type = from_type;
        this.from_id = from_id;
        this.to_type = to_type;
        this.to_id = to_id;
        this.timestamp = timestamp;
        this.device = device;
        this.content_type = content_type;
        this.content = content;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() ){
            Log.e(tag, "Cannot get url");
            return false;
        }

        execute(url, from_type, from_id, to_type, to_id,
                timestamp, device, content_type, content);
        return true;
    }

    public void execute(String url,
                        int from_type, String from_id,
                        int to_type, String to_id,
                        String timestamp, String device,
                        int content_type, String content){
        Object response = request(url, from_type, from_id, to_type,to_id,
                timestamp, device, content_type, content);
        if (response != null)
            parse(response);
    }

    public Object request(String url,
                                 int from_type, String from_id,
                                 int to_type, String to_id,
                                 String timestamp, String device,
                                 int content_type, String content) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfig.JSON_KEY_FROM_TYPE, from_type);
            data.put(HttpConfig.JSON_KEY_FROM_ID, from_id);
            data.put(HttpConfig.JSON_KEY_TO_TYPE, to_type);
            data.put(HttpConfig.JSON_KEY_TO_ID, to_id);
            data.put(HttpConfig.JSON_KEY_SEND_DATE_TIME, timestamp);
            data.put(HttpConfig.JSON_KEY_FROM_DEVICE, device);
            data.put(HttpConfig.JSON_KEY_CONTENT_TYPE, content_type);
            data.put(HttpConfig.JSON_KEY_CONTENT, content);

            Log.i(tag, "Post Json: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public static boolean parse(Object response) {
        try {
            String str = response.toString();
            Log.i(tag, "Server response string: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfig.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Server parse: " + HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS);
                return true;
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
    }

}
