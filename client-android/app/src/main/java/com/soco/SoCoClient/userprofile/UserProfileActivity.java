package com.soco.SoCoClient.userprofile;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.events.common.EventDetailsActivity;
import com.soco.SoCoClient.groups.GroupDetailsActivity;
import com.soco.SoCoClient.userprofile.model.User;
import com.soco.SoCoClient.userprofile.task.UserProfileTask;
import com.soco.SoCoClient.userprofile.ui.UserProfileTabsAdapter;
import com.soco.SoCoClient.common.util.SocoApp;


public class UserProfileActivity extends ActionBarActivity
        implements android.support.v7.app.ActionBar.TabListener, TaskCallBack
{
    String tag = "UserProfileActivity";

    private ViewPager viewPager;
    private UserProfileTabsAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

    Context context;
    SocoApp socoApp;
    User user;
    int tabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        context = getApplicationContext();
        socoApp = (SocoApp) context;

        Intent i = getIntent();
        tabIndex = i.getIntExtra(Config.USER_PROFILE_TAB_INDEX, 0);
        Log.v(tag, "get tabindex: " + tabIndex);

        String userId = i.getStringExtra(User.USER_ID);
        Log.v(tag, "userid: " + userId);
        if(userId != null && !userId.isEmpty() && socoApp.suggestedBuddies != null && socoApp.suggestedBuddiesMap.containsKey(userId))     {
            user = socoApp.suggestedBuddiesMap.get(userId);
            Log.v(tag, "get user from suggested buddies map: " + user.toString());
            socoApp.currentUserOnProfile = user;
            setActionbar();
        }
        else{
            if(userId == null)
                Log.w(tag, "userid is null");
            else if(userId.isEmpty())
                Log.w(tag, "userid is empty");
            else if(socoApp.suggestedBuddies == null)
                Log.w(tag, "suggested buddies is null");
            else if(!socoApp.suggestedBuddiesMap.containsKey(userId))
                Log.w(tag, "suggested buddies map does not contain user: " + userId);

            Log.v(tag, "cannot get user from current suggested buddies, request from server: " + userId);
            user = new User();
            user.setUser_id(userId);
            new UserProfileTask(context, user, this).execute();

            Log.v(tag, "set current user flag: " + user.getUser_id() + ", " + user.getUser_name());
            socoApp.currentUserOnProfile = user;
        }
    }

    void setActionbar(){
        Log.v(tag, "set actionbar");
        viewPager = (ViewPager) findViewById(R.id.pager);

        actionBar = getSupportActionBar();
        if(actionBar == null){
            Log.e(tag, "Cannot get action bar object");
            return;
        }

        mAdapter = new UserProfileTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        Log.v(tag, "set actionbar custom view");
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowCustomEnabled(true);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_user_profile, null);

        android.support.v7.app.ActionBar.LayoutParams layout = new android.support.v7.app.ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, layout);
        Toolbar parent = (Toolbar) customView.getParent();
        Log.v(tag, "remove margin in actionbar area");
        parent.setContentInsetsAbsolute(0, 0);

        TextView userName = (TextView) customView.findViewById(R.id.name);
        userName.setText(user.getUser_name());
        TextView userLocation = (TextView) customView.findViewById(R.id.location);
        if(user.getLocation().isEmpty())
            Log.w(tag, "user location is empty, use default Hong Kong");
        else
            userLocation.setText((user.getLocation()));
        Log.v(tag, "set user name and location: " + user.getUser_name() + ", " + user.getLocation());

        Log.v(tag, "set user icon: " + user.getUser_id() + ", " + user.getUser_name());
        ImageView icon = (ImageView) customView.findViewById(R.id.icon);
        IconUrlUtil.setImageForButtonLarge(context.getResources(), icon, UrlUtil.getUserIconUrl(user.getUser_id()));

        Log.v(tag, "Adding tabs");
        android.support.v7.app.ActionBar.Tab tabProfile = actionBar.newTab().setText(R.string.userprofile_tab_profile).setTabListener(this);
        actionBar.addTab(tabProfile);
        android.support.v7.app.ActionBar.Tab tabGroups = actionBar.newTab().setText(R.string.userprofile_tab_groups).setTabListener(this);
        actionBar.addTab(tabGroups);
        android.support.v7.app.ActionBar.Tab tabEvents = actionBar.newTab().setText(R.string.userprofile_tab_events).setTabListener(this);
        actionBar.addTab(tabEvents);

        Log.v(tag, "set starting tab " + tabIndex);
        if(tabIndex == Config.USER_PROFILE_TAB_INDEX_PROFILE)
            actionBar.selectTab(tabProfile);
        else if(tabIndex == Config.USER_PROFILE_TAB_INDEX_EVENTS)
            actionBar.selectTab(tabEvents);
        else if(tabIndex == Config.USER_PROFILE_TAB_INDEX_GROUPS)
            actionBar.selectTab(tabGroups);

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

    public void close(View view){
        Log.v(tag, "tap on close");
        finish();
    }

    public void doneTask(Object o){
        Log.v(tag, "done task, set actionbar");
        setActionbar();
    }

    public void eventdetails(View view){
        Log.v(tag, "check event details");
        Intent i = new Intent(this, EventDetailsActivity.class);
        Long id = (Long) view.getTag();
        i.putExtra(EventDetailsActivity.EVENT_ID, id);
        Log.v(tag, "put extra eventid: " + id);
        startActivity(i);
    }

    public void groupdetails(View view){
        Log.v(tag, "tap group details");
        Intent i = new Intent(this, GroupDetailsActivity.class);
        String groupId = (String) view.getTag();
        i.putExtra(GroupDetailsActivity.GROUP_ID, groupId);
        Log.v(tag, "put extra groupid: " + groupId);
        startActivity(i);
    }

}
