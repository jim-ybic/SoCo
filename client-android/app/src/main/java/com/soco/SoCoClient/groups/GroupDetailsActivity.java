package com.soco.SoCoClient.groups;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.groups.ui.GroupDetailsTabsAdapter;

public class GroupDetailsActivity extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener{

    String tag = "GroupDetailActivity";

    private ViewPager viewPager;
    private GroupDetailsTabsAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

    static final String UPCOMING = "Upcoming";
    static final String PASTEVENTS = "Past Events";

    SocoApp socoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        socoApp = (SocoApp) getApplicationContext();
        viewPager = (ViewPager) findViewById(R.id.pager);

        setActionbar();

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

        mAdapter = new GroupDetailsTabsAdapter(getSupportFragmentManager());
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

//        Log.v(tag, "set starting tab");
//        if(socoApp.eventGroupsBuddiesTabIndex == 0)
//            actionBar.selectTab(tabGroups);
//        else if(socoApp.eventGroupsBuddiesTabIndex == 1)
//            actionBar.selectTab(tabBuddies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_detail, menu);
        return true;
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
}
