package com.soco.SoCoClient.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URI;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.soco.SoCoClient.control.Config;

public class DropboxUploader extends AsyncTask<Void, Void, Boolean> {

    private DropboxAPI<AndroidAuthSession> dropbox;
    private String remotePath;
    private Context context;

    public String key, secret;
    public AccessTokenPair accessTokenPair;
    public String sigEmail, sigProgram;
    public String localPath;

    public URI uri;
    public String filename = "unnamed.file";
    public InputStream inputStream;
    public FileInputStream fileInputStream;

    public DropboxUploader(Context context, DropboxAPI<AndroidAuthSession> dropbox,
                           String path) {
        this.context = context.getApplicationContext();
        this.dropbox = dropbox;
        this.remotePath = path;

        reAuthenticateDropboxApi();
    }

    void reAuthenticateDropboxApi(){
        Log.i("upload", "Validate session authentication ");

        if (dropbox != null && dropbox.getSession() != null) {
            Log.i("dropbox", "DropboxAPI and Session success with existing token. "
                    + "No OA2 authentication executed");
            return;
        }

//        AndroidAuthSession session = dropbox.getSession();
        // TODO: if validation fail, need to start OA2 authentication before checking result
        if (dropbox.getSession().authenticationSuccessful()) {
            Log.i("upload", "Session authentication successful");
            try {
                Log.i("upload", "Session finish authentication");
                dropbox.getSession().finishAuthentication();

//                TokenPair accessToken = dropbox.getSession().getAccessTokenPair();
                Log.i("dropbox", "Token after reAuthentication: " +
                        dropbox.getSession().getOAuth2AccessToken());
//                TokenPair tokens = session.getAccessTokenPair();
//                SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
//                Editor editor = prefs.edit();
//                editor.putString(ACCESS_KEY, tokens.key);
//                editor.putString(ACCESS_SECRET, tokens.secret);
//                editor.commit();
//                if (accessToken != null) {
//                    key = accessToken.key;
//                    secret = accessToken.secret;
//                }
//                loggedIn(true);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("upload", "Session authentication failed");
        }

    }


    @Override
    protected Boolean doInBackground(Void... params) {
//        final File tempDir = context.getCacheDir();
//        File tempFile;
//        FileWriter fr;
//        try {
//            tempFile = File.createTempFile("file", ".txt", tempDir);
//            fr = new FileWriter(tempFile);
//            fr.write("Test file uploaded using Dropbox API for Android");
//            fr.close();

        Log.i("upload", "Upload in background");
        File file = new File("test.txt");
        try {
            if (!file.exists()) {
                file = new File(localPath, "test.txt");
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file), Config.ENCODING));
                writer.write(Profile.PROFILE_EMAIL + ":" + "jim.ybic@gmail.com");
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String loginEmail = "jim.ybic@gmail.com";
//        Log.i("hash", "Hash code of, " + loginEmail + ", "
//                + ShowSingleProgramActivity.hash(loginEmail));
//        String program = "Dinner w Jenny";
//        Log.i("hash", "Hash code of, " + program + ", "
//                + ShowSingleProgramActivity.hash(program));

//        String remotePath = "/" + ShowSingleProgramActivity.hash(loginEmail)
//                + "/" + ShowSingleProgramActivity.hash(program) + "/";
        Log.i("hash", "Remote file remotePath: " + remotePath);

//        Log.i("upload", "Begin to putfile");
        try {
//                FileInputStream fileInputStream = new FileInputStream(file);
//                FileInputStream fileInputStream = new FileInputStream(inputStream);
                Log.i("upload", "Begin to put file: " + filename);
                DropboxAPI.Entry response = dropbox.putFile(
                        remotePath + filename, inputStream,
                    file.length(), null, null);
                Log.i("upload", "Put file status: " + response.toString());
        } catch (Exception e1) {
            e1.printStackTrace();
            Toast.makeText(context, "File Upload failed.", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
//        if (result) {
            Toast.makeText(context, "File Uploaded Successfully.", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(context, "Failed to upload file", Toast.LENGTH_LONG)
//                    .show();
//        }
    }
}
