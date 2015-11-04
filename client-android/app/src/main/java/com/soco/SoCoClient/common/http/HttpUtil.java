package com.soco.SoCoClient.common.http;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;


public class HttpUtil {

    public static String tag = "HttpUtil";

    public static Object executeHttpPost(String url, JSONObject data) {
        Object response = null;
        Log.v(tag, "executeHttpPost, url: " + url);
        Log.v(tag, "executeHttpPost, data: " + data);

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(url);
            StringEntity se = new StringEntity(data.toString(), HTTP.UTF_8);
//            Log.d(tag, "string entity: " + se.toString());
            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
//            httpost.setHeader("Content-type", "application/json;charset=UTF-8");
//            httpost.setHeader("charset", HTTP.UTF_8);
            ResponseHandler responseHandler = new BasicResponseHandler();

            response = httpclient.execute(httpost, responseHandler);
            Log.v(tag, "Post success, response: " + response);
        } catch (Exception e) {
            Log.e(tag, "Post fail: " + e.toString());
            e.printStackTrace();
            return null;
        }

        return response;
    }

    public static Object executeHttpGet(String url) {
        Object response = null;
        Log.v(tag, "executeHttpGet, url: " + url);

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("charset", HTTP.UTF_8);
            ResponseHandler responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httpGet, responseHandler);
            Log.v(tag, "Get success, response: " + response);

//            HttpResponse httpResponse = httpGet.execute(httpGet, responseHandler);
//            HttpEntity entity = ((HttpResponse) response).getEntity();
//            try {
//                String str = EntityUtils.toString(entity, HTTP.UTF_8);
//                Log.w(tag, "decoded response: " + str);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        } catch (Exception e) {
            Log.e(tag, "Get fail: " + e.toString());
            e.printStackTrace();
            return null;
        }

        return response;
    }

    public static HttpResponse executeHttpGet2(String url) {
        Object response = null;
        Log.v(tag, "executeHttpGet, url: " + url);

        HttpResponse httpResponse = null;
        try {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "utf-8");
            params.setBooleanParameter("http.protocol.expect-continue", false);

            DefaultHttpClient httpclient = new DefaultHttpClient(params);
            HttpGet httpGet = new HttpGet(url);
//            httpGet.setHeader("charset", HTTP.UTF_8);
            ResponseHandler responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httpGet, responseHandler);
            Log.v(tag, "Get success, response: " + response);

            httpResponse = httpclient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            try {
                String str = EntityUtils.toString(entity, HTTP.UTF_8);
                Log.w(tag, "decoded http response: " + str);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.e(tag, "Get fail: " + e.toString());
            e.printStackTrace();
            return null;
        }

        return httpResponse;
    }

}
