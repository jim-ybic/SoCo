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
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.groups.ui.SimpleGroupCardAdapter;

import java.util.ArrayList;
import java.util.List;


public class GroupCardListFragment extends Fragment implements View.OnClickListener {

    static String tag = "GroupCardListFragment";

    View rootView;
    Context context;

    RecyclerView mRecyclerView;
    SimpleGroupCardAdapter simpleGroupCardAdapter;
    List<Event> events = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        generateDummyEvents();
    }

    private void generateDummyEvents() {

        //todo: use groups, instead of events

        Log.v(tag, "add 5 dummy events");
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_userprofile_groups, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        simpleGroupCardAdapter = new SimpleGroupCardAdapter(getActivity(), events);
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


}
