package com.soco.SoCoClient.view.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.config.GeneralConfig;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.util.ProjectUtil;
import com.soco.SoCoClient.model.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.soco.SoCoClient.view.ui.config.ProfileActivity;
import com.soco.SoCoClient.view.ui.project.list.ShowInactiveProjectsActivity;
import com.soco.SoCoClient.view.ui.project.single.ShowSingleProjectActivity;
import com.soco.SoCoClient.view.ui.section.SectionEntryListAdapter;
import com.soco.SoCoClient.view.ui.section.EntryItem;
import com.soco.SoCoClient.view.ui.section.Item;
import com.soco.SoCoClient.view.ui.section.SectionItem;

public class DashboardActivitiesFragment extends Fragment implements View.OnClickListener {

    // Local view
    private ListView lv_active_programs;
    public static String tag = "DashboardActivities";

    public static int INTENT_SHOW_SINGLE_PROGRAM = 101;

    // Local variable
    private DBManagerSoco dbmgrSoco;
    private ArrayList<Project> projects;
    private String loginEmail, loginPassword;
    ArrayList<Item> activeProjectItems;

    View rootView;
    SocoApp socoApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_active_projects);
        setHasOptionsMenu(true);

        socoApp = (SocoApp) getActivity().getApplication();

        loginEmail = socoApp.loginEmail;
        loginPassword = socoApp.loginPassword;
        Log.i(tag, "onCreate, get login info: " + loginEmail + ", " + loginPassword);

        dbmgrSoco = new DBManagerSoco(getActivity());
//        dbmgrSoco.context = getActivity().getApplicationContext();
//        socoApp.dbManagerSoco = dbmgrSoco;
        projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_ACTIVITY_ACTIVE);
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
        rootView.findViewById(R.id.create).setOnClickListener(this);
        rootView.findViewById(R.id.archive).setOnClickListener(this);
//        rootView.findViewById(R.id.exit).setOnClickListener(this);

        listProjects();

        lv_active_programs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //new
                if(!activeProjectItems.get(position).isSection()) {
                    EntryItem item = (EntryItem) activeProjectItems.get(position);
                    Log.d(tag, "You clicked: " + item.title);

                    String name = item.title;
                    int pid = ProjectUtil.findPidByPname(projects, name);
                    socoApp.pid = pid;
                    String pid_onserver = dbmgrSoco.findProjectIdOnserver(pid);
                    socoApp.pid_onserver = pid_onserver;
                    Log.i(tag, "pid/pid_onserver: " + pid + ", " + pid_onserver);

                    //new fragment-based activity
                    Intent i = new Intent(view.getContext(), ShowSingleProjectActivity.class);
                    startActivityForResult(i, INTENT_SHOW_SINGLE_PROGRAM);
                }
            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (101): {
                Log.i(tag, "onActivityResult, return from ShowSingleProject");
                Log.i(tag, "Current email and password: " + loginEmail + ", " + loginPassword);
                projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_ACTIVITY_ACTIVE);
                listProjects();
                break;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_show_active_projects, menu);
        super.onCreateOptionsMenu(menu, inflater);
        return;
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

        return super.onOptionsItemSelected(item);
    }

    public void createProject() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Create new project");
        alert.setMessage("So I want to ...");
        final EditText input = new EditText(getActivity());
        alert.setView(input);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String n = input.getText().toString();
                Project p = new Project(n);
                int pid = dbmgrSoco.addProject(p);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Project created.", Toast.LENGTH_SHORT).show();
                ProjectUtil.serverCreateProject(n, getActivity().getApplicationContext(),
                        loginEmail, loginPassword, String.valueOf(pid),
                        p.getSignature(), p.getTag(), p.getType());
                projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_ACTIVITY_ACTIVE);
                listProjects();
            }
        });
        alert.setNeutralButton("Details", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String n = input.getText().toString();
                Project p = new Project(n);
                int pid = dbmgrSoco.addProject(p);
                ProjectUtil.serverCreateProject(n, getActivity().getApplicationContext(),
                        loginEmail, loginPassword, String.valueOf(pid),
                        p.getSignature(), p.getTag(), p.getType());
                Intent intent = new Intent(getActivity().getApplicationContext(), ShowSingleProjectActivity.class);
                socoApp.pid = pid;
                Log.i(tag, "Start activity to view programName details");
                startActivityForResult(intent, -1);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    public void listProjects() {
        Log.d(tag, "List projects test");
        activeProjectItems = new ArrayList<Item>();

        HashMap<String, ArrayList<Project>> map = ProjectUtil.groupingProjectsByTag(projects);
        for(Map.Entry<String, ArrayList<Project>> e : map.entrySet()){
            String tag = e.getKey();
            ArrayList<Project> pp = e.getValue();

            activeProjectItems.add(new SectionItem(tag));
            for(Project p : pp)
                activeProjectItems.add(new EntryItem(p.pname, p.getMoreInfo()));
        }

        SectionEntryListAdapter adapter = new SectionEntryListAdapter(getActivity(), activeProjectItems);
        lv_active_programs.setAdapter(adapter);
    }

    public void showCompletedProjects() {
        Intent intent = new Intent(getActivity(), ShowInactiveProjectsActivity.class);
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
        Project p = new Project(name);
        int pid = dbmgrSoco.addProject(p);
        Toast.makeText(getActivity().getApplicationContext(),
                "Project created success.", Toast.LENGTH_SHORT).show();
        ProjectUtil.serverCreateProject(name, getActivity().getApplicationContext(),
                loginEmail, loginPassword, String.valueOf(pid),
                p.getSignature(), p.getTag(), p.getType());
        projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_ACTIVITY_ACTIVE);
        listProjects();
        ((EditText)rootView.findViewById(R.id.et_quickadd)).setText("", TextView.BufferType.EDITABLE);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow((rootView.findViewById(R.id.et_quickadd)).getWindowToken(), 0);
    }

    public void onResume() {
        super.onResume();
        Log.i(tag, "onResume start, reload active projects");
        projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_ACTIVITY_ACTIVE);
        listProjects();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                quickAdd();
                break;
            case R.id.create:
                createProject();
                break;
            case R.id.archive:
                showCompletedProjects();
                break;
//            case R.id.exit:
//                exit(v);
//                break;
        }
    }

}
