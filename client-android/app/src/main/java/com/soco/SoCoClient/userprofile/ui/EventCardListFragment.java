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
import com.soco.SoCoClient.events.allevents.SimpleEventCardAdapter;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.userprofile.task.UserEventTask;

import java.util.ArrayList;
import java.util.List;


public class EventCardListFragment extends Fragment implements View.OnClickListener,TaskCallBack {

    static String tag = "EventCardListFragment";

    View rootView;
    Context context;

    RecyclerView mRecyclerView;
    SimpleEventCardAdapter simpleEventCardAdapter;
    List<Event> events = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        SocoApp socoApp= (SocoApp) context;
        String[] paramNames = new String[1];
        paramNames[0]=UserEventTask.BUDDY_USER_ID;
        UserEventTask uet = new UserEventTask(SocoApp.user_id,SocoApp.token,paramNames,this);
        uet.execute(socoApp.currentUserOnProfile.getUser_id());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_userprofile_events, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        simpleEventCardAdapter = new SimpleEventCardAdapter(getActivity(), events);
        mRecyclerView.setAdapter(simpleEventCardAdapter);

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

    public void doneTask(Object o){
        if(o==null){
            return;
        }
        events = (ArrayList<Event>) o;
        Log.v(tag, "done task: ");

        simpleEventCardAdapter = new SimpleEventCardAdapter(getActivity(), events);
        mRecyclerView.setAdapter(simpleEventCardAdapter);
    }


}
