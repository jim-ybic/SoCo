package com.soco.SoCoClient.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.Config;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ShowSharedFilesActivity extends ActionBarActivity {

    String tag = "ShowSharedFiles";
//    int pid;
//    HashMap<String, String> attrMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shared_files);

        int pid = ((SocoApp) getApplicationContext()).getPid();
        DBManagerSoco dbManagerSoco = ((SocoApp) getApplicationContext()).dbManagerSoco;
        final ArrayList<String> sharedFilesLocalPath = dbManagerSoco.getSharedFilesLocalPath(pid);

//        attrMap = ((SocoApp) getApplicationContext()).getAttrMap();
//        ArrayList<String> sharedFiles = new ArrayList<>();
//        for(HashMap.Entry<String, String> e : attrMap.entrySet()){
//            if (e.getKey().equals(DataConfig.ATTRIBUTE_NAME_FILE_REMOTE_PATH))
//                sharedFiles.add(e.getValue());
//        }
        ArrayList<String> displayFilenames = dbManagerSoco.getSharedFilesDisplayName(pid);
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
                //todo: show file
                viewFile(localPath);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                startActivity(intent);
            }
        });
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

//    ArrayList<String> getDisplayFilenames(){
//        ArrayList<String> displayFilenames = new ArrayList<>();
//        ArrayList<HashMap<String, String>> attrMap = ((SocoApp) getApplicationContext()).getAttrMap();
//        Log.d(tag, "Number of attributes loaded: " + attrMap.size());
//
//        for(HashMap<String, String> map : attrMap) {
//            for (HashMap.Entry<String, String> e : map.entrySet()) {
//                Log.d(tag, "Current entry key: " + e.getKey());
//                if (e.getKey().equals(DataConfig.ATTRIBUTE_NAME_FILE_REMOTE_PATH)) {
//                    String remotePath = e.getValue();
//                    int pos = remotePath.lastIndexOf("/");
//                    String displayName = remotePath.substring(pos + 1, remotePath.length());
//                    Log.d(tag, "Found remote path: " + remotePath + ", "
//                            + " display name: " + displayName);
//                    displayFilenames.add(displayName);
//                }
//            }
//        }
//        return ;
//    }


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
        Log.i(tag, "refresh shared files start");
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

}
