package com.soco.SoCoClient.control.http;

import android.util.Log;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class HttpUtil {

    public static String tag = "HttpUtil";

    public static Object executeHttpPost(String url, JSONObject data) {
        Object response = null;
        Log.d(tag, "executeHttpPost, url: " + url + ", data: " + data);

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(url);
            StringEntity se = new StringEntity(data.toString());
            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            ResponseHandler responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httpost, responseHandler);
            Log.d(tag, "Post success, raw parse: " + response);
        } catch (Exception e) {
            Log.e(tag, "Post fail: " + e.toString());
        }

        return response;
    }
}
