package com.soco.SoCoClient.v2.view.activities;

//import info.androidhive.tabsswipe.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.dropbox.client2.DropboxAPI;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.v2.control.config.SocoApp;
import com.soco.SoCoClient.obsolete.v1.control.config.GeneralConfig;
import com.soco.SoCoClient.obsolete.v1.control.db.DBManagerSoco;
import com.soco.SoCoClient.obsolete.v1.control.dropbox.DropboxUtil;
import com.soco.SoCoClient.v2.control.dropbox.UploaderWatcher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityResourcesFragment extends Fragment implements View.OnClickListener {

    String tag = "ProjectResourcesFragment";
    View rootView;
    String loginEmail, loginPassword;

    SocoApp socoApp;
    int pid, pid_onserver;
    DropboxAPI dropboxApi;
    DBManagerSoco dbManagerSoco;
    ArrayList<String> sharedFilesLocalPath;
    ArrayList<String> displayFilenames;
    SimpleAdapter resourcesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        setContentView(R.layout.activity_show_shared_files);
        Log.d(tag, "on create: project resources fragment");

        socoApp = (SocoApp) getActivity().getApplication();
        pid = socoApp.pid;
        pid_onserver = socoApp.pid_onserver;
        loginEmail = socoApp.loginEmail;
        loginPassword = socoApp.loginPassword;
        dropboxApi = socoApp.dropboxApi;

        socoApp.cr = getActivity().getContentResolver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(tag, "create activity resources fragment view");
        rootView = inflater.inflate(R.layout.v1_fragment_activity_resources, container, false);
//        if(rootView == null)
//            Log.e(tag, "cannot find rootview");
//        else
//            Log.d(tag, "find rootview " + rootView);

//        Log.d(tag, "add listeners");
//        rootView.findViewById(R.id.add).setOnClickListener(this);
//        rootView.findViewById(R.id.camera).setOnClickListener(this);

        Log.d(tag, "show project resources");
        dbManagerSoco = ((SocoApp) getActivity().getApplicationContext()).dbManagerSoco;
        sharedFilesLocalPath = dbManagerSoco.getSharedFilesLocalPath(pid);
        displayFilenames = dbManagerSoco.getSharedFilesDisplayName(pid);
        showSharedFiles(displayFilenames);

        ListView lv_files = (ListView) rootView.findViewById(R.id.lv_files);
        lv_files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                HashMap<String, String> map = (HashMap<String, String>)
                        listView.getItemAtPosition(position);
                String name = map.get(GeneralConfig.ACTIVITY_NAME);
                Log.i(tag, "Click on shared file list: " + name);
                String localPath = sharedFilesLocalPath.get(position);
                Log.i(tag, "Shared file local path: " + localPath);
                viewFile(localPath);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){
            Log.w(tag, "Activity return result is null");
            return;
        }

        SocoApp socoApp = (SocoApp) getActivity().getApplication();

        //add file
        if (requestCode == GeneralConfig.ACTIVITY_OPEN_FILE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.i(tag, "File selected with uri: " + uri.toString());
//            FileUtils.checkUriMeta(getActivity().getContentResolver(), uri);
            DropboxUtil.uploadToDropbox(uri, loginEmail, loginPassword, pid, dropboxApi,
                    getActivity().getContentResolver(), getActivity().getApplicationContext());
            socoApp.setUploadStatus(SocoApp.UPLOAD_STATUS_START);
            // check status
            ((SocoApp)getActivity().getApplicationContext()).uri = uri;
            Log.d(tag, "Start upload watcher");
            Intent intent = new Intent(getActivity(), UploaderWatcher.class);
            getActivity().startService(intent);
        }

        //take picture
        if (requestCode == GeneralConfig.ACTIVITY_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.d(tag, "Photo uri: " + uri);
//            FileUtils.checkUriMeta(getActivity().getContentResolver(), uri);
            DropboxUtil.uploadToDropbox(uri, loginEmail, loginPassword, pid, dropboxApi,
                    getActivity().getContentResolver(), getActivity().getApplicationContext());
            socoApp.setUploadStatus(SocoApp.UPLOAD_STATUS_START);
            // check status
            socoApp.uri = uri;
            Log.i(tag, "Start status service");
            Intent intent = new Intent(getActivity(), UploaderWatcher.class);
            getActivity().startService(intent);
        }

        Log.d(tag, "reload resource details from database and refresh UI");
        sharedFilesLocalPath = dbManagerSoco.getSharedFilesLocalPath(pid);
        displayFilenames = dbManagerSoco.getSharedFilesDisplayName(pid);
        showSharedFiles(displayFilenames);
        //todo: add photo description

//        return;
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

    public void showSharedFiles(ArrayList<String> sharedFiles) {
        Log.d(tag, "refresh shared files start");
        ArrayList<Map<String, String>> list = new ArrayList<>();
        for (String filename : sharedFiles) {
            Log.d(tag, "Shared file list adding: " + filename);
            HashMap<String, String> map = new HashMap<>();
            map.put(GeneralConfig.ACTIVITY_NAME, filename);
            map.put(GeneralConfig.ACTIVITY_INFO, "no more info");
            list.add(map);
        }

        ListView lv_files = (ListView) rootView.findViewById(R.id.lv_files);
        resourcesAdapter = new SimpleAdapter(getActivity(), list,
                android.R.layout.simple_list_item_2,
                new String[]{GeneralConfig.ACTIVITY_NAME, GeneralConfig.ACTIVITY_INFO},
                new int[]{android.R.id.text1, android.R.id.text2});
        lv_files.setAdapter(resourcesAdapter);
    }

    public void addFile(){
        Log.d(tag, "add file start");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, GeneralConfig.ACTIVITY_OPEN_FILE);
    }

    public void takePicture(){
        Log.i(tag, "Start activity: take picture");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivityForResult(intent, GeneralConfig.ACTIVITY_TAKE_PHOTO);
    }

    @Override
    public void onClick(View v) {
        Log.d(tag, "click on " + v.getId());
//        switch (v.getId()) {
//            case R.id.add:
//                addFile();
//                break;
//            case R.id.camera:
//                takePicture();
//                break;
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity_resources, menu);
        super.onCreateOptionsMenu(menu, inflater);
//        return;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(tag, "onOptionsItemSelected:" + item.getItemId());

        switch (item.getItemId()) {
            case R.id.attach:
                addFile();
                break;
            case R.id.photo:
                takePicture();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}