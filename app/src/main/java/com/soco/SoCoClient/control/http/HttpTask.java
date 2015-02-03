package com.soco.SoCoClient.control.http;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpTask extends AsyncTask<Void, Void, Boolean> {
    static String tag = "HttpTask";
    public static String HTTP_TYPE_LOGIN = "login";
    public static String HTTP_TYPE_REGISTER = "loginSocoServer";
    public static String JSCON_KEY_USERNAME = "username";
    public static String JSCON_KEY_PASSWORD = "password";
    public static String JSCON_KEY_ACCESS_TOKEN = "access_token";

    String url;
    String type;
    String loginEmail, loginPassword;

    public HttpTask(String url, String type, String loginEmail, String loginPassword){
        this.url = url;
        this.type = type;
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() || type == null || type.isEmpty()){
            Log.e(tag, "Cannot get url/type");
            return false;
        }
        Log.i(tag, "Start http task, url is " + url + ", type is " + type);

        if(type.equals(HTTP_TYPE_LOGIN)) {
            Object response = loginSocoServer();
            try {
                JSONObject json = new JSONObject(response.toString());
                String access_token = json.getString(JSCON_KEY_ACCESS_TOKEN);
                Log.i(tag, "Get access token: " + access_token);
                //TODO: save token into shared preference
            } catch (Exception e) {
                Log.e(tag, "Cannot convert response to Json object: " + e.toString());
                e.printStackTrace();
            }
        }
        return true;
    }

    private Object loginSocoServer() {
        JSONObject data = new JSONObject();
        Object response = null;
        try {
            data.put(JSCON_KEY_USERNAME, loginEmail);
            data.put(JSCON_KEY_PASSWORD, loginPassword);
            Log.i(tag, "Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create Json post data");
            e.printStackTrace();
        }

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(url);
            StringEntity se = new StringEntity(data.toString());
            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            ResponseHandler responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httpost, responseHandler);
            Log.i(tag, "Login success.");
            Log.i(tag, "Json response: " + response);
        } catch (Exception e) {
            Log.e(tag, "Login fail." + e.toString());
            e.printStackTrace();
        }

        return response;
    }


}
