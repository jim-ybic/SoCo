package com.soco.SoCoClient.events._ref;

//import info.androidhive.tabsswipe.R;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient._ref.DataConfigV1;
import com.soco.SoCoClient._ref.HttpConfigV1;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient.common.database._ref.DBManagerSoco;
import com.soco.SoCoClient.common.http.task._ref.SendMessageTaskAsync;
import com.soco.SoCoClient.common.util.SignatureUtil;
import com.soco.SoCoClient.common.ui.SectionEntryListAdapter;
import com.soco.SoCoClient.common.ui.EntryItem;
import com.soco.SoCoClient.events.ui.Item;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.model.Profile;

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

@Deprecated
public class ActivityUpdatesFragment extends Fragment implements View.OnClickListener  {

    String tag = "ProjectUpdatesFragment";
    View rootView;
    ListView lv_updates;
    SectionEntryListAdapter adapter_updates;

    int pid, pid_onserver;
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
        rootView = inflater.inflate(R.layout.fragment_activity_updates, container, false);
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
//                HttpConfigV1.MESSAGE_FROM_TYPE_1, email,
//                HttpConfigV1.MESSAGE_TO_TYPE_1, "jim.ybic@gmail.com",
//                SignatureUtil.now(), TEST_DEVICE_SAMSUNG,
//                HttpConfigV1.MESSAGE_CONTENT_TYPE_1, comment);
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

        String url = UrlUtil.getSendMessageUrl(getActivity());
        SendMessageTaskAsync task = new SendMessageTaskAsync(url,
                HttpConfigV1.MESSAGE_FROM_TYPE_1, email,
                HttpConfigV1.MESSAGE_TO_TYPE_2, String.valueOf(pid_onserver),
                SignatureUtil.now(), GeneralConfigV1.TEST_DEVICE_SAMSUNG,
                HttpConfigV1.MESSAGE_CONTENT_TYPE_1, comment);
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
            String user = u.get(DataConfigV1.UPDATE_INDEX_NAME);
            String comment = u.get(DataConfigV1.UPDATE_INDEX_COMMENT);
            String timestamp = u.get(DataConfigV1.UPDATE_INDEX_TIMESTAMP);
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