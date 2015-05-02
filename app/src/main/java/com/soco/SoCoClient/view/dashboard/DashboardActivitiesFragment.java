package com.soco.SoCoClient.view.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.config.GeneralConfig;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.util.ActivityUtil;
import com.soco.SoCoClient.model.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.soco.SoCoClient.view.activities.CompletedActivitiessActivity;
import com.soco.SoCoClient.view.common.sectionlist.FolderItem;
import com.soco.SoCoClient.view.config.ProfileActivity;
import com.soco.SoCoClient.view.activities.SingleActivityActivity;
import com.soco.SoCoClient.view.common.sectionlist.SectionEntryListAdapter;
import com.soco.SoCoClient.view.common.sectionlist.EntryItem;
import com.soco.SoCoClient.view.common.sectionlist.Item;
import com.soco.SoCoClient.view.common.sectionlist.SectionItem;

public class DashboardActivitiesFragment extends Fragment implements View.OnClickListener {

    // Local view
    private ListView lv_active_programs;
    public static String tag = "DashboardActivities";

    public static int INTENT_SHOW_SINGLE_PROGRAM = 101;

    // Local variable
    private DBManagerSoco dbmgrSoco;
    private ArrayList<Activity> activities;
    private String loginEmail, loginPassword;
    ArrayList<Item> activeProjectItems;
    HashMap<String, String> folders; //name, desc

    View rootView;
    SocoApp socoApp;
    SectionEntryListAdapter activitiesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_active_projects);
        setHasOptionsMenu(true);

        socoApp = (SocoApp) getActivity().getApplication();

        loginEmail = socoApp.loginEmail;
        loginPassword = socoApp.loginPassword;
        Log.i(tag, "onCreate, get login info: " + loginEmail + ", " + loginPassword);

//        dbmgrSoco = new DBManagerSoco(getActivity());
        dbmgrSoco = socoApp.dbManagerSoco;
//        dbmgrSoco.context = getActivity().getApplicationContext();
//        socoApp.dbManagerSoco = dbmgrSoco;
//        activities = dbmgrSoco.loadActivitiessByActiveness(DataConfig.VALUE_ACTIVITY_ACTIVE);
        activities = dbmgrSoco.loadActiveActivitiesByPath(socoApp.currentPath);
        folders = dbmgrSoco.loadFolders(socoApp.currentPath);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(tag, "on create view");
        rootView = inflater.inflate(R.layout.fragment_dashboard_activities, container, false);
        Log.d(tag, "Found root view: " + rootView);

        lv_active_programs = (ListView) rootView.findViewById(R.id.lv_active_programs);

        //set button listeners
        rootView.findViewById(R.id.add).setOnClickListener(this);
//        rootView.findViewById(R.id.create).setOnClickListener(this);
//        rootView.findViewById(R.id.archive).setOnClickListener(this);
//        rootView.findViewById(R.id.exit).setOnClickListener(this);

        refreshList();

        lv_active_programs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(activeProjectItems.get(position).getType().equals(GeneralConfig.LIST_ITEM_TYPE_ENTRY)) {
                    EntryItem ei = (EntryItem) activeProjectItems.get(position);
                    Log.d(tag, "You clicked on activity: " + ei.title);

                    String name = ei.title;
                    int pid = ActivityUtil.findPidByPname(activities, name);
                    socoApp.pid = pid;
                    String pid_onserver = dbmgrSoco.findActivityIdOnserver(pid);
                    socoApp.pid_onserver = pid_onserver;
                    Log.i(tag, "pid/pid_onserver: " + pid + ", " + pid_onserver);

                    //new fragment-based activity
                    Intent i = new Intent(view.getContext(), SingleActivityActivity.class);
                    startActivityForResult(i, INTENT_SHOW_SINGLE_PROGRAM);
                }
                else if(activeProjectItems.get(position).getType().equals(GeneralConfig.LIST_ITEM_TYPE_FOLDER)) {
                    FolderItem fi = (FolderItem) activeProjectItems.get(position);
                    Log.d(tag, "You clicked on folder: " + fi.title);

                    socoApp.currentPath += fi.title + "/";
                    Log.d(tag, "new current path " + socoApp.currentPath);

                    //todo: load activities of folders
//                    activities = dbmgrSoco.loadActivitiessByActiveness(DataConfig.VALUE_ACTIVITY_ACTIVE);
                    activities = dbmgrSoco.loadActiveActivitiesByPath(socoApp.currentPath);
                    folders = dbmgrSoco.loadFolders(socoApp.currentPath);
                    refreshList();

                    //todo: test start
//                    ArrayList<Item> testItems = getTestingItems();
//                    activitiesAdapter = new SectionEntryListAdapter(getActivity(), testItems);
//                    lv_active_programs.setAdapter(activitiesAdapter);
                    //todo: test end

//                    String name = fi.title;
//                    int pid = ActivityUtil.findPidByPname(activities, name);
//                    socoApp.pid = pid;
//                    String pid_onserver = dbmgrSoco.findActivityIdOnserver(pid);
//                    socoApp.pid_onserver = pid_onserver;
//                    Log.i(tag, "pid/pid_onserver: " + pid + ", " + pid_onserver);
//
//                    Intent i = new Intent(view.getContext(), SingleActivityActivity.class);
//                    startActivityForResult(i, INTENT_SHOW_SINGLE_PROGRAM);
                }
            }
        });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(tag, "keyCode: " + keyCode + ", eventAction: " + event.getAction());
                if (event.getAction() != KeyEvent.ACTION_DOWN)      //only process key down event
                    return true;

                if( keyCode == KeyEvent.KEYCODE_BACK ) {
                    Log.i(tag, "onKey Back listener");
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    if(socoApp.currentPath.equals(GeneralConfig.PATH_ROOT))
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Top level already, click again to exit", Toast.LENGTH_SHORT).show();
                    else {
                        String path = socoApp.currentPath;
                        path = path.substring(0, path.length()-1);
                        int pos = path.lastIndexOf("/");
                        path = path.substring(0, pos+1);
                        Log.d(tag, "new current path " + path + ", refresh UI");
                        socoApp.currentPath = path;
                        activities = dbmgrSoco.loadActiveActivitiesByPath(path);
                        folders = dbmgrSoco.loadFolders(path);
                        refreshList();
                    }
//                    Log.d(tag, "current path: " + socoApp.currentPath);
                    return true;
                } else {
                    return false;
                }
            }
        });

        return rootView;
    }

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
        switch (requestCode) {
            case (101): {
                Log.i(tag, "onActivityResult, return from ShowSingleProject");
//                Log.i(tag, "Current email and password: " + loginEmail + ", " + loginPassword);
//                activities = dbmgrSoco.loadActivitiessByActiveness(DataConfig.VALUE_ACTIVITY_ACTIVE);
                activities = dbmgrSoco.loadActiveActivitiesByPath(socoApp.currentPath);
                refreshList();
                break;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard_activities, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Log.i("setting", "Click on Profile.");
            Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
            intent.putExtra(GeneralConfig.LOGIN_EMAIL, loginEmail);
            intent.putExtra(GeneralConfig.LOGIN_PASSWORD, loginPassword);
            startActivity(intent);
        }
        else if (id == R.id.archive) {
            showCompletedProjects();
        }
        else if (id == R.id.add) {
            createActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    public void createActivity() {

        Log.d(tag, "create dialog elements");
        Context context = getActivity();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText nameBox = new EditText(context);
        nameBox.setHint("Name");
        layout.addView(nameBox);
        final EditText descBox = new EditText(context);
        descBox.setHint("Description (Optional)");
        layout.addView(descBox);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("New");
//        alert.setMessage("Task description: ");
//        final EditText input = new EditText(getActivity());
        alert.setView(layout);

        alert.setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = nameBox.getText().toString();
//                String desc = descBox.getText().toString();
                Log.d(tag, "create activity and insert into database: " + name);
                Activity p = new Activity(name, socoApp.currentPath);
                int pid = dbmgrSoco.addActivity(p);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Project created.", Toast.LENGTH_SHORT).show();
                Log.d(tag, "send new activity to server: " + name + ", pid " + pid);
                ActivityUtil.serverCreateActivity(name, getActivity().getApplicationContext(),
                        loginEmail, loginPassword, String.valueOf(pid),
                        p.getSignature(), p.getTag(), p.getType());
//                activities = dbmgrSoco.loadActivitiessByActiveness(DataConfig.VALUE_ACTIVITY_ACTIVE);
                Log.d(tag, "add into active list and refresh UI");
                activities.add(p);
                refreshList();
            }
        });
        alert.setNeutralButton("Add Folder", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = nameBox.getText().toString();
                String desc = descBox.getText().toString();
                String path = socoApp.currentPath;
                Log.d(tag, "create folder and insert into database: " + name);
                int fid = dbmgrSoco.addFolder(name, desc, socoApp.currentPath);
//                activeProjectItems.add(new FolderItem(name, desc));
                Log.d(tag, "send new folder to server");
                //todo
                Log.d(tag, "add into active list and refresh UI");
                folders.put(name, desc);
                refreshList();
            }
        });
//        alert.setNeutralButton("Details", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String n = input.getText().toString();
//                Activity p = new Activity(n);
//                int pid = dbmgrSoco.addActivity(p);
//                ActivityUtil.serverCreateActivity(n, getActivity().getApplicationContext(),
//                        loginEmail, loginPassword, String.valueOf(pid),
//                        p.getSignature(), p.getTag(), p.getType());
//                Intent intent = new Intent(getActivity().getApplicationContext(), SingleActivityActivity.class);
//                socoApp.pid = pid;
//                Log.i(tag, "Start activity to view programName details");
//                startActivityForResult(intent, -1);
//            }
//        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }


    public void refreshList() {
        Log.d(tag, "List projects");
        activeProjectItems = new ArrayList<>();

        Log.d(tag, "Add activities");
        HashMap<String, ArrayList<Activity>> map = ActivityUtil.groupingActivitiesByTag(activities);
        for(Map.Entry<String, ArrayList<Activity>> e : map.entrySet()){
            String tag = e.getKey();
            ArrayList<Activity> pp = e.getValue();

            activeProjectItems.add(new SectionItem(tag));
            for(Activity p : pp) {
                //fix Bug #4 new activity created from invitation has delay in showing activity title
                if(p.invitation_status == DataConfig.ACTIVITY_INVITATION_STATUS_COMPLETE)
                    activeProjectItems.add(new EntryItem(p.pname, p.getMoreInfo()));
                else
                    Log.i(tag, "skip showing project that pending invitation complete: " + p.pid);
            }
        }

        Log.d(tag, "Add folders");
        for(Map.Entry<String, String> e : folders.entrySet()){
            activeProjectItems.add(new FolderItem(e.getKey(), e.getValue()));
        }

        Log.d(tag, "refresh UI");
        activitiesAdapter = new SectionEntryListAdapter(getActivity(), activeProjectItems);
        lv_active_programs.setAdapter(activitiesAdapter);
    }

    public void showCompletedProjects() {
        Intent intent = new Intent(getActivity(), CompletedActivitiessActivity.class);
        intent.putExtra(GeneralConfig.LOGIN_EMAIL, loginEmail);
        intent.putExtra(GeneralConfig.LOGIN_PASSWORD, loginPassword);
        startActivity(intent);
    }

//    public void exit(View view) {
//        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra(LoginActivity.FLAG_EXIT, true);
//        startActivity(intent);
//    }

    public void quickAdd(){
        String name = ((EditText)rootView.findViewById(R.id.et_quickadd)).getText().toString();
        Log.d(tag, "quick add activity " + name);

        Activity p = new Activity(name, socoApp.currentPath);
        int pid = dbmgrSoco.addActivity(p);
        Toast.makeText(getActivity().getApplicationContext(),
                "Project created success.", Toast.LENGTH_SHORT).show();
        ActivityUtil.serverCreateActivity(name, getActivity().getApplicationContext(),
                loginEmail, loginPassword, String.valueOf(pid),
                p.getSignature(), p.getTag(), p.getType());
//        activities = dbmgrSoco.loadActivitiessByActiveness(DataConfig.VALUE_ACTIVITY_ACTIVE);
        activities = dbmgrSoco.loadActiveActivitiesByPath(socoApp.currentPath);
        refreshList();
        ((EditText)rootView.findViewById(R.id.et_quickadd)).setText("", TextView.BufferType.EDITABLE);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow((rootView.findViewById(R.id.et_quickadd)).getWindowToken(), 0);
    }

    public void onResume() {
        super.onResume();
        Log.i(tag, "onResume start, reload active projects");
//        activities = dbmgrSoco.loadActivitiessByActiveness(DataConfig.VALUE_ACTIVITY_ACTIVE);
        activities = dbmgrSoco.loadActiveActivitiesByPath(socoApp.currentPath);
        refreshList();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                quickAdd();
                break;
//            case R.id.create:
//                createActivity();
//                break;
            case R.id.archive:
                showCompletedProjects();
                break;
            case R.id.home:
                Log.d(tag, "click on home");
                break;
        }
    }



}
