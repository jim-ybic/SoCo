package com.soco.SoCoClient.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.events.common.UserIconGridAdapter;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.groups.task.GroupMembersTask;
import com.soco.SoCoClient.userprofile.UserProfileActivity;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class GroupMembersActivity extends ActionBarActivity implements TaskCallBack {

    static String tag = "GroupMembersActivity";

    UserIconGridAdapter adapter;
    //    ExpandableHeightGridView view;
    private SwipeRefreshLayoutBottom swipeContainer;
    RecyclerView mRecyclerView;
    Context context;
    //    ArrayList<HashMap<String, Object>> members = new ArrayList<>();
    ArrayList<User> members = new ArrayList<>();
    String group_name = "";
    String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        context = getApplicationContext();

        ((TextView) findViewById(R.id.group_name)).setText("Group Member of " + group_name);
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

        adapter = new UserIconGridAdapter(this, members);
        mRecyclerView.setAdapter(adapter);
        groupId = getIntent().getStringExtra(GroupDetailsActivity.GROUP_ID);
        startTask();
    }

    private void startTask() {
        GroupMembersTask task = new GroupMembersTask(SocoApp.user_id, SocoApp.token, groupId, this);
        if (members != null && members.size() > 0) {
            User lastMember = members.get(members.size() - 1);
            task.execute(lastMember.getUser_id());
        } else {
            task.execute();
        }
    }

    private void showMembers() {
        ((TextView) findViewById(R.id.group_name)).setText("Group Member of " + group_name);
        adapter.notifyDataSetChanged();
    }

    public void userprofile(View view) {
        Log.v(tag, "check event details");
        Intent i = new Intent(this, UserProfileActivity.class);
        String id = (String) view.getTag();
        i.putExtra(User.USER_ID, id);
        startActivity(i);
    }

    public void doneTask(Object o) {
        if (o instanceof Group) {
            Group g = (Group) o;
            ArrayList<User> users = g.getMembers();
            //todo currently only show 2 at one time. to revert back later
//            for (User u : users) {
//                HashMap<String, Object> item = getItemForShow(u);
//                members.add(item);
//                Log.v(tag, "added buddy: " + u.getUser_id() + ", " + u.getUser_name());
//            }
            if (users != null && users.size() > 0) {
                for (int i = 0; i < users.size() && i < 2; i++) {
                    User u = users.get(i);
                    members.add(u);
                    Log.v(tag, "added buddy: " + u.getUser_id() + ", " + u.getUser_name());

                }
            }
            if (!StringUtil.isEmptyString(g.getGroup_name())) {
                group_name = g.getGroup_name();
            }
            showMembers();
        }
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }
}
