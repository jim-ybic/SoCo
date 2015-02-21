package com.soco.SoCoClient.control.dropbox;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.util.ProjectUtil;


public class UploaderWatcher extends IntentService {
    static String tag = "UploaderWatcher";

    Uri uri;
    int pid;
    String loginEmail, loginPassword;
    DBManagerSoco dbManagerSoco;

    public UploaderWatcher() {
        super("UploaderWatcher");
    }

//    public UploaderWatcher(String name) {
//        super(name);
//    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        Log.i(tag, "Status service bind");
//        return null;
//    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(tag, "Handle intent:" + intent);

        int count = 1;
        String status = ((SocoApp)getApplicationContext()).getUploadStatus();
        while(status.equals(SocoApp.UPLOAD_STATUS_START)) {
            Log.d(tag, "Wait for " + count*2 + "s");
            long endTime = System.currentTimeMillis() + 2*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                        Log.e(tag, "Error in waiting");
                    }
                }
            }
            count++;
            status = ((SocoApp)getApplicationContext()).getUploadStatus();
        }

        if(status.equals(SocoApp.UPLOAD_STATUS_SUCCESS)){
            Log.i(tag, "Status: upload success");
            Toast.makeText(this, "File upload success", Toast.LENGTH_SHORT).show();
            ProjectUtil.addSharedFileToDb(uri, loginEmail, loginPassword, pid,
                    getContentResolver(), dbManagerSoco);
        }
        else if(status.equals(SocoApp.UPLOAD_STATUS_FAIL)){
            Log.i(tag, "Status: upload fail");
            Toast.makeText(this, "File upload fail. Please check network status.",
                    Toast.LENGTH_SHORT).show();
        }
        else
            Log.e(tag, "Status: unknown upload status");
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(tag, "Status service on start command");
//        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
//        return super.onStartCommand(intent,flags,startId);
//    }

    @Override
    public void onCreate() {
        Log.d(tag, "Create uploader watcher");
        Toast.makeText(this, "File uploading in background...", Toast.LENGTH_LONG).show();

        uri = ((SocoApp)getApplicationContext()).uri;
        loginEmail = ((SocoApp)getApplicationContext()).loginEmail;
        loginPassword = ((SocoApp)getApplicationContext()).loginPassword;
        pid = ((SocoApp) getApplicationContext()).getPid();
        dbManagerSoco = ((SocoApp) getApplicationContext()).dbManagerSoco;

        super.onCreate();
    }

//    @Override
//    public void onStart(Intent intent, int startId) {
//        Log.d(tag, "start");
//        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
//    }

//    @Override
//    public void onDestroy() {
//        Log.i(tag, "Status service destroy");
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
//    }


}
