package com.soco.SoCoClient.userprofile;

import android.app.ActionBar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.userprofile.ui.UserProfileTabsAdapter;
import com.soco.SoCoClient.common.util.SocoApp;

public class UserProfileActivity extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener{

    String tag = "UserProfileActivity";

    private ViewPager viewPager;
    private UserProfileTabsAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

    static final String PROFILE = "Profile";
    static final String GROUPS = "Groups";
    static final String EVENTS = "Events";

    SocoApp socoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        socoApp = (SocoApp) getApplicationContext();

//        Log.v(tag, "set activity title as event title");
//        if(socoApp.OFFLINE_MODE)
//            setTitle("Sample Event Title");
//        else
//            setTitle(socoApp.suggestedEvents.get(socoApp.currentEventIndex).getTitle());

        viewPager = (ViewPager) findViewById(R.id.pager);
        setActionbar();

//        Log.v(tag, "set action bar tab background color");
//        actionBar.setStackedBackgroundDrawable(colorDrawable);

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

//        Log.v(tag, "set actionbar background color");
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));
//        actionBar.setBackgroundDrawable(colorDrawable);

        Log.v(tag, "Adding tabs");
        android.support.v7.app.ActionBar.Tab tabProfile = actionBar.newTab().setText(PROFILE).setTabListener(this);
        actionBar.addTab(tabProfile);
        android.support.v7.app.ActionBar.Tab tabGroups = actionBar.newTab().setText(GROUPS).setTabListener(this);
        actionBar.addTab(tabGroups);
        android.support.v7.app.ActionBar.Tab tabEvents = actionBar.newTab().setText(EVENTS).setTabListener(this);
        actionBar.addTab(tabEvents);

//        Log.v(tag, "set starting tab");
//        if(socoApp.eventGroupsBuddiesTabIndex == 0)
//            actionBar.selectTab(tabGroups);
//        else if(socoApp.eventGroupsBuddiesTabIndex == 1)
//            actionBar.selectTab(tabBuddies);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_activity_user_profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void close(View view){
        Log.v(tag, "tap on close");
        finish();
    }

}
