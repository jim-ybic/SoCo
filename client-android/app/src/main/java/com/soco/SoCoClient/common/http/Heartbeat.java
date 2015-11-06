package com.soco.SoCoClient.common.http;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.profile.Config;
import com.soco.SoCoClient.common.http.task.JoinTaskByInviteJob;
import com.soco.SoCoClient.common.http.task.ReceiveMessageJob;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.common.model.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

@Deprecated
public class Heartbeat extends Service {

    String tag = "Heartbeat";

    Context context;

    public void onCreate() {
        super.onCreate();
        Log.v(tag, "onCreate");

        context = getApplicationContext();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(tag, "Heartbeat onStartCommand");
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    public void run(){
                        Log.d(tag, ">>> Heartbeat:" + TimeUtil.now());
                        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
                        String token = settings.getString(com.soco.SoCoClient.common.http.Config.PROFILE_LOGIN_ACCESS_TOKEN, "");
                        if (token == null || token.isEmpty())
                            Log.i(tag, "access token is not available, skip heart check");
                        else {
                            Object response = HttpUtil.executeHttpPost(getUrl(), new JSONObject());
                            if (response != null)
                                parse(response);
                        }
                    }
                },
                0,          //delay start time (ms)
                5*1000     //frequency (ms)
        );
        return super.onStartCommand(intent, flags, startId);
    }

    String getUrl(){
        SharedPreferences settings = context.getSharedPreferences(Config.PROFILE_FILENAME, 0);
        String ip = settings.getString(com.soco.SoCoClient.common.http.Config.PROFILE_SERVER_IP, "");
        String port = settings.getString(com.soco.SoCoClient.common.http.Config.PROFILE_SERVER_PORT, "");
        String token = settings.getString(com.soco.SoCoClient.common.http.Config.PROFILE_LOGIN_ACCESS_TOKEN, "");
        if(ip.isEmpty() || port.isEmpty() || token.isEmpty()) {
            Log.e(tag, "cannot load ip/port/token from shared preference");
            return "";
        }

        String path = com.soco.SoCoClient.common.http.Config.SERVER_PATH_HEARTBEAT;
        String url = "http://" + ip + ":" + port + path + "?"
                + HttpConfigV1.HTTP_TOKEN_TYPE + "=" + token;

        Log.v(tag, "get url: " + url);
        return url;
    }

    public boolean parse(Object response) {
        try{
            JSONObject data = new JSONObject(response.toString());
            String isSuccess = data.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_STATUS);

            if(isSuccess.equals(com.soco.SoCoClient.common.http.Config.JSON_VALUE_SUCCESS)){
                if(data.has(com.soco.SoCoClient.common.http.Config.JSON_KEY_MESSAGE)) {
                    Log.d(tag, "new message on server, start retrieve message job");
                    retrieveMessage();
                }
                if(data.has(com.soco.SoCoClient.common.http.Config.JSON_KEY_INVITATION)){
                    Log.d(tag, "new invitation on server, start join task job");
                    joinTask(data);
                }

                //todo: handle other flags
            }
        }catch (Exception e){
            Log.e(tag, "cannot parse json data:" + e);
            e.printStackTrace();
            return false;
        }

        return true;
    }

    void retrieveMessage(){
        ReceiveMessageJob job = new ReceiveMessageJob(context);
        job.execute();
    }

    void joinTask(JSONObject data){
        Log.v(tag, "join task via response data: " + data);
        try{
            JSONArray invitations = new JSONArray(data.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_INVITATION));
            for(int i=0; i<invitations.length(); i++){
                JSONObject invitation = invitations.getJSONObject(i);
                Log.d(tag, "get invitation: " + invitation);
                int taskIdServer = Integer.parseInt(invitation.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_ACTIVITY));
                String date = invitation.getString(com.soco.SoCoClient.common.http.Config.JSON_KEY_DATE);
                Task task = new Task(context);
                task.setTaskIdServer(taskIdServer);
                task.setTaskName(com.soco.SoCoClient.common.database.Config.ENTITY_VALUE_EMPTY);
                task.save();
                Log.d(tag, "saved new task into database: " + task.toString());

                Log.v(tag, "get task details via JoinTaskByInviteJob");
                JoinTaskByInviteJob job = new JoinTaskByInviteJob(context, task);
                job.execute();
            }

        }catch (Exception e){
            Log.e(tag, "cannot parse json data: " + e);
            e.printStackTrace();
        }
    }


// //todo
//    private boolean getActivityEvent(JSONObject json) {
//        try {
//            String status = json.getString(HttpConfigV1.JSON_KEY_ACTIVITY_EVENT);
//            Log.i(tag, "new activity event status: " + status + " (must be true)");
//
//            String url = UrlUtil.getGetActivityEventUrl(getApplicationContext());
//            GetActivityEventTaskAsync task = new GetActivityEventTaskAsync(
//                    url, getApplicationContext());
//            task.execute();
//        } catch (Exception e) {
//            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
//
//    private boolean retrieveMessage(JSONObject json) {
//        try {
//            String status = json.getString(HttpConfigV1.JSON_KEY_MESSAGE);
//            Log.i(tag, "new message status: " + status);
//            //retrieve message
//            String url = UrlUtil.getRetrieveMessageUrl(getApplicationContext());
//            RetrieveMessageTaskAsync task = new RetrieveMessageTaskAsync(
//                    url, getApplicationContext());
//            task.execute();
//        } catch (Exception e) {
//            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
//
//    /*
//    Sample message:
//        "invitation"":"[
//            {inviter:test@test.com,
//            activity:1,
//            date:2015-05-05 11:1:11},
//            {another invitation}
//        ]"}
//     */
//    private boolean joinActivititiesByInvite(JSONObject json) {
//        try {
//            Log.i(tag, "Invitation str: " + json.getString(com.soco.SoCoClient.control.config.ref.HttpConfigV1.JSON_KEY_INVITATION));
//            JSONArray invitationArray = new JSONArray(
//                    json.getString(com.soco.SoCoClient.control.config.ref.HttpConfigV1.JSON_KEY_INVITATION));
//
//            //process each activity invitation
//            for (int i = 0; i < invitationArray.length(); i++) {
//                JSONObject invitation = invitationArray.getJSONObject(i);
//                String inviterEmail = invitation.getString(com.soco.SoCoClient.control.config.ref.HttpConfigV1.JSON_KEY_INVITER);
//                String pid_onserver = invitation.getString(com.soco.SoCoClient.control.config.ref.HttpConfigV1.JSON_KEY_PROJECT_ID);
//                String date = invitation.getString(com.soco.SoCoClient.control.config.ref.HttpConfigV1.JSON_KEY_DATE);
//                Log.i(tag, "Get invitation: " + inviterEmail + ", " + pid_onserver + ", " + date);
//
//                //add project into database
//                Activity p = new Activity("", com.soco.SoCoClient.control.config.ref.GeneralConfigV1.PATH_ROOT);  //new activity without name yet, will be updated later
//                p.pid_onserver = pid_onserver;
//                p.invitation_status = DataConfigV1.ACTIVITY_INVITATION_STATUS_INCOMPLETE;
//                int pid = dbManagerSoco.addActivity(p);
//                Log.i(tag, "New project added to database, pid_onserver is " + pid_onserver);
//
//                //retrieve project details
//                String url = UrlUtil.getJoinProjectByInviteUrl(getApplicationContext());
//                JoinActivityByInviteTaskAsync task = new JoinActivityByInviteTaskAsync(
//                        url, String.valueOf(pid), pid_onserver,
//                        getApplicationContext(), inviterEmail);
//                task.execute();
//            }
//        } catch (Exception e) {
//            Log.e(tag, "Cannot convert parse to Json object: " + e.toString());
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }


}
