package com.soco.SoCoClient.dashboard;

//import info.androidhive.tabsswipe.contactsAdapter.TabsPagerAdapter;
//import info.androidhive.tabsswipe.R;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.support.v7.app.ActionBar.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.buddies.AddBuddyActivity;
import com.soco.SoCoClient.buddies.AllBuddyMatchesActivity;
import com.soco.SoCoClient.buddies.CommonEventsActivity;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.allevents.AllEventsActivity;
import com.soco.SoCoClient.events.comments.EventCommentsActivity;
import com.soco.SoCoClient.events.common.EventBuddiesActivity;
import com.soco.SoCoClient.events.common.EventDetailsActivity;
import com.soco.SoCoClient.events.common.EventGroupsBuddiesActivity;
import com.soco.SoCoClient.events.common.JoinEventActivity;
import com.soco.SoCoClient.events.photos.EventPhotosActivity;
import com.soco.SoCoClient.buddies.CommonBuddiesActivity;
import com.soco.SoCoClient.buddies.CommonGroupsActivity;
import com.soco.SoCoClient.userprofile.SettingsActivity;
import com.soco.SoCoClient.userprofile.UserEventsActivity;
import com.soco.SoCoClient.userprofile.UserProfileActivity;

public class Dashboard extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener {

    String tag = "Dashboard";

    private ViewPager viewPager;
    private DashboardTabsAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

    SocoApp socoApp;

    // Tab titles
    private String[] tabs = {
            "Events",
            "Buddies",
//            "Stream",
//            "Messages"
    };

    private Toolbar toolbar;

    boolean isLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        socoApp = (SocoApp) getApplicationContext();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
//        setSupportActionBar(toolbar);

        //use custom toolbar
//        toolbar = (Toolbar) findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);

        //set background color
//        viewPager.setBackgroundColor(Color.WHITE);

        actionBar = getSupportActionBar();
        if(actionBar == null){
            Log.e(tag, "Cannot get action bar object");
            return;
        }

        mAdapter = new DashboardTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        Log.v(tag, "set actionbar custom view");
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowCustomEnabled(true);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_dashboard, null);
        LayoutParams layout = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, layout);
        Toolbar parent = (Toolbar) customView.getParent();
        Log.v(tag, "remove margin in actionbar area");
        parent.setContentInsetsAbsolute(0, 0);


        Log.v(tag, "set actionbar background color");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));
        actionBar.setBackgroundDrawable(colorDrawable);

        Log.v(tag, "Adding tabs");
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

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

    void findViews(){
        Log.v(tag, "find views");

        //todo
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

    public void eventphotos (View view){
        Log.d(tag, "show all event photos");
        Intent i = new Intent(getApplicationContext(), EventPhotosActivity.class);
        startActivity(i);
    }

    public void eventcomments (View view){
        Log.d(tag, "show all event comments");
        Intent i = new Intent(getApplicationContext(), EventCommentsActivity.class);
        startActivity(i);
    }

    public void eventgroups(View view){
        Log.d(tag, "show all event groups");
        socoApp.eventGroupsBuddiesTabIndex = 0;
//        Intent i = new Intent(getApplicationContext(), EventOrganizersActivity.class);
        Intent i = new Intent(getApplicationContext(), EventGroupsBuddiesActivity.class);
        startActivity(i);

    }

    public void eventbuddies(View view){
        Log.d(tag, "show all event buddies");
        socoApp.eventGroupsBuddiesTabIndex = 1;
        Intent i = new Intent(getApplicationContext(), EventGroupsBuddiesActivity.class);
        startActivity(i);
    }

    public void commongroups (View view){
        Log.d(tag, "show all common groups");
        Intent i = new Intent(getApplicationContext(), CommonGroupsActivity.class);
        startActivity(i);
    }

    public void commonbuddies (View view){
        Log.d(tag, "show all common buddies");
        Intent i = new Intent(getApplicationContext(), CommonBuddiesActivity.class);
        startActivity(i);
    }

    public void commonevents (View view){
        Log.d(tag, "show all common events");
        Intent i = new Intent(getApplicationContext(), CommonEventsActivity.class);
        startActivity(i);
    }

    public void personevents (View view){
        Log.v(tag, "tap show the person's upcoming events");
        Intent i = new Intent(getApplicationContext(), UserEventsActivity.class);
        startActivity(i);
    }

    public void eventdetails (View view){
        Log.d(tag, "event detail");
        Intent i = new Intent(getApplicationContext(), EventDetailsActivity.class);
        startActivity(i);
    }

    public void allevents(View view){
        Log.v(tag, "show all events");
        Intent i = new Intent(getApplicationContext(), AllEventsActivity.class);
        startActivity(i);
    }

    public void likeevent(View view){
        Log.v(tag, "tap like event");

//        isLiked = false;

//        //test
//        ((TextView) findViewById(R.id.viewallevents)).setText("updated");
//
//        socoApp.eventCardStackAdapter.getCardModel(0).setTitle("settitle");
//        socoApp.mEventCardContainer.setAdapter(socoApp.eventCardStackAdapter);

        //todo
        //check if user has liked this event before
        //update isliked value

//        Button button = (Button) findViewById(R.id.likeevent);
//        Log.d(tag, "button text: " + button.getText().toString());
//        if(isLiked){
//
//            Toast.makeText(getApplicationContext(), "No like.", Toast.LENGTH_SHORT).show();
//
//            Button p1_button = (Button)findViewById(R.id.likeevent);
//            p1_button.setText("No like");
//
//            ((Button) view).setCompoundDrawables(getResources().getDrawable(R.drawable.eventcard_nolike), null, null, null);
////            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.eventcard_nolike, 0, 0, 0);
////            button.setText("No like");
//
//            isLiked = false;
////            button.invalidate();
//            //todo
//            //update to server
//        }
//        else {
//            Toast.makeText(getApplicationContext(), "Like this event.", Toast.LENGTH_SHORT).show();
//
//            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.eventcard_liked, 0, 0, 0);
//            button.setText("Liked");
//            isLiked = true;
//        }

        //todo
        //send like signal to server
    }

    public void joinevent(View view){
        Log.v(tag, "tap join event");
        Intent i = new Intent(getApplicationContext(), JoinEventActivity.class);
        startActivity(i);
    }

    public void me(View view){
        Log.v(tag, "tap me: popup menu window");

        View popupView = getLayoutInflater().inflate(R.layout.popup_window, null);
        PopupWindow window = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        window.showAsDropDown(findViewById(R.id.me));

//        View layout = getLayoutInflater().inflate(R.layout.popup_window, null);
        setPopupListeners(window, popupView);
    }

    void setPopupListeners(final PopupWindow window, View view){

        TextView myprofile = (TextView) view.findViewById(R.id.popup_profile);
        Log.d(tag, "myprofile: " + myprofile);
        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                Log.v(tag, "show my profile");

                //todo

            }
        });

        TextView allevents = (TextView) view.findViewById(R.id.popup_allevents);
        Log.d(tag, "allevents: " + allevents);
        allevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                Log.v(tag, "show all events");
                Intent i = new Intent(getApplicationContext(), AllEventsActivity.class);
                startActivity(i);
            }
        });

        TextView allbuddies = (TextView) view.findViewById(R.id.popup_allbuddies);
        Log.d(tag, "allbuddies: " + allbuddies);
        allbuddies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                Log.v(tag, "show all buddies");

                //todo

            }
        });

        TextView allgroups = (TextView) view.findViewById(R.id.popup_allgroups);
        Log.d(tag, "allgroups: " + allgroups);
        allgroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                Log.v(tag, "show all groups");

                //todo

            }
        });

        TextView settings = (TextView) view.findViewById(R.id.popup_settings);
        Log.d(tag, "settings: " + settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                Log.v(tag, "show settings");
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });

    }

    public void closecard(View view){
        Log.v(tag, "tap close event card");

        //todo
        //dismiss current card
    }

    public void allbuddymatches(View view){
        Log.v(tag, "show all buddy matches");
        Intent i = new Intent(getApplicationContext(), AllBuddyMatchesActivity.class);
        startActivity(i);
    }

    public void addbuddy(View view){
        Log.v(tag, "show add buddy");
        Intent i = new Intent(getApplicationContext(), AddBuddyActivity.class);
        startActivity(i);
    }

    public void buddydetails(View view){
        Log.v(tag, "show buddy details");
        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(i);
    }



}