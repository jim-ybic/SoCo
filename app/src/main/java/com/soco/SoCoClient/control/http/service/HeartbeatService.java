package com.soco.SoCoClient.control.http.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.http.HttpUtil;
import com.soco.SoCoClient.control.util.ProfileUtil;
import com.soco.SoCoClient.control.util.SignatureUtil;

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