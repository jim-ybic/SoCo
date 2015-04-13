package com.soco.SoCoClient.view.ui.tab;

//import info.androidhive.tabsswipe.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.http.task.InviteProjectMemberTaskAsync;
import com.soco.SoCoClient.control.util.ProfileUtil;

public class ProjectMembersFragment extends Fragment implements View.OnClickListener {

    String tag = "ProjectMembersFragment";
    View rootView;

    int pid;
    String pid_onserver;
    SocoApp socoApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        socoApp = (SocoApp)(getActivity().getApplication());
        pid = socoApp.pid;
        pid_onserver = socoApp.pid_onserver;
        Log.d(tag, "pid is " + pid + ", pid_onserver is " + pid_onserver);

        //todo: test script
        if(pid_onserver == null)
            pid_onserver = "1";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(tag, "create project members fragment view");
        rootView = inflater.inflate(R.layout.fragment_project_members, container, false);

        rootView.findViewById(R.id.bt_add).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                add();
                break;
        }
    }

    public void add(){
        String email = ((EditText) rootView.findViewById(R.id.et_add_member)).getText().toString();
        InviteProjectMemberTaskAsync httpTask = new InviteProjectMemberTaskAsync(
                        ProfileUtil.getInviteProjectMemberUrl(getActivity()),     //url
//                        HttpConfig.HTTP_TYPE_INVITE_PROJECT_MEMBER,               //type
//                        "", "",                                     //login email & password
//                        getActivity().getApplicationContext(),      //context
//                        null,                                       //project name
                        String.valueOf(pid_onserver),
//                        pid_onserver,          //local & remote pid
//                        null,                                       //attribute map
                        email                                       //invite email
        );
        httpTask.execute();
    }

}