package com.soco.SoCoClient.common.http.task._ref;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.database._ref.DBManagerSoco;
import com.soco.SoCoClient._ref.HttpUtilV1;

import org.json.JSONObject;

@Deprecated
public class CreateActivityTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "ArchiveProjectTask";

    String url, pname;
    String pid;
    Context context;
    String projectSignature, projectTag, projectType;

    public CreateActivityTaskAsync(
            String url,
            String pname,
            String pid,
            Context context,
            String projectSignature,
            String projectTag,
            String projectType
    ){
        Log.i(tag, "Create new HttpTask: " + url);
        this.url = url;
        this.pname = pname;
        this.pid = pid;
        this.context = context;
        this.projectSignature = projectSignature;
        this.projectTag = projectTag;
        this.projectType = projectType;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty()){
            Log.e(tag, "Cannot get url/type");
            return false;
        }

        execute(url, pname, pid, context, projectSignature, projectTag, projectType);
        return true;
    }

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
            data.put(HttpConfigV1.JSON_KEY_NAME, pname);
            data.put(HttpConfigV1.JSON_KEY_PROJECT_SIGNATURE, projectSignature);
            data.put(HttpConfigV1.JSON_KEY_PROJECT_TYPE, projectType);
            data.put(HttpConfigV1.JSON_KEY_PROJECT_TAG, projectTag);
            Log.i(tag, "Create project Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return HttpUtilV1.executeHttpPost(url, data);
    }

    public static boolean parse(Object response, String pid, DBManagerSoco dbManagerSoco) {
        try {
            String str = response.toString();
            Log.i(tag, "Parse string: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfigV1.JSON_KEY_RESPONSE_STATUS);
            if(isSuccess.equals(HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Create project server parse: " +
                        HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS);
                String pid_onserver = json.getString(HttpConfigV1.JSON_KEY_PROJECT_ID_ONSERVER);
                Log.i(tag, "Project pid " + pid + ", set pid_onserver " + pid_onserver);
                dbManagerSoco.updateActivityIdOnserver(Integer.parseInt(pid), pid_onserver);
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
