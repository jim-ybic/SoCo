package com.soco.SoCoClient.common.http.task._ref;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.http._ref.HttpUtilV1;

import org.json.JSONObject;

public class AddFileToActivityTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "AddFileToActivityTask";

    String url;
    int aid_onserver;
    String displayName, uri, remotePath, localPath, user;

//    SocoApp socoApp;
//    Profile profile;
//    Context context;

    public AddFileToActivityTaskAsync(
            String url,
            int aid_onserver,
            String displayName,
            String uri,
            String remotePath,
            String localPath,
            String user
//            Context context
    ){
        Log.i(tag, "Create new HttpTask: "
                + url + ", " + aid_onserver + ", " + uri + ", "
                + remotePath + ", " + localPath + ", " + user);
        this.url = url;
        this.aid_onserver = aid_onserver;
        this.displayName = displayName;
        this.uri = uri;
        this.remotePath = remotePath;
        this.localPath = localPath;
        this.user = user;
//        this.context = context;

//        socoApp = (SocoApp)context;
//        profile = socoApp.profile;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() ){
            Log.e(tag, "Cannot get url");
            return false;
        }

        execute(url, aid_onserver, displayName, uri, remotePath, localPath, user);
        return true;
    }

    public void execute(String url,
                        int aid_onserver,
                        String displayName,
                        String uri,
                        String remotePath,
                        String localPath,
                        String user){
        Object response = request(url, aid_onserver, displayName, uri, remotePath, localPath, user);
        if (response != null)
            parse(response);
    }

    public Object request(String url,
                          int aid_onserver,
                          String displayName,
                          String uri,
                          String remotePath,
                          String localPath,
                          String user) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfigV1.JSON_KEY_PROJECT, aid_onserver);
            data.put(HttpConfigV1.JSON_KEY_FILE_NAME, displayName);
            data.put(HttpConfigV1.JSON_KEY_URI, uri);
            data.put(HttpConfigV1.JSON_KEY_REMOTE_PATH, remotePath);
            data.put(HttpConfigV1.JSON_KEY_LOCAL_PATH, localPath);
            data.put(HttpConfigV1.JSON_KEY_USER, user);
            Log.i(tag, "Post Json: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return HttpUtilV1.executeHttpPost(url, data);
    }

    public boolean parse(Object response) {
        try {
            String str = response.toString();
            Log.i(tag, "Server response string: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfigV1.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Server parse: " + HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS);
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
