package com.soco.SoCoClient.events.service;


import android.app.IntentService;
import android.content.Entity;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DownloadSuggestedEventsService extends IntentService {

    static final String tag = "DownloadSuggestedEvents";

    static SocoApp socoApp;

    public DownloadSuggestedEventsService() {
        super("CreateEventService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        socoApp = (SocoApp) getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(tag, "download suggested event service, handle intent:" + intent
                        + ", userid: " + socoApp.user_id + ", token: " + socoApp.token
        );

        Log.v(tag, "validate data");
        if(!socoApp.SKIP_LOGIN &&
                (socoApp.user_id == null || socoApp.user_id.isEmpty()
                        || socoApp.token == null || socoApp.token.isEmpty())){
            Log.e(tag, "user id or token or event is not available");
            return;
        }

        String url = UrlUtil.getSuggestedEventsUrl();
        Object response = request(
                url,
                socoApp.user_id,
                socoApp.token
        );

        Log.v(tag, "set response flag as true");
        socoApp.downloadSuggestedEventsResponse = true;

        if (response != null) {
            Log.v(tag, "parse response");
            parse(response);
        }
        else {
            Log.e(tag, "response is null, cannot parse");
        }

        return;
    }

    public static Object request(
            String url,
            String user_id,
            String token
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
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        Log.d(tag, "request url: " + url);

        return HttpUtil.executeHttpGet(url);
//        HttpUtil.executeHttpGet2(url);
    }

//    JSONObject convertHttpResponseToJsonObject (HttpResponse response) {
//        HttpEntity entity = response.getEntity();
//        InputStream is = null;
//        try {
//            is = entity.getContent();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuilder sb = new StringBuilder();
//
//        String line = null;
//        try {
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
////        return sb.toString();
//
//        JSONObject myObject = null;
//        try {
//            myObject = new JSONObject(sb.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return myObject;
//    }

    public boolean parse(Object response) {
//        Log.d(tag, "parse response: " + response.getEntity().toString());

        Log.v(tag, "clear suggested events");
        socoApp.suggestedEvents = new ArrayList<>();

        try {
            JSONObject json;
            if(socoApp.USE_SIMULATOR_SUGGESTED_EVENTS) {
                Log.w(tag, "use simulated response");
                json = new JSONObject(JsonKeys.TEST_DOWNLOAD_SUGGESTED_EVENTS_RESPONSE);
            }
            else {
                json = new JSONObject(response.toString());
                Log.d(tag, "converted json: " + json);
            }

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                String alleventsString = json.getString(JsonKeys.EVENTS);
                Log.v(tag, "all events string: " + alleventsString);
                JSONArray allEvents = new JSONArray(alleventsString);
//                JSONArray allEvents2 = json.getJSONArray(JsonKeys.EVENTS);  //alternative to be investigated
                Log.d(tag, "retrieve event list: " + allEvents.length() + " events downloaded");

                for(int i=0; i<allEvents.length(); i++){
                    Event e = new Event();
                    JSONObject obj = allEvents.getJSONObject(i);
                    Log.v(tag, "current event json: " + obj.toString());

                    parseEventBasics(e, obj);
                    parseTimedate(e, obj);
                    parseAddress(e, obj);
                    parseCategories(e, obj);

                    parseOrganizer(obj, e);
                    parseBuddies(obj, e);

                    Log.d(tag, "event created: " + e.toString());
                    socoApp.suggestedEvents.add(e);
                }

                socoApp.downloadSuggestedEventsResult = true;
                socoApp.mappingSuggestedEventListToMap();
                Log.d(tag, socoApp.suggestedEvents.size() + " events created from json response");
            }
            else {
                String error_code = json.getString(JsonKeys.ERROR_CODE);
                String message = json.getString(JsonKeys.MESSAGE);
                String more_info = json.getString(JsonKeys.MORE_INFO);
                Log.d(tag, "create event fail, " +
                        "error code: " + error_code + ", message: " + message + ", more info: " + more_info
                );
                socoApp.downloadSuggestedEventsResult = false;
            }
        } catch (Exception e) {
            Log.e(tag, "cannot convert parse to json object: " + e.toString());
            e.printStackTrace();
            socoApp.downloadSuggestedEventsResult = false;
            return false;
        }

        return true;
    }

    private void parseEventBasics(Event e, JSONObject obj) throws JSONException {
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

    private void parseCategories(Event e, JSONObject obj) throws JSONException {
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

    private void parseAddress(Event e, JSONObject obj) throws JSONException {
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

    private void parseTimedate(Event e, JSONObject obj) throws JSONException {
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
        if(!eventObj.has(JsonKeys.REPRESENTATIVE_BUDDIES))
            Log.w(tag, "no representative buddies info is found in json");
        else {
            String buddiesStr = eventObj.getString(JsonKeys.REPRESENTATIVE_BUDDIES);
            JSONObject buddiesObj = new JSONObject(buddiesStr);
            Log.v(tag, "representative buddies obj: " + buddiesObj);

            if(!buddiesObj.has(JsonKeys.BUDDIES_JOINED))
                Log.w(tag, "no joined buddies found in json");
            else{
                String joinedStr = buddiesObj.getString(JsonKeys.BUDDIES_JOINED);
                JSONObject joinedObj = new JSONObject(joinedStr);
                if(joinedObj.has(JsonKeys.FRIENDS)){
                    String joinedFriends = joinedObj.getString(JsonKeys.FRIENDS);
                    JSONArray friends = new JSONArray(joinedFriends);
                    Log.v(tag, "all joined friends: " + friends);
                    for(int i=0; i<friends.length(); i++){
                        JSONObject friend = friends.getJSONObject(i);
                        String friendId = friend.getString(JsonKeys.FRIEND_ID);
                        String friendName = friend.getString(JsonKeys.FRIEND_NAME);
                        String friendIconUrl = friend.getString(JsonKeys.FRIEND_ICON_URL);
                        Log.v(tag, "friend info: " + friendId + ", " + friendName + ", " + friendIconUrl);

                        User user = new User();
                        user.setUser_id(friendId);
                        user.setUser_name(friendName);
                        user.setUser_icon_url(friendIconUrl);

                        Log.v(tag, "add event joined friend: " + user.toString());
                        event.addJoinedFriends(user);
                    }
                }
                if(joinedObj.has(JsonKeys.GROUP_MEMBERS)){
                    String joinedGroupMembersStr = joinedObj.getString(JsonKeys.GROUP_MEMBERS);
                    JSONArray groupMembers = new JSONArray(joinedGroupMembersStr);
                    Log.v(tag, "all joined group members: " + groupMembers);
                    for(int i=0; i<groupMembers.length(); i++){
                        JSONObject member = groupMembers.getJSONObject(i);
                        String memberId = member.getString(JsonKeys.MEMBER_ID);
                        String memberName = member.getString(JsonKeys.MEMBER_NAME);
                        String memberIconUrl = member.getString(JsonKeys.MEMBER_ICON_URL);
                        Log.v(tag, "group member info: " + memberId + ", " + memberName + ", " + memberIconUrl);

                        User user = new User();
                        user.setUser_id(memberId);
                        user.setUser_name(memberName);
                        user.setUser_icon_url(memberIconUrl);

                        Log.v(tag, "add event joined group member: " + user.toString());
                        event.addJoinedGroupMembers(user);
                    }
                }
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
                Log.w(tag, "no liked buddies found in json");
            else{
                String likedStr = buddiesObj.getString(JsonKeys.BUDDIES_LIKED);
                JSONObject likedObj = new JSONObject(likedStr);
                if(likedObj.has(JsonKeys.FRIENDS)){
                    String likedFriends = likedObj.getString(JsonKeys.FRIENDS);
                    JSONArray friends = new JSONArray(likedFriends);
                    Log.v(tag, "all liked friends: " + friends);
                    for(int i=0; i<friends.length(); i++){
                        JSONObject friend = friends.getJSONObject(i);
                        String friendId = friend.getString(JsonKeys.FRIEND_ID);
                        String friendName = friend.getString(JsonKeys.FRIEND_NAME);
                        String friendIconUrl = friend.getString(JsonKeys.FRIEND_ICON_URL);
                        Log.v(tag, "friend info: " + friendId + ", " + friendName + ", " + friendIconUrl);

                        User user = new User();
                        user.setUser_id(friendId);
                        user.setUser_name(friendName);
                        user.setUser_icon_url(friendIconUrl);

                        Log.v(tag, "add event liked friend: " + user.toString());
                        event.addLikedFriends(user);
                    }
                }
                if(likedObj.has(JsonKeys.GROUP_MEMBERS)){
                    String likedGroupMembersStr = likedObj.getString(JsonKeys.GROUP_MEMBERS);
                    JSONArray groupMembers = new JSONArray(likedGroupMembersStr);
                    Log.v(tag, "all liked group members: " + groupMembers);
                    for(int i=0; i<groupMembers.length(); i++){
                        JSONObject member = groupMembers.getJSONObject(i);
                        String memberId = member.getString(JsonKeys.MEMBER_ID);
                        String memberName = member.getString(JsonKeys.MEMBER_NAME);
                        String memberIconUrl = member.getString(JsonKeys.MEMBER_ICON_URL);
                        Log.v(tag, "group member info: " + memberId + ", " + memberName + ", " + memberIconUrl);

                        User user = new User();
                        user.setUser_id(memberId);
                        user.setUser_name(memberName);
                        user.setUser_icon_url(memberIconUrl);

                        Log.v(tag, "add event liked group member: " + user.toString());
                        event.addLikedGroupMembers(user);
                    }
                }
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
