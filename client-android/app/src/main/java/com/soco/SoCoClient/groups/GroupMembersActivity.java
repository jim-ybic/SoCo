package com.soco.SoCoClient.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.ui.ExpandableHeightGridView;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.events.common.BuddiesGridSimpleAdapter;
import com.soco.SoCoClient.events.common.EventBuddiesFragment;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.groups.task.GroupDetailsTask;
import com.soco.SoCoClient.userprofile.UserProfileActivity;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupMembersActivity extends ActionBarActivity implements TaskCallBack {

    static String tag = "GroupMembersActivity";

    static final String ItemImage = "ItemImage";
    static final String ItemName = "ItemName";
    static final String ItemId = "ItemId";

    Context context;
    ArrayList<HashMap<String, Object>> members = new ArrayList<>();
    String group_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        context = getApplicationContext();

        String groupId = getIntent().getStringExtra(GroupDetailsActivity.GROUP_ID);
        GroupDetailsTask task = new GroupDetailsTask(SocoApp.user_id, SocoApp.token, this);
        task.execute(groupId);

        showMembers();
    }

     private void showMembers(){
         ((TextView)findViewById(R.id.group_name)).setText("Group Member of "+group_name);
        BuddiesGridSimpleAdapter adapter = new BuddiesGridSimpleAdapter(this,
                members,
                R.layout.eventbuddiesgrid_entry,
                new String[] {ItemImage,ItemName,ItemId},
                new int[] {R.id.image,R.id.name,R.id.id});
        ExpandableHeightGridView view = (ExpandableHeightGridView) findViewById(R.id.grid);
        view.setExpanded(true);
        view.setAdapter(adapter);
        view.setFocusable(false);

        view.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                            View arg1,//The view within the AdapterView that was clicked
                                            int arg2,//The position of the view in the adapter
                                            long arg3//The row id of the item that was clicked
                    ) {
                        HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                        Log.v(EventBuddiesFragment.tag, "text: " + item.get(ItemName) + ", " + item.get(ItemId));

                        Intent i = new Intent(context, UserProfileActivity.class);
                        i.putExtra(User.USER_ID, String.valueOf(item.get(ItemId)));
                        Log.v(tag, "put userid: " + item.get(ItemId));
                        startActivity(i);
                    }
                }
        );
    }
    public void doneTask(Object o) {
        if(o==null){
            return;
        }
        if(o instanceof Group) {
            Group g = (Group) o;
            ArrayList<User> users = g.getMembers();
            for (User u : users) {
                HashMap<String, Object> item = getItemForShow(u);
                members.add(item);
                Log.v(tag, "added buddy: " + u.getUser_id() + ", " + u.getUser_name());
            }
            if(!StringUtil.isEmptyString(g.getGroup_name())){
                group_name=g.getGroup_name();
            }
            showMembers();
        }
    }
    private HashMap<String,Object> getItemForShow(User user){
        Log.v(tag, "show friend item: " + user.getUser_name() + ", " + UrlUtil.getUserIconUrl(user.getUser_id()));
        HashMap<String, Object> item = new HashMap<>();
        item.put(ItemImage, UrlUtil.getUserIconUrl(user.getUser_id()));
        item.put(ItemName, user.getUser_name());
        item.put(ItemId, user.getUser_id());
        return item;
    }
}
