package com.soco.SoCoClient.view.ui.project.single;

//import info.androidhive.tabsswipe.R;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.http.task.SendMessageTaskAsync;
import com.soco.SoCoClient.control.util.SignatureUtil;
import com.soco.SoCoClient.model.Profile;
import com.soco.SoCoClient.view.ui.section.SectionEntryListAdapter;
import com.soco.SoCoClient.view.ui.section.EntryItem;
import com.soco.SoCoClient.view.ui.section.Item;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProjectUpdatesFragment extends Fragment implements View.OnClickListener  {

    public static String TEST_DEVICE_SAMSUNG = "TEST DEVICE SAMSUNG";

    String tag = "ProjectUpdatesFragment";
    View rootView;
    ListView lv_updates;
    SectionEntryListAdapter adapter_updates;

    int pid;
    String pid_onserver;
    SocoApp socoApp;
    Profile profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        socoApp = (SocoApp)(getActivity().getApplication());
        profile = socoApp.profile;
        pid = socoApp.pid;
        pid_onserver = socoApp.pid_onserver;
        Log.d(tag, "pid is " + pid + ", pid_onserver is " + pid_onserver);

        //todo: test script
//        if(pid_onserver == null)
//            pid_onserver = "1";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(tag, "create project updates fragment view");
        rootView = inflater.inflate(R.layout.fragment_project_updates, container, false);
        lv_updates = (ListView) rootView.findViewById(R.id.list_comments);

        rootView.findViewById(R.id.send).setOnClickListener(this);
//        rootView.findViewById(R.id.sendToJim).setOnClickListener(this);
        listComments();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                add();
                break;
//            case R.id.sendToJim:
//                addToJim();
//                break;
        }
    }

//    public void addToJim(){
//        EditText et_comment = (EditText)rootView.findViewById(R.id.commentToJim);
//        String comment = et_comment.getText().toString();
//        Log.d(tag, "add comment into database: " + comment);
//
//        String user = socoApp.username;
//        String email = socoApp.loginEmail;
//        if(user == null || user.isEmpty())
//            user = socoApp.loginEmail;
//        Log.i(tag, "current user: " + user);
//
//        DBManagerSoco dbManagerSoco = socoApp.dbManagerSoco;
//        dbManagerSoco.addCommentToProject(comment, pid, user);
//
//        Toast.makeText(getActivity().getApplicationContext(), "Comment added",
//                Toast.LENGTH_SHORT).show();
//
//        String url = ProfileUtil.getSendMessageUrl(getActivity());
//        SendMessageTaskAsync task = new SendMessageTaskAsync(url,
//                HttpConfig.MESSAGE_FROM_TYPE_1, email,
//                HttpConfig.MESSAGE_TO_TYPE_1, "jim.ybic@gmail.com",
//                SignatureUtil.now(), TEST_DEVICE_SAMSUNG,
//                HttpConfig.MESSAGE_CONTENT_TYPE_1, comment);
//        task.execute();
//
//        //refresh UI
//        et_comment.setText("");
//        listComments();
//    }

    public void add(){
        EditText et_comment = (EditText)rootView.findViewById(R.id.comment);
        String comment = et_comment.getText().toString();
        Log.d(tag, "add comment into database: " + comment);

        String user = socoApp.username;
        String email = socoApp.loginEmail;
        if(user == null || user.isEmpty())
            user = socoApp.loginEmail;
        Log.i(tag, "current user: " + user);

        DBManagerSoco dbManagerSoco = socoApp.dbManagerSoco;
        dbManagerSoco.addCommentToProject(comment, pid, user);

        Toast.makeText(getActivity().getApplicationContext(), "Comment added",
                Toast.LENGTH_SHORT).show();

        String url = profile.getSendMessageUrl(getActivity());
        SendMessageTaskAsync task = new SendMessageTaskAsync(url,
                HttpConfig.MESSAGE_FROM_TYPE_1, email,
                HttpConfig.MESSAGE_TO_TYPE_2, pid_onserver,
                SignatureUtil.now(), TEST_DEVICE_SAMSUNG,
                HttpConfig.MESSAGE_CONTENT_TYPE_1, comment);
        task.execute();

        //refresh UI
        et_comment.setText("");
        listComments();
    }


    public void listComments() {
        Log.d(tag, "List project comments");
        ArrayList<Item> updateItems = new ArrayList<Item>();
        DBManagerSoco dbManagerSoco = socoApp.dbManagerSoco;
        ArrayList<ArrayList<String>> updates = dbManagerSoco.getUpdatesOfActivity(pid);

        for(ArrayList<String> u : updates){
            String user = u.get(DataConfig.UPDATE_INDEX_NAME);
            String comment = u.get(DataConfig.UPDATE_INDEX_COMMENT);
            String timestamp = u.get(DataConfig.UPDATE_INDEX_TIMESTAMP);
            Log.d(tag, "get an update: " + user + ", " + comment + ", " + timestamp);
            updateItems.add(new EntryItem(user, comment + " " + timestamp));
        }

//        for(Map.Entry<String, String> e : updates.entrySet()){
//            Log.d(tag, "found member: " + e.getValue() + ", " + e.getKey());
//            memberItems.add(new EntryItem(e.getValue(), e.getKey()));
//        }

        adapter_updates = new SectionEntryListAdapter(getActivity(), updateItems);
        lv_updates.setAdapter(adapter_updates);

        scrollMyListViewToBottom();
    }

    private void scrollMyListViewToBottom() {
        lv_updates.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lv_updates.setSelection(adapter_updates.getCount() - 1);
            }
        });
    }
}