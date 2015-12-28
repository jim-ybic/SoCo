package com.soco.SoCoClient.events.suggested;

//todo: a bug to be fixed, steps to replicate-
//1) go inside a folder, 2) quick create an activity, 3) press android Back button
//expected: return to the up level
//actual: return to login acreen

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
import com.soco.SoCoClient.buddies.allbuddies.ui.MyBuddiesListEntryItem;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.ui.card.model.Orientations;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.common.JoinEventActivity;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.model.ui.EventCardContainer;
import com.soco.SoCoClient.events.model.ui.EventCardModel;
import com.soco.SoCoClient.events.model.ui.EventCardStackAdapter;
import com.soco.SoCoClient.events.service.DownloadSuggestedEventsService;
import com.soco.SoCoClient.events.service.DownloadSuggestedEventsTask;

import java.util.ArrayList;

//import com.soco.SoCoClient.control.config.ref.DataConfigV1;

public class SuggestedEventsFragment extends Fragment
        implements View.OnClickListener, TaskCallBack {

    static String tag = "SuggestedEventsFragment";

    View rootView;
//    SocoApp socoApp;

    Context context;


    //    EventCardContainer mEventCardContainer;
    SocoApp socoApp;

    ProgressDialog pd;

    //new variables
//    DataLoader dataLoader;
//    ArrayList<Event> events;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_active_projects);
        setHasOptionsMenu(true);


        context = getActivity().getApplicationContext();
        socoApp = (SocoApp) context;

//        socoApp = (SocoApp) getActivity().getApplication();

//        loginEmail = socoApp.loginEmail;
//        loginPassword = socoApp.loginPassword;
//        Log.i(tag, "onCreate, get login info: " + loginEmail + ", " + loginPassword);

//        dbmgrSoco = new DBManagerSoco(getActivity());
//        dbmgrSoco = socoApp.dbManagerSoco;
//        dbmgrSoco.context = getActivity().getApplicationContext();
//        socoApp.dbManagerSoco = dbmgrSoco;
//        activities = dbmgrSoco.loadActivitiessByActiveness(DataConfigV1.VALUE_ACTIVITY_ACTIVE);
//        activities = dbmgrSoco.loadActiveActivitiesByPath(socoApp.currentPath);
//        folders = dbmgrSoco.loadFoldersByPath(socoApp.currentPath);

        //new code
//        dataLoader = new DataLoader(context);
//        events = dataLoader.loadEvents();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(tag, "on create view");
        rootView = inflater.inflate(R.layout.fragment_suggested_events, container, false);

        Log.v(tag, "update global variable");
        socoApp.suggestedEventsView = rootView;

        Log.v(tag, "show progress dialog, fetch suggested events from server");
        pd = ProgressDialog.show(getActivity(), "Downloading events", "Please wait...");
        new Thread(new Runnable(){
            public void run(){
                downloadEventsInBackgroud(getActivity());
            }
        }).start();

        return rootView;
    }

    public void downloadEventsInBackgroud(Context context) {
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
        pd.dismiss();
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
//                    "Event #" + i,
//                    "Description goes here",
//                    r.getDrawable(R.drawable.picture3_crop));
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
        //card - end

        Log.v(tag, "set current event index");
        socoApp.currentEventIndex = 0;
    }

//    void findViewItems(View rootView){
//        lv_active_programs = (ListView) rootView.findViewById(R.id.all_events);
//        et_quick_add = ((EditText)rootView.findViewById(R.id.et_quickadd));
//
//        rootView.findViewById(R.id.add).setOnClickListener(this);
//    }

//    ArrayList<Item> getTestingItems(){
//        ArrayList<Item> items = new ArrayList<>();
//        items.add(new SectionItem("Section ABC"));
//        items.add(new EntryItem("Entry DDD", "This is ddd"));
//        items.add(new FolderItem("Folder BBB", "This is folder bbb"));
//        return items;
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case com.soco.SoCoClient.control.config.DataConfigV1.INTENT_SHOW_EVENT_DETAIL: {
//                Log.i(tag, "return from event details");
//                Log.i(tag, "Current email and password: " + loginEmail + ", " + loginPassword);
//                activities = dbmgrSoco.loadActivitiessByActiveness(DataConfigV1.VALUE_ACTIVITY_ACTIVE);
//                activities = dbmgrSoco.loadActiveActivitiesByPath(socoApp.currentPath);
//                refreshList();
//                events = dataLoader.loadEvents();
//                show(events);
//                break;
//            }
//        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_fragment_suggested_events, menu);
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
//        if (id == R.id.archive) {
//            showCompletedProjects();
//        }
//        else if (id == R.id.add) {
//            createActivity(null);
//        }

//        //primary function
//        if (id == R.id.events){
//            Log.d(tag, "click on menu: all events");
//            Intent i = new Intent(getActivity().getApplicationContext(), AllEventsActivityV1.class);
//            startActivity(i);
//        }
//        //secondary functions
//        else if (id == R.id.profile){
//            Log.d(tag, "click on menu: profile");
//            Intent i = new Intent(getActivity().getApplicationContext(), MyProfileActivity.class);
//            startActivity(i);
//        }
//        else if (id == R.id.notifications){
//            Log.d(tag, "click on menu: notifications");
//            Intent i = new Intent(getActivity().getApplicationContext(), ActivityNotifications.class);
//            startActivity(i);
//        }
//        else if (id == R.id.chats){
//            Log.d(tag, "click on menu: chats");
//            Intent i = new Intent(getActivity().getApplicationContext(), ActivityChats.class);
//            startActivity(i);
//        }
//
//        //for testing
//        if (id == R.id.createevent){
//            Log.d(tag, "create new event");
//            Intent i = new Intent(getActivity().getApplicationContext(), CreateEventActivity.class);
//            startActivity(i);
//        }
//        else if (id == R.id.test){
//            Log.v(tag, "test activity");
//            Intent i = new Intent(getActivity().getApplicationContext(), Test2Activity.class);
//            startActivity(i);
//        }

        return super.onOptionsItemSelected(item);
    }

//    public void createActivity() {
//
//        Log.d(tag, "create dialog elements");
//        Context context = getActivity();
//        LinearLayout layout = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        final EditText nameBox = new EditText(context);
//        nameBox.setHint("Name");
//        layout.addView(nameBox);
//        final EditText descBox = new EditText(context);
//        descBox.setHint("Description (Optional)");
//        layout.addView(descBox);
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//        alert.setTitle("New");
////        alert.setMessage("Task description: ");
////        final EditText input = new EditText(getActivity());
//        alert.setView(layout);
//
//        alert.setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String name = nameBox.getText().toString();
////                String desc = descBox.getText().toString();
//                Log.d(tag, "create activity and insert into database: " + name);
//                Activity p = new Activity(name, socoApp.currentPath);
//                int pid = dbmgrSoco.addActivity(p);
//                Toast.makeText(getActivity().getApplicationContext(),
//                        "Project created.", Toast.LENGTH_SHORT).show();
//                Log.d(tag, "send new activity to server: " + name + ", pid " + pid);
//                ActivityUtil.serverCreateActivity(name, getActivity().getApplicationContext(),
//                        loginEmail, loginPassword, String.valueOf(pid),
//                        p.getSignature(), p.getTag(), p.getType());
////                activities = dbmgrSoco.loadActivitiessByActiveness(DataConfigV1.VALUE_ACTIVITY_ACTIVE);
//                Log.d(tag, "add into active list and refresh UI");
//                activities.add(p);
//                refreshList();
//            }
//        });
//        alert.setNeutralButton("Add Folder", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String name = nameBox.getText().toString();
//                String desc = descBox.getText().toString();
//                String path = socoApp.currentPath;
//                Log.d(tag, "create folder and insert into database: " + name);
//                int fid = dbmgrSoco.addFolder(name, desc, socoApp.currentPath);
////                allListItems.add(new FolderItem(name, desc));
//                Log.d(tag, "send new folder to server");
//                //todo
//                Log.d(tag, "add into active list and refresh UI");
//                folders.add(new Folder(name, desc, socoApp.currentPath));
//                refreshList();
//            }
//        });
////        alert.setNeutralButton("Details", new DialogInterface.OnClickListener() {
////            public void onClick(DialogInterface dialog, int whichButton) {
////                String n = input.getText().toString();
////                Activity p = new Activity(n);
////                int pid = dbmgrSoco.addActivity(p);
////                ActivityUtil.serverCreateActivity(n, getActivity().getApplicationContext(),
////                        loginEmail, loginPassword, String.valueOf(pid),
////                        p.getSignature(), p.getTag(), p.getType());
////                Intent intent = new Intent(getActivity().getApplicationContext(), SingleActivityActivity.class);
////                socoApp.pid = pid;
////                Log.i(tag, "Start activity to view programName details");
////                startActivityForResult(intent, -1);
////            }
////        });
//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//            }
//        });
//
//        alert.show();
//    }


//    public void refreshList() {
//        Log.d(tag, "refresh list start");
//
//        allListItems = new ArrayList<>();
//        HashMap<String, String> tags = new HashMap<>();
//
//        Log.d(tag, "grouping activities and add into list");
//        HashMap<String, ArrayList<Activity>> activitiesMap = ActivityUtil.groupingActivitiesByTag(activities);
//        for(Map.Entry<String, ArrayList<Activity>> e : activitiesMap.entrySet()){
//            String tag = e.getKey();
//            tags.put(tag, tag);
//            ArrayList<Activity> pp = e.getValue();
//            allListItems.add(new SectionItem(tag));   //add section
//            for(Activity p : pp) {  //add activity
//                //fix Bug #4 new activity created from invitation has delay in showing activity name
//                if(p.invitation_status == DataConfig.ACTIVITY_INVITATION_STATUS_COMPLETE)
//                    allListItems.add(new EntryItem(p.pname, p.getMoreInfo()));
//                else
//                    Log.d(tag, "skip showing project that pending invitation complete: " + p.pid);
//            }
//        }
//
//        Log.d(tag, "grouping folders and add into list");
//        HashMap<String, ArrayList<Folder>> foldersMap = ActivityUtil.groupingFoldersByTag(folders);
//        for(Map.Entry<String, ArrayList<Folder>> e : foldersMap.entrySet()){
//            String tag = e.getKey();
//            ArrayList<Folder> ff = e.getValue();
//            if(!tags.containsKey(tag))  //new tag for folders only (i.e. no activities)
//                allListItems.add(new SectionItem(tag));
//            for(Folder f : ff){
//                allListItems.add(new FolderItem(f.fname, f.fdesc));
//            }
//        }
//
//        Log.d(tag, "refresh UI");
//        SectionEntryListAdapter activitiesAdapter;
//        activitiesAdapter = new SectionEntryListAdapter(getActivity(), allListItems);
//        lv_active_programs.setAdapter(activitiesAdapter);
//    }

//    public void show(ArrayList<Event> events) {
//        Log.v(tag, "show events to ui");
//        allListItems = new ArrayList<>();
//
//        for(Event e : events){
//            allListItems.add(new EventListEntryItem(e.getName(), e.getDesc(), e.getDate()));
//        }
//
////        Log.d(tag, "refresh UI");
//        EventListAdapter eventCardStackAdapter = new EventListAdapter(getActivity(), allListItems);
//        lv_active_programs.setAdapter(eventCardStackAdapter);
//    }

//    public void showCompletedProjects() {
//        Intent intent = new Intent(getActivity(), CompletedActivitiessActivity.class);
//        intent.putExtra(GeneralConfigV1.LOGIN_EMAIL, loginEmail);
//        intent.putExtra(GeneralConfigV1.LOGIN_PASSWORD, loginPassword);
//        startActivity(intent);
//    }

//    public void exit(View view) {
//        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivityV1.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra(LoginActivityV1.FLAG_EXIT, true);
//        startActivity(intent);
//    }

//    public void add(){
//        String name = et_quick_add.getText().toString();
//        Log.d(tag, "quick add event: " + name);
//
//        Log.v(tag, "create new event");
//        Event e = new Event(getActivity().getApplicationContext());
//        e.setName(name);
////        e.addContext(context);
//        e.save();
//
//        DataLoader dataLoader = new DataLoader(context);
//        events = dataLoader.loadEvents();
//        show(events);
//
//        //clean up
//        et_quick_add.setText("", TextView.BufferType.EDITABLE);
//        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow((rootView.findViewById(R.id.et_quickadd)).getWindowToken(), 0);
//    }

    public void onResume() {
        super.onResume();
//        Log.i(tag, "onResume start, reload active projects");
//        activities = dbmgrSoco.loadActivitiessByActiveness(DataConfigV1.VALUE_ACTIVITY_ACTIVE);
//        activities = dbmgrSoco.loadActiveActivitiesByPath(socoApp.currentPath);
//        refreshList();
//        events = dataLoader.loadEvents();
//        show(events);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.add:
//                add();
//                break;
//            case R.id.create:
//                createActivity();
//                break;
//            case R.id.archive:
//                showCompletedProjects();
//                break;
//            case R.id.home:
//                Log.d(tag, "click on home");
//                break;
//            case R.id.eventbuddies:
//                Log.d(tag, "show all event friends");
//                Intent ief = new Intent(getActivity().getApplicationContext(), ActivityEventFriends.class);
//                startActivity(ief);
//                break;
//            case R.id.eventgroups:
//                Log.d(tag, "show all event groups");
//                Intent ieg = new Intent(getActivity().getApplicationContext(), ActivityEventGroups.class);
//                startActivity(ieg);
//                break;
//            case R.id.detail:
//                Log.d(tag, "event detail");
//                Intent ied = new Intent(getActivity().getApplicationContext(), EventDetailsActivity.class);
//                startActivity(ied);
//                break;
//            case R.id.more:
//                Log.d(tag, "show event details");
//                Intent ied = new Intent(getActivity().getApplicationContext(), ActivityEventDetails.class);
//                startActivity(ied);
//                break;
            case R.id.joinevent:
                Log.d(tag, "join this event");
                Intent ije = new Intent(getActivity().getApplicationContext(), JoinEventActivity.class);
                startActivity(ije);
                break;
//            case R.id.allevents:
//                Log.v(tag, "show all events");
//                Intent i = new Intent(getActivity().getApplicationContext(), AllEventsActivity.class);
//                startActivity(i);
//                break;
        }
    }


}
