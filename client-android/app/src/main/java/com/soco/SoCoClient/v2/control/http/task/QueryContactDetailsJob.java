package com.soco.SoCoClient.v2.control.http.task;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.v2.control.config.ref.HttpConfigV1;
import com.soco.SoCoClient.v2.control.config.DataConfig;
import com.soco.SoCoClient.v2.control.config.GeneralConfig;
import com.soco.SoCoClient.v2.control.config.HttpConfig;
import com.soco.SoCoClient.v2.control.http.HttpUtil;
import com.soco.SoCoClient.v2.model.Contact;

import org.json.JSONObject;

public class QueryContactDetailsJob extends AsyncTask<Void, Void, Boolean>{

    String tag = "QueryContactDetailsJob";

    Context context;
    Contact contact;

    public QueryContactDetailsJob(Context context, Contact contact){
        Log.v(tag, "query contact on server: " + contact.toString());
        this.context = context;
        this.contact = contact;
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

        String path = HttpConfig.SERVER_PATH_QUERY_CONTACT_DETAIL;
        String url = "http://" + ip + ":" + port + path + "?"
                + HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;

        Log.d(tag, "get url [QueryContactDetailsJob]: " + url);
        return url;
    }

    JSONObject getJsonData(){
        JSONObject data = new JSONObject();
        try{
            data.put(HttpConfig.JSON_KEY_EMAIL, contact.getContactEmail());
        }catch(Exception e){
            Log.e(tag, "cannot create json data: " + e);
            e.printStackTrace();
        }

        Log.d(tag, "get json data: " + data);
        return data;
    }

    boolean parse(Object response){
        Log.d(tag, "parse server response: " + response);
        try {
            JSONObject data = new JSONObject(response.toString());
            String isSuccess = data.getString(HttpConfig.JSON_KEY_STATUS);
            if(isSuccess.equals(HttpConfig.JSON_VALUE_SUCCESS)){
                Log.v(tag, "server response success, parse user details");
                String user = data.getString(HttpConfig.JSON_KEY_USER);
                if(user != null && !user.isEmpty()){
                    JSONObject userData = new JSONObject(user);
                    int contactIdServer = userData.getInt(HttpConfig.JSON_KEY_ID);
                    String contactUsername = userData.getString(HttpConfig.JSON_KEY_NAME);
                    contact.setContactIdServer(contactIdServer);
                    contact.setContactUsername(contactUsername);
                    contact.setContactServerStatus(DataConfig.CONTACT_SERVER_STATUS_VALID);
                    contact.save();
                    Log.d(tag, "updated contact details with server response: " + contact.toString());
                }
            }else {
                Log.e(tag, "cannot receive success response from server");
                contact.setContactServerStatus(DataConfig.CONTACT_SERVER_STATUS_INVALID);
                contact.save();
                Log.d(tag, "updated contact details with server response: " + contact.toString());
                return false;
            }
        }catch (Exception e){
            Log.e(tag, "cannot create json data: " + e);
            e.printStackTrace();
        }
        return true;
    }
}
