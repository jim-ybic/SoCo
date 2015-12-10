package com.soco.SoCoClient.events.service;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.soco.SoCoClient.common.HttpStatus;
import com.soco.SoCoClient.common.http.HttpUtil;
import com.soco.SoCoClient.common.http.JsonKeys;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.EventsResponseUtil;
import com.soco.SoCoClient.common.util.SocoApp;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DownloadSuggestedEventsService extends IntentService {

    static final String tag = "DownloadSuggestedEvents";
    static final String PERFS_NAME = "EVENT_BUDDY_PERFS";
    static final String USER_ID = "user_id";
    static final String TOKEN = "token";

    Context context;
    static SocoApp socoApp;

    public DownloadSuggestedEventsService() {
        super("CreateEventService");
    }

    @Override
    public void onCreate() {
        super.onCreate();   //important

        context = getApplicationContext();
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
            Log.e(tag, "user id or token or event is not available in memory");

            SharedPreferences settings = context.getSharedPreferences(PERFS_NAME, 0);
            String userId = settings.getString(USER_ID, "");
            String token = settings.getString(TOKEN, "");
            Log.v(tag, "get stored userid/token: " + userId+ ", " + token);
            if(token != null && !token.isEmpty()){
                socoApp.user_id = userId;
                socoApp.token = token;
            }
            else {
                Log.e(tag, "cannot get userid/token from shared preference");
                socoApp.downloadSuggestedEventsResponse = true;
                socoApp.downloadSuggestedEventsResult = false;
                return;
            }
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
            params.add(new BasicNameValuePair(JsonKeys.KEYWORD, "more"));
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
                socoApp.suggestedEvents= EventsResponseUtil.parseEventsFromJSONArray(allEvents);
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

}
