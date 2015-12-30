package com.soco.SoCoClient.dashboard;

//import info.androidhive.tabsswipe.contactsAdapter.TabsPagerAdapter;
//import info.androidhive.tabsswipe.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.buddies.allbuddies.AllBuddiesActivity;
import com.soco.SoCoClient.buddies.service.AddBuddyTask;
import com.soco.SoCoClient.buddies.suggested.SuggestedBuddiesFragment;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.allevents.AllEventsActivity;
import com.soco.SoCoClient.events.comments.EventCommentsActivity;
import com.soco.SoCoClient.events.common.EventDetailsActivity;
import com.soco.SoCoClient.events.common.EventGroupsBuddiesActivity;
import com.soco.SoCoClient.events.common.JoinEventActivity;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.photos.EventPhotosActivity;
import com.soco.SoCoClient.events.service.LikeEventTask;
import com.soco.SoCoClient.events.suggested.SuggestedEventsFragment;
import com.soco.SoCoClient.groups.AllGroupsActivity;
import com.soco.SoCoClient.userprofile.SettingsActivity;
import com.soco.SoCoClient.userprofile.UserProfileActivity;
import com.soco.SoCoClient.userprofile.model.User;
import java.net.URL;

public class Dashboard extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener {

    String tag = "Dashboard";

    private ViewPager viewPager;
    private DashboardTabsAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

    SocoApp socoApp;
    Activity activity;

    Bitmap mePhoto;
    ImageButton meButton;
    View actionbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        activity = this;
        socoApp = (SocoApp) getApplicationContext();
        viewPager = (ViewPager) findViewById(R.id.pager);

        Log.v(tag, "initial screen resolution to app");
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
//        IconUrlUtil = size.x;
        display.getRealSize(size);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        Log.v(tag, "init icon downloader: " + size + ", " + cacheSize);
        IconUrlUtil.initialForIconDownloader(Math.min(size.x, size.y), cacheSize);


        setActionbar();
        findViews();
        setMyProfilePhoto();
    }

    private void setActionbar() {
        Log.v(tag, "set custom actionbar");
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
        actionbarView = getLayoutInflater().inflate(R.layout.actionbar_dashboard, null);
        LayoutParams layout = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionbarView, layout);
        Toolbar parent = (Toolbar) actionbarView.getParent();
        Log.v(tag, "remove margin in actionbar area");
        parent.setContentInsetsAbsolute(0, 0);

        Log.v(tag, "set my icon photo with userid: " + socoApp.user_id);
        ImageButton ib = (ImageButton) actionbarView.findViewById(R.id.mebutton);
        User u = new User();
        u.setUser_id(socoApp.user_id);
        Log.v(tag, "ib: " + ib + ", user: " + u.toString());
        IconUrlUtil.setImageForButtonNormal(getResources(), ib, u.getUser_icon_url());

        Log.v(tag, "set actionbar background color");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));
        actionBar.setBackgroundDrawable(colorDrawable);

        Log.v(tag, "Adding tabs");
        android.support.v7.app.ActionBar.Tab tabEvents = actionBar.newTab().setText(R.string.dashboard_tab_events).setTabListener(this);
        actionBar.addTab(tabEvents);
        android.support.v7.app.ActionBar.Tab tabBuddies= actionBar.newTab().setText(R.string.dashboard_tab_buddies).setTabListener(this);
        actionBar.addTab(tabBuddies);

        Log.v(tag, "set starting tab");
        if(socoApp.TEST_BUDDY_TAB_FIRST)
            actionBar.selectTab(tabBuddies);
        else
            actionBar.selectTab(tabEvents);

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

        Log.d(tag, "set dashboard title " + R.string.dashboard_title);
        TextView title = (TextView) actionbarView.findViewById(R.id.dashboard_title);
        title.setText(R.string.dashboard_title);
    }

    private void setMyProfilePhoto() {
        if (socoApp.loginViaFacebookResult) {
            Log.v(tag, "login via facebook result is true, get user facebook profile photo");
            new Thread() {
                public void run() {
                    Log.d(tag, "get facebook user profile picture");
                    try {
                        URL imageUrl = new URL("https://graph.facebook.com/" + "10153298013434285" + "/picture?type=normal");
                        mePhoto = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                        Log.d(tag, "me photo: " + mePhoto);

                        //todo
                        //upload image to server
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    downloadProfilehandler.sendEmptyMessage(0);
                }
            }.start();
        } else {
            //use find user's saved photo for profile
            Log.v(tag, "set my icon photo with userid: " + socoApp.user_id);
            ImageButton ib = (ImageButton) actionbarView.findViewById(R.id.mebutton);
            User u = new User();
            u.setUser_id(socoApp.user_id);
//            Log.v(tag, "ib: " + ib + ", user: " + u.toString());
            IconUrlUtil.setImageForButtonNormal(getResources(), ib, UrlUtil.getUserIconUrl(socoApp.user_id));
        }
    }

    private Handler downloadProfilehandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(tag, "handle message");
            meButton.setImageBitmap(mePhoto);
        }

        ;
    };

    void findViews() {
        Log.v(tag, "custom view: " + this.actionbarView);
        meButton = (ImageButton) this.actionbarView.findViewById(R.id.mebutton);
        Log.v(tag, "me button: " + meButton);
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

    public void eventphotos(View view) {
        Log.d(tag, "show all event photos");
        Intent i = new Intent(getApplicationContext(), EventPhotosActivity.class);
        startActivity(i);
    }

    public void eventcomments(View view) {
        Log.d(tag, "show all event comments");
        Intent i = new Intent(getApplicationContext(), EventCommentsActivity.class);
        startActivity(i);
    }

    public void eventgroups(View view) {
        Log.d(tag, "show all event groups");
        socoApp.eventGroupsBuddiesTabIndex = 0;
//        Intent i = new Intent(getApplicationContext(), EventOrganizersActivity.class);
        Intent i = new Intent(getApplicationContext(), EventGroupsBuddiesActivity.class);
        Event event = socoApp.getCurrentSuggestedEvent();
        i.putExtra(EventGroupsBuddiesActivity.EVENT_ID, event.getId());
        Log.v(tag, "current event id: " + event.getId());
        startActivity(i);
    }

    public void eventbuddies(View view) {
        Log.d(tag, "show all event buddies");
        socoApp.eventGroupsBuddiesTabIndex = 1;
        Intent i = new Intent(getApplicationContext(), EventGroupsBuddiesActivity.class);
        Event event = socoApp.getCurrentSuggestedEvent();
        i.putExtra(EventGroupsBuddiesActivity.EVENT_ID, event.getId());
        Log.v(tag, "current event id: " + event.getId());
        startActivity(i);
    }

    public void userevents(View view) {
        Log.d(tag, "show user's events (common or joined)");
        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
        User u = socoApp.getCurrentSuggestedBuddy();
        i.putExtra(User.USER_ID, u.getUser_id());
        i.putExtra(Config.USER_PROFILE_TAB_INDEX, Config.USER_PROFILE_TAB_INDEX_EVENTS);
        startActivity(i);
    }

    public void usergroups(View view) {
        Log.d(tag, "show user's groups (common or joined)");
        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
        User u = socoApp.getCurrentSuggestedBuddy();
        i.putExtra(User.USER_ID, u.getUser_id());
        i.putExtra(Config.USER_PROFILE_TAB_INDEX, Config.USER_PROFILE_TAB_INDEX_GROUPS);
        startActivity(i);
    }

    public void eventdetails(View view) {
        Log.d(tag, "event detail");
        Intent i = new Intent(getApplicationContext(), EventDetailsActivity.class);
        Event event = socoApp.getCurrentSuggestedEvent();
        i.putExtra(Event.EVENT_ID, event.getId());
        startActivity(i);
    }

    public void allevents(View view) {
        Log.v(tag, "show all events");
        Intent i = new Intent(getApplicationContext(), AllEventsActivity.class);
        startActivity(i);
    }

    public void likeevent(View view) {
        Log.v(tag, "tap like event");

        //update the event item in the SocoApp
        SocoApp socoApp = (SocoApp) getApplicationContext();
        Event event = socoApp.getCurrentSuggestedEvent();
        Button button = (Button) view.findViewById(R.id.likeevent);
        boolean isLiked = button.isActivated();
        LikeEventTask let = new LikeEventTask(SocoApp.user_id, SocoApp.token, event, button, isLiked);
        let.execute();
    }

    public void joinevent(View view) {
        Log.v(tag, "tap join event");
        Intent i = new Intent(getApplicationContext(), JoinEventActivity.class);
        Event event = socoApp.getCurrentSuggestedEvent();
        i.putExtra(Event.EVENT_ID, Long.toString(event.getId()));
        startActivity(i);
    }

    public void me(View view) {
        Log.v(tag, "tap me: popup menu window");

        View popupView = getLayoutInflater().inflate(R.layout.popup_window, null);
        PopupWindow window = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        window.showAsDropDown(findViewById(R.id.mebutton));
        setPopupListeners(window, popupView);
    }

    void setPopupListeners(final PopupWindow window, View view) {
        TextView myprofile = (TextView) view.findViewById(R.id.popup_profile);
        Log.d(tag, "myprofile: " + myprofile);
        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                Log.v(tag, "show my profile");
                Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                i.putExtra(Config.USER_PROFILE_TAB_INDEX, Config.USER_PROFILE_TAB_INDEX_PROFILE);
                i.putExtra(User.USER_ID,SocoApp.user_id);
                startActivity(i);
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
                Intent i = new Intent(getApplicationContext(), AllBuddiesActivity.class);
                startActivity(i);
            }
        });

        TextView allgroups = (TextView) view.findViewById(R.id.popup_allgroups);
        Log.d(tag, "allgroups: " + allgroups);
        allgroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                Log.v(tag, "show all groups");
                Intent i = new Intent(getApplicationContext(), AllGroupsActivity.class);
                startActivity(i);
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

    public void allbuddymatches(View view) {
        Log.v(tag, "show all buddy matches");
        Intent i = new Intent(getApplicationContext(), AllBuddiesActivity.class);
        startActivity(i);
    }

    public void addbuddy(View view) {
        Log.v(tag, "add buddy");
        TextView tv = (TextView) view.findViewById(R.id.addFriend);
        if (tv.isEnabled()) {
//            String user_id = (String) view.getTag();
            AddBuddyTask task = new AddBuddyTask(tv);
            task.execute(socoApp.getCurrentSuggestedBuddy().getUser_id());
        }
    }

    public void userdetails(View view) {
        Log.v(tag, "show buddy details");
        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
        User u = socoApp.getCurrentSuggestedBuddy();
        i.putExtra(User.USER_ID, u.getUser_id());
        startActivity(i);
    }

    public void buddydetails(View view) {
        Log.v(tag, "show buddy details");
        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
        String user_id = (String)view.getTag();
        i.putExtra(User.USER_ID, user_id);
        startActivity(i);
    }

}