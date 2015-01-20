package com.soco.SoCoClient.model;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.soco.SoCoClient.control.util.FileUtils;

public class DropboxUploader extends AsyncTask<Void, Void, Boolean> {

    static String tag = "DropboxUploader";

    Context context;
    DropboxAPI<AndroidAuthSession> dropboxApi;
    String sigEmail, sigProgram;
    String remotePath;
    Uri uri;
    String filename;
    InputStream inputStream;
    ContentResolver cr;

    public DropboxUploader(Context context,
                           DropboxAPI<AndroidAuthSession> dropboxApi,
                           String sigEmail, String sigProgram,
                           Uri uri,
                           String filename,
                           InputStream is,
                           ContentResolver cr) {

        reAuthenticateDropboxApi(dropboxApi);

        this.context = context.getApplicationContext();
        this.dropboxApi = dropboxApi;
        this.sigEmail = sigEmail;
        this.sigProgram = sigProgram;
        this.remotePath = "/" + sigEmail + "/" + sigProgram + "/";
        this.uri = uri;
        this.filename = filename;
        this.inputStream = is;
        this.cr = cr;
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
            Log.i(tag, "Dropbox putfile: " + remotePath + ", " + filename + ", "
                    + inputStream + ", " + len);
            DropboxAPI.Entry response = dropboxApi.putFile(
                    remotePath + filename, inputStream, len, null, null);
            Log.i(tag, "Dropbox put file status: " + response.toString());
        } catch (Exception e1) {
            e1.printStackTrace();
            Toast.makeText(context, "File Upload failed.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Toast.makeText(context, "File Uploaded Successfully.", Toast.LENGTH_LONG).show();
    }

}
