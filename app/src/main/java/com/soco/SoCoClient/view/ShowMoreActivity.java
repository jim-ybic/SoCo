package com.soco.SoCoClient.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.Config;
import com.soco.SoCoClient.control.http.HttpTask;
import com.soco.SoCoClient.control.util.FileUtils;
import com.soco.SoCoClient.control.util.SignatureUtil;
import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.model.DropboxUploader;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ShowMoreActivity extends ActionBarActivity {

    static int READ_REQUEST_CODE = 42;
    static int REQUEST_TAKE_PHOTO = 100;
    public static String tag = "ShowMore";

    DropboxAPI<AndroidAuthSession> dropbox;
    String ACCESS_KEY = "7cfm4ur90xw54pv";
    String ACCESS_SECRET = "9rou23wi8t4htkz";
    AppKeyPair appKeyPair;
    AccessTokenPair accessTokenPair;

    String loginEmail, loginPassword, programName;
    String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more);

        Log.i(tag, "onCreate, original values: " +
                loginEmail + ", " + loginPassword + ", " + programName);

        if (loginEmail == null) {
            Intent intent = getIntent();
            loginEmail = intent.getStringExtra(Config.LOGIN_EMAIL);
            loginPassword = intent.getStringExtra(Config.LOGIN_PASSWORD);
            programName = intent.getStringExtra(Config.PROGRAM_PNAME);
            Log.i(tag, "ShowMoreActivity get extra: " +
                    loginEmail + ", " + loginPassword + ", " + programName);
        }

        initDropboxApiAuthentication();
    }

    public void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void takePicture(View view){
        Log.i(tag, "Start activity: take picture");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    void gotoPreviousScreen() {
        Intent intent = new Intent();
        intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
        intent.putExtra(Config.LOGIN_PASSWORD, loginPassword);
        intent.putExtra(Config.PROGRAM_PNAME, programName);
        Log.i(tag, "gotoPreviousScreen, put extra: "
                + Config.LOGIN_EMAIL + ":" + loginEmail + ", "
                + Config.LOGIN_PASSWORD + ":" + loginPassword + ", "
                + Config.PROGRAM_PNAME + ":" + programName);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(tag, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(tag, "Menu click: home.");
                gotoPreviousScreen();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Log.i(tag, "onActivityResult - request code: " + requestCode
                + ", result code: " + resultCode
                + ", result data: " + resultData);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(tag, "File selected with uri: " + uri.toString());
                FileUtils.checkUriMeta(getContentResolver(), uri);
                uploadToDropbox(uri);
            }
        }
        else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            //TEST
            SocoApp socoApp = (SocoApp) getApplicationContext();
            if (resultData != null){
                Uri uri = resultData.getData();
                Log.d(tag, "Photo uri: " + uri);
                FileUtils.checkUriMeta(getContentResolver(), uri);
                uploadToDropbox(uri);
            }
        }
    }

    void uploadToDropbox(Uri uri){
        Log.i(tag, "Upload to dropbox uri: " + uri);

        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            Log.d(tag, "Input stream created: " + is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String sigEmail = SignatureUtil.genSHA1(loginEmail, loginPassword);
        String sigProgram = SignatureUtil.genSHA1(programName, loginPassword);

        DropboxUploader upload = new DropboxUploader(
                this, dropbox, sigEmail, sigProgram,
                uri, FileUtils.getDisplayName(getContentResolver(), uri),
                is, getContentResolver());

        upload.execute();

        TextView tv_file_log = (TextView) findViewById(R.id.tv_file_log);
        tv_file_log.append("File upload success: "
                + FileUtils.getDisplayName(getContentResolver(), uri) + "\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_more, menu);
        return true;
    }

    void initDropboxApiAuthentication(){
        AndroidAuthSession session;

        Log.d(tag, "Step 1: Create appKeyPair from Key/Secret: "
                + ACCESS_KEY + "/" + ACCESS_SECRET);
        appKeyPair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);
        accessTokenPair = new AccessTokenPair(ACCESS_KEY, ACCESS_SECRET);

        Log.d(tag, "Step 2: Create session with appKeyPair: " + appKeyPair
                + ", AccessType: " + Session.AccessType.APP_FOLDER
                + ", accessTokenPair: " + accessTokenPair);
        session = new AndroidAuthSession(appKeyPair, Session.AccessType.APP_FOLDER);

        Log.d(tag, "Step 3: Create DropboxAPI from session: " + session);
        dropbox = new DropboxAPI<AndroidAuthSession>(session);

        boolean useSoCoDropboxAccount = true;
        //TODO: choose if use SoCo's dropbox account or user's own dropbox account

        if (useSoCoDropboxAccount) {
            Log.d(tag, "Step 4 (approach a): Load SoCo's dropbox account and OA2 token");
            String OA2token = "JWWNa2LgL2UAAAAAAAAANNpl6wfgG5wTX6_OrNik5a_yKGsnySogfHYMK-uxjLJd";
            Log.d(tag, "Set DropboxAPI OA2 token: " + OA2token);
            dropbox.getSession().setOAuth2AccessToken(OA2token);
        } else {
            Log.d(tag, "Step 4 (approach b): Let user login");
        }

        Log.d(tag, "Validate DropboxAPI and Session");
        if (dropbox != null && dropbox.getSession() != null
                && dropbox.getSession().getOAuth2AccessToken() != null)
            Log.i(tag, "Validation success, token: " + dropbox.getSession().getOAuth2AccessToken());
        else {
            Log.i(tag, "Session authentication failed, create new OA2 validation session");
            dropbox.getSession().startOAuth2Authentication(ShowMoreActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(tag, "ShowSingleProgramActivity:OnResume, check if OA2 authentication success");
        Log.i(tag, "onResume, Session token: " + dropbox.getSession().getOAuth2AccessToken());

        if (dropbox != null && dropbox.getSession() != null
                && dropbox.getSession().getOAuth2AccessToken() != null) {
            Log.d(tag, "DropboxAPI and Session created with existing token: "
                    + dropbox.getSession().getOAuth2AccessToken());
            return;
        }

        Log.d(tag, "Check OA2 authentication result");
        if (dropbox.getSession().authenticationSuccessful()) {
            Log.d(tag, "Dropbox OA2 authentication success");
            try {
                Log.d(tag, "Session finish authentication, set OA2 token");
                dropbox.getSession().finishAuthentication();
                Log.d(tag, "Session finish authentication complete with token: "
                        + dropbox.getSession().getOAuth2AccessToken());
            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error during Dropbox authentication",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(tag, "Dropbox OA2 authentication failed (possibly timing issue)");
        }
    }

    public void httpGet(View view){
//        String url = "http://192.168.0.101:8888/android.png";
        //String url = "http://google.com";
//        String url = "http://192.168.43.240:8080/SocoServer/echo";
//        HttpTask getTest = new HttpTask(url, HttpTask.HTTP_TYPE_LOGIN,
//                "jim.ybic@gmail.com", "Pass@123");
//        getTest.execute();
    }



}
