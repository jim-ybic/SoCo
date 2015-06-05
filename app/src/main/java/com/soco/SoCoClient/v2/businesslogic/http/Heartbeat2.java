package com.soco.SoCoClient.v2.businesslogic.http;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.soco.SoCoClient.v1.control.SocoApp;
import com.soco.SoCoClient.v1.control.config.HttpConfig;
import com.soco.SoCoClient.v2.businesslogic.config.GeneralConfig2;
import com.soco.SoCoClient.v2.businesslogic.config.HttpConfig2;
import com.soco.SoCoClient.v2.businesslogic.http.task.ReceiveMessageJob;
import com.soco.SoCoClient.v2.businesslogic.util.TimeUtil;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Heartbeat2 extends Service {

    String tag = "Heartbeat2";

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
        Log.v(tag, "Heartbeat2 onStartCommand");
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    public void run(){
                        Log.d(tag, ">>> Heartbeat:" + TimeUtil.now());
                        SharedPreferences settings = context.getSharedPreferences(GeneralConfig2.PROFILE_FILENAME, 0);
                        String token = settings.getString(HttpConfig2.PROFILE_LOGIN_ACCESS_TOKEN, "");
                        if (token == null || token.isEmpty())
                            Log.i(tag, "access token is not available, skip heart check");
                        else {
                            Object response = HttpUtil2.executeHttpPost(getUrl(), new JSONObject());
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
        SharedPreferences settings = context.getSharedPreferences(GeneralConfig2.PROFILE_FILENAME, 0);
        String ip = settings.getString(HttpConfig2.PROFILE_SERVER_IP, "");
        String port = settings.getString(HttpConfig2.PROFILE_SERVER_PORT, "");
        String token = settings.getString(HttpConfig2.PROFILE_LOGIN_ACCESS_TOKEN, "");
        if(ip.isEmpty() || port.isEmpty() || token.isEmpty()) {
            Log.e(tag, "cannot load ip/port/token from shared preference");
            return "";
        }

        String path = HttpConfig2.SERVER_PATH_HEARTBEAT;
        String url = "http://" + ip + ":" + port + path + "?"
                + HttpConfig.HTTP_TOKEN_TYPE + "=" + token;

        Log.v(tag, "get url: " + url);
        return url;
    }

    //todo
    public boolean parse(Object response) {

        try{
            JSONObject data = new JSONObject(response.toString());
            String isSuccess = data.getString(HttpConfig2.JSON_KEY_STATUS);
            if(isSuccess.equals(HttpConfig2.JSON_VALUE_SUCCESS)){
                if(data.has(HttpConfig2.JSON_KEY_MESSAGE))
                    retrieveMessage();

                //todo: other flags
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
}
