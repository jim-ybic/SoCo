package com.soco.SoCoClient.events.allevents;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.CreateEventActivity;
import com.soco.SoCoClient.events.details.EventDetailsActivity;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.userprofile.UserProfileActivity;
import com.soco.SoCoClient.userprofile.model.User;
import com.soco.SoCoClient.userprofile.task.DownloadEventsTask;

import java.util.ArrayList;
import java.util.List;

public class AllEventsFragment extends Fragment
        implements TaskCallBack {

    static final String tag = "AllEventsActivity";
    static final String EVENT_CATEGORY_ALL = "all";
    static final String EVENT_CATEGORY_BUSINESS = "business";
    static final String EVENT_CATEGORY_GAME = "game";
    static final String EVENT_CATEGORY_SOCIAL = "social";
    static final String EVENT_CATEGORY_LANGUAGE = "language";

    SwipeRefreshLayoutBottom swipeContainer;
    RecyclerView mRecyclerView;
    SimpleEventCardAdapter simpleEventCardAdapter;
    List<Event> events = new ArrayList<>();
    //    private final int MAX_EVENTS_TOSHOW=50;
    android.support.v7.app.ActionBar actionBar;
    View actionbarView;

    Context context;
    SocoApp socoApp;
    View rootView;
    ProgressDialog pd;
    TextView allEvents, businessEvents, gameEvents, socialEvents, languageEvents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        socoApp = (SocoApp) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(tag, "create fragment view.....");
        rootView = inflater.inflate(R.layout.activity_all_events, container, false);

        swipeContainer = (SwipeRefreshLayoutBottom) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                downloadEventsInBackgroud();
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        simpleEventCardAdapter = new SimpleEventCardAdapter(getActivity(), events);
        mRecyclerView.setAdapter(simpleEventCardAdapter);

        Log.v(tag, "show progress dialog, fetch events from server");
        pd = ProgressDialog.show(getActivity(),
                context.getString(R.string.msg_downloading_events),
                context.getString(R.string.msg_pls_wait));
        new Thread(new Runnable(){
            public void run(){
                downloadEventsInBackgroud();
            }
        }).start();

        setOnclickListener();
        return rootView;
    }


    public void doneTask(Object o) {
        if (o != null && o instanceof ArrayList) {
            ArrayList<Event> result = (ArrayList<Event>) o;
            for (Event e : result) {
                events.add(e);
            }
//            int currentSize = events.size();
//            if(currentSize>MAX_EVENTS_TOSHOW){
//                int toRemove = currentSize-MAX_EVENTS_TOSHOW;
//                if(toRemove>0){
//                    for(int i=0;i<toRemove;i++){
//                        events.remove(0);
//                    }
//                }
//            }
            Log.v(tag, "done task: ");
            simpleEventCardAdapter.notifyDataSetChanged();
        }
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
        pd.dismiss();
    }

    private void setOnclickListener(){
        allEvents = (TextView) rootView.findViewById(R.id.allEvents);
        allEvents.setTypeface(null, Typeface.BOLD); //default
        allEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(tag, "set on click listener: all");
                highlightCategory(EVENT_CATEGORY_ALL);
                //todo: load required events
            }
        });

        businessEvents = (TextView) rootView.findViewById(R.id.businessEvents);
        businessEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(tag, "set on click listener: business");
                highlightCategory(EVENT_CATEGORY_BUSINESS);
                //todo: load required events
            }
        });

        gameEvents = (TextView) rootView.findViewById(R.id.gamesEvents);
        gameEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(tag, "set on click listener: game");
                highlightCategory(EVENT_CATEGORY_GAME);
                //todo: load required events
            }
        });

        socialEvents = (TextView) rootView.findViewById(R.id.socialEvents);
        socialEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(tag, "set on click listener: social");
                highlightCategory(EVENT_CATEGORY_SOCIAL);
                //todo: load required events
            }
        });

        languageEvents = (TextView) rootView.findViewById(R.id.languageEvents);
        languageEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(tag, "set on click listener: language");
                highlightCategory(EVENT_CATEGORY_LANGUAGE);
                //todo: load required events
            }
        });

    }

    private void downloadEventsInBackgroud() {
        if (events != null && events.size() > 0) {
            Log.v(tag, "load more events");
            Event lastEvent = events.get(events.size() - 1);
            String[] params = new String[1];
            params[0] = DownloadEventsTask.START_EVENT_ID;
            DownloadEventsTask uet = new DownloadEventsTask(SocoApp.user_id, SocoApp.token, params, this);
            uet.execute(Long.toString(lastEvent.getId()));
        } else {
            Log.v(tag, "load initial events");
            DownloadEventsTask task = new DownloadEventsTask(SocoApp.user_id, SocoApp.token, this);
            task.execute();
        }
    }


    public void createevent(View view) {
        Log.v(tag, "create event");
        Intent i = new Intent(getActivity(), CreateEventActivity.class);
        startActivity(i);
    }

    public void eventdetails(View view) {
        Log.v(tag, "check event details");
        Intent i = new Intent(getActivity(), EventDetailsActivity.class);
        Long id = (Long) view.getTag();
        i.putExtra(EventDetailsActivity.EVENT_ID, id);
        startActivity(i);
    }

//    public void myevents(View view) {
//        Log.v(tag, "tap show my events");
//        String myUserid = socoApp.user_id;
//        Log.v(tag, "my userid: " + myUserid);
//
//        Intent i = new Intent(getActivity(), UserProfileActivity.class);
//        i.putExtra(User.USER_ID, myUserid);
//        i.putExtra(Config.USER_PROFILE_TAB_INDEX, Config.USER_PROFILE_TAB_INDEX_EVENTS);
//        startActivity(i);
//    }

    private void highlightCategory(String cat){
        if(cat.equals(EVENT_CATEGORY_ALL)){
            allEvents.setTypeface(null, Typeface.BOLD);
            businessEvents.setTypeface(null, Typeface.NORMAL);
            gameEvents.setTypeface(null, Typeface.NORMAL);
            socialEvents.setTypeface(null, Typeface.NORMAL);
            languageEvents.setTypeface(null, Typeface.NORMAL);
        }
        else if(cat.equals(EVENT_CATEGORY_BUSINESS)){
            allEvents.setTypeface(null, Typeface.NORMAL);
            businessEvents.setTypeface(null, Typeface.BOLD);
            gameEvents.setTypeface(null, Typeface.NORMAL);
            socialEvents.setTypeface(null, Typeface.NORMAL);
            languageEvents.setTypeface(null, Typeface.NORMAL);
        }
        else if(cat.equals(EVENT_CATEGORY_GAME)){
            allEvents.setTypeface(null, Typeface.NORMAL);
            businessEvents.setTypeface(null, Typeface.NORMAL);
            gameEvents.setTypeface(null, Typeface.BOLD);
            socialEvents.setTypeface(null, Typeface.NORMAL);
            languageEvents.setTypeface(null, Typeface.NORMAL);
        }
        else if(cat.equals(EVENT_CATEGORY_SOCIAL)){
            allEvents.setTypeface(null, Typeface.NORMAL);
            businessEvents.setTypeface(null, Typeface.NORMAL);
            gameEvents.setTypeface(null, Typeface.NORMAL);
            socialEvents.setTypeface(null, Typeface.BOLD);
            languageEvents.setTypeface(null, Typeface.NORMAL);
        }
        else if(cat.equals(EVENT_CATEGORY_LANGUAGE)){
            allEvents.setTypeface(null, Typeface.NORMAL);
            businessEvents.setTypeface(null, Typeface.NORMAL);
            gameEvents.setTypeface(null, Typeface.NORMAL);
            socialEvents.setTypeface(null, Typeface.NORMAL);
            languageEvents.setTypeface(null, Typeface.BOLD);
        }
    }

}
