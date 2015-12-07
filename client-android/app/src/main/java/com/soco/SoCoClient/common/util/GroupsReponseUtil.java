package com.soco.SoCoClient.common.util;

import android.util.Log;

import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.userprofile.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by David_WANG on 12/01/2015.
 */
public class GroupsReponseUtil {
    static final String tag = "GroupsResponseUtil";

    public static ArrayList<Group> parseGroupsResponse(JSONObject json) throws Exception{
        String groupsStr = json.getString(JsonKeys.GROUPS);
        JSONArray allGroups = new JSONArray(groupsStr);
        ArrayList<Group> groups = new ArrayList<>();
        for (int i = 0; i < allGroups.length(); i++) {
            JSONObject obj = allGroups.getJSONObject(i);
            Group g = parseGroupResponse(obj);
            groups.add(g);
        }
        return groups;
    }

    public static Group parseGroupResponse(JSONObject obj) throws Exception{
        Log.v(tag, "parse group response: " + obj);

        Group g = new Group();
        if(obj.has(JsonKeys.GROUP_ID)) g.setGroup_id(obj.getString(JsonKeys.GROUP_ID));
        if(obj.has(JsonKeys.GROUP_NAME)) g.setGroup_name(obj.getString(JsonKeys.GROUP_NAME));
        if(obj.has(JsonKeys.NAME))  g.setGroup_name(obj.getString(JsonKeys.NAME));
        if(obj.has(JsonKeys.DESCRIPTION)) g.setDescription(obj.getString(JsonKeys.DESCRIPTION));
        if(obj.has(JsonKeys.INTRODUCTION)) g.setDescription(obj.getString(JsonKeys.INTRODUCTION));
        if(obj.has(JsonKeys.NUMBER_OF_MEMBERS)) g.setNumberOfMembers(obj.getString(JsonKeys.NUMBER_OF_MEMBERS));

        String membersStr = null;
        if(obj.has(JsonKeys.GROUP_MEMBERS))
            membersStr=obj.getString(JsonKeys.GROUP_MEMBERS);
        if(membersStr!=null){
            Log.v(tag, "parse members: " + membersStr);
            JSONArray memberObjs = new JSONArray(membersStr);
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

        if(obj.has(JsonKeys.CATEGORIES)){
//            String catStr = obj.getString(JsonKeys.CATEGORIES);
//            Log.v(tag, "parse group categories: " + catStr);
//            JSONArray array = new JSONArray(catStr);
            JSONArray array = obj.getJSONArray(JsonKeys.CATEGORIES);
            Log.v(tag, "parse group categories: " + array);
            for(int i=0; i<array.length(); i++){
                JSONObject o = array.getJSONObject(i);
                String cat = o.getString(JsonKeys.CATEGORY);
                Log.v(tag, "get group category: " + cat);
                g.addCategory(cat);
            }
        }
        else
            Log.v(tag, "no category for group: " + g.getGroup_name());

//        if(obj.has(JsonKeys.))
        return g;
    }
    public static Group parseEventsAttachToGroup(JSONObject obj, Group g) throws Exception{
        if(obj.has(JsonKeys.UPCOMING_EVENTS)){
            String upcomingEventsStr = obj.getString(JsonKeys.UPCOMING_EVENTS);
            JSONArray upcomingEventsJson = new JSONArray(upcomingEventsStr);
            ArrayList<Event> upcoming = EventsResponseUtil.parseEventsFromJSONArray(upcomingEventsJson);
            g.setUpcomingEvents(upcoming);
        }
        if(obj.has(JsonKeys.PAST_EVENTS)){
            String pastEventsStr = obj.getString(JsonKeys.PAST_EVENTS);
            JSONArray pastEventsJson = new JSONArray(pastEventsStr);
            ArrayList<Event> past = EventsResponseUtil.parseEventsFromJSONArray(pastEventsJson);
            g.setPastEvents(past);
        }
        return g;
    }
}
