package com.soco.SoCoClient.zz;

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
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.config.GeneralConfig;
import com.soco.SoCoClient.control.dropbox.DropboxUtil;
import com.soco.SoCoClient.control.util.FileUtils;
import com.soco.SoCoClient.control.SocoApp;

public class ShowMoreActivity extends ActionBarActivity {

    public static String tag = "ShowMore";

    DropboxAPI<AndroidAuthSession> dropboxApi;
    String ACCESS_KEY = "7cfm4ur90xw54pv";
    String ACCESS_SECRET = "9rou23wi8t4htkz";
//    AppKeyPair appKeyPair;
//    AccessTokenPair accessTokenPair;

    String loginEmail, loginPassword;
    int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more);

        Log.i(tag, "onCreate, original values: " +
                loginEmail + ", " + loginPassword + ", " + pid);

        if (loginEmail == null) {
            Intent intent = getIntent();
            loginEmail = intent.getStringExtra(GeneralConfig.LOGIN_EMAIL);
            loginPassword = intent.getStringExtra(GeneralConfig.LOGIN_PASSWORD);
            pid = intent.getIntExtra(GeneralConfig.PROJECT_PID, -1);
            Log.i(tag, "ShowMoreActivity get extra: " +
                    loginEmail + ", " + loginPassword + ", " + pid);
        }

        dropboxApi = initDropboxApiAuthentication();
    }

    public void openFile(View view) {
        Log.d(tag, "Start open file");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, GeneralConfig.READ_REQUEST_CODE);
    }

    public void takePicture(View view){
        Log.i(tag, "Start activity: take picture");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent, GeneralConfig.ACTIVITY_TAKE_PHOTO);
    }

    void gotoPreviousScreen() {
        Intent intent = new Intent();
        intent.putExtra(GeneralConfig.LOGIN_EMAIL, loginEmail);
        intent.putExtra(GeneralConfig.LOGIN_PASSWORD, loginPassword);
        intent.putExtra(GeneralConfig.PROJECT_PID, pid);
        Log.i(tag, "gotoPreviousScreen, put extra: "
                + GeneralConfig.LOGIN_EMAIL + ":" + loginEmail + ", "
                + GeneralConfig.LOGIN_PASSWORD + ":" + loginPassword + ", "
                + GeneralConfig.PROJECT_PID + ":" + pid);

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

        if (requestCode == GeneralConfig.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(tag, "File selected with uri: " + uri.toString());
                FileUtils.checkUriMeta(getContentResolver(), uri);
                DropboxUtil.uploadToDropbox(
                        uri, loginEmail, loginPassword, pid, dropboxApi,
                        getContentResolver(), getApplicationContext());
            }
        }
        else if (requestCode == GeneralConfig.ACTIVITY_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            SocoApp socoApp = (SocoApp) getApplicationContext();
            if (resultData != null){
                Uri uri = resultData.getData();
                Log.d(tag, "Photo uri: " + uri);
                FileUtils.checkUriMeta(getContentResolver(), uri);
                DropboxUtil.uploadToDropbox(uri, loginEmail, loginPassword, pid, dropboxApi,
                        getContentResolver(), getApplicationContext());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_more, menu);
        return true;
    }

    DropboxAPI<AndroidAuthSession> initDropboxApiAuthentication(){
        AndroidAuthSession session;

        Log.d(tag, "Step 1: Create appKeyPair from Key/Secret: "
                + ACCESS_KEY + "/" + ACCESS_SECRET);
        AppKeyPair appKeyPair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);
        AccessTokenPair accessTokenPair = new AccessTokenPair(ACCESS_KEY, ACCESS_SECRET);

        Log.d(tag, "Step 2: Create session with appKeyPair: " + appKeyPair
                + ", AccessType: " + Session.AccessType.APP_FOLDER
                + ", accessTokenPair: " + accessTokenPair);
        session = new AndroidAuthSession(appKeyPair, Session.AccessType.APP_FOLDER);

        Log.d(tag, "Step 3: Create DropboxAPI from session: " + session);
        DropboxAPI<AndroidAuthSession> dropboxApi = new DropboxAPI<AndroidAuthSession>(session);

        boolean useSoCoDropboxAccount = true;
        //TODO: choose if use SoCo's dropboxApi account or user's own dropboxApi account

        if (useSoCoDropboxAccount) {
            Log.d(tag, "Step 4 (approach a): Load SoCo's dropboxApi account and OA2 token");
            String OA2token = "JWWNa2LgL2UAAAAAAAAANNpl6wfgG5wTX6_OrNik5a_yKGsnySogfHYMK-uxjLJd";
            Log.d(tag, "Set DropboxAPI OA2 token: " + OA2token);
            dropboxApi.getSession().setOAuth2AccessToken(OA2token);
        } else {
            Log.d(tag, "Step 4 (approach b): Let user login");
        }

        Log.d(tag, "Validate DropboxAPI and Session");
        if (dropboxApi != null && dropboxApi.getSession() != null
                && dropboxApi.getSession().getOAuth2AccessToken() != null)
            Log.i(tag, "Validation success, token: " + dropboxApi.getSession().getOAuth2AccessToken());
        else {
            Log.i(tag, "Session authentication failed, create new OA2 validation session");
            dropboxApi.getSession().startOAuth2Authentication(ShowMoreActivity.this);
        }

        return dropboxApi;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(tag, "ShowSingleProgramActivity:OnResume, check if OA2 authentication success");
        Log.i(tag, "onResume, Session token: " + dropboxApi.getSession().getOAuth2AccessToken());

        if (dropboxApi != null && dropboxApi.getSession() != null
                && dropboxApi.getSession().getOAuth2AccessToken() != null) {
            Log.d(tag, "DropboxAPI and Session created with existing token: "
                    + dropboxApi.getSession().getOAuth2AccessToken());
            return;
        }

        Log.d(tag, "Check OA2 authentication result");
        if (dropboxApi.getSession().authenticationSuccessful()) {
            Log.d(tag, "Dropbox OA2 authentication success");
            try {
                Log.d(tag, "Session finish authentication, set OA2 token");
                dropboxApi.getSession().finishAuthentication();
                Log.d(tag, "Session finish authentication complete with token: "
                        + dropboxApi.getSession().getOAuth2AccessToken());
            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error during Dropbox authentication",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(tag, "Dropbox OA2 authentication failed (possibly timing issue)");
        }
    }



}
