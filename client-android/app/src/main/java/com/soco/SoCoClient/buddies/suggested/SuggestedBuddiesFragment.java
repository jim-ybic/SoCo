package com.soco.SoCoClient.buddies.suggested;

//import info.androidhive.tabsswipe.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.buddies.service.DownloadSuggestedBuddiesService;
import com.soco.SoCoClient.buddies.service.DownloadSuggestedBuddiesTask;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.card.model.Orientations;
import com.soco.SoCoClient.buddies.suggested.ui.BuddyCardModel;
import com.soco.SoCoClient.buddies.suggested.ui.BuddyCardContainer;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.secondary.chat.model.SingleConversation;
import com.soco.SoCoClient.buddies.suggested.ui.BuddyCardStackAdapter;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class SuggestedBuddiesFragment extends Fragment
        implements View.OnClickListener, TaskCallBack {

    static String tag = "SuggestedBuddies";

    SocoApp socoApp;
    ProgressDialog pd;
    View rootView;
    Context context;
    ArrayList<SingleConversation> singleConversations;

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

        Log.v(tag, "create fragment view.....");
        rootView = inflater.inflate(R.layout.fragment_suggested_buddies, container, false);

        Log.v(tag, "update global variable");
        socoApp.suggestedBuddiesView = rootView;

        Log.v(tag, "show progress dialog, fetch suggested users from server");
        pd = ProgressDialog.show(getActivity(), "Downloading users", "Please wait...");
        new Thread(new Runnable(){
            public void run(){
                downloadBuddiesInBackgroud(getActivity());
            }
        }).start();

        return rootView;
    }

    public void downloadBuddiesInBackgroud(Context context) {
        socoApp.downloadSuggestedBuddiesResult = false;
        new DownloadSuggestedBuddiesTask(context.getApplicationContext(), this).execute();
    }

    public void doneTask(Object o){
        Log.v(tag, "handle receive message and dismiss dialog");
        if(socoApp.downloadSuggestedBuddiesResponse && socoApp.downloadSuggestedBuddiesResult){
            Log.v(tag, "download suggested buddy - success");
            Toast.makeText(getActivity().getApplicationContext(), socoApp.suggestedBuddies.size() + " buddies found.", Toast.LENGTH_SHORT).show();
            initBuddyCards(rootView, context, socoApp, getActivity());
        }
        else{
            Log.e(tag, "download suggested buddy fail, notify user");
            Toast.makeText(getActivity().getApplicationContext(), "Download buddies error, please try again later.", Toast.LENGTH_SHORT).show();
        }
        pd.dismiss();
    }

    public void initBuddyCards(View rootView, Context context, final SocoApp socoApp, Activity activity){
        Log.v(tag, "start buddy card init");

        BuddyCardContainer mBuddyCardContainer = (BuddyCardContainer) rootView.findViewById(R.id.personcards);
        mBuddyCardContainer.setOrientation(Orientations.Orientation.Ordered);

        Resources r = context.getResources();
        socoApp.buddyCardStackAdapter = new BuddyCardStackAdapter(activity);

        Log.v(tag, "normal online mode: insert downloaded buddies card");
        if(socoApp.suggestedBuddies!=null) {
            for (int i = 0; i < socoApp.suggestedBuddies.size(); i++) {
                int pos = socoApp.suggestedBuddies.size() - 1 - i;
                User u = socoApp.suggestedBuddies.get(pos);

                BuddyCardModel buddyCardModel = new BuddyCardModel();
                buddyCardModel.setUser(u);
                buddyCardModel.setOnClickListener(new BuddyCardModel.OnClickListener() {
                    @Override
                    public void OnClickListener() {
                        Log.v(tag, "I am pressing the card");
                    }
                });
                buddyCardModel.setOnCardDismissedListener(new BuddyCardModel.OnCardDismissedListener() {
                    @Override
                    public void onLike() {
                        Log.v(tag, "I like the card");
                        socoApp.currentBuddyIndex++;
                        if(socoApp.suggestedBuddies.size()==socoApp.currentBuddyIndex){
                            Log.v(tag, "show progress dialog, fetch suggested buddies from server");
                            pd = ProgressDialog.show(getActivity(), "Downloading buddies", "Please wait...");
                            new Thread(new Runnable(){
                                public void run(){
                                    downloadBuddiesInBackgroud(getActivity());
                                }
                            }).start();
                        }
                    }
                    @Override
                    public void onDislike() {
                        Log.v(tag, "I dislike the card");
                        socoApp.currentBuddyIndex++;
                        if(socoApp.suggestedBuddies.size()==socoApp.currentBuddyIndex){
                            Log.v(tag, "show progress dialog, fetch suggested buddies from server");
                            pd = ProgressDialog.show(getActivity(), "Downloading buddies", "Please wait...");
                            new Thread(new Runnable(){
                                public void run(){
                                    downloadBuddiesInBackgroud(getActivity());
                                }
                            }).start();
                        }
                    }
                });
                socoApp.buddyCardStackAdapter.add(buddyCardModel);
            }
        }else{
            Log.w(tag, "cannot download suggested buddies, notify user");
            Toast.makeText(getActivity().getApplicationContext(), "Download buddies error, please try again later.", Toast.LENGTH_SHORT).show();
        }
        mBuddyCardContainer.setAdapter(socoApp.buddyCardStackAdapter);

        Log.v(tag, "set current user index");
        socoApp.currentBuddyIndex = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


}