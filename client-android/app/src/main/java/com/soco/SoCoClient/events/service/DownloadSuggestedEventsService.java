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
                    parseOrganizers(e, obj);

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

    private void parseOrganizers(Event e, JSONObject obj2)throws JSONException {
        Log.v(tag, "parse organizers: creator and groups");
        if(obj2.has(JsonKeys.ORGANIZER)) {
            String organizerStr = obj2.getString(JsonKeys.ORGANIZER);
            JSONObject obj = new JSONObject(organizerStr);

            if (obj.has(JsonKeys.CREATOR_ID)) {
                e.setCreator_id(obj.getString(JsonKeys.CREATOR_ID));
                e.setCreator_name(obj.getString(JsonKeys.CREATOR_NAME));
                e.setCreator_icon_url(obj.getString(JsonKeys.CREATOR_ICON_URL));
                Log.v(tag, "creator info: " + e.getCreator_id() + ", " + e.getCreator_name() + ", " + e.getCreator_icon_url());
            } else
                Log.v(tag, "no creator info found in json");

            if (obj.has(JsonKeys.ENTERPRISE_ID)) {
                e.setEnterprise_id(obj.getString(JsonKeys.ENTERPRISE_ID));
                e.setEnterprise_name(obj.getString(JsonKeys.ENTERPRISE_NAME));
                e.setEnterprise_icon_url(obj.getString(JsonKeys.ENTERPRISE_ICON_URL));
                Log.v(tag, "enterprise info: " + e.getEnterprise_id() + ", " + e.getEnterprise_name() + ", " + e.getEnterprise_icon_url());
            } else
                Log.v(tag, "no enterprise info found in json");

            if (obj.has(JsonKeys.SUPPORTING_GROUPS)) {
                String allGroupsString = obj.getString(JsonKeys.SUPPORTING_GROUPS);
                JSONArray allGroups = new JSONArray(allGroupsString);
                Log.v(tag, allGroups.length() + " groups found: " + allGroups);
                for (int i = 0; i < allGroups.length(); i++) {
                    JSONObject group = allGroups.getJSONObject(i);
                    Group g = new Group();
                    g.setGroup_id(group.getString(JsonKeys.GROUP_ID));
                    g.setGroup_name(group.getString(JsonKeys.GROUP_NAME));
                    g.setGroup_icon_url(group.getString(JsonKeys.GROUP_ICON_URL));
                    Log.v(tag, "adding group: " + g.getGroup_id() + ", " + g.getGroup_name() + ", " + g.getGroup_icon_url());
                    e.addSupporting_group(g);
                }
            } else
                Log.v(tag, "no supporting groups info found in json");
        }
        else
            Log.v(tag, "no organizer info found in json");
    }

}
