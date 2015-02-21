package com.soco.SoCoClient.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.Config;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.dropbox.DropboxUtil;
import com.soco.SoCoClient.control.util.FileUtils;
import com.soco.SoCoClient.control.util.ProjectUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ShowSharedFilesActivity extends ActionBarActivity {

    String tag = "ShowSharedFiles";
    int pid;
    String loginEmail, loginPassword;
    DropboxAPI<AndroidAuthSession> dropboxApi;
    DBManagerSoco dbManagerSoco;
    ArrayList<String> displayFilenames;
    ArrayList<String> sharedFilesLocalPath;
//    HashMap<String, String> attrMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shared_files);

        pid = ((SocoApp) getApplicationContext()).getPid();
        loginEmail = ((SocoApp)getApplicationContext()).loginEmail;
        loginPassword = ((SocoApp)getApplicationContext()).loginPassword;
        dropboxApi = ((SocoApp)getApplicationContext()).dropboxApi;

        dbManagerSoco = ((SocoApp) getApplicationContext()).dbManagerSoco;
        sharedFilesLocalPath = dbManagerSoco.getSharedFilesLocalPath(pid);
        displayFilenames = dbManagerSoco.getSharedFilesDisplayName(pid);
        showSharedFiles(displayFilenames);

        ListView lv_files = (ListView) findViewById(R.id.lv_files);
        lv_files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                HashMap<String, String> map = (HashMap<String, String>)
                        listView.getItemAtPosition(position);
                String name = map.get(Config.PROJECT_PNAME);
                Log.i(tag, "Click on shared file list: " + name);
                String localPath = sharedFilesLocalPath.get(position);
                Log.i(tag, "Shared file local path: " + localPath);
                viewFile(localPath);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){
            Log.w(tag, "Activity return result is null");
            return;
        }

        SocoApp app = (SocoApp) getApplicationContext();

        //add file
        if (requestCode == Config.ACTIVITY_OPEN_FILE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.i(tag, "File selected with uri: " + uri.toString());
            FileUtils.checkUriMeta(getContentResolver(), uri);
            DropboxUtil.uploadToDropbox(uri, loginEmail, loginPassword, pid, dropboxApi,
                    getContentResolver(), getApplicationContext());
            app.setUploadStatus(SocoApp.UPLOAD_STATUS_START);
            // check result
            boolean isSuccess = false;
            for (int i = 1; i <= Config.UPLOAD_RETRY; i++) {
                Log.d(tag, "Wait for upload response: " + i + "/" + Config.UPLOAD_RETRY);
                SystemClock.sleep(Config.UPLOAD_WAIT);
                Log.d(tag, "Current upload status is: " + app.getUploadStatus());
                if (app.getUploadStatus().equals(SocoApp.UPLOAD_STATUS_SUCCESS)) {
                    isSuccess = true;
                    break;
                } else if (app.getUploadStatus().equals(SocoApp.UPLOAD_STATUS_FAIL)) {
                    isSuccess = false;
                    break;
                }
            }
            if (isSuccess) {
                Log.i(tag, "File upload success");
                new AlertDialog.Builder(this)
                        .setTitle("File upload success")
                        .setMessage("File has been saved in the cloud")
                        .setPositiveButton("OK", null)
                        .show();
                ProjectUtil.addSharedFileToDb(uri, loginEmail, loginPassword, pid,
                        getContentResolver(), dbManagerSoco);
            } else {
                Log.i(tag, "File upload failed");
                new AlertDialog.Builder(this)
                        .setTitle("File upload failed")
                        .setMessage("Review upload details and try again")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }

        //take picture
        if (requestCode == Config.ACTIVITY_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.d(tag, "Photo uri: " + uri);
            FileUtils.checkUriMeta(getContentResolver(), uri);
            DropboxUtil.uploadToDropbox(uri, loginEmail, loginPassword, pid, dropboxApi,
                    getContentResolver(), getApplicationContext());
            app.setUploadStatus(SocoApp.UPLOAD_STATUS_START);
            // check result
            boolean isSuccess = false;
            for (int i = 1; i <= Config.UPLOAD_RETRY; i++) {
                Log.d(tag, "Wait for upload response: " + i + "/" + Config.UPLOAD_RETRY);
                SystemClock.sleep(Config.UPLOAD_WAIT);
                Log.d(tag, "Current upload status is: " + app.getUploadStatus());
                if (app.getUploadStatus().equals(SocoApp.UPLOAD_STATUS_SUCCESS)) {
                    isSuccess = true;
                    break;
                } else if (app.getUploadStatus().equals(SocoApp.UPLOAD_STATUS_FAIL)) {
                    isSuccess = false;
                    break;
                }
            }
            if (isSuccess) {
                Log.i(tag, "File upload success");
                new AlertDialog.Builder(this)
                        .setTitle("File upload success")
                        .setMessage("File has been saved in the cloud")
                        .setPositiveButton("OK", null)
                        .show();
                ProjectUtil.addSharedFileToDb(uri, loginEmail, loginPassword, pid,
                        getContentResolver(), dbManagerSoco);
            } else {
                Log.i(tag, "File upload failed");
                new AlertDialog.Builder(this)
                        .setTitle("File upload failed")
                        .setMessage("Review upload details and try again")
                        .setPositiveButton("OK", null)
                        .show();
            }

        }

        //always refresh the list view in the end
        sharedFilesLocalPath = dbManagerSoco.getSharedFilesLocalPath(pid);
        displayFilenames = dbManagerSoco.getSharedFilesDisplayName(pid);
        showSharedFiles(displayFilenames);

        return;
    }

    public void viewFile(String localPath){
        Log.i(tag, "View local file: " + localPath);
        File file = new File(localPath);
        Uri uri = Uri.fromFile(file);
        Log.i(tag, "Created uri: " + uri);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);
        if (type == null)
            type = "*/*";
        Log.i(tag, "File name is: " + file.getName()
                + " ,file ext is: " + ext
                + ", type is: " + type);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_shared_files, menu);
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


    public void showSharedFiles(ArrayList<String> sharedFiles) {
        Log.d(tag, "refresh shared files start");
        ArrayList<Map<String, String>> list = new ArrayList<>();
        for (String filename : sharedFiles) {
            Log.d(tag, "Shared file list adding: " + filename);
            HashMap<String, String> map = new HashMap<>();
            map.put(Config.PROJECT_PNAME, filename);
            map.put(Config.PROJECT_PINFO, "no more info");
            list.add(map);
        }

        ListView lv_files = (ListView) findViewById(R.id.lv_files);
        SimpleAdapter adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2,
                new String[]{Config.PROJECT_PNAME, Config.PROJECT_PINFO},
                new int[]{android.R.id.text1, android.R.id.text2});
        lv_files.setAdapter(adapter);
    }

    public void addFile(View view){
        Log.d(tag, "add file start");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, Config.ACTIVITY_OPEN_FILE);
    }

    public void takePicture(View view){
        Log.i(tag, "Start activity: take picture");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, Config.ACTIVITY_TAKE_PHOTO);
    }
}
