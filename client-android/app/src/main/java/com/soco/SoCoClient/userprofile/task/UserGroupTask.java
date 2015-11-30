package com.soco.SoCoClient.userprofile.task;


import android.os.AsyncTask;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.userprofile.model.User;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserGroupTask extends AsyncTask<String, Void, ArrayList<Group> >{

    String tag = "UserGroupTask";
    String user_id;
    String token;

    TaskCallBack callBack;

    public UserGroupTask(String user_id, String token, TaskCallBack cb){
        Log.v(tag, "user group task: " );
        this.user_id = user_id;
        this.token = token;
        callBack = cb;

    }


    protected ArrayList<Group>  doInBackground(String... params) {
        Log.v(tag, "validate data");
        String url = UrlUtil.getUserGroupUrl();
        Object response = request(
                url,
                SocoApp.user_id,
                SocoApp.token,
                params[0]
        );

        if (response != null) {
            Log.v(tag, "parse response");
            return parse(response);
        }
        else {
            Log.e(tag, "response is null, cannot parse");
        }

        return null;
    }

    Object request(
            String url,
            String user_id,
            String token,
            String buddy_id){

        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair(JsonKeys.USER_ID, user_id));
        params.add(new BasicNameValuePair(JsonKeys.TOKEN, token));

        params.add(new BasicNameValuePair(JsonKeys.BUDDY_ID, buddy_id));
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        Log.d(tag, "request url: " + url);

        return HttpUtil.executeHttpGet(url);
    }

    ArrayList<Group>  parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                String groupsStr = json.getString(JsonKeys.GROUPS);
                JSONArray allGroups = new JSONArray(groupsStr);
                ArrayList<Group> groups = new ArrayList<>();
                for (int i = 0; i < allGroups.length(); i++) {
                    JSONObject obj = allGroups.getJSONObject(i);
                    Group g = new Group();
                    g.setGroup_id(obj.getString(JsonKeys.GROUP_ID));
                    g.setGroup_name(obj.getString(JsonKeys.GROUP_NAME));
                    g.setDescription(obj.getString(JsonKeys.DESCRIPTION));

                    String membersStr = obj.getString(JsonKeys.GROUP_MEMBERS);
                    if(membersStr!=null){
                        JSONArray memberObjs = new JSONArray(membersStr);
//                        JSONArray memberObjs =obj.getJSONArray(JsonKeys.GROUP_MEMBERS);
                        ArrayList<User> memberList = new ArrayList<>();
                        for(int j=0;j<memberObjs.length();j++){
                            JSONObject memberObj = memberObjs.getJSONObject(j);
                            User u = new User();
                            u.setUser_name(memberObj.getString(JsonKeys.MEMBER_NAME));
                            u.setUser_id(memberObj.getString(JsonKeys.MEMBER_ID));
                            u.setUser_icon_url(memberObj.getString(JsonKeys.MEMBER_ICON_URL));
                            memberList.add(u);
                        }
                        g.setMembers(memberList);
                    }
                    groups.add(g);
                }
                return groups;
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "request fail, "
                                + "error code: " + error_code
                                + ", message: " + message
                                + ", more info: " + more_info
                );
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();

        }

        return null;
    }

    protected void onPostExecute(ArrayList<Group>  result) {
        callBack.doneTask(result);
    }

}
