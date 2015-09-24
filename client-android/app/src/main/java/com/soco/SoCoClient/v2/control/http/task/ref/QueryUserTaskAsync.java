package com.soco.SoCoClient.v2.control.http.task.ref;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.v2.control.config.ref.HttpConfigV1;
import com.soco.SoCoClient.v2.control.http.ref.HttpUtilV1;
import com.soco.SoCoClient.v2.control.config.SocoApp;
import com.soco.SoCoClient.v2.control.database.ref.DBManagerSoco;
import com.soco.SoCoClient.v2.model.Profile;

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
            data.put(HttpConfigV1.JSON_KEY_EMAIL, email);
            Log.i(tag, "Post Json: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return HttpUtilV1.executeHttpPost(url, data);
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
            String isSuccess = json.getString(HttpConfigV1.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Server parse: " + HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS);

                String userStr = json.getString(HttpConfigV1.JSON_KEY_USER);
                Log.i(tag, "user string: " + userStr);

                if(userStr != null && !userStr.isEmpty()){
                    JSONObject user = new JSONObject(userStr);
                    int contact_id_onserver = user.getInt(HttpConfigV1.JSON_KEY_ID);
                    String name = user.getString(HttpConfigV1.JSON_KEY_NAME);

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
