package com.soco.SoCoClient.v2.view.activities;

//import info.androidhive.tabsswipe.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.v2.control.config.SocoApp;
import com.soco.SoCoClient.obsolete.v1.control.config.HttpConfig;
import com.soco.SoCoClient.obsolete.v1.control.db.DBManagerSoco;
import com.soco.SoCoClient.obsolete.v1.control.http.task.InviteActivityMemberTaskAsync;
import com.soco.SoCoClient.v2.view.sectionlist.SectionEntryListAdapter;
import com.soco.SoCoClient.v2.view.sectionlist.EntryItem;
import com.soco.SoCoClient.v2.view.sectionlist.Item;
import com.soco.SoCoClient.v2.control.http.UrlUtil;
import com.soco.SoCoClient.v2.model.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityMembersFragment extends Fragment implements View.OnClickListener {

    String tag = "ProjectMembersFragment";
    View rootView;

    int pid, pid_onserver;
    SocoApp socoApp;
    Profile profile;
    DBManagerSoco dbManagerSoco;
    SectionEntryListAdapter membersAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        socoApp = (SocoApp)(getActivity().getApplication());
        profile = socoApp.profile;
        dbManagerSoco = socoApp.dbManagerSoco;

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

        Log.d(tag, "create project members fragment view");
        rootView = inflater.inflate(R.layout.v1_fragment_activity_members, container, false);

//        rootView.findViewById(R.id.bt_add).setOnClickListener(this);
        listMembers();

        return rootView;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bt_add:
//                add();
//                break;
//        }
    }

    public void add(String email, String nickname){
        String url = UrlUtil.getInviteProjectMemberUrl(getActivity());
//        String email = ((EditText) rootView.findViewById(R.id.et_add_member)).getText().toString();
        InviteActivityMemberTaskAsync httpTask = new InviteActivityMemberTaskAsync(
                        url,
                        String.valueOf(pid_onserver),
                        email                                       //invite email
        );
        httpTask.execute();

        Toast.makeText(getActivity().getApplicationContext(), "Sent invitation success",
                Toast.LENGTH_SHORT).show();

//        String userEmail = profile.email;
//        String userName = profile.username;
        Log.i(tag, "save into db new member " + email + "/" + HttpConfig.TEST_UNKNOWN_USERNAME
                + " into project " + pid);
        dbManagerSoco.addMemberToActivity(
                email,
                "",         //todo: add username
                nickname,
                pid);
        listMembers();

        //todo: set member invitation status (invited, accepted, rejected)
    }

    public void listMembers() {
        Log.d(tag, "List project members");
        ArrayList<Item> memberItems = new ArrayList<>();
        HashMap<String, String> map = dbManagerSoco.getMembersOfActivity(pid);

        for(Map.Entry<String, String> e : map.entrySet()){
            Log.d(tag, "found member: " + e.getValue() + ", " + e.getKey());
            memberItems.add(new EntryItem(e.getValue(), e.getKey()));
        }

        membersAdapter = new SectionEntryListAdapter(getActivity(), memberItems);
        ListView lv = (ListView) rootView.findViewById(R.id.listview_members);
        lv.setAdapter(membersAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity_members, menu);
        super.onCreateOptionsMenu(menu, inflater);
//        return;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(tag, "onOptionsItemSelected:" + item.getItemId());

        switch (item.getItemId()) {
            case R.id.add:
                addMember();
                break;
              }

        return super.onOptionsItemSelected(item);
    }

    public void addMember() {
        Context context = getActivity();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText emailBox = new EditText(context);
        emailBox.setHint("Email address");
        emailBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(emailBox);

        final EditText nicknameBox = new EditText(context);
        nicknameBox.setHint("Nick name");
        layout.addView(nicknameBox);

//        dialog.setView(layout);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Invite new member");
//        alert.setMessage("Email address:");
//        final EditText input = new EditText(getActivity());
        alert.setView(layout);

        alert.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String email = emailBox.getText().toString();
                String nickname = nicknameBox.getText().toString();
                add(email, nickname);
//                Toast.makeText(getActivity().getApplicationContext(),
//                        "Invited contact complete.", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }


}