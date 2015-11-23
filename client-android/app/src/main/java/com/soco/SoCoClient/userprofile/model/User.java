package com.soco.SoCoClient.userprofile.model;

import android.util.Log;

import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class User extends UserBrief{
    static final String tag = "User";

    private String location;
    private ArrayList<String> interests = new ArrayList<>();

//    private int number_common_event;
//    private String common_event_name;
//    private int number_common_group;
//    private String common_group_name;

    private int number_common_buddy;
    private ArrayList<UserBrief> common_buddies = new ArrayList<>();

    int number_event;
    String event_name;
    int number_group;
    String group_name;

    String hometown;
    ArrayList<User> friends_list = new ArrayList<>();
    HashSet<String> friendIds = new HashSet<>();
    String biography;

    public User(){}

    public User(JSONObject obj){
        try {
            this.setUser_id(obj.getString(JsonKeys.USER_ID));
            this.setUser_name(obj.getString(JsonKeys.USER_NAME));
            this.setUser_icon_url(obj.getString(JsonKeys.USER_ICON_URL));
//            Log.v(tag, "user created from json: " + getUser_id() + ", " + getUser_name() + ", " + getUser_icon_url());
        } catch (JSONException e) {
            Log.e(tag, "cannot create user brief from json: " + obj);
            e.printStackTrace();
        }
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interest) {
        this.interests = interest;
    }

    public void addInterest(String interest){
//        if(this.interests==null){
//            this.interests = new ArrayList<>();
//        }
        this.interests.add(interest);
    }
//    public int getNumber_common_event() {
//        return number_common_event;
//    }
//
//    public void setNumber_common_event(int number_common_event) {
//        this.number_common_event = number_common_event;
//    }
//
//    public String getCommon_event_name() {
//        return common_event_name;
//    }
//
//    public void setCommon_event_name(String common_event_name) {
//        this.common_event_name = common_event_name;
//    }
//
//    public int getNumber_common_group() {
//        return number_common_group;
//    }
//
//    public void setNumber_common_group(int number_common_group) {
//        this.number_common_group = number_common_group;
//    }
//
//    public String getCommon_group_name() {
//        return common_group_name;
//    }
//
//    public void setCommon_group_name(String common_group_name) {
//        this.common_group_name = common_group_name;
//    }

    public int getNumber_common_buddy() {
        return number_common_buddy;
    }

    public void setNumber_common_buddy(int number_common_buddy) {
        this.number_common_buddy = number_common_buddy;
    }

    public ArrayList<UserBrief> getCommon_buddies() {
        return common_buddies;
    }

    public void setCommon_buddies(ArrayList<UserBrief> common_buddies) {
        this.common_buddies = common_buddies;
    }
    public void addCommon_buddy(UserBrief userBrief){
//        if(this.common_buddies==null){
//            this.common_buddies = new ArrayList<>();
//        }
        this.common_buddies.add(userBrief);
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public ArrayList<User> getFriends_list() {
        return friends_list;
    }

    public void setFriends_list(ArrayList<User> friends_list) {
        for(User u : friends_list)
            friendIds.add(u.getUser_id());
        this.friends_list = friends_list;
    }

    public void addFriends_list(User u){
        if(friendIds.contains(u.getUser_id()))
            Log.w(tag, "user already in friend list: " + u.getUser_name());
        else {
            friendIds.add(u.getUser_id());
            this.friends_list.add(u);
        }
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getNumber_event() {
        return number_event;
    }

    public void setNumber_event(int number_event) {
        this.number_event = number_event;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public int getNumber_group() {
        return number_group;
    }

    public void setNumber_group(int number_group) {
        this.number_group = number_group;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + getUser_id() + '\'' +
                "user_name='" + getUser_name() + '\'' +
                "user_icon_url='" + getUser_icon_url() + '\'' +
                "location='" + location + '\'' +
                ", interests=" + interests +
//                ", number_common_event=" + number_common_event +
//                ", common_event_name='" + common_event_name + '\'' +
//                ", number_common_group=" + number_common_group +
//                ", common_group_name='" + common_group_name + '\'' +
                ", number_common_buddy=" + number_common_buddy +
                ", common_buddies=" + common_buddies +
                ", hometown='" + hometown + '\'' +
                ", bio='" + biography + '\'' +
                ", friends_list=" + friends_list +
                '}';
    }


}
