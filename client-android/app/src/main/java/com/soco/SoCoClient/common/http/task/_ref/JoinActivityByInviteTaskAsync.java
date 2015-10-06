package com.soco.SoCoClient.common.http.task._ref;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.http._ref.HttpUtilV1;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.database._ref.DBManagerSoco;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class JoinActivityByInviteTaskAsync extends AsyncTask<Void, Void, Boolean> {

    static String tag = "JoinProjectByInviteTask";

    String url;
    String pid, pid_onserver;
    Context context;
    String inviterEmail;

    public JoinActivityByInviteTaskAsync(
            String url,
            String pid,
            String pid_onserver,
            Context context,
            String inviterEmail
    ){
        Log.i(tag, "Create new HttpTask: "
                + url + ", " + pid_onserver);
        this.url = url;
        this.pid = pid;
        this.pid_onserver = pid_onserver;
        this.context = context;
        this.inviterEmail = inviterEmail;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if(url == null || url.isEmpty() || pid_onserver == null){
            Log.e(tag, "Cannot get url/type");
            return false;
        }

        execute(url, pid, pid_onserver, context);
        return true;
    }

    public void execute(String url, String pid, String pid_onserver, Context context){
        Log.i(tag, "Execute: " + url  + ", " + pid_onserver);
        Object response = request(url, pid_onserver);
        if (response != null)
            parse(response, pid, context);
    }

    public static Object request(String url, String pid_onserver) {
        JSONObject data = new JSONObject();
        try {
            data.put(HttpConfigV1.JSON_KEY_PROJECT_ID, Long.parseLong(pid_onserver));
            Log.i(tag, "Create project Json post: " + data);
        } catch (Exception e) {
            Log.e(tag, "Cannot create create project Json post data");
            e.printStackTrace();
        }

        return HttpUtilV1.executeHttpPost(url, data);
    }

    public boolean parse(Object response, String pid, Context context) {
        try {
            String str = response.toString();
            Log.i(tag, "Update project name parse string: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfigV1.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Server parse: " + HttpConfigV1.JSON_VALUE_RESPONSE_STATUS_SUCCESS);

                //retrieve project details
                if(json.has(HttpConfigV1.JSON_KEY_PROJECT)){
                    String projectStr = json.getString(HttpConfigV1.JSON_KEY_PROJECT);
                    Log.i(tag, "Project str:" + projectStr);
                    JSONObject p = new JSONObject(projectStr);
                    String name = p.getString(HttpConfigV1.JSON_KEY_NAME);
                    String tag = p.getString(HttpConfigV1.JSON_KEY_PROJECT_TAG);
                    String signature = p.getString(HttpConfigV1.JSON_KEY_PROJECT_SIGNATURE);
                    String type = p.getString(HttpConfigV1.JSON_KEY_PROJECT_TYPE);
                    //update project details in database
                    DBManagerSoco dbManagerSoco = ((SocoApp)context).dbManagerSoco;
                    dbManagerSoco.updateActivityName(Integer.valueOf(pid), name);
                    dbManagerSoco.updateAcivityTag(Integer.valueOf(pid), tag);
                    //todo: add project signature and type
                }
                else
                    Log.i(tag, "No project str is found");

                //add all attributes to database
                if(json.has(HttpConfigV1.JSON_KEY_ATTRIBUTES)) {
                    String attrStr = json.getString(HttpConfigV1.JSON_KEY_ATTRIBUTES);
                    Log.i(tag, "Attribute str: " + attrStr);
                    HashMap<String, String> attrMap = new HashMap<>();
                    JSONArray attributes = new JSONArray(attrStr);
                    for (int i = 0; i < attributes.length(); i++) {
                        JSONObject attr = attributes.getJSONObject(i);
                        String attrName = attr.getString(HttpConfigV1.JSON_KEY_ATTRIBUTE_NAME);
//                        String attrType = attr.getString("type");
                        String attrValue = attr.getString(HttpConfigV1.JSON_KEY_ATTRIBUTE_VALUE);
                        //add attribute to project
                        attrMap.put(attrName, attrValue);
                    }
                    DBManagerSoco dbManagerSoco = ((SocoApp) context).dbManagerSoco;
                    dbManagerSoco.updateDbActivityAttributes(Integer.valueOf(pid), attrMap);
                    dbManagerSoco.setInvitationStatusCompleted(Integer.valueOf(pid));
                }
                else
                    Log.i(tag, "No attribute string is found");

                //add inviterEmail to project memberlist
                DBManagerSoco dbManagerSoco = ((SocoApp)context).dbManagerSoco;
                dbManagerSoco.addMemberToActivity(
                        inviterEmail,
                        "",             //todo: get username
                        "",             //todo: set nickname
                        Integer.valueOf(pid));

                return true;
            }
            else {
                Log.e(tag, "Server did not return success response");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

}
