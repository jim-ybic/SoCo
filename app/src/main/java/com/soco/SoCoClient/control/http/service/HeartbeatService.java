package com.soco.SoCoClient.control.http.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.http.HttpUtil;
import com.soco.SoCoClient.control.http.task.JoinProjectByInviteTaskAsync;
import com.soco.SoCoClient.control.util.ProfileUtil;
import com.soco.SoCoClient.control.util.SignatureUtil;
import com.soco.SoCoClient.model.Project;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class HeartbeatService extends Service {

    public static final String tag = "HeartbeatService";

    Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(tag, "onCreate() executed");
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
                        Object response = request();
                        if (response != null)
                            parse(response);
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
        String url = ProfileUtil.getHeartbeatUrl(getApplicationContext());
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
                if(json.has(HttpConfig.JSON_KEY_INVITATION)) {
                    JSONArray invitationArray = new JSONArray(json.getString("invitation"));
                    Log.i(tag, "Invitation str: " + json.getString(HttpConfig.JSON_KEY_INVITATION));
                    for (int i = 0; i < invitationArray.length(); i++) {
                        JSONObject invitation = invitationArray.getJSONObject(i);
                        String inviter = invitation.getString("inviter");
                        String pid_onserver = invitation.getString("activity");
                        String date = invitation.getString("date");
                        Log.i(tag, "Get invitation: " + inviter + ", " + pid_onserver + ", " + date);

                        //add project into database
                        DBManagerSoco dbManagerSoco = ((SocoApp) getApplication()).dbManagerSoco;
                        Project p = new Project("");
                        p.pid_onserver = pid_onserver;
                        int pid = dbManagerSoco.addProject(p);
                        Log.i(tag, "New project added to database, pid_onserver is " + pid_onserver);

                        //retrieve project details
                        String url = ProfileUtil.getJoinProjectByInviteUrl(getApplicationContext());
                        JoinProjectByInviteTaskAsync task = new JoinProjectByInviteTaskAsync(
                                url, String.valueOf(pid), pid_onserver,
                                getApplicationContext(), inviter);
                        task.execute();
                    }
                }

                return true;
            }
            else {
                Log.e(tag, "Cannot receive parse from server");
                return false;
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

}