package com.soco.SoCoClient.events.service;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.userprofile.model.User;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class EventGroupsBuddiesService extends IntentService {
    static final String tag = "EventGBService";

    public static final String EVENT_ID="event_id";

    SocoApp socoApp;
    Event event;

    public EventGroupsBuddiesService() {
        super("EventGroupsBuddiesService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        socoApp = (SocoApp) getApplicationContext();
        event = socoApp.getCurrentSuggestedEvent();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(tag, "event group and buddies service, handle intent:" + intent);

        Log.v(tag, "validate data");
        if(socoApp.user_id.isEmpty() || socoApp.token.isEmpty()){
            Log.e(tag, "user id or token or event is not available");
            return;
        }

        Long event_id = intent.getLongExtra(EVENT_ID, 0);
        if(event_id==0) {
//            Event event = socoApp.getCurrentSuggestedEvent();
//            event_id = event.getId();
//            Log.v(tag, "update event_id from event: " + event_id);
            Log.e(tag, "cannot get event id from intent");
        }
        else
            Log.v(tag, "event_id: " + event_id);

        String url = UrlUtil.getEventGroupsBuddiesUrl();
        Object response = request(
                url,
                socoApp.user_id,
                socoApp.token,
                event_id
        );

        Log.v(tag, "set response flag as true");
        socoApp.eventGroupsBuddiesResponse = true;

        if (response != null) {
            Log.v(tag, "parse response");
            parse(response);
        }
        else {
            Log.e(tag, "response is null, cannot parse");
        }

        return;
    }

    public Object request(
            String url,
            String user_id,
            String token,
            long event_id
    ) {
        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<>();
        if(socoApp.SKIP_LOGIN) {
            Log.v(tag, "test user id: " + JsonKeys.TEST_USER_ID + ", test token: " + JsonKeys.TEST_TOKEN);
            params.add(new BasicNameValuePair(JsonKeys.USER_ID, JsonKeys.TEST_USER_ID));
            params.add(new BasicNameValuePair(JsonKeys.TOKEN, JsonKeys.TEST_TOKEN));
        }
        else{
            params.add(new BasicNameValuePair(JsonKeys.USER_ID, user_id));
            params.add(new BasicNameValuePair(JsonKeys.TOKEN, token));
        }

        params.add(new BasicNameValuePair(JsonKeys.EVENT_ID, String.valueOf(event_id)));

        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;
        Log.d(tag, "request url: " + url);

        return HttpUtil.executeHttpGet(url);
    }

    public boolean parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                Log.d(tag, "response is success");
                socoApp.eventGroupsBuddiesResult = true;

                String eventStr = json.getString(JsonKeys.EVENT);
                JSONObject eventObj = new JSONObject(eventStr);
                Log.v(tag, "parse event obj: " + eventObj);

                //comment out below line since needed info already downloaded in suggested event interface
                //parseOrganizer(eventObj, event);

                parseBuddies(eventObj, event);

                Log.v(tag, "updated event: " + event.toString());
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "get event groups and buddies fail, " +
                        "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
                socoApp.eventGroupsBuddiesResult = false;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            socoApp.eventGroupsBuddiesResult = false;
            return false;
        }

        return true;
    }

    void parseOrganizer(JSONObject eventObj, Event event) throws JSONException {
//        Log.v(tag, "parse organizer: " + eventObj);
        if(!eventObj.has(JsonKeys.ORGANIZER)) {
            Log.w(tag, "no organizer info is found in json");
        }
        else {
            String organizerStr = eventObj.getString(JsonKeys.ORGANIZER);
            JSONObject orgObj = new JSONObject(organizerStr);
            Log.v(tag, "org obj: " + orgObj);

            if(!orgObj.has(JsonKeys.CREATOR_ID))
                Log.w(tag, "no creator info found in json");
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
                Log.w(tag, "no enterprise info found in json");
            else{
                event.setEnterprise_id(orgObj.getString(JsonKeys.ENTERPRISE_ID));
                event.setEnterprise_name(orgObj.getString(JsonKeys.ENTERPRISE_NAME));
                event.setEnterprise_icon_url(orgObj.getString(JsonKeys.ENTERPRISE_ICON_URL));
                Log.v(tag, "enterprise info: " + event.getEnterprise_id() + ", " + event.getEnterprise_name() + ", " + event.getEnterprise_icon_url());
            }

            if(!orgObj.has(JsonKeys.SUPPORTING_GROUPS)) {
                Log.w(tag, "no supporting groups info is found in json");
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
                        Log.w(tag, "no group members info is found in json");
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
    void parseBuddies(JSONObject eventObj, Event event) throws JSONException {
        if(!eventObj.has(JsonKeys.BUDDIES))
            Log.w(tag, "no buddies info is found in json");
        else {
            String buddiesStr = eventObj.getString(JsonKeys.BUDDIES);
            JSONObject buddiesObj = new JSONObject(buddiesStr);
            Log.v(tag, "buddies obj: " + buddiesObj);

            if(!buddiesObj.has(JsonKeys.BUDDIES_JOINED))
                Log.w(tag, "no joined buddies found in json");
            else{
                String joinedStr = buddiesObj.getString(JsonKeys.BUDDIES_JOINED);
                JSONObject joinedObj = new JSONObject(joinedStr);
                if(!joinedObj.has(JsonKeys.FRIENDS))
                    Log.w(tag, "no joined friends found in json");
                else{
                    String joinedFriends = joinedObj.getString(JsonKeys.FRIENDS);
                    JSONArray friends = new JSONArray(joinedFriends);
                    Log.v(tag, "all joined friends: " + friends);
                    for(int i=0; i<friends.length(); i++){
                        JSONObject friend = friends.getJSONObject(i);
                        String friendId = friend.getString(JsonKeys.FRIEND_ID);
                        String friendName = friend.getString(JsonKeys.FRIEND_NAME);
                        String friendIconUrl = friend.getString(JsonKeys.FRIEND_ICON_URL);
                        Log.v(tag, "friend info: " + friendId + ", " + friendName + ", " + friendIconUrl);

                        if(event.hasJoinedFriend(friendId))
                            Log.w(tag, "joined friend already added: " + friendId + ", " + friendName + ", " + friendIconUrl);
                        else {
                            User user = new User();
                            user.setUser_id(friendId);
                            user.setUser_name(friendName);
                            user.setUser_icon_url(friendIconUrl);
                            Log.v(tag, "add event joined friend: " + user.toString());
                            event.addJoinedFriends(user);
                        }
                    }
                }
                if(!joinedObj.has(JsonKeys.GROUP_MEMBERS))
                    Log.w(tag, "no joined group members found in json");
                else{
                    String joinedGroupMembersStr = joinedObj.getString(JsonKeys.GROUP_MEMBERS);
                    JSONArray groupMembers = new JSONArray(joinedGroupMembersStr);
                    Log.v(tag, "all joined group members: " + groupMembers);
                    for(int i=0; i<groupMembers.length(); i++){
                        JSONObject member = groupMembers.getJSONObject(i);
                        String memberId = member.getString(JsonKeys.MEMBER_ID);
                        String memberName = member.getString(JsonKeys.MEMBER_NAME);
                        String memberIconUrl = member.getString(JsonKeys.MEMBER_ICON_URL);
                        Log.v(tag, "group member info: " + memberId + ", " + memberName + ", " + memberIconUrl);

                        if(event.hasJoinedGroupMembers(memberId))
                            Log.w(tag, "joined group members already added: " + member);
                        else {
                            User user = new User();
                            user.setUser_id(memberId);
                            user.setUser_name(memberName);
                            user.setUser_icon_url(memberIconUrl);
                            Log.v(tag, "add event joined group member: " + user.toString());
                            event.addJoinedGroupMembers(user);
                        }
                    }
                }
            }

            if(!buddiesObj.has(JsonKeys.BUDDIES_LIKED))
                Log.w(tag, "no liked buddies found in json");
            else{
                String likedStr = buddiesObj.getString(JsonKeys.BUDDIES_LIKED);
                JSONObject likedObj = new JSONObject(likedStr);
                if(!likedObj.has(JsonKeys.FRIENDS))
                    Log.w(tag, "no liked friends found in json");
                else{
                    String likedFriends = likedObj.getString(JsonKeys.FRIENDS);
                    JSONArray friends = new JSONArray(likedFriends);
                    Log.v(tag, "all liked friends: " + friends);
                    for(int i=0; i<friends.length(); i++){
                        JSONObject friend = friends.getJSONObject(i);
                        String friendId = friend.getString(JsonKeys.FRIEND_ID);
                        String friendName = friend.getString(JsonKeys.FRIEND_NAME);
                        String friendIconUrl = friend.getString(JsonKeys.FRIEND_ICON_URL);
                        Log.v(tag, "friend info: " + friendId + ", " + friendName + ", " + friendIconUrl);

                        if(event.hasLikedFriend(friendId))
                            Log.w(tag, "liked friend already added: " + friend);
                        else {
                            User user = new User();
                            user.setUser_id(friendId);
                            user.setUser_name(friendName);
                            user.setUser_icon_url(friendIconUrl);
                            Log.v(tag, "add event liked friend: " + user.toString());
                            event.addLikedFriends(user);
                        }
                    }
                }
                if(!likedObj.has(JsonKeys.GROUP_MEMBERS))
                    Log.w(tag, "no liked group members found in json");
                else{
                    String likedGroupMembersStr = likedObj.getString(JsonKeys.GROUP_MEMBERS);
                    JSONArray groupMembers = new JSONArray(likedGroupMembersStr);
                    Log.v(tag, "all liked group members: " + groupMembers);
                    for(int i=0; i<groupMembers.length(); i++){
                        JSONObject member = groupMembers.getJSONObject(i);
                        String memberId = member.getString(JsonKeys.MEMBER_ID);
                        String memberName = member.getString(JsonKeys.MEMBER_NAME);
                        String memberIconUrl = member.getString(JsonKeys.MEMBER_ICON_URL);
                        Log.v(tag, "group member info: " + memberId + ", " + memberName + ", " + memberIconUrl);

                        if(event.hasLikedGroupMembers(memberId))
                            Log.w(tag, "liked group member already added: " + member);
                        else {
                            User user = new User();
                            user.setUser_id(memberId);
                            user.setUser_name(memberName);
                            user.setUser_icon_url(memberIconUrl);
                            Log.v(tag, "add event liked group member: " + user.toString());
                            event.addLikedGroupMembers(user);
                        }
                    }
                }
            }
        }
    }

}
