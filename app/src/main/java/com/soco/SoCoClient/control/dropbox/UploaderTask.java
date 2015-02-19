package com.soco.SoCoClient.control.dropbox;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.util.FileUtils;

public class UploaderTask extends AsyncTask<Void, Void, Boolean> {

    static String tag = "UploaderTask";

//    Context context;
    DropboxAPI<AndroidAuthSession> dropboxApi;
    String sigEmail, sigProgram;
//    String remotePath;
    String remoteFilePath;
    Uri uri;
//    String filename;
    InputStream inputStream;
    ContentResolver cr;
    Context context;

    public UploaderTask(//Context context,
                        DropboxAPI<AndroidAuthSession> dropboxApi,
//                       String sigEmail, String sigProgram,
//                        String remotePath,
                        String remoteFilePath,
                        Uri uri,
//                        String filename,
                        InputStream is,
                        ContentResolver cr,
                        Context context) {

        reAuthenticateDropboxApi(dropboxApi);

//        this.context = context.getApplicationContext();
        this.dropboxApi = dropboxApi;
//        this.sigEmail = sigEmail;
//        this.sigProgram = sigProgram;
//        this.remotePath = "/" + sigEmail + "/" + sigProgram + "/";
//        this.remotePath = remotePath;
        this.remoteFilePath = remoteFilePath;
        this.uri = uri;
//        this.filename = filename;
        this.inputStream = is;
        this.cr = cr;
        this.context = context;
    }

    void reAuthenticateDropboxApi(DropboxAPI<AndroidAuthSession> dropbox){
        if (dropbox != null && dropbox.getSession() != null)
            Log.i(tag, "DropboxAPI and Session success with existing token");
        else if (dropbox.getSession().authenticationSuccessful()) {
            Log.i(tag, "Session authentication successful");
            try {
                Log.d(tag, "Session finish authentication");
                dropbox.getSession().finishAuthentication();
                Log.d(tag, "Token after reAuthentication: " +
                        dropbox.getSession().getOAuth2AccessToken());
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Log.e(tag, "Failed to authenticate dropboxApi api");
            }
        } else {
            Log.e(tag, "Session authentication failed");
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Long len = Long.valueOf(FileUtils.checkUriSize(cr, uri));
        try {
            Log.i(tag, "Dropbox put file to remote: " + remoteFilePath + ", "
                    + inputStream + ", " + len);
            DropboxAPI.Entry response = dropboxApi.putFile(
                    remoteFilePath, inputStream, len, null, null);
            Log.i(tag, "Dropbox put file status: " + response.toString());
            SocoApp app = (SocoApp) context;
            app.setUploadStatus(SocoApp.UPLOAD_STATUS_SUCCESS);
        } catch (Exception e1) {
            e1.printStackTrace();
//            Toast.makeText(context, "File Upload failed.", Toast.LENGTH_LONG).show();
            SocoApp app = (SocoApp) context;
            app.setUploadStatus(SocoApp.UPLOAD_STATUS_FAIL);
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
//        Toast.makeText(context, "File Uploaded Successfully.", Toast.LENGTH_LONG).show();
    }

}
