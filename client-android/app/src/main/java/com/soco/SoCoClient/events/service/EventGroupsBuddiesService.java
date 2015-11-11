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

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class EventGroupsBuddiesService extends IntentService {
    static final String tag = "EventGBService";

    public static final String EVENT_ID="event_id";

    static SocoApp socoApp;

    public EventGroupsBuddiesService() {
        super("EventGroupsBuddiesService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        socoApp = (SocoApp) getApplicationContext();
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

    public static Object request(
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

    public static boolean parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                Log.d(tag, "response is success");
                socoApp.eventGroupsBuddiesResult = true;

                String eventStr = json.getString(JsonKeys.EVENT);
                JSONObject eventObj = new JSONObject(eventStr);
                Log.v(tag, "event obj: " + eventObj);

                if(!eventObj.has(JsonKeys.ORGANIZER)) {
                    Log.w(tag, "no organizer info is found in json");
                }
                else {
                    String organizerStr = eventObj.getString(JsonKeys.ORGANIZER);
                    JSONObject orgObj = new JSONObject(organizerStr);
                    Log.v(tag, "org obj: " + orgObj);

                    String creatorId = orgObj.getString(JsonKeys.CREATOR_ID);
                    String creatorName = orgObj.getString(JsonKeys.CREATOR_NAME);
                    String creatorIconUrl = orgObj.getString(JsonKeys.CREATOR_ICON_URL);
                    Log.v(tag, "creator info: " + creatorId + ", " + creatorName + ", " + creatorIconUrl);

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
                                }
                            }
                        }
                    }
                }

                if(!eventObj.has(JsonKeys.BUDDIES)){
                    Log.w(tag, "no buddies info is found in json");
                }
                else {
                    //todo: parse buddies details
                }
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

}
