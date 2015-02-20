package com.soco.SoCoClient.control.dropbox;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.util.FileUtils;
import com.soco.SoCoClient.control.util.SignatureUtil;
import com.soco.SoCoClient.view.ShowMoreActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class DropboxUtil {

    static String tag = "DropboxUtil";

    public static void downloadFromDropbox(Uri uri,
                                       String loginEmail, String loginPassword, int pid,
                                       DropboxAPI<AndroidAuthSession> dropboxApi,
                                       ContentResolver cr,
                                       Context context){
        Log.i(tag, "Download from dropboxApi uri: " + uri);

        InputStream is = null;
        try {
            is = cr.openInputStream(uri);
            Log.d(tag, "Input stream created: " + is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String remoteFilePath = getRemotePath(uri, loginEmail, loginPassword, pid, cr);
        Log.d(tag, "Remote file path is: " + remoteFilePath);

        DownloaderTask dropboxTask = new DownloaderTask(
                dropboxApi, remoteFilePath, uri, is, null, cr, context);
        dropboxTask.execute();

//        TextView tv_file_log = (TextView) showMoreActivity.findViewById(R.id.tv_file_log);
//        tv_file_log.append("File upload success: "
//                + FileUtils.getDisplayName(showMoreActivity.getContentResolver(), uri) + "\n");
    }


    public static void uploadToDropbox(Uri uri,
                                       String loginEmail, String loginPassword, int pid,
                                       DropboxAPI<AndroidAuthSession> dropboxApi,
                                       ContentResolver cr,
                                       Context context){
        Log.i(tag, "Upload to dropboxApi uri: " + uri);

        InputStream is = null;
        try {
            is = cr.openInputStream(uri);
            Log.d(tag, "Input stream created: " + is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String remoteFilePath = getRemotePath(uri, loginEmail, loginPassword, pid, cr);
        Log.d(tag, "Remote file path is: " + remoteFilePath);

        UploaderTask dropboxTask = new UploaderTask(
                dropboxApi, remoteFilePath, uri, is, cr, context);
        dropboxTask.execute();

//        TextView tv_file_log = (TextView) showMoreActivity.findViewById(R.id.tv_file_log);
//        tv_file_log.append("File upload success: "
//                + FileUtils.getDisplayName(showMoreActivity.getContentResolver(), uri) + "\n");
    }

    public static String getRemotePath(Uri uri, String loginEmail, String loginPassword,
                                       int pid, ContentResolver cr) {
        String sigEmail = SignatureUtil.genSHA1(loginEmail, loginPassword);
        String sigProgram = SignatureUtil.genSHA1(String.valueOf(pid), loginPassword);
        String filename = FileUtils.getDisplayName(cr, uri);
        return "/" + sigEmail + "/" + sigProgram + "/" + filename;
    }
}
