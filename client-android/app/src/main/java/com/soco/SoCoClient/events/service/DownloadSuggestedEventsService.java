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
        if(!socoApp.SKIP_LOGIN && (socoApp.user_id.isEmpty() || socoApp.token.isEmpty())){
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
    }

    public static boolean parse(Object response) {
        Log.d(tag, "parse response: " + response.toString());

        try {
            JSONObject json = new JSONObject();
            if(socoApp.USE_SIMULATOR_SUGGESTED_EVENTS) {
                Log.w(tag, "use simulated response");
                json = new JSONObject(JsonKeys.TEST_DOWNLOAD_SUGGESTED_EVENTS_RESPONSE);
            }
            else
                json = new JSONObject(response.toString());

            int status = json.getInt(JsonKeys.STATUS);
            if(status == HttpStatus.SUCCESS) {
                Log.d(tag, "create event success, retrieve event list");

                String alleventsString = json.getString(JsonKeys.EVENTS);
                Log.v(tag, "all events string: " + alleventsString);
                JSONArray allEvents = new JSONArray(alleventsString);
//                JSONArray allEvents2 = json.getJSONArray(JsonKeys.EVENTS);  //alternative to be investigated
                Log.d(tag, allEvents.length() + " events downloaded");

                for(int i=0; i<allEvents.length(); i++){
                    JSONObject e = allEvents.getJSONObject(i);
                    Log.d(tag, "current event: " + e.toString());
                    String id = e.getString(JsonKeys.ID);
                    String title = e.getString(JsonKeys.NAME);

                    String timedateStr = e.getString(JsonKeys.TIMEDATE);
                    JSONObject timedate = new JSONObject(timedateStr);
                    Log.v(tag, "current timedate: " + timedate.toString());
                    String start_date = timedate.getString(JsonKeys.START_DATE);
                    String start_time = timedate.getString(JsonKeys.START_TIME);

                    //todo
                }

                //todo

                socoApp.downloadSuggestedEventsResult = true;
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
