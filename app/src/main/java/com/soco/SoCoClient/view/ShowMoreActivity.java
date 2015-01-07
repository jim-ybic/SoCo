package com.soco.SoCoClient.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
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
import com.soco.SoCoClient.control.SignatureUtil;
import com.soco.SoCoClient.model.DropboxUploader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class ShowMoreActivity extends ActionBarActivity {

    static int READ_REQUEST_CODE = 42;

    DropboxAPI<AndroidAuthSession> dropbox;
    String ACCESS_KEY = "7cfm4ur90xw54pv";
    String ACCESS_SECRET = "9rou23wi8t4htkz";
    String accessToken;
    AppKeyPair appKeyPair;
    AccessTokenPair accessTokenPair;

    String loginEmail, loginPassword, programName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more);

        Intent intent = getIntent();
        loginEmail = intent.getStringExtra(LoginActivity.LOGIN_EMAIL);
        loginPassword = intent.getStringExtra(LoginActivity.LOGIN_PASSWORD);
        programName = intent.getStringExtra(ShowSingleProgramActivity.PROGRAM);
        Log.i("intent", "ShowMoreActivity get extra: " +
                loginEmail + ", " + loginPassword + ", " + programName);

        initDropboxApiAuthentication();
    }

    public void openFile(View view){

/**
 * Fires an intent to spin up the "file chooser" UI and select an image.
 */

            // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
            // browser.
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Filter to show only images, using the image MIME data type.
            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

        public void dumpImageMetaData(Uri uri) {

            // The query, since it only applies to a single document, will only return
            // one row. There's no need to filter, sort, or select fields, since we want
            // all fields for one document.
            Cursor cursor = getContentResolver()
                    .query(uri, null, null, null, null, null);

            try {
                // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
                // "if there's anything to look at, look at it" conditionals.
                if (cursor != null && cursor.moveToFirst()) {

                    // Note it's called "Display Name".  This is
                    // provider-specific, and might not necessarily be the file name.
                    String displayName = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.i("file", "Display Name: " + displayName);

                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    // If the size is unknown, the value stored is null.  But since an
                    // int can't be null in Java, the behavior is implementation-specific,
                    // which is just a fancy term for "unpredictable".  So as
                    // a rule, check if it's null before assigning to an int.  This will
                    // happen often:  The storage API allows for remote files, whose
                    // size might not be locally known.
                    String size = null;
                    if (!cursor.isNull(sizeIndex)) {
                        // Technically the column stores an int, but cursor.getString()
                        // will do the conversion automatically.
                        size = cursor.getString(sizeIndex);
                    } else {
                        size = "Unknown";
                    }
                    Log.i("file", "Size: " + size);
                }
            } finally {
                cursor.close();
            }
        }

    public String getName(Uri uri) {

        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                String displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i("file", "Display Name: " + displayName);
                return displayName;
            }
        } finally {
            cursor.close();
        }

        return "name_not_found";
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i("file", "File selected, Uri: " + uri.toString());
//                showImage(uri);
                dumpImageMetaData(uri);
//                readTextFromUri(uri);
                uploadToDropbox(uri);
            }
        }
    }

    void uploadToDropbox(Uri uri){
        Log.i("more", "More: upload to dropbox, " + uri);

        String sigEmail = SignatureUtil.genSHA1(loginEmail, loginPassword);
        Log.i("hash", "Login email SHA1 signature, " + loginEmail + ", " + sigEmail);
        String sigProgram = SignatureUtil.genSHA1(programName, loginPassword);
        Log.i("hash", "Program name SHA1 signature, " + programName + ", " + sigProgram);

        String p = "/" + sigEmail + "/" + sigProgram + "/";
        Log.i("dropbox",  "Remote file path: " + p);

        DropboxUploader upload = new DropboxUploader(this, dropbox, p);
        upload.key = ACCESS_KEY;
        upload.secret = ACCESS_SECRET;
        upload.accessTokenPair = accessTokenPair;
        upload.sigEmail = sigEmail;
        upload.sigProgram = sigProgram;
        upload.localPath = getApplicationContext().getFilesDir().toString();

        InputStream inputStream = null;
        FileInputStream fileInputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            Log.i("more", "uri getpath: " + uri.getPath());
            File f = new File(uri.getPath());
            fileInputStream = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        upload.fileInputStream = fileInputStream;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            upload.inputStream = inputStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        upload.filename = getName(uri);

        Log.i("dropbox", "Create UploadFileToDropbox with accessTokenPair: " + accessTokenPair);
        upload.execute();

        TextView tv_file_log = (TextView) findViewById(R.id.tv_file_log);
        tv_file_log.append("File upload success: " + getName(uri) + "\n");
    }

//    private void readTextFromUri(Uri uri)  {
//        try {
//            InputStream inputStream = getContentResolver().openInputStream(uri);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    inputStream));
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                Log.i("file", "Read line: " + line);
//                stringBuilder.append(line);
//            }
//            reader.close();
//            inputStream.close();
//            return;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void initDropboxApiAuthentication(){
        Log.i("dropbox", "Create DropboxAPI object");

        AndroidAuthSession session;

        Log.i("dropbox", "Step 1: Create appKeyPair from Key/Secret: "
                + ACCESS_KEY + "/" + ACCESS_SECRET);
        appKeyPair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);
        accessTokenPair = new AccessTokenPair(ACCESS_KEY, ACCESS_SECRET);

        Log.i("dropbox", "Step 2: Create session with appKeyPair: " + appKeyPair
                + ", AccessType: " + Session.AccessType.APP_FOLDER
                + ", accessTokenPair: " + accessTokenPair);
        session = new AndroidAuthSession(appKeyPair, Session.AccessType.APP_FOLDER);

        Log.i("dropbox", "Step 3: Create DropboxAPI from session: " + session);
        dropbox = new DropboxAPI<AndroidAuthSession>(session);

        boolean useSoCoDropboxAccount = true;
        //TODO: choose if use SoCo's dropbox account or user's own dropbox account

        if (useSoCoDropboxAccount) {
            Log.i("dropbox", "Step 4 (approach a): Load SoCo's dropbox account and OA2 token");
            String OA2token = "JWWNa2LgL2UAAAAAAAAANNpl6wfgG5wTX6_OrNik5a_yKGsnySogfHYMK-uxjLJd";
            Log.i("dropbox", "Set DropboxAPI OA2 token: " + OA2token);
            dropbox.getSession().setOAuth2AccessToken(OA2token);
        } else {
            Log.i("dropbox", "Step 4 (approach b): Let user login");
        }

        Log.i("dropbox", "Validate DropboxAPI and Session");
        if (dropbox != null && dropbox.getSession() != null
                && dropbox.getSession().getOAuth2AccessToken() != null)
            Log.i("dropbox", "Validation success, token: " + dropbox.getSession().getOAuth2AccessToken());
        else {
            Log.i("dropbox", "Session authentication failed, create new OA2 validation session");
            dropbox.getSession().startOAuth2Authentication(ShowMoreActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("dropbox", "ShowSingleProgramActivity:OnResume, check if OA2 authentication success");
        Log.i("dropbox", "Session token: " + dropbox.getSession().getOAuth2AccessToken());

        if (dropbox != null && dropbox.getSession() != null
                && dropbox.getSession().getOAuth2AccessToken() != null) {
            Log.i("dropbox", "DropboxAPI and Session created with existing token: "
                    + dropbox.getSession().getOAuth2AccessToken());
            return;
        }

        Log.i("dropbox", "Check OA2 authentication result");
        if (dropbox.getSession().authenticationSuccessful()) {
            Log.i("dropbox", "Dropbox OA2 authentication success");
            try {
                Log.i("dropbox", "Session finish authentication, set OA2 token");
                dropbox.getSession().finishAuthentication();
                Log.i("dropbox", "Session finish authentication complete with token: "
                        + dropbox.getSession().getOAuth2AccessToken());
            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error during Dropbox authentication",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i("dropbox", "Dropbox OA2 authentication failed (possibly timing issue)");
        }
    }


}
