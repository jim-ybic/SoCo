package com.soco.SoCoClient.userprofile.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.groups.ui.SimpleGroupCardAdapter;
import com.soco.SoCoClient.userprofile.task.UserGroupTask;


import java.util.ArrayList;


public class GroupCardListFragment extends Fragment implements View.OnClickListener, TaskCallBack {

    static String tag = "GroupCardListFragment";

    View rootView;
    Context context;

    RecyclerView mRecyclerView;
    SimpleGroupCardAdapter simpleGroupCardAdapter;
    ArrayList<Group> groups = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        SocoApp socoApp = (SocoApp) context;
        UserGroupTask ugt = new UserGroupTask(SocoApp.user_id, SocoApp.token, this);
        ugt.execute(socoApp.currentUserOnProfile.getUser_id());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_userprofile_groups, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        simpleGroupCardAdapter = new SimpleGroupCardAdapter(getActivity(), groups);
        mRecyclerView.setAdapter(simpleGroupCardAdapter);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public void doneTask(Object o) {
        if (o == null) {
            return;
        }
        groups = (ArrayList<Group>) o;

        simpleGroupCardAdapter = new SimpleGroupCardAdapter(getActivity(), groups);
        mRecyclerView.setAdapter(simpleGroupCardAdapter);
    }
}