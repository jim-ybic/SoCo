package com.soco.SoCoClient.control.dropbox;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.soco.SoCoClient.control.config._ref.GeneralConfigV1;
import com.soco.SoCoClient.control.config.SocoApp;
import com.soco.SoCoClient.control.database._ref.DBManagerSoco;
import com.soco.SoCoClient.control.http.task._ref.AddFileToActivityTaskAsync;
import com.soco.SoCoClient.control.dropbox._ref.DropboxUtilV1;
import com.soco.SoCoClient.control.util.ActivityUtil;
import com.soco.SoCoClient.control.util.FileUtils;
import com.soco.SoCoClient.control.http.UrlUtil;
import com.soco.SoCoClient.model.Profile;


public class UploaderWatcher extends IntentService {
    static String tag = "UploaderWatcher";

    Uri uri;
    int pid, pid_onserver;
    String loginEmail, loginPassword;

    SocoApp socoApp;
    DBManagerSoco dbManagerSoco;
    Profile profile;
    ContentResolver cr;

    public UploaderWatcher() {
        super("UploaderWatcher");
    }

    @Override
    public void onCreate() {
        Log.d(tag, "Create uploader watcher");
        Toast.makeText(this, "File uploading in background...", Toast.LENGTH_LONG).show();

        socoApp = (SocoApp) getApplicationContext();
        dbManagerSoco = socoApp.dbManagerSoco;
        profile = socoApp.profile;

        uri = socoApp.uri;
        loginEmail = socoApp.loginEmail;
        loginPassword = socoApp.loginPassword;
        pid = socoApp.pid;
        pid_onserver = socoApp.pid_onserver;
        cr = socoApp.cr;

        super.onCreate();
    }


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
            Log.i(tag, "Status: upload success, save details to database");
            ActivityUtil.addSharedFileToDb(uri, loginEmail, loginPassword, pid,
                    getContentResolver(), dbManagerSoco);
            Toast.makeText(this, "File upload success", Toast.LENGTH_SHORT).show();

            Log.d(tag, "send resource details to server");
            String url = UrlUtil.getAddFileToActivityUrl(getApplicationContext());
            String displayName = FileUtils.getDisplayName(cr, uri);
            String remotePath = DropboxUtilV1.getRemotePath(uri,
                    loginEmail, loginPassword, pid, cr);
            String localPath = GeneralConfigV1.TEST_LOCAL_PATH;
            String user = GeneralConfigV1.TEST_USER;
            AddFileToActivityTaskAsync task = new AddFileToActivityTaskAsync(
                    url, pid_onserver, displayName, uri.toString(), remotePath, localPath, user);
            task.execute();

        }
        else if(status.equals(SocoApp.UPLOAD_STATUS_FAIL)){
            Log.i(tag, "Status: upload fail");
            Toast.makeText(this, "File upload fail. Please check network status.",
                    Toast.LENGTH_SHORT).show();
        }
        else
            Log.e(tag, "Status: unknown upload status");
    }



}
