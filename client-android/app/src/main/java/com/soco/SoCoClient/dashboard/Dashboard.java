package com.soco.SoCoClient.dashboard;

//import info.androidhive.tabsswipe.contactsAdapter.TabsPagerAdapter;
//import info.androidhive.tabsswipe.R;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.support.v7.app.ActionBar.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.buddies.AddBuddyActivity;
import com.soco.SoCoClient.buddies.AllBuddyMatchesActivity;
import com.soco.SoCoClient.buddies.CommonEventsActivity;
import com.soco.SoCoClient.common.ui.card.model.Orientations;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.allevents.AllEventsActivity;
import com.soco.SoCoClient.events.comments.EventCommentsActivity;
import com.soco.SoCoClient.events.common.EventDetailsActivity;
import com.soco.SoCoClient.events.common.EventGroupsBuddiesActivity;
import com.soco.SoCoClient.events.common.JoinEventActivity;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.model.ui.EventCardContainer;
import com.soco.SoCoClient.events.model.ui.EventCardModel;
import com.soco.SoCoClient.events.model.ui.EventCardStackAdapter;
import com.soco.SoCoClient.events.photos.EventPhotosActivity;
import com.soco.SoCoClient.buddies.CommonBuddiesActivity;
import com.soco.SoCoClient.buddies.CommonGroupsActivity;
import com.soco.SoCoClient.events.service.DownloadSuggestedEventsService;
import com.soco.SoCoClient.userprofile.SettingsActivity;
import com.soco.SoCoClient.userprofile.UserEventsActivity;
import com.soco.SoCoClient.userprofile.UserProfileActivity;

import java.net.URL;

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

    // Tab titles
    private String[] tabs = {
            "Events",
            "Buddies",
//            "Stream",
//            "Messages"
    };

    private Toolbar toolbar;
    boolean isLiked;


    Bitmap mePhoto;
    ImageButton meButton;
    View actionbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        socoApp = (SocoApp) getApplicationContext();
        viewPager = (ViewPager) findViewById(R.id.pager);

        setActionbar();
        findViews();
        setMyProfilePhoto();

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

        Log.v(tag, "set actionbar background color");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));
        actionBar.setBackgroundDrawable(colorDrawable);

        Log.v(tag, "Adding tabs");
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
    }

    private void setMyProfilePhoto() {
        if(socoApp.loginViaFacebookResult) {
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
        }
        else{
            //todo
            //use find user's saved photo for profile

            Log.v(tag, "login via facebook is false, use default photo");
            Drawable image = getResources().getDrawable(R.drawable.eventbuddies_person1);
            meButton.setImageDrawable(image);
        }
    }

    private Handler downloadProfilehandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(tag, "handle message");
            meButton.setImageBitmap(mePhoto);
        };
    };

    void findViews(){
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

    public void refreshevents(View view){
        Log.v(tag, "refresh events from server");
        suggestedEventsView = socoApp.suggestedEventsView;
//        Log.d(tag, "Found root view: " + suggestedEventsView);

        Log.v(tag, "show progress dialog, fetch suggested events from server");
        pd = ProgressDialog.show(this, "Refreshing events from server", "Please wait...");
        new Thread(new Runnable(){
            public void run(){
                downloadEventsInBackgroud();
                downloadEventsHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    void downloadEventsInBackgroud() {
        if(socoApp.OFFLINE_MODE){
            Log.w(tag, "offline mode: bypass download events");
            socoApp.downloadSuggestedEventsResult = true;
            return;
        }

        Log.v(tag, "start download events service at backend");
        Intent i = new Intent(this, DownloadSuggestedEventsService.class);
        startService(i);

        Log.v(tag, "set response flag as false");
        socoApp.downloadSuggestedEventsResponse = false;

        Log.v(tag, "wait and check response flag");
        int count = 0;
        while(!socoApp.downloadSuggestedEventsResponse && count < WAIT_ITERATION) {   //wait for 10s
            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                        Log.e(tag, "Error in waiting");
                    }
                }
            }
            count++;
        }
    }

    Handler downloadEventsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");

            if(socoApp.downloadSuggestedEventsResult){
                if(socoApp.OFFLINE_MODE){
                    Log.w(tag, "offline mode, bypassed downloaded events");
                }
                else {
                    Log.d(tag, "download suggested event - success");
                    Toast.makeText(getApplicationContext(), socoApp.suggestedEvents.size() + " events downloaded.", Toast.LENGTH_SHORT).show();
                    initCards(suggestedEventsView);
                }
            }
            else{
                Log.e(tag, "download suggested event fail, notify user");
                Toast.makeText(getApplicationContext(), "Download events error, please try again later.", Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();
        }
    };

    void initCards(View rootView){
        Log.v(tag, "start event card init");

        socoApp.mEventCardContainer = (EventCardContainer) rootView.findViewById(R.id.eventcards);
        socoApp.mEventCardContainer.setOrientation(Orientations.Orientation.Ordered);


//        mCardContainer.setOrientation(Orientations.Orientation.Ordered);
        Resources r = getResources();
//        SimpleCardStackAdapter eventCardStackAdapter = new SimpleCardStackAdapter(getActivity());
        socoApp.eventCardStackAdapter = new EventCardStackAdapter(this);
//        eventCardStackAdapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
//        eventCardStackAdapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
//        eventCardStackAdapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
//        CardModel card = new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1);


        if(socoApp.OFFLINE_MODE) {
            Log.w(tag, "insert testing events in offline mode for testing");
            for (int i = 0; i < 10; i++) {
                EventCardModel m = new EventCardModel();
                m.setOnClickListener(new EventCardModel.OnClickListener() {
                    @Override
                    public void OnClickListener() {
                        Log.v(tag, "I am pressing the card");
                    }
                });
                m.setOnCardDismissedListener(new EventCardModel.OnCardDismissedListener() {
                    @Override
                    public void onLike() {
                        Log.v(tag, "I like the card");
                        socoApp.currentEventIndex++;
                    }

                    @Override
                    public void onDislike() {
                        Log.v(tag, "I dislike the card");
                        socoApp.currentEventIndex++;
                    }
                });
                socoApp.eventCardStackAdapter.add(m);
            }
        }
        else {
            Log.v(tag, "normal online mode: insert downloaded event card");
            for (int i = 0; i < socoApp.suggestedEvents.size(); i++) {
                Event e = socoApp.suggestedEvents.get(i);

                EventCardModel eventCardModel = new EventCardModel();
//                    "Event #" + i,
//                    "Description goes here",
//                    r.getDrawable(R.drawable.picture3_crop));
                eventCardModel.setTitle(e.getTitle());
                eventCardModel.setAddress(e.getAddress());
                eventCardModel.setStart_date(e.getStart_date());
                eventCardModel.setEnd_date(e.getEnd_date());
                eventCardModel.setEnd_time(e.getEnd_time());

                eventCardModel.setOnClickListener(new EventCardModel.OnClickListener() {
                    @Override
                    public void OnClickListener() {
                        Log.v(tag, "I am pressing the card");
                    }
                });
                eventCardModel.setOnCardDismissedListener(new EventCardModel.OnCardDismissedListener() {
                    @Override
                    public void onLike() {
                        Log.v(tag, "I like the card");
                        socoApp.currentEventIndex++;
                    }

                    @Override
                    public void onDislike() {
                        Log.v(tag, "I dislike the card");
                        socoApp.currentEventIndex++;
                    }
                });
                socoApp.eventCardStackAdapter.add(eventCardModel);
            }
        }

        socoApp.mEventCardContainer.setAdapter(socoApp.eventCardStackAdapter);
        //card - end

        Log.v(tag, "set current event index");
        socoApp.currentEventIndex = 0;
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
        window.showAsDropDown(findViewById(R.id.mebutton));

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