package com.soco.SoCoClient.control.http.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.config.GeneralConfig;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.http.HttpUtil;
import com.soco.SoCoClient.control.http.task.JoinActivityByInviteTaskAsync;
import com.soco.SoCoClient.control.http.task.RetrieveMessageTaskAsync;
import com.soco.SoCoClient.control.util.SignatureUtil;
import com.soco.SoCoClient.model.Profile;
import com.soco.SoCoClient.model.Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class HeartbeatService extends Service {

    public static final String tag = "HeartbeatService";

    Timer timer;
    SocoApp socoApp;
    Profile profile;
    DBManagerSoco dbManagerSoco;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(tag, "onCreate() executed");

        socoApp = (SocoApp)getApplication();
        profile = socoApp.profile;
        dbManagerSoco = socoApp.dbManagerSoco;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(tag, "onStartCommand() executed");


        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run(){
                        Log.i(tag, ">>> Heartbeat:" + SignatureUtil.now());

                        String access_token = profile.getLoginAccessToken(getApplicationContext());
                        if(access_token == null || access_token.isEmpty()){
                            Log.i(tag, "access token is not available, skip heart check");
                        }
                        else {
                            Object response = request();
                            if (response != null)
                                parse(response);
                        }
                    }
                },
                0,      //delay start time (ms)
                2*60*1000    //frequency (ms)
        );

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(tag, "onDestroy() executed");
    }

    @Override
    public MyBinder onBind(Intent intent) {
        return null;
    }

    class MyBinder extends Binder {
    }

    public Object request() {
        JSONObject data = new JSONObject();
        try {
            Log.d(tag, "No json created for heart beat request");
        } catch (Exception e) {
            Log.e(tag, "Cannot create request");
            e.printStackTrace();
        }
        String url = profile.getHeartbeatUrl(getApplicationContext());
        Log.d(tag, "Sending heartbeat to " + url);

        return HttpUtil.executeHttpPost(url, data);
    }

    public boolean parse(Object response) {
        try {
            String str = response.toString();
            Log.i(tag, "Heartbeat response: " + str);

            JSONObject json = new JSONObject(response.toString());
            String isSuccess = json.getString(HttpConfig.JSON_KEY_RESPONSE_STATUS);

            if(isSuccess.equals(HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS)) {
                Log.i(tag, "Parse result: " + HttpConfig.JSON_VALUE_RESPONSE_STATUS_SUCCESS);

                //retrieve all invitations, if any
                if(json.has(HttpConfig.JSON_KEY_INVITATION))
                    return joinActivititiesByInvite(json);

                //check if there is any new message
                if(json.has(HttpConfig.JSON_KEY_MESSAGE))
                    retrieveMessage(json);

            }
            else {
                Log.e(tag, "Server response status is failure");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean retrieveMessage(JSONObject json) {
        try {
            String status = json.getString(HttpConfig.JSON_KEY_MESSAGE);
            Log.i(tag, "new message status: " + status);
            //retrieve message
            String url = profile.getRetrieveMessageUrl(getApplicationContext());
            RetrieveMessageTaskAsync task = new RetrieveMessageTaskAsync(
                    url, getApplicationContext());
            task.execute();
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /*
    Sample message:
        "invitation"":"[
            {inviter:test@test.com,
            activity:1,
            date:2015-05-05 11:1:11},
            {another invitation}
        ]"}
     */
    private boolean joinActivititiesByInvite(JSONObject json) {
        try {
            Log.i(tag, "Invitation str: " + json.getString(HttpConfig.JSON_KEY_INVITATION));
            JSONArray invitationArray = new JSONArray(
                    json.getString(HttpConfig.JSON_KEY_INVITATION));

            //process each activity invitation
            for (int i = 0; i < invitationArray.length(); i++) {
                JSONObject invitation = invitationArray.getJSONObject(i);
                String inviterEmail = invitation.getString(HttpConfig.JSON_KEY_INVITER);
                String pid_onserver = invitation.getString(HttpConfig.JSON_KEY_PROJECT_ID);
                String date = invitation.getString(HttpConfig.JSON_KEY_DATE);
                Log.i(tag, "Get invitation: " + inviterEmail + ", " + pid_onserver + ", " + date);

                //add project into database
                Activity p = new Activity("", GeneralConfig.PATH_ROOT);  //new activity without name yet, will be updated later
                p.pid_onserver = pid_onserver;
                p.invitation_status = DataConfig.ACTIVITY_INVITATION_STATUS_INCOMPLETE;
                int pid = dbManagerSoco.addActivity(p);
                Log.i(tag, "New project added to database, pid_onserver is " + pid_onserver);

                //retrieve project details
                String url = profile.getJoinProjectByInviteUrl(getApplicationContext());
                JoinActivityByInviteTaskAsync task = new JoinActivityByInviteTaskAsync(
                        url, String.valueOf(pid), pid_onserver,
                        getApplicationContext(), inviterEmail);
                task.execute();
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }

}