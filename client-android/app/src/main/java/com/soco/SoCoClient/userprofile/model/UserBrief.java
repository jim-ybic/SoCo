package com.soco.SoCoClient.userprofile.model;

import android.util.Log;

import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserBrief {
    static final String tag = "UserBrief";
    public static final String USER_ID = "USER_ID";

    private String user_id, user_name, user_icon_url;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_icon_url() {
//        return "https://graph.facebook.com/10153298013434285/picture?type=normal";
        StringBuffer sb = new StringBuffer();
        sb.append(UrlUtil.getUserIconUrlPrefix());
        sb.append(SocoApp.getCurrentUserTokenForUrl());
        if(SocoApp.user_id!=null&&!SocoApp.user_id.equalsIgnoreCase(this.user_id)){
            sb.append("&buddy_user_id=");
            sb.append(this.user_id);
        }
        return sb.toString();
    }

    public void setUser_icon_url(String user_icon_url) {
        this.user_icon_url = user_icon_url;
    }

    public UserBrief(){

    }
    public UserBrief(String Puser_id, String Puser_name, String Puser_icon_url){
        this.user_id=Puser_id;
        this.user_name=Puser_name;
        this.user_icon_url=Puser_icon_url;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_icon_url='" + user_icon_url + '\'' +
                '}';
    }
}
