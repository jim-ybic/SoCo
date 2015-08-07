package com.soco.SoCoClient.obsolete.v1.control.dropbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.soco.SoCoClient.obsolete.v1.control.SocoApp;
import com.soco.SoCoClient.obsolete.v1.control.util.FileUtils;

public class DownloaderTask extends AsyncTask<Void, Void, Boolean> {

    static String tag = "DownloaderTask";

    DropboxAPI<AndroidAuthSession> dropboxApi;
    String remoteFilePath;
    Uri uri;
    InputStream inputStream;
    OutputStream outputStream;
    ContentResolver cr;
    Context context;

    public DownloaderTask(
                        DropboxAPI<AndroidAuthSession> dropboxApi,
                        String remoteFilePath,
                        Uri uri,
                        InputStream is,
                        OutputStream os,
                        ContentResolver cr,
                        Context context) {

        reAuthenticateDropboxApi(dropboxApi);

        this.dropboxApi = dropboxApi;
        this.remoteFilePath = remoteFilePath;
        this.uri = uri;
        this.inputStream = is;
        this.outputStream = os;
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
//            Log.i(tag, "Dropbox put file to remote: " + remoteFilePath + ", "
//                    + inputStream + ", " + len);
//            DropboxAPI.Entry parse = dropboxApi.putFile(
//                    remoteFilePath, inputStream, len, null, null);
            Log.d(tag, "Test read dropbox file");

            File file = new File(context.getExternalFilesDir(null), "1.jpg");
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                Log.e(tag, "Cannot create file: " + e.toString());
                e.printStackTrace();
            }
            Log.d(tag, "Local file created");

            DropboxAPI.DropboxFileInfo info = null;
            try {
                //todo: testing, download function not ready yet
                Log.d(tag, "Start to get file from dropbox");
                info = dropboxApi.getFile("/c1025defa913032715d4aac356ebd44f8eab30c4" +
                        "/c2b605fb03833b5d739373b28d43d68c493f75c5" +
                        "/IMG-20150219-WA0001.jpg", null, outputStream, null);
                Log.i(tag, "Get file parse: " + info);
            } catch (DropboxException e) {
                Log.e(tag, "Cannot find file on dropbox: " + e.toString());
                e.printStackTrace();
            }

            MimeTypeMap map = MimeTypeMap.getSingleton();
            String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
            String type = map.getMimeTypeFromExtension(ext);

            if (type == null)
                type = "*/*";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.fromFile(file);

            Log.i(tag, "Downloaded file data and type: " + data + ", " + type);
            ((SocoApp) context).setDropboxDownloadUri(data);
            ((SocoApp) context).setDropboxDownloadType(type);

            intent.setDataAndType(data, type);

            SocoApp app = (SocoApp) context;
            app.setUploadStatus(SocoApp.UPLOAD_STATUS_SUCCESS);
        } catch (Exception e1) {
            e1.printStackTrace();
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
