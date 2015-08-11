package com.soco.SoCoClient.v2.control.http;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.soco.SoCoClient.v2.control.config.DataConfig;
import com.soco.SoCoClient.v2.control.config.GeneralConfig;
import com.soco.SoCoClient.v2.control.config.HttpConfig;
import com.soco.SoCoClient.v2.control.http.task.JoinTaskByInviteJob;
import com.soco.SoCoClient.v2.control.http.task.ReceiveMessageJob;
import com.soco.SoCoClient.v2.control.util.TimeUtil;
import com.soco.SoCoClient.v2.model.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

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
                        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
                        String token = settings.getString(HttpConfig.PROFILE_LOGIN_ACCESS_TOKEN, "");
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
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig.PROFILE_FILENAME, 0);
        String ip = settings.getString(HttpConfig.PROFILE_SERVER_IP, "");
        String port = settings.getString(HttpConfig.PROFILE_SERVER_PORT, "");
        String token = settings.getString(HttpConfig.PROFILE_LOGIN_ACCESS_TOKEN, "");
        if(ip.isEmpty() || port.isEmpty() || token.isEmpty()) {
            Log.e(tag, "cannot load ip/port/token from shared preference");
            return "";
        }

        String path = HttpConfig.SERVER_PATH_HEARTBEAT;
        String url = "http://" + ip + ":" + port + path + "?"
                + com.soco.SoCoClient.obsolete.v1.control.config.HttpConfig.HTTP_TOKEN_TYPE + "=" + token;

        Log.v(tag, "get url: " + url);
        return url;
    }

    public boolean parse(Object response) {
        try{
            JSONObject data = new JSONObject(response.toString());
            String isSuccess = data.getString(HttpConfig.JSON_KEY_STATUS);
            if(isSuccess.equals(HttpConfig.JSON_VALUE_SUCCESS)){
                if(data.has(HttpConfig.JSON_KEY_MESSAGE)) {
                    Log.d(tag, "new message on server, start retrieve message job");
                    retrieveMessage();
                }
                if(data.has(HttpConfig.JSON_KEY_INVITATION)){
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
            JSONArray invitations = new JSONArray(data.getString(HttpConfig.JSON_KEY_INVITATION));
            for(int i=0; i<invitations.length(); i++){
                JSONObject invitation = invitations.getJSONObject(i);
                Log.d(tag, "get invitation: " + invitation);
                int taskIdServer = Integer.parseInt(invitation.getString(HttpConfig.JSON_KEY_ACTIVITY));
                String date = invitation.getString(HttpConfig.JSON_KEY_DATE);
                Task task = new Task(context);
                task.setTaskIdServer(taskIdServer);
                task.setTaskName(DataConfig.ENTITY_VALUE_EMPTY);
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
}
