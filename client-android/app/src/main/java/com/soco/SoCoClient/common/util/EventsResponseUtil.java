package com.soco.SoCoClient.common.util;

import android.util.Log;

import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.userprofile.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by David_WANG on 12/03/2015.
 */
public class EventsResponseUtil {
    static final String tag = "EventsResponseUtil";

    public static ArrayList<Event> parseEventsFromJSONArray(JSONArray allEvents) throws Exception{
        ArrayList<Event> result = new ArrayList<>();
        for (int i = 0;i < allEvents.length(); i++){
            JSONObject obj = allEvents.getJSONObject(i);
            Log.v(tag, "current event json: " + obj.toString());
            Event e = parseEventFromJSONObj(obj);
            Log.d(tag, "event created: " + e.getTitle());
            result.add(e);
        }
        return result;
    }
    public static Event parseEventFromJSONObj(JSONObject obj)throws Exception{
        Event e = new Event();
        parseEventBasics(e, obj);
        parseTimedate(e, obj);
        parseAddress(e, obj);
        parseCategories(e, obj);

        parseOrganizer(obj, e);
        parseBuddies(obj, e);
        return e;
    }
    public static void parseEventBasics(Event e, JSONObject obj) throws JSONException {
        e.setId(obj.getLong(JsonKeys.EVENT_ID));
        e.setTitle(obj.getString(JsonKeys.NAME));
        e.setIntroduction(obj.getString(JsonKeys.DESCRIPTION));
        e.setNumber_of_comments(obj.getInt(JsonKeys.NUMBER_OF_COMMENTS));
        e.setNumber_of_likes(obj.getInt(JsonKeys.NUMBER_OF_LIKES));
        try {
            e.setIsLikedEvent(obj.getBoolean(JsonKeys.IS_LIKED_BY_USER));
        }catch (JSONException exc){
            //in case of exception, set false as the default value
            e.setIsLikedEvent(false);
        }
    }

    public static void parseCategories(Event e, JSONObject obj) throws JSONException {
        Log.v(tag, "parse categories");
        if(obj.has(JsonKeys.CATEGORIES)) {
            String allCategoriesString = obj.getString(JsonKeys.CATEGORIES);
            JSONArray allCategories = new JSONArray(allCategoriesString);
            Log.v(tag, allCategories.length() + " categories found: " + allCategories);
            for (int j = 0; j < allCategories.length(); j++) {
                JSONObject cat = allCategories.getJSONObject(j);
                String category = cat.getString(JsonKeys.CATEGORY);
                e.addCategory(category);
            }
        }
        else
            Log.v(tag, "no category info found in json");
    }

    private static void parseAddress(Event e, JSONObject obj) throws JSONException {
        Log.v(tag, "parse venue and address");
        if(obj.has(JsonKeys.VENUE)) {
            String venueStr = obj.getString(JsonKeys.VENUE);
            JSONObject venue = new JSONObject(venueStr);
            Log.v(tag, "current venue: " + venue.toString());
            e.setAddress(venue.getString(JsonKeys.ADDRESS));
        }
        else
            Log.v(tag, "no address info found in json");
    }

    public static void parseTimedate(Event e, JSONObject obj) throws JSONException {
        Log.v(tag, "parse timedate");
        if(obj.has(JsonKeys.TIMEDATE)) {
            String timedateStr = obj.getString(JsonKeys.TIMEDATE);
            JSONObject timedate = new JSONObject(timedateStr);
            Log.v(tag, "current timedate: " + timedate.toString());
            e.setStart_date(timedate.getString(JsonKeys.START_DATE));
            e.setEnd_date(timedate.getString(JsonKeys.END_DATE));
            e.setStart_time(timedate.getString(JsonKeys.START_TIME));
            e.setEnd_time(timedate.getString(JsonKeys.END_TIME));
        }
        else
            Log.v(tag, "no timedate info found in json");
    }

    public static void parseOrganizer(JSONObject eventObj, Event event) throws JSONException {
//        Log.v(tag, "parse organizer: " + eventObj);
        if(!eventObj.has(JsonKeys.ORGANIZER)) {
            Log.v(tag, "no organizer info is found in json");
        }
        else {
            String organizerStr = eventObj.getString(JsonKeys.ORGANIZER);
            JSONObject orgObj = new JSONObject(organizerStr);
            Log.v(tag, "org obj: " + orgObj);

            if(!orgObj.has(JsonKeys.CREATOR_ID))
                Log.v(tag, "no creator info found in json");
            else {
                String creatorId = orgObj.getString(JsonKeys.CREATOR_ID);
                String creatorName = orgObj.getString(JsonKeys.CREATOR_NAME);
                String creatorIconUrl = orgObj.getString(JsonKeys.CREATOR_ICON_URL);
                Log.v(tag, "creator info: " + creatorId + ", " + creatorName + ", " + creatorIconUrl);

                event.setCreator_id(creatorId);
                event.setCreator_name(creatorName);
                event.setCreator_icon_url(creatorIconUrl);
            }

            if (!orgObj.has(JsonKeys.ENTERPRISE_ID))
                Log.v(tag, "no enterprise info found in json");
            else{
                event.setEnterprise_id(orgObj.getString(JsonKeys.ENTERPRISE_ID));
                event.setEnterprise_name(orgObj.getString(JsonKeys.ENTERPRISE_NAME));
                event.setEnterprise_icon_url(orgObj.getString(JsonKeys.ENTERPRISE_ICON_URL));
                Log.v(tag, "enterprise info: " + event.getEnterprise_id() + ", " + event.getEnterprise_name() + ", " + event.getEnterprise_icon_url());
            }

            if(!orgObj.has(JsonKeys.SUPPORTING_GROUPS)) {
                Log.v(tag, "no supporting groups info is found in json");
            }
            else {
                String groupsStr = orgObj.getString(JsonKeys.SUPPORTING_GROUPS);
                JSONArray allGroups = new JSONArray(groupsStr);
                Log.v(tag, "all groups: " + allGroups);

                for (int i = 0; i < allGroups.length(); i++) {
                    JSONObject group = allGroups.getJSONObject(i);
                    String groupId = group.getString(JsonKeys.GROUP_ID);
                    String groupName = group.getString(JsonKeys.GROUP_NAME);
                    String groupIconUrl = group.getString(JsonKeys.GROUP_ICON_URL);
                    Log.v(tag, "group info: " + groupId + ", " + groupName + ", " + groupIconUrl);

                    Group g = new Group();
                    g.setGroup_id(groupId);
                    g.setGroup_name(groupName);
                    g.setGroup_icon_url(groupIconUrl);

                    if(!group.has(JsonKeys.GROUP_MEMBERS)) {
                        Log.v(tag, "no group members info is found in json");
                    }
                    else {
                        String membersStr = group.getString(JsonKeys.GROUP_MEMBERS);
                        JSONArray allMembers = new JSONArray(membersStr);
                        Log.v(tag, "all members: " + allMembers);

                        for (int j = 0; j < allMembers.length(); j++) {
                            JSONObject member = allMembers.getJSONObject(j);
                            String memberId = member.getString(JsonKeys.MEMBER_ID);
                            String memberName = member.getString(JsonKeys.MEMBER_NAME);
                            String memberIconUrl = member.getString(JsonKeys.MEMBER_ICON_URL);
                            Log.v(tag, "member info: " + memberId + ", " + memberName + ", " + memberIconUrl);

                            User user = new User();
                            user.setUser_id(memberId);
                            user.setUser_name(memberName);
                            user.setUser_icon_url(memberIconUrl);
                            g.addMember(user);
                        }
                    }

                    Log.v(tag, "add group: " + g);
                    event.addSupporting_group(g);
                }
            }
        }
    }

    /* sample response for buddies:
    buddies: list of event buddies
        buddies_joined:
            friends [array]: list of friends
                friend_id: friend id
                friend_name: friend name
                friend_icon_url: url of friend’s icon
            group_members [array]: list of group members
                member_id: group member id
                member_name: group member name
                member_icon_url: url of group member’s icon
        buddies_liked:
            friends [array]: list of friends
                friend_id: friend id
                friend_name: friend name
                friend_icon_url: url of friend’s icon
            group_members [array]: list of group members
                member_id: group member id
                member_name: group member name
                member_icon_url: url of group member’s icon
     */
    public static void parseBuddies(JSONObject eventObj, Event event) throws JSONException {
        if(!eventObj.has(JsonKeys.REPRESENTATIVE_BUDDIES)&&!eventObj.has(JsonKeys.BUDDIES))
            Log.v(tag, "no representative buddies info is found in json");
        else {
            String buddiesStr="";
            if(eventObj.has(JsonKeys.REPRESENTATIVE_BUDDIES)) {
                buddiesStr = eventObj.getString(JsonKeys.REPRESENTATIVE_BUDDIES);
            }else if(eventObj.has(JsonKeys.BUDDIES)){
                buddiesStr=eventObj.getString(JsonKeys.BUDDIES);
            }
            JSONObject buddiesObj = new JSONObject(buddiesStr);
            Log.v(tag, "representative buddies obj: " + buddiesObj);

            if(!buddiesObj.has(JsonKeys.BUDDIES_JOINED))
                Log.v(tag, "no joined buddies found in json");
            else{
                String joinedStr = buddiesObj.getString(JsonKeys.BUDDIES_JOINED);
                JSONObject joinedObj = new JSONObject(joinedStr);
//                if(joinedObj.has(JsonKeys.FRIENDS)){
//                    String joinedFriends = joinedObj.getString(JsonKeys.FRIENDS);
//                    JSONArray friends = new JSONArray(joinedFriends);
//                    Log.v(tag, "all joined friends: " + friends);
//                    for(int i=0; i<friends.length(); i++){
//                        JSONObject friend = friends.getJSONObject(i);
//                        String friendId = friend.getString(JsonKeys.FRIEND_ID);
//                        String friendName = friend.getString(JsonKeys.FRIEND_NAME);
//                        String friendIconUrl = friend.getString(JsonKeys.FRIEND_ICON_URL);
//                        Log.v(tag, "friend info: " + friendId + ", " + friendName + ", " + friendIconUrl);
//
//                        User user = new User();
//                        user.setUser_id(friendId);
//                        user.setUser_name(friendName);
//                        user.setUser_icon_url(friendIconUrl);
//
//                        Log.v(tag, "add event joined friend: " + user.toString());
//                        event.addJoinedFriends(user);
//                    }
//                }
//                if(joinedObj.has(JsonKeys.GROUP_MEMBERS)){
//                    String joinedGroupMembersStr = joinedObj.getString(JsonKeys.GROUP_MEMBERS);
//                    JSONArray groupMembers = new JSONArray(joinedGroupMembersStr);
//                    Log.v(tag, "all joined group members: " + groupMembers);
//                    for(int i=0; i<groupMembers.length(); i++){
//                        JSONObject member = groupMembers.getJSONObject(i);
//                        String memberId = member.getString(JsonKeys.MEMBER_ID);
//                        String memberName = member.getString(JsonKeys.MEMBER_NAME);
//                        String memberIconUrl = member.getString(JsonKeys.MEMBER_ICON_URL);
//                        Log.v(tag, "group member info: " + memberId + ", " + memberName + ", " + memberIconUrl);
//
//                        User user = new User();
//                        user.setUser_id(memberId);
//                        user.setUser_name(memberName);
//                        user.setUser_icon_url(memberIconUrl);
//
//                        Log.v(tag, "add event joined group member: " + user.toString());
//                        event.addJoinedGroupMembers(user);
//                    }
//                }
                if(joinedObj.has(JsonKeys.BUDDIES)){
                    String joinedBuddiesStr = joinedObj.getString(JsonKeys.BUDDIES);
                    JSONArray joinedBuddies = new JSONArray(joinedBuddiesStr);
                    Log.v(tag, "all joined buddies: " + joinedBuddies);
                    for(int i=0; i<joinedBuddies.length(); i++){
                        JSONObject buddy = joinedBuddies.getJSONObject(i);
                        String buddyId = buddy.getString(JsonKeys.USER_ID);
                        String buddyName = buddy.getString(JsonKeys.USER_NAME);
                        String buddyIconUrl = buddy.getString(JsonKeys.USER_ICON_URL);
                        Log.v(tag, "buddy info: " + buddyId + ", " + buddyName + ", " + buddyIconUrl);

                        User user = new User();
                        user.setUser_id(buddyId);
                        user.setUser_name(buddyName);
                        user.setUser_icon_url(buddyIconUrl);

                        Log.v(tag, "add event joined group member: " + user.toString());
                        event.addJoinedBuddies(user);
                    }
                }
            }

            if(!buddiesObj.has(JsonKeys.BUDDIES_LIKED))
                Log.v(tag, "no liked buddies found in json");
            else{
                String likedStr = buddiesObj.getString(JsonKeys.BUDDIES_LIKED);
                JSONObject likedObj = new JSONObject(likedStr);
//                if(likedObj.has(JsonKeys.FRIENDS)){
//                    String likedFriends = likedObj.getString(JsonKeys.FRIENDS);
//                    JSONArray friends = new JSONArray(likedFriends);
//                    Log.v(tag, "all liked friends: " + friends);
//                    for(int i=0; i<friends.length(); i++){
//                        JSONObject friend = friends.getJSONObject(i);
//                        String friendId = friend.getString(JsonKeys.FRIEND_ID);
//                        String friendName = friend.getString(JsonKeys.FRIEND_NAME);
//                        String friendIconUrl = friend.getString(JsonKeys.FRIEND_ICON_URL);
//                        Log.v(tag, "friend info: " + friendId + ", " + friendName + ", " + friendIconUrl);
//
//                        User user = new User();
//                        user.setUser_id(friendId);
//                        user.setUser_name(friendName);
//                        user.setUser_icon_url(friendIconUrl);
//
//                        Log.v(tag, "add event liked friend: " + user.toString());
//                        event.addLikedFriends(user);
//                    }
//                }
//                if(likedObj.has(JsonKeys.GROUP_MEMBERS)){
//                    String likedGroupMembersStr = likedObj.getString(JsonKeys.GROUP_MEMBERS);
//                    JSONArray groupMembers = new JSONArray(likedGroupMembersStr);
//                    Log.v(tag, "all liked group members: " + groupMembers);
//                    for(int i=0; i<groupMembers.length(); i++){
//                        JSONObject member = groupMembers.getJSONObject(i);
//                        String memberId = member.getString(JsonKeys.MEMBER_ID);
//                        String memberName = member.getString(JsonKeys.MEMBER_NAME);
//                        String memberIconUrl = member.getString(JsonKeys.MEMBER_ICON_URL);
//                        Log.v(tag, "group member info: " + memberId + ", " + memberName + ", " + memberIconUrl);
//
//                        User user = new User();
//                        user.setUser_id(memberId);
//                        user.setUser_name(memberName);
//                        user.setUser_icon_url(memberIconUrl);
//
//                        Log.v(tag, "add event liked group member: " + user.toString());
//                        event.addLikedGroupMembers(user);
//                    }
//                }
                if(likedObj.has(JsonKeys.BUDDIES)){
                    String likedBuddiesStr = likedObj.getString(JsonKeys.BUDDIES);
                    JSONArray likedBuddies = new JSONArray(likedBuddiesStr);
                    Log.v(tag, "all joined buddies: " + likedBuddies);
                    for(int i=0; i<likedBuddies.length(); i++){
                        JSONObject buddy = likedBuddies.getJSONObject(i);
                        String buddyId = buddy.getString(JsonKeys.USER_ID);
                        String buddyName = buddy.getString(JsonKeys.USER_NAME);
                        String buddyIconUrl = buddy.getString(JsonKeys.USER_ICON_URL);
                        Log.v(tag, "buddy info: " + buddyId + ", " + buddyName + ", " + buddyIconUrl);

                        User user = new User();
                        user.setUser_id(buddyId);
                        user.setUser_name(buddyName);
                        user.setUser_icon_url(buddyIconUrl);

                        Log.v(tag, "add event joined group member: " + user.toString());
                        event.addLikedBuddies(user);
                    }
                }
            }
        }
    }
}
