package com.soco.SoCoClient.buddies.suggested;

//import info.androidhive.tabsswipe.R;

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
import com.soco.SoCoClient.common.database.DataLoader;
import com.soco.SoCoClient.common.ui.card.model.Orientations;
import com.soco.SoCoClient.buddies.suggested.ui.BuddyCardModel;
import com.soco.SoCoClient.buddies.suggested.ui.BuddyCardContainer;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.secondary.chat.model.SingleConversation;
import com.soco.SoCoClient.buddies.suggested.ui.BuddyCardStackAdapter;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class SuggestedBuddiesFragment extends Fragment implements View.OnClickListener {

    static String tag = "SuggestedBuddies";


    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 5;
    static final int THOUSAND = 1000;

//    int pid;
//    String pid_onserver;
    SocoApp socoApp;
    ProgressDialog pd;
//    Profile profile;
//    DBManagerSoco dbManagerSoco;
//    ArrayList<Item> contactItems;
//    SectionEntryListAdapter contactsAdapter;

    //new code
    View rootView;
    Context context;
    DataLoader dataLoader;
    ArrayList<SingleConversation> singleConversations;

    BuddyCardContainer mBuddyCardContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();

        socoApp = (SocoApp) context;
        dataLoader = new DataLoader(context);

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
                downloadBuddiesInBackgroud();
                downloadBuddiesHandler.sendEmptyMessage(0);
            }
        }).start();
        initCards(rootView);
        return rootView;
    }

    void downloadBuddiesInBackgroud() {

        Log.v(tag, "start download users service at backend");
        Intent i = new Intent(getActivity(), DownloadSuggestedBuddiesService.class);
        getActivity().startService(i);

        Log.v(tag, "set response flag as false");
        socoApp.downloadSuggestedBuddiesResponse = false;

        Log.v(tag, "wait and check response flag");
        int count = 0;
        while(!socoApp.downloadSuggestedBuddiesResponse && count < WAIT_ITERATION) {   //wait for 10s
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

    Handler downloadBuddiesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");

            if(socoApp.downloadSuggestedBuddiesResult){
//                if(socoApp.OFFLINE_MODE){
//                    Log.w(tag, "offline mode, bypassed downloaded events");
//                }
//                else {
                    Log.v(tag, "download suggested buddy - success");
                    Toast.makeText(getActivity().getApplicationContext(), socoApp.suggestedBuddies.size() + " buddies found.", Toast.LENGTH_SHORT).show();
                    initCards(rootView);
//                }
            }
            else{
                Log.e(tag, "download suggested buddy fail, notify user");
                Toast.makeText(getActivity().getApplicationContext(), "Download events error, please try again later.", Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();
        }
    };

    void initCards(View rootView){
        Log.v(tag, "start buddy card init");

        mBuddyCardContainer = (BuddyCardContainer) rootView.findViewById(R.id.personcards);
        mBuddyCardContainer.setOrientation(Orientations.Orientation.Ordered);

        Resources r = getResources();
        socoApp.buddyCardStackAdapter = new BuddyCardStackAdapter(getActivity());

        Log.v(tag, "normal online mode: insert downloaded event card");
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
                    }

                    @Override
                    public void onDislike() {
                        Log.v(tag, "I dislike the card");
                        socoApp.currentBuddyIndex++;
                    }
                });
                socoApp.buddyCardStackAdapter.add(buddyCardModel);
            }
        }else{
            BuddyCardModel dummy = new BuddyCardModel();
            User u = new User();
            u.setUser_id("10101010101010");
            u.setUser_name("david");
            u.setLocation("Hong Kong");
            u.setCommon_group_name("JoggingHK");
            u.setNumber_common_group(1);
            u.setCommon_event_name("hiking");
            u.setNumber_common_event(2);
            u.setNumber_common_buddy(10);
            u.addInterest("Hiking");
            u.addInterest("Jogging");
            dummy.setUser(u);

            socoApp.buddyCardStackAdapter.add(dummy);
        }
        mBuddyCardContainer.setAdapter(socoApp.buddyCardStackAdapter);
        //card - end
        Log.v(tag, "set current user index");
        socoApp.currentBuddyIndex = 0;
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.add:
//                add();
//                break;
//            case R.id.personevents:
//                Log.d(tag, "show all event friends");
//                Intent ipe = new Intent(getActivity().getApplicationContext(), ActivityPersonEvents.class);
//                startActivity(ipe);
//                break;
//            case R.id.persongroups:
//                Log.d(tag, "show all event groups");
//                Intent ipg = new Intent(getActivity().getApplicationContext(), ActivityPersonGroups.class);
//                startActivity(ipg);
//                break;
//            case R.id.detail:
//                Log.d(tag, "pass this event");
//                break;
//            case R.id.detail:
//                Log.d(tag, "show person details");
//                Intent ipd = new Intent(getActivity().getApplicationContext(), UserProfileActivity.class);
//                startActivity(ipd);
//                break;
//            case R.id.add:
//                Log.d(tag, "add this friend");
//                Intent iaf = new Intent(getActivity().getApplicationContext(), AddFriendActivity.class);
//                startActivity(iaf);
//                break;
        }
    }


//    public void add(String email, String nickname){
////        String email = ((EditText) rootView.findViewById(R.id.email)).getText().toString();
//        Log.i(tag, "save into db new member " + email);
//        dbManagerSoco.saveContact(email, nickname);
//
//        //todo: send invitation to server and save contact id onserver
//        Log.d(tag, "send add friend request to server: " + email);
//        String url = UrlUtil.getAddFriendUrl(getActivity());
//        AddFriendTaskAsync task = new AddFriendTaskAsync(url, email, getActivity().getApplicationContext());
//        task.execute();
//
//        Toast.makeText(getActivity().getApplicationContext(), "Sent invitation success",
//                Toast.LENGTH_SHORT).show();
//
////        listContacts();
//    }

//    public void listContacts() {
//        Log.d(tag, "List contacts");
//
//        contactItems = new ArrayList<>();
//        HashMap<String, String> map = dbManagerSoco.getContacts();
//
//        for(Map.Entry<String, String> e : map.entrySet()){
//            Log.d(tag, "found contact: " + e.getValue() + ", " + e.getKey());
//            contactItems.add(new EntryItem(e.getValue(), e.getKey()));
//        }
//
//        Log.d(tag, "set contacts eventCardStackAdapter");
//        contactsAdapter = new SectionEntryListAdapter(getActivity(), contactItems);
//        ListView lv = (ListView) rootView.findViewById(R.id.listview_contacts);
//        lv.setAdapter(contactsAdapter);
//    }

    @Override
    public void onResume() {
        super.onResume();

//        Log.v(tag, "onResume start, reload conversations");
//        singleConversations = dataLoader.loadSingleConversations();
//        showConversations(singleConversations);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.add) {
//            Log.i(tag, "Click on add.");
//            addContact();
//        }
//        if (id == R.id.create) {
//            Log.d(tag, "tap menu item: create");
////            addContact();
//        }

        //primary function
//        if (id == R.id.friends) {
//            Log.d(tag, "tap menu item: contacts");
//            Intent i = new Intent(getActivity(), AllBuddiesActivityV1.class);
//            startActivity(i);
//        }

        return super.onOptionsItemSelected(item);
    }



}