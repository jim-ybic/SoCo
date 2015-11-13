package com.soco.SoCoClient.events.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.events.ui.EventGroupListEntryItem;
import com.soco.SoCoClient.events.ui.Item;
import com.soco.SoCoClient.events.ui.EventGroupListSectionItem;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.ui.EventGroupListAdapter;
import com.soco.SoCoClient.groups.model.Group;

import java.util.ArrayList;

public class EventGroupsFragment extends Fragment implements View.OnClickListener {

    static String tag = "EventGroupsFragment";

//    public static final String EVENT_ID = "event_id";
    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 10;
    static final int THOUSAND = 1000;

    static final String EVENT_ORGANIZER = "EVENT ORGANIZER";
    static final String SUPPORTING_GROUPS = "SUPPORTING GROUPS";

    View rootView;
    Context context;
    SocoApp socoApp;
    Event event;
    ArrayList<Item> items = new ArrayList<>();

//    RecyclerView mRecyclerView;
//    SimpleGroupCardAdapter simpleGroupCardAdapter;
//    List<Event> events = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        socoApp = (SocoApp) context;

        event = socoApp.getCurrentSuggestedEvent();
        Log.v(tag, "current event: " + event.toString());

//        generateDummyEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_groups, container, false);

//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setHasFixedSize(true);
//
//        simpleGroupCardAdapter = new SimpleGroupCardAdapter(getActivity(), events);
//        mRecyclerView.setAdapter(simpleGroupCardAdapter);

//        showMyMatches();
        addOrganizers();
        addSupportingGroups();

        EventGroupListAdapter adapter = new EventGroupListAdapter(getActivity(), items);
        ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(adapter);

        return rootView;
    }

//    void showGroups(
//            ArrayList<Person> persons, ArrayList<Person> phoneContacts
//    ) {
//        Log.v(tag, "show contacts: " + persons.size() + " friends, " + phoneContacts.size() + " phone contacts");
//        ArrayList<Person> persons = new ArrayList<>();
//        for(int i=0; i<5; i++)
//            persons.add(new Person(context, "a","b","c"));
//        ArrayList<Person> phoneContacts = new ArrayList<>();
//        for(int j=0; j<10; j++)
//            phoneContacts.add(new Person(context, "x","y","z"));
//    }

    void addOrganizers() {
        Log.v(tag, "add new section label for organizer");
        items.add(new EventGroupListSectionItem(EVENT_ORGANIZER));

        Log.v(tag, "add dummy items for testing");
        for(int i=0; i<1; i++){
            EventGroupListEntryItem item = new EventGroupListEntryItem();
            item.setGroup_name("Testing Organizer " + i);
            items.add(item);
        }

        Log.v(tag, "add event organizer (enterprise)");
        if(!event.getEnterprise_id().isEmpty()){
            EventGroupListEntryItem enterpriseItem = new EventGroupListEntryItem();
            enterpriseItem.setGroup_name(event.getEnterprise_name());
            enterpriseItem.setGroup_icon_url(event.getEnterprise_icon_url());

            //todo: set number of Like
            //todo: set representative follower's icon

            items.add(enterpriseItem);
        }
        else
            Log.w(tag, "no enterprise info found");

        Log.v(tag, "add event organizer (creator)");
        if(!event.getCreator_id().isEmpty()){
            EventGroupListEntryItem creatorItem = new EventGroupListEntryItem();
            creatorItem.setGroup_name(event.getCreator_name());
            creatorItem.setGroup_icon_url(event.getCreator_icon_url());

            //todo: set number of participants
            //todo: set representative member's icon

            items.add(creatorItem);
        }
        else
            Log.w(tag, "no creator found");
    }

    void addSupportingGroups() {
        Log.v(tag, "add new section lable for supporting groups");
        items.add(new EventGroupListSectionItem(SUPPORTING_GROUPS));

        Log.v(tag, "add dummy groups for testing");
        for(int i=0; i<5; i++){
            EventGroupListEntryItem item = new EventGroupListEntryItem();
            item.setGroup_name("Testing Supporting Group " + i);
            items.add(item);
        }

        Log.v(tag, "add event supporting groups");
        if(!event.getSupporting_groups().isEmpty()){
            for(Group g : event.getSupporting_groups()){
                EventGroupListEntryItem groupItem = new EventGroupListEntryItem();
                groupItem.setGroup_name(g.getGroup_name());
                groupItem.setGroup_icon_url(g.getGroup_icon_url());

                //todo: set number of Like
                //todo: set representative follower's icon

                items.add(groupItem);
            }
        }
        else
            Log.w(tag, "no supporting group found");
    }

//    void showMyMatches() {
//        Log.v(tag, "show my matches");
//
//        ArrayList<MyMatchListEntryItem> items = new ArrayList<>();
//
//        Log.v(tag, "add dummy suggested buddies");
//        for(int i=0; i<20; i++)
//            items.add(new MyMatchListEntryItem());
//
//        //todo: fill in the items with the list of suggested buddies
//
//
//        MyMatchListAdapter adapter = new MyMatchListAdapter(getActivity(), items);
//
//        ListView list = (ListView) rootView.findViewById(R.id.friends);
//        list.setAdapter(adapter);
//    }

//    private void generateDummyEvents() {
//
//        //todo: use groups, instead of events
//
//        Log.v(tag, "add 5 dummy events");
//        events.add(new Event());
//        events.add(new Event());
//        events.add(new Event());
//        events.add(new Event());
//        events.add(new Event());
//    }

//    private void showOrganizers() {
////        Log.v(tag, "wait and check register status");
////        int count = 0;
////        while(!socoApp.eventGroupsBuddiesResponse && count < WAIT_ITERATION) {   //wait for 10s
////            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
////            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
////            while (System.currentTimeMillis() < endTime) {
////                synchronized (this) {
////                    try {
////                        wait(endTime - System.currentTimeMillis());
////                    } catch (Exception e) {
////                        Log.e(tag, "Error in waiting");
////                    }
////                }
////            }
////            count++;
////        }
//        event = socoApp.getCurrentSuggestedEvent();
//        Log.v(tag, "current event: " + event.toString());
//
////        Log.v(tag, "show organizer on the screen");
////        ImageButton viewOrg1Icon = (ImageButton) rootView.findViewById(R.id.org1icon);
//        //todo: download organizer image
////        Drawable image1 = context.getResources().getDrawable(R.drawable.eventgroups_group1);	//testing icon
////        viewOrg1Icon.setImageDrawable(image1);
//
////        TextView viewOrg1Name = (TextView) rootView.findViewById(R.id.org1name);
////        viewOrg1Name.setText("abc group");
//
//        //todo: not finished yet
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_fragment_event_groups, menu);
//
////        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.badge).getActionView();
////        TextView tv = (TextView) badgeLayout.findViewById(R.id.actionbar_notifcation_textview);
////        tv.setText("12");
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_profile) {
//            Log.i("setting", "Click on Profile.");
//            Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
//            intent.putExtra(GeneralConfigV1.LOGIN_EMAIL, loginEmail);
//            intent.putExtra(GeneralConfigV1.LOGIN_PASSWORD, loginPassword);
//            startActivity(intent);
//        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.add:
//                add();
//                break;
        }
    }


}
