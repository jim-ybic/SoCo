package com.soco.SoCoClient.control.http;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpTask extends AsyncTask<Void, Void, Boolean> {
    static String tag = "HttpTask";
    public static String HTTP_TYPE_TEST = "test";
    public static String HTTP_TYPE_LOGIN = "login";
    public static String HTTP_TYPE_REGISTER = "register";

    String url;
    String type;

    public HttpTask(String url, String type){
        this.url = url;
        this.type = type;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        if(url == null || url.isEmpty()){
            Log.e(tag, "Cannot get url");
            return false;
        }

        if(type.equals(HTTP_TYPE_TEST)) {
            testHttpGet();
        }

        return true;
    }

    private void testHttpGet() {
        HttpGet httpGet = new HttpGet(url);
        Log.d(tag, "HttpGet: " + httpGet);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            Log.d(tag, "HttpResponse: " + httpResponse.toString());
            int reponseCode = httpResponse.getStatusLine().getStatusCode();
            if (reponseCode == HttpStatus.SC_OK) {
                InputStream inputStream = httpResponse.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null)
                    total.append(line);
                Log.d(tag, "Total response: " + total.toString());
                inputStream.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
