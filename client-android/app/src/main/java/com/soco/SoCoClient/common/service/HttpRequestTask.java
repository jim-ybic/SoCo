package com.soco.SoCoClient.common.service;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.events.model.Event;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JohnWU on 25/2/2016.
 */
public class HttpRequestTask extends AsyncTask<String, Integer, String> {

    String tag = "HttpRequestTask";
    TaskCallBack callBack;


    public HttpRequestTask(TaskCallBack cb){
        callBack = cb;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";
        String method = params[0];
        String URL = params[1];
        if(method.equals("GET")) {
            response = (String) HttpUtil.executeHttpGet(URL);
        }else if(method.equals("POST")){
            try {
                JSONObject json = new JSONObject();
                int count =  params.length;
                for(int i = 2; i < count; i++) {
                    json.put(params[i++], params[i]);
                }
                response = (String) HttpUtil.executeHttpPost(URL, json);
            }catch(JSONException e){
                Log.e(tag, e.getMessage());
            }
        }
        Log.d(tag,"Get response : " + response);
        return response;
    }

    protected void onPostExecute(String result) {
        if(callBack != null)
            callBack.doneTask(result);

    }
}
