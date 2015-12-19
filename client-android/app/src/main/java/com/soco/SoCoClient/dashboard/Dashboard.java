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

//import android.widget.Toolbar;

public class Dashboard extends ActionBarActivity implements
        android.support.v7.app.ActionBar.TabListener {

    String tag = "Dashboard";

    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 10;
    static final int THOUSAND = 1000;

    private ViewPager viewPager;
    private DashboardTabsAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;

    SocoApp socoApp;
    ProgressDialog pd;
    View suggestedEventsView;
    View suggestedBuddiesView;
    Activity activity;

//    // Tab titles
//    private String[] tabs = {
//            "Events",
//            "Buddies",
////            "Stream",
////            "Messages"
//    };

    static final String EVENTS = "Events";
    static final String BUDDIES = "Buddies";

    private Toolbar toolbar;
    boolean isLiked;


    Bitmap mePhoto;
    ImageButton meButton;
    View actionbarView;
    boolean isFirstDownloadEvent = true;
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
        //set background color
//        viewPager.setBackgroundColor(Color.WHITE);

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
        android.support.v7.app.ActionBar.Tab tabEvents = actionBar.newTab().setText(EVENTS).setTabListener(this);
        actionBar.addTab(tabEvents);
        android.support.v7.app.ActionBar.Tab tabBuddies= actionBar.newTab().setText(BUDDIES).setTabListener(this);
        actionBar.addTab(tabBuddies);
//        for (String tab_name : tabs) {
//            actionBar.addTab(actionBar.newTab().setText(tab_name)
//                    .setTabListener(this));
//        }

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
        Log.v(tag, "find views");

//        Window window = getWindow();
//        View v = window.getDecorView();
//        int resId = getResources().getIdentifier("action_bar_container", "id", "android");
//        Log.d(tag, "resid: " + resId);
//        View actionbarView = v.findViewById(resId);
//        Log.d(tag, "actionbar view: " + actionbarView);
//        meButton = (ImageButton) actionbarView.findViewById(R.id.mebutton);
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

//    public void commongroups (View view){
//        Log.d(tag, "show all common groups");
//        Intent i = new Intent(getApplicationContext(), CommonGroupsActivity.class);
//        startActivity(i);
//    }

//    public void commonbuddies (View view){
//        Log.d(tag, "show all common buddies");
//        Intent i = new Intent(getApplicationContext(), CommonBuddiesActivity.class);
//        startActivity(i);
//    }

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

//
//    public void personevents (View view){
//        Log.v(tag, "tap show the person's upcoming events");
//        Intent i = new Intent(getApplicationContext(), UserEventsActivity.class);
//        startActivity(i);
//    }

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

    public void refreshevents(View view) {
        Log.v(tag, "refresh events from server");
        suggestedEventsView = socoApp.suggestedEventsView;

        Log.v(tag, "show progress dialog, fetch suggested events from server");
        pd = ProgressDialog.show(this, "Refreshing events", "Please wait...");
        new Thread(new Runnable() {
            public void run() {
                SuggestedEventsFragment.downloadEventsInBackgroud(getApplicationContext(), socoApp);
                downloadEventsHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    Handler downloadEventsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");
            if (socoApp.downloadSuggestedEventsResponse && socoApp.downloadSuggestedEventsResult) {
                Log.d(tag, "download suggested event - success");
                Toast.makeText(getApplicationContext(), socoApp.suggestedEvents.size() + " events downloaded.", Toast.LENGTH_SHORT).show();
                if(isFirstDownloadEvent) {
                    SuggestedEventsFragment.initEventCards(suggestedEventsView, getApplicationContext(), socoApp, activity);
                }else{
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                Log.e(tag, "download suggested event fail, notify user");
                Toast.makeText(getApplicationContext(), "Download events error, please try again later.", Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }
    };

    public void refreshbuddies(View view) {
        Log.v(tag, "refresh buddies from server");
        suggestedBuddiesView = socoApp.suggestedBuddiesView;

        Log.v(tag, "show progress dialog, fetch suggested buddies from server");
        pd = ProgressDialog.show(this, "Refreshing buddies", "Please wait...");
        new Thread(new Runnable() {
            public void run() {
                SuggestedBuddiesFragment.downloadBuddiesInBackgroud(getApplicationContext(), socoApp);
                downloadBuddiesHandler.sendEmptyMessage(0);
            }
        }).start();

    }

    Handler downloadBuddiesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");
            if (socoApp.downloadSuggestedBuddiesResponse && socoApp.downloadSuggestedBuddiesResult) {
                Log.v(tag, "download suggested buddy - success");
                Toast.makeText(getApplicationContext(), socoApp.suggestedBuddies.size() + " buddies found.", Toast.LENGTH_SHORT).show();
                SuggestedBuddiesFragment.initBuddyCards(suggestedBuddiesView, getApplicationContext(), socoApp, activity);
            } else {
                Log.e(tag, "download suggested buddy fail, notify user");
                Toast.makeText(getApplicationContext(), "Download events error, please try again later.", Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }
    };

//    void downloadEventsInBackgroud() {
//        if(socoApp.OFFLINE_MODE){
//            Log.w(tag, "offline mode: bypass download events");
//            socoApp.downloadSuggestedEventsResult = true;
//            return;
//        }
//
//        Log.v(tag, "start download events service at backend");
//        Intent i = new Intent(this, DownloadSuggestedEventsService.class);
//        startService(i);
//
//        Log.v(tag, "set response flag as false");
//        socoApp.downloadSuggestedEventsResponse = false;
//
//        Log.v(tag, "wait and check response flag");
//        int count = 0;
//        while(!socoApp.downloadSuggestedEventsResponse && count < WAIT_ITERATION) {   //wait for 10s
//            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
//            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
//            while (System.currentTimeMillis() < endTime) {
//                synchronized (this) {
//                    try {
//                        wait(endTime - System.currentTimeMillis());
//                    } catch (Exception e) {
//                        Log.e(tag, "Error in waiting");
//                    }
//                }
//            }
//            count++;
//        }
//    }


//    void initEventCards(View rootView){
//        Log.v(tag, "start event card init");
//
//        socoApp.mEventCardContainer = (EventCardContainer) rootView.findViewById(R.id.eventcards);
//        socoApp.mEventCardContainer.setOrientation(Orientations.Orientation.Ordered);
//
//
////        mCardContainer.setOrientation(Orientations.Orientation.Ordered);
//        Resources r = getResources();
////        SimpleCardStackAdapter eventCardStackAdapter = new SimpleCardStackAdapter(getActivity());
//        socoApp.eventCardStackAdapter = new EventCardStackAdapter(this);
////        eventCardStackAdapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
////        eventCardStackAdapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
////        eventCardStackAdapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
////        CardModel card = new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1);
//
//
//        if(socoApp.OFFLINE_MODE) {
//            Log.w(tag, "insert testing events in offline mode for testing");
//            for (int i = 0; i < 10; i++) {
//                EventCardModel m = new EventCardModel();
//                m.setEvent(new Event());
//                m.setOnClickListener(new EventCardModel.OnClickListener() {
//                    @Override
//                    public void OnClickListener() {
//                        Log.v(tag, "I am pressing the card");
//                    }
//                });
//                m.setOnCardDismissedListener(new EventCardModel.OnCardDismissedListener() {
//                    @Override
//                    public void onLike() {
//                        Log.v(tag, "I like the card");
//                        socoApp.currentEventIndex++;
//                    }
//
//                    @Override
//                    public void onDislike() {
//                        Log.v(tag, "I dislike the card");
//                        socoApp.currentEventIndex++;
//                    }
//                });
//                socoApp.eventCardStackAdapter.add(m);
//            }
//        }
//        else {
//            Log.v(tag, "normal online mode: insert downloaded event card");
//            for (int i = 0; i < socoApp.suggestedEvents.size(); i++) {
//                Event e = socoApp.suggestedEvents.get(i);
//
//                EventCardModel eventCardModel = new EventCardModel();
////                    "Event #" + i,
////                    "Description goes here",
////                    r.getDrawable(R.drawable.picture3_crop));
//                eventCardModel.setTitle(e.getTitle());
//                eventCardModel.setAddress(e.getAddress());
//                eventCardModel.setStart_date(e.getStart_date());
//                eventCardModel.setStart_time(e.getStart_time());
//                eventCardModel.setEnd_date(e.getEnd_date());
//                eventCardModel.setEnd_time(e.getEnd_time());
//                eventCardModel.setNumber_of_comments(e.getNumber_of_comments());
//                eventCardModel.setNumber_of_likes(e.getNumber_of_likes());
//
//                eventCardModel.setEvent(e);
//
//                eventCardModel.setOnClickListener(new EventCardModel.OnClickListener() {
//                    @Override
//                    public void OnClickListener() {
//                        Log.v(tag, "I am pressing the card");
//                    }
//                });
//                eventCardModel.setOnCardDismissedListener(new EventCardModel.OnCardDismissedListener() {
//                    @Override
//                    public void onLike() {
//                        Log.v(tag, "I like the card");
//                        socoApp.currentEventIndex++;
//                    }
//
//                    @Override
//                    public void onDislike() {
//                        Log.v(tag, "I dislike the card");
//                        socoApp.currentEventIndex++;
//                    }
//                });
//                socoApp.eventCardStackAdapter.add(eventCardModel);
//            }
//        }
//
//        socoApp.mEventCardContainer.setAdapter(socoApp.eventCardStackAdapter);
//        //card - end
//
//        Log.v(tag, "set current event index");
//        socoApp.currentEventIndex = 0;
//    }


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
//        if(isLiked){
//            //trigger the cancel action for previous like
//            LikeUtil.updateLikeButtonStatus(button,event,!isLiked);
//            //send like signal to server
////            pd = ProgressDialog.show(this, "Revert Like event request...", "Please wait...");
//            new Thread(new Runnable(){
//                public void run(){
//                    revertLikeEventRequestInBackground(event_id);
//                    Message msg = new Message();
//                    msg.obj = event;
//                    revertLikeEventHandler.sendMessage(msg);
//                }
//            }).start();
//        }else {
//
//            //update the button: image + count
//            LikeUtil.updateLikeButtonStatus(button, event, !isLiked);
//
//            //send like signal to server
////            pd = ProgressDialog.show(this, "Like event request...", "Please wait...");
//            new Thread(new Runnable(){
//                public void run(){
//                    likeEventRequestInBackground(event_id);
//                    Message msg = new Message();
//                    msg.obj = event;
//                    likeEventHandler.sendMessage(msg);
//                }
//            }).start();
//        }
//    }
//    private void likeEventRequestInBackground(long event_id) {
//        Log.v(tag, "start like event service at back end");
//        Intent i = new Intent(this, LikeEventService.class);
//        i.putExtra(Event.EVENT_ID,event_id);
//        startService(i);
//
//        Log.v(tag, "set like response flag as false");
//        socoApp.likeEventResponse = false;
//        Log.v(tag, "wait and check status");
//        int count = 0;
//        while(!socoApp.likeEventResponse && count < WAIT_ITERATION) {   //wait for 10s
//            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
//            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
//            while (System.currentTimeMillis() < endTime) {
//                synchronized (this) {
//                    try {
//                        wait(endTime - System.currentTimeMillis());
//                    } catch (Exception e) {
//                        Log.e(tag, "Error in waiting");
//                    }
//                }
//            }
//            count++;
//        }
//    }
//
//    Handler likeEventHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Log.v(tag, "handle receive message ");
//
//            if(socoApp.likeEventResponse && socoApp.likeEventResult){
//                Log.d(tag, "Like event success");
//                ((Event) msg.obj).setIsLikedEvent(true);
////                Toast.makeText(getApplicationContext(), "Like event suceess.", Toast.LENGTH_SHORT).show();
////                finish();
//            }
//            else{
//                Log.e(tag, "like event fail, notify user");
//                if(socoApp.error_message != null && !socoApp.error_message.isEmpty())
//                    Toast.makeText(getApplicationContext(), socoApp.error_message, Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(getApplicationContext(), "Network error, please try again later.", Toast.LENGTH_SHORT).show();
//            }
//
////            pd.dismiss();
//        }
//    };
//    private void revertLikeEventRequestInBackground(long event_id) {
//        Log.v(tag, "start revert like event service at back end");
//        Intent i = new Intent(this, RevertLikeEventService.class);
//        i.putExtra(Event.EVENT_ID, event_id);
//        startService(i);
//
//        Log.v(tag, "set revert like response flag as false");
//        socoApp.revertLikeEventResponse = false;
//        Log.v(tag, "wait and check status");
//        int count = 0;
//        while(!socoApp.revertLikeEventResponse && count < WAIT_ITERATION) {   //wait for 10s
//            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
//            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
//            while (System.currentTimeMillis() < endTime) {
//                synchronized (this) {
//                    try {
//                        wait(endTime - System.currentTimeMillis());
//                    } catch (Exception e) {
//                        Log.e(tag, "Error in waiting");
//                    }
//                }
//            }
//            count++;
//        }
//    }
//
//    Handler revertLikeEventHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Log.v(tag, "handle receive message");
//
//            if(socoApp.revertLikeEventResponse && socoApp.revertLikeEventResult){
//                Log.d(tag, "revert like event success");
//                ((Event) msg.obj).setIsLikedEvent(false);
////                Toast.makeText(getApplicationContext(), "revert Like event suceess.", Toast.LENGTH_SHORT).show();
////                finish();
//            }
//            else{
//                Log.e(tag, "revert like event fail, notify user");
//                if(socoApp.error_message != null && !socoApp.error_message.isEmpty())
//                    Toast.makeText(getApplicationContext(), socoApp.error_message, Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(getApplicationContext(), "Network error, please try again later.", Toast.LENGTH_SHORT).show();
//            }
//
////            pd.dismiss();
//        }
//    };


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

//        View layout = getLayoutInflater().inflate(R.layout.popup_window, null);
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

    public void closeeventcard(View view) {
        Log.v(tag, "tap close event card");

        //todo
        //dismiss current card
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

//
//    private void addBuddyInBackground() {
//        Log.v(tag, "start add buddy service at back end");
//        Intent i = new Intent(this, AddBuddyService.class);
//        startService(i);
//
//        Log.v(tag, "set register response flag as false");
//        socoApp.addBuddyResponse = false;
//
//        Log.v(tag, "wait and check status");
//        int count = 0;
//        while(!socoApp.addBuddyResponse && count < WAIT_ITERATION) {   //wait for 10s
//            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
//            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
//            while (System.currentTimeMillis() < endTime) {
//                synchronized (this) {
//                    try {
//                        wait(endTime - System.currentTimeMillis());
//                    } catch (Exception e) {
//                        Log.e(tag, "Error in waiting");
//                    }
//                }
//            }
//            count++;
//        }
//    }
//
//    Handler addBuddyHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Log.v(tag, "handle receive message and dismiss dialog");
//
//            if(socoApp.addBuddyResponse && socoApp.addBuddyResult){
//                Log.d(tag, "add buddy success");
//                Toast.makeText(getApplicationContext(), "Suceess.", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Log.e(tag, "add buddy fail, notify user");
//                if(socoApp.error_message != null && !socoApp.error_message.isEmpty())
//                    Toast.makeText(getApplicationContext(), socoApp.error_message, Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(getApplicationContext(), "Network error, please try again later.", Toast.LENGTH_SHORT).show();
//            }
//
//            pd.dismiss();
//        }
//    };

    public void userdetails(View view) {
        Log.v(tag, "show buddy details");
        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
        User u = socoApp.getCurrentSuggestedBuddy();
        i.putExtra(User.USER_ID, u.getUser_id());
        startActivity(i);
    }


}