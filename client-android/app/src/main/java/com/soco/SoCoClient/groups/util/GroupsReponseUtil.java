package com.soco.SoCoClient.groups.util;

import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.userprofile.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by David_WANG on 12/01/2015.
 */
public class GroupsReponseUtil {
    public static ArrayList<Group> parseGroupsResponse(JSONObject json) throws Exception{
        String groupsStr = json.getString(JsonKeys.GROUPS);
        JSONArray allGroups = new JSONArray(groupsStr);
        ArrayList<Group> groups = new ArrayList<>();
        for (int i = 0; i < allGroups.length(); i++) {
            JSONObject obj = allGroups.getJSONObject(i);
            Group g = new Group();
            g.setGroup_id(obj.getString(JsonKeys.GROUP_ID));
            g.setGroup_name(obj.getString(JsonKeys.GROUP_NAME));
            g.setDescription(obj.getString(JsonKeys.DESCRIPTION));
            g.setNumberOfMembers(obj.getString(JsonKeys.NUMBER_OF_MEMBERS));

            String membersStr = obj.getString(JsonKeys.GROUP_MEMBERS);
            if(membersStr!=null){
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
            groups.add(g);
        }
        return groups;
    }
}
