package com.soco.SoCoClient.events.suggested;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.card.model.Orientations;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.model.ui.EventCardContainer;
import com.soco.SoCoClient.events.model.ui.EventCardModel;
import com.soco.SoCoClient.events.model.ui.EventCardStackAdapter;
import com.soco.SoCoClient.events.service.DownloadSuggestedEventsTask;


public class SuggestedEventsFragment extends Fragment
        implements View.OnClickListener, TaskCallBack {

    static String tag = "SuggestedEventsFragment";

    View rootView;
    Context context;
    SocoApp socoApp;
    ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        socoApp = (SocoApp) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(tag, "on create view");
        rootView = inflater.inflate(R.layout.fragment_suggested_events, container, false);

        Log.v(tag, "update global variable");
        socoApp.suggestedEventsView = rootView;

//        Log.v(tag, "show progress dialog, fetch suggested events from server");
//        pd = ProgressDialog.show(getActivity(),
//                context.getString(R.string.msg_downloading_events),
//                context.getString(R.string.msg_pls_wait));
//        new Thread(new Runnable(){
//            public void run(){
//                downloadEventsInBackgroud(getActivity());
//            }
//        }).start();
        downloadEventsInBackgroud(getActivity());

        return rootView;
    }

    private void downloadEventsInBackgroud(Context context) {
        Log.v(tag, "downloadEventsInBackgroud start");
        socoApp.downloadSuggestedEventsResult = false;
        new DownloadSuggestedEventsTask(context.getApplicationContext(), this).execute();
    }

    public void doneTask(Object o) {
        Log.v(tag, "handle receive message and dismiss dialog");
        if(socoApp.downloadSuggestedEventsResult){
            Log.v(tag, "download suggested event - success");
            Toast.makeText(getActivity().getApplicationContext(), socoApp.suggestedEvents.size() + " events downloaded.", Toast.LENGTH_SHORT).show();
            initEventCards(rootView, context, socoApp, getActivity());
        }
        else{
            Log.e(tag, "download suggested event fail, notify user");
            Toast.makeText(getActivity().getApplicationContext(), "Download events error, please try again later.", Toast.LENGTH_SHORT).show();
        }
//        pd.dismiss();
    }

    public void initEventCards(final View rootView, Context context, final SocoApp socoApp, Activity activity){
        Log.v(tag, "start event card init");

        socoApp.mEventCardContainer = (EventCardContainer) rootView.findViewById(R.id.eventcards);
        socoApp.mEventCardContainer.setOrientation(Orientations.Orientation.Ordered);

//        mCardContainer.setOrientation(Orientations.Orientation.Ordered);
        Resources r = context.getResources();
//        SimpleCardStackAdapter eventCardStackAdapter = new SimpleCardStackAdapter(getActivity());
        socoApp.eventCardStackAdapter = new EventCardStackAdapter(activity);

        Log.v(tag, "normal online mode: insert downloaded event card");
        for (int i = 0; i < socoApp.suggestedEvents.size(); i++) {
            Event e = socoApp.suggestedEvents.get(i);

            EventCardModel eventCardModel = new EventCardModel();
            eventCardModel.setEvent(e);

            //todo: use the above event object to set/get event attribute,
            //todo: and remove all the below statements
            eventCardModel.setTitle(e.getTitle());
            eventCardModel.setAddress(e.getAddress());
            eventCardModel.setStart_date(e.getStart_date());
            eventCardModel.setStart_time(e.getStart_time());
            eventCardModel.setEnd_date(e.getEnd_date());
            eventCardModel.setEnd_time(e.getEnd_time());
            eventCardModel.setNumber_of_comments(e.getNumber_of_comments());
            eventCardModel.setNumber_of_likes(e.getNumber_of_likes());
            eventCardModel.setCategories(e.getCategories());

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
                    if(socoApp.suggestedEvents.size()==socoApp.currentEventIndex){
                        Log.v(tag, "show progress dialog, fetch suggested events from server");
                        pd = ProgressDialog.show(getActivity(), "Downloading events", "Please wait...");
                        new Thread(new Runnable(){
                            public void run(){
                                downloadEventsInBackgroud(getActivity());
                            }
                        }).start();
                    }
                }
                @Override
                public void onDislike() {
                    Log.v(tag, "I dislike the card");
                    socoApp.currentEventIndex++;
                    if(socoApp.suggestedEvents.size()==socoApp.currentEventIndex){
                        Log.v(tag, "show progress dialog, fetch suggested events from server");
                        pd = ProgressDialog.show(getActivity(), "Downloading events", "Please wait...");
                        new Thread(new Runnable(){
                            public void run(){
                                downloadEventsInBackgroud(getActivity());
                            }
                        }).start();
                    }
                }
            });
            socoApp.eventCardStackAdapter.add(eventCardModel);
        }

        socoApp.mEventCardContainer.setAdapter(socoApp.eventCardStackAdapter);
        Log.v(tag, "set current event index");
        socoApp.currentEventIndex = 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}
