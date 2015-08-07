package com.soco.SoCoClient.obsolete.v1.control.dropbox;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.soco.SoCoClient.obsolete.v1.control.util.FileUtils;
import com.soco.SoCoClient.obsolete.v1.control.util.SignatureUtil;

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
    }

    public static String getRemotePath(Uri uri, String loginEmail, String loginPassword,
                                       int pid, ContentResolver cr) {
        String sigEmail = SignatureUtil.genSHA1(loginEmail, loginPassword);
        String sigProgram = SignatureUtil.genSHA1(String.valueOf(pid), loginPassword);
        String filename = FileUtils.getDisplayName(cr, uri);
        return "/" + sigEmail + "/" + sigProgram + "/" + filename;
    }

    public static DropboxAPI<AndroidAuthSession> initDropboxApiAuthentication(
                                       String ACCESS_KEY, String ACCESS_SECRET,
                                       String OA2token,
                                       Context context){

       Log.d(tag, "initDropboxApiAuthentication: start");

       Log.v(tag, "Step 1: Create appKeyPair from Key/Secret: "
               + ACCESS_KEY + "/" + ACCESS_SECRET);
       AppKeyPair appKeyPair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);
       AccessTokenPair accessTokenPair = new AccessTokenPair(ACCESS_KEY, ACCESS_SECRET);

       Log.v(tag, "Step 2: Create session with appKeyPair: " + appKeyPair
               + ", AccessType: " + Session.AccessType.APP_FOLDER
               + ", accessTokenPair: " + accessTokenPair);
       AndroidAuthSession session = new AndroidAuthSession(
                                       appKeyPair, Session.AccessType.APP_FOLDER);

       Log.v(tag, "Step 3: Create DropboxAPI from session: " + session);
       DropboxAPI<AndroidAuthSession> dropboxApi = new DropboxAPI<AndroidAuthSession>(session);

       boolean useSoCoDropboxAccount = true;
       if (useSoCoDropboxAccount) {
           Log.v(tag, "Step 4 (approach a): Load SoCo's dropboxApi account and OA2 token");
           Log.v(tag, "Set DropboxAPI OA2 token: " + OA2token);
           dropboxApi.getSession().setOAuth2AccessToken(OA2token);
       } else {
           Log.v(tag, "Step 4 (approach b): Let user login");
       }

       Log.v(tag, "Validate DropboxAPI and Session");
       if (dropboxApi != null && dropboxApi.getSession() != null
               && dropboxApi.getSession().getOAuth2AccessToken() != null) {
           Log.v(tag, "Validation success, token: "
                   + dropboxApi.getSession().getOAuth2AccessToken());
       }
       else {
           Log.v(tag, "Session authentication failed, create new OA2 validation session");
           dropboxApi.getSession().startOAuth2Authentication(context);
       }

       return dropboxApi;
   }
}
