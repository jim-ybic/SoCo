package com.soco.SoCoClient._v2.businesslogic.http.task;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient._v2.businesslogic.config.DataConfig2;
import com.soco.SoCoClient._v2.businesslogic.config.GeneralConfig2;
import com.soco.SoCoClient._v2.businesslogic.config.HttpConfig2;
import com.soco.SoCoClient._v2.businesslogic.http.HttpUtil2;
import com.soco.SoCoClient._v2.datamodel.Contact;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.http.task.QueryUserTaskAsync;

import org.json.JSONObject;

public class CreateContactOnServerJob extends AsyncTask<Void, Void, Boolean>{

    String tag = "CreateContactOnServerJob";

    Context context;
    Contact contact;

    public CreateContactOnServerJob(Context context, Contact contact){
        Log.v(tag, "create contact on server: " + contact.toString());
        this.context = context;
        this.contact = contact;
    }

    protected Boolean doInBackground(Void... params) {
        String url = getUrl();
        JSONObject data = getJsonData();
        Object response = HttpUtil2.executeHttpPost(url, data);
        if(response != null)
            parse(response);
        return null;
    }

    String getUrl(){
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig2.PROFILE_FILENAME, 0);
        String ip = settings.getString(HttpConfig2.PROFILE_SERVER_IP, "");
        String port = settings.getString(HttpConfig2.PROFILE_SERVER_PORT, "");
        String token = settings.getString(HttpConfig2.PROFILE_LOGIN_ACCESS_TOKEN, "");
        if(ip.isEmpty() || port.isEmpty() || token.isEmpty()) {
            Log.e(tag, "cannot load ip/port/token from shared preference");
            return "";
        }

        String path = HttpConfig2.SERVER_PATH_CREATE_CONTACT;
        String url = "http://" + ip + ":" + port + path + "?"
                + HttpConfig.HTTP_TOKEN_TYPE + "=" + token;

        Log.d(tag, "get url: " + url);
        return url;
    }

    JSONObject getJsonData(){
        JSONObject data = new JSONObject();
        try{
            data.put(HttpConfig2.JSON_KEY_EMAIL, contact.getContactEmail());
        }catch(Exception e){
            Log.e(tag, "cannot create json data: " + e);
            e.printStackTrace();
        }

        Log.d(tag, "get json data: " + data);
        return data;
    }

    boolean parse(Object response){
        Log.v(tag, "parse server response: " + response);
        try {
            JSONObject data = new JSONObject(response.toString());
            String isSuccess = data.getString(HttpConfig2.JSON_KEY_STATUS);
            if(isSuccess.equals(HttpConfig2.JSON_VALUE_SUCCESS)){
                Log.d(tag, "server response success, query user details");
                QueryContactDetailsJob job = new QueryContactDetailsJob(context, contact);
                job.execute();
            }else {
                Log.e(tag, "cannot receive success response from server");
                contact.setContactServerStatus(DataConfig2.CONTACT_SERVER_STATUS_VALID);
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