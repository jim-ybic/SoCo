package com.soco.SoCoClient.control.http.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.http.HttpUtil;
import com.soco.SoCoClient.model.Profile;

import org.json.JSONArray;
import org.json.JSONObject;

//todo
public class GetActivityEventTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "GetActivityEventTask";

    String url;
    Context context;
    SocoApp socoApp;
    Profile profile;
    DBManagerSoco dbManagerSoco;

    public GetActivityEventTaskAsync(
            String url,
            Context context
    ){
        Log.i(tag, "Create new HttpTask: " + url);
        this.url = url;
        this.context = context;

        socoApp = (SocoApp)context;
        profile = socoApp.profile;
        dbManagerSoco = socoApp.dbManagerSoco;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() ){
            Log.e(tag, "Cannot get url");
            return false;
        }

        execute(url, context);
        return true;
    }

    public void execute(String url, Context context){
        Object response = request(url);
        if (response != null)
            parse(response, context);
    }

    public Object request(String url) {
        JSONObject data = new JSONObject();
        //no data needed for retrieve message
        return HttpUtil.executeHttpPost(url, data);
    }

    /*
    Sample response
    */
    public boolean parse(Object response, Context context) {
        try {
            String str = response.toString();
            Log.i(tag, "Server response string: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfig.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Server response status success");

                //todo
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
