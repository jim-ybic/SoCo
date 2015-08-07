package com.soco.SoCoClient.obsolete.v1.control.http.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.obsolete.v1.control.SocoApp;
import com.soco.SoCoClient.obsolete.v1.control.config.HttpConfig;
import com.soco.SoCoClient.obsolete.v1.control.db.DBManagerSoco;
import com.soco.SoCoClient.obsolete.v1.control.http.HttpUtil;
import com.soco.SoCoClient.obsolete.v1.model.Profile;

import org.json.JSONObject;

public class QueryUserTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "QueryUserTask";

    String url;
    String email;
    SocoApp socoApp;
    Profile profile;
    DBManagerSoco dbManagerSoco;

    public QueryUserTaskAsync(
            String url,
            Context context,
            String email
    ){
        Log.i(tag, "Create new HttpTask: "
                + url + ", " + email );
        this.url = url;
        this.email = email;

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

        execute(url, email);
        return true;
    }

    public void execute(String url,String email){
        Object response = request(url, email);
        if (response != null)
            parse(response);
    }

    public Object request(String url,String email) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfig.JSON_KEY_EMAIL, email);
            Log.i(tag, "Post Json: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    /*
    Sample response:
        {“status”:"success","user":"{'id':1,'name':john,’email’:”test@gmail.com}"}
    Or
        {status:’failure’}
     */
    public boolean parse(Object response) {
        try {
            String str = response.toString();
            Log.i(tag, "Server response string: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfig.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Server parse: " + HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS);

                String userStr = json.getString(HttpConfig.JSON_KEY_USER);
                Log.i(tag, "user string: " + userStr);

                if(userStr != null && !userStr.isEmpty()){
                    JSONObject user = new JSONObject(userStr);
                    int contact_id_onserver = user.getInt(HttpConfig.JSON_KEY_ID);
                    String name = user.getString(HttpConfig.JSON_KEY_NAME);

                    Log.i(tag, "udpate database contact " + email
                            + " with contact id onserver " + contact_id_onserver);
                    dbManagerSoco.updateContactNameIdOnserver(email, name, contact_id_onserver);
                }

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
