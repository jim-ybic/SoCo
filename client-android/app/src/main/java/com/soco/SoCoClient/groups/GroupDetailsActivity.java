package com.soco.SoCoClient.groups;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.details.EventDetailsActivity;
import com.soco.SoCoClient.events.ui.ViewElementHelper;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.groups.task.GroupDetailsTask;
import com.soco.SoCoClient.groups.task.JoinGroupTask;
import com.soco.SoCoClient.groups.ui.GroupDetailsTabsAdapter;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class GroupDetailsActivity extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener, TaskCallBack{

    String tag = "GroupDetailActivity";

    public static final String GROUP_ID = "group_id";

    private ViewPager viewPager;
    private GroupDetailsTabsAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

    private Context mContext;
    private Group group;
    private String groupId;
    SocoApp socoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        mContext = getApplicationContext();
        socoApp = (SocoApp)mContext;
        viewPager = (ViewPager) findViewById(R.id.pager);

        groupId = getIntent().getStringExtra(GROUP_ID);
        Log.v(tag, "get from extra groupid: " + groupId);

        setActionbar();

        GroupDetailsTask task = new GroupDetailsTask(SocoApp.user_id, SocoApp.token, this);
        task.execute(groupId);
    }

    void setActionbar(){
        actionBar = getSupportActionBar();
        if(actionBar == null){
            Log.e(tag, "Cannot get action bar object");
            return;
        }

        mAdapter = new GroupDetailsTabsAdapter(getSupportFragmentManager(), groupId);
        viewPager.setAdapter(mAdapter);

        Log.v(tag, "set actionbar custom view");
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowCustomEnabled(true);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_group_details, null);

        android.support.v7.app.ActionBar.LayoutParams layout = new android.support.v7.app.ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, layout);
        Toolbar parent = (Toolbar) customView.getParent();
        Log.v(tag, "remove margin in actionbar area");
        parent.setContentInsetsAbsolute(0, 0);

        Log.v(tag, "Adding tabs");
        android.support.v7.app.ActionBar.Tab tabProfile = actionBar.newTab().setText(R.string.groupdetails_tab_upcoming).setTabListener(this);
        actionBar.addTab(tabProfile);
        android.support.v7.app.ActionBar.Tab tabGroups = actionBar.newTab().setText(R.string.groupdetails_tab_past).setTabListener(this);
        actionBar.addTab(tabGroups);
        showGroupDetails(group);
//        Log.v(tag, "set starting tab");
//        if(socoApp.eventGroupsBuddiesTabIndex == 0)
//            actionBar.selectTab(tabGroups);
//        else if(socoApp.eventGroupsBuddiesTabIndex == 1)
//            actionBar.selectTab(tabBuddies);

        Log.v(tag, "Set listener");
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.v(tag, "position is " + position);
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab,
                              android.support.v4.app.FragmentTransaction fragmentTransaction) {
        Log.v(tag, "get position: " + tab.getPosition());
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab,
                                android.support.v4.app.FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab,
                                android.support.v4.app.FragmentTransaction fragmentTransaction) {
    }

    private void showGroupDetails(Group g){
        if(g==null){
            return;
        }
        if(g.getGroup_name()!=null) {
            ((TextView) findViewById(R.id.group_name)).setText(g.getGroup_name());
        }
        if(g.getLocation()!=null) {
            ((TextView) findViewById(R.id.location)).setText(g.getLocation());
        }
        if(g.getDescription()!=null) {
            ((TextView) findViewById(R.id.intro)).setText(g.getDescription());
        }
//        if(g.getNumberOfMembers()!=null) {
//            ((TextView) findViewById(R.id.number_of_members)).setText(g.getNumberOfMembers());
//        }
        if(g.getMembers()!=null) {
            ArrayList<User> members = g.getMembers();
            LinearLayout membersLayout = (LinearLayout) findViewById(R.id.members);
            membersLayout.removeAllViews();
            membersLayout.setOrientation(LinearLayout.HORIZONTAL);
            int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};
            TypedArray ta = getApplicationContext().obtainStyledAttributes(attrs);
            int backgroundResource = ta.getResourceId(0, 0);
            ta.recycle();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT;
            for(int i=0;i<members.size()&&i<6;i++){
                User u = members.get(i);
//                addImageButtonToView(params,backgroundResource,u,membersLayout);
                ViewElementHelper.addImageButtonToView(params, backgroundResource, u, membersLayout, mContext);
            }
        }

        if(g.getCategories().size()>0){
            LinearLayout layout = (LinearLayout) findViewById(R.id.categories);
            ViewElementHelper.showCategories(g, layout, mContext);
        }
    }

    public void close(View view){
        Log.v(tag, "tap on close");
        finish();
    }

    public void groupmembers(View view){
        Log.v(tag, "tap group members");
        Intent i = new Intent(this, GroupMembersActivity.class);
        i.putExtra(GROUP_ID, groupId);
        startActivity(i);
    }

    public void eventdetails(View view){
        Log.v(tag, "check event details");
        Intent i = new Intent(this, EventDetailsActivity.class);
        Long id = (Long) view.getTag();
        i.putExtra(EventDetailsActivity.EVENT_ID, id);
        Log.v(tag, "put extra eventid: " + id);
        startActivity(i);
    }

    public void doneTask(Object o){
        if(o == null){
            Log.d(tag, "donetask, return null");
            return;
        }
        else if (o instanceof Group) {   // group details
            group = (Group) o;
            Log.d(tag, "show group details: " + group.toString());
            showGroupDetails(group);
        }
        else if (o instanceof Boolean){     //join group
            Boolean ret = (Boolean) o;
            Log.d(tag, "join group result: " + ret);
            if(ret)
                Toast.makeText(this, "Join group success.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Cannot join group.", Toast.LENGTH_SHORT).show();
        }
    }

    public void joingroup(View view) {
        Log.v(tag, "tap join group");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to join " + group.getGroup_name());
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d(tag, "tap Yes");
                        joinGroupInBackground();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d(tag, "tap Cancel");
                    }
                });
        builder.show();
    }

    private void joinGroupInBackground(){
        new JoinGroupTask(getApplicationContext(), group, this).execute();
    }

    public void groupIntro(View view){
        Log.v(tag, "show group full intro message");
        String text = ((TextView) findViewById(R.id.intro)).getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.msg_info);
        builder.setMessage(text);
        builder.setPositiveButton(
                getApplicationContext().getString(R.string.msg_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d(tag, "tap OK");
                    }
                });
        builder.show();
    }

}
