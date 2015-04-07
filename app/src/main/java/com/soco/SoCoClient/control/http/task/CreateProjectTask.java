package com.soco.SoCoClient.control.http.task;

import android.content.Context;
import android.util.Log;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.http.HttpUtil;

import org.json.JSONObject;

public class CreateProjectTask {

    public static String tag = "CreateProjectTask";

    public static void execute(String url, String pname, String pid, Context context,
                               String projectSignature, String projectTag, String projectType){
        Object response = request(url, pname, projectSignature, projectTag, projectType);
        DBManagerSoco dbManagerSoco = ((SocoApp)context).dbManagerSoco;
        if (response != null)
            parse(response, pid, dbManagerSoco);
    }

    public static Object request(String url, String pname,
                                 String projectSignature, String projectTag, String projectType) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfig.JSON_KEY_PROJECT_NAME, pname);
            data.put(HttpConfig.JSON_KEY_PROJECT_SIGNATURE, projectSignature);
            data.put(HttpConfig.JSON_KEY_PROJECT_TYPE, projectType);
            data.put(HttpConfig.JSON_KEY_PROJECT_TAG, projectTag);
            Log.i(tag, "Create project Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return HttpUtil.executeHttpPost(url, data);
    }

    public static boolean parse(Object response, String pid, DBManagerSoco dbManagerSoco) {
        try {
            String str = response.toString();
            Log.i(tag, "Parse string: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfig.JSON_KEY_RESPONSE_STATUS);
            if(isSuccess.equals(HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Create project server parse: " +
                        HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS);
                String pid_onserver = json.getString(HttpConfig.JSON_KEY_PROJECT_ID_ONSERVER);
                Log.i(tag, "Project pid " + pid + ", set pid_onserver " + pid_onserver);
                dbManagerSoco.updateProjectIdOnserver(Integer.parseInt(pid), pid_onserver);
                return true;
            }
            else {
                Log.e(tag, "Cannot receive create project status parse from server");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
