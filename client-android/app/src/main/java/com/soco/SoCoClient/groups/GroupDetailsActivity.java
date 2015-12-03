package com.soco.SoCoClient.groups;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.common.EventDetailsActivity;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.groups.task.GroupDetailsTask;
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

    static final String UPCOMING = "Upcoming";
    static final String PASTEVENTS = "Past Events";
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

//        Log.v(tag, "set actionbar background color");
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));
//        actionBar.setBackgroundDrawable(colorDrawable);

        Log.v(tag, "Adding tabs");
        android.support.v7.app.ActionBar.Tab tabProfile = actionBar.newTab().setText(UPCOMING).setTabListener(this);
        actionBar.addTab(tabProfile);
        android.support.v7.app.ActionBar.Tab tabGroups = actionBar.newTab().setText(PASTEVENTS).setTabListener(this);
        actionBar.addTab(tabGroups);
        showGroupDetails(group);
//        Log.v(tag, "set starting tab");
//        if(socoApp.eventGroupsBuddiesTabIndex == 0)
//            actionBar.selectTab(tabGroups);
//        else if(socoApp.eventGroupsBuddiesTabIndex == 1)
//            actionBar.selectTab(tabBuddies);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_group_detail, menu);
//        return true;
//    }

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
                addImageButtonToView(params,backgroundResource,u,membersLayout);
            }
        }
    }

    private void addImageButtonToView(LinearLayout.LayoutParams params,int backgroundResource, User u, LinearLayout list){
        ImageButton user = new ImageButton(mContext);
        user.setLayoutParams(params);
        user.setBackgroundResource(backgroundResource);
        user.setPadding(10, 0, 10, 0);
        IconUrlUtil.setImageForButtonSmall(mContext.getResources(), user, UrlUtil.getUserIconUrl(u.getUser_id()));
        list.addView(user);
    }
    public void close(View view){
        Log.v(tag, "tap on close");
        finish();
    }

    public void groupmembers(View view){
        Log.v(tag, "tap group members");
        Intent i = new Intent(this, GroupMembersActivity.class);

        //todo: pass group id

        startActivity(i);
    }

    public void joingroup(View view){
        Log.v(tag, "tap join group");
        Intent i = new Intent(this, JoinGroupActivity.class);

        //todo: pass group id

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
        if(o==null){
            return;
        }
       group = (Group) o;
        showGroupDetails(group);
    }
}
