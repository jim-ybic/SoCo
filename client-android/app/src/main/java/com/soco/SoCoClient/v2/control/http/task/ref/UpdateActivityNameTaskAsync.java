package com.soco.SoCoClient.v2.control.http.task.ref;

import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.v2.control.config.ref.HttpConfigV1;
import com.soco.SoCoClient.v2.control.http.ref.HttpUtilV1;

import org.json.JSONObject;

public class UpdateActivityNameTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "UpdateProjectNameTaskAsync";

    String url;
    String pname, pid_onserver;

    public UpdateActivityNameTaskAsync(
            String url,
            String pname,
            String pid_onserver
    ){
        Log.i(tag, "Create new HttpTask: "
                + url + ", " + pid_onserver);
        this.url = url;
        this.pname = pname;
        this.pid_onserver = pid_onserver;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() || pid_onserver == null){
            Log.e(tag, "Cannot get url/type");
            return false;
        }

        execute(url, pname, pid_onserver);
        return true;
    }

    public static void execute(String url, String pname, String pid_onserver){
        Log.i(tag, "Execute: " + url + ", " + pname + ", " + pid_onserver);
        Object response = request(url, pname, pid_onserver);
        if (response != null)
            parse(response);
    }

    public static Object request(String url, String pname, String pid_onserver) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfigV1.JSON_KEY_NAME, pname);
            data.put(HttpConfigV1.JSON_KEY_PROJECT_ID, Integer.parseInt(pid_onserver));
            Log.i(tag, "Create project Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return HttpUtilV1.executeHttpPost(url, data);
    }

    public static boolean parse(Object response) {
        try {
            String str = response.toString();
            Log.i(tag, "Update project name parse string: " + str);

            //TODO: server interface to be fixed

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfigV1.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Server parse: " + HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS);
                return true;
            }
            else {
                Log.e(tag, "Cannot receive parse from server");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

}
