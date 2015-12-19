package com.soco.SoCoClient.groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.groups.task.GroupsListTask;
import com.soco.SoCoClient.groups.ui.SimpleGroupCardAdapter;
import com.soco.SoCoClient.userprofile.UserProfileActivity;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class AllGroupsActivity extends ActionBarActivity implements TaskCallBack {

    static final String tag = "AllGroupsActivity";
    static final int CREATE_GROUP = 1001;

    private SwipeRefreshLayoutBottom swipeContainer;
    RecyclerView mRecyclerView;
    SimpleGroupCardAdapter simpleGroupCardAdapter;
    ArrayList<Group> groups = new ArrayList<>();

    android.support.v7.app.ActionBar actionBar;
    View actionbarView;

    SocoApp socoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_groups);

        socoApp = (SocoApp) getApplicationContext();
        swipeContainer = (SwipeRefreshLayoutBottom) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                startTask();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        simpleGroupCardAdapter = new SimpleGroupCardAdapter(this, groups);
        mRecyclerView.setAdapter(simpleGroupCardAdapter);
        startTask();
    }

    public void creategroup(View view) {
        Log.v(tag, "tap create new group");
        Intent i = new Intent(this, CreateGroupActivity.class);
        startActivityForResult(i, CREATE_GROUP);
    }

    public void groupdetails(View view) {
        Log.v(tag, "tap on a single group, show details");
        Intent i = new Intent(this, GroupDetailsActivity.class);
        String id = (String) view.getTag();
        i.putExtra(GroupDetailsActivity.GROUP_ID, id);
        startActivity(i);
    }

    public void mygroups(View view) {
        Log.v(tag, "tap show my groups");
        Intent i = new Intent(this, UserProfileActivity.class);
        i.putExtra(Config.USER_PROFILE_TAB_INDEX, Config.USER_PROFILE_TAB_INDEX_GROUPS);
        i.putExtra(User.USER_ID,SocoApp.user_id);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(tag, "on activity result: request code" + requestCode
                + ", result code: " + resultCode
                + ", intent data: " + data);

        if (requestCode == CREATE_GROUP && socoApp.createGroupResult) {
            Log.v(tag, "create group success, continue to the new group details screen");

            Intent i = new Intent(this, GroupDetailsActivity.class);
            //todo: pass group id as parameter
            startActivity(i);
        }

        return;
    }

    private void startTask() {
        if (groups != null && groups.size() > 0) {
            Group lastGroup = groups.get(groups.size() - 1);
            String[] params = new String[1];
            params[0] = GroupsListTask.START_GROUP_ID;
            GroupsListTask glt = new GroupsListTask(SocoApp.user_id, SocoApp.token, params, this);
            glt.execute(lastGroup.getGroup_id());
        } else {
            GroupsListTask glt = new GroupsListTask(SocoApp.user_id, SocoApp.token, null, this);
            glt.execute();
        }
    }

    public void doneTask(Object o) {
        if (o != null && o instanceof ArrayList) {

            ArrayList<Group> result = (ArrayList<Group>) o;
//            for (Group g : result) {
//                groups.add(g);
//            }
            for (int i = 0; i < 2 && i < result.size(); i++) {
                groups.add(result.get(i));
            }
            simpleGroupCardAdapter.notifyDataSetChanged();
        }
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

}
