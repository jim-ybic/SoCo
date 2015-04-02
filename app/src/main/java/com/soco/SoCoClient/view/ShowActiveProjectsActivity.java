package com.soco.SoCoClient.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.Config;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.util.ProjectUtil;
import com.soco.SoCoClient.model.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.soco.SoCoClient.view.ui.section.EntryAdapter;
import com.soco.SoCoClient.view.ui.section.EntryItem;
import com.soco.SoCoClient.view.ui.section.Item;
import com.soco.SoCoClient.view.ui.section.SectionItem;

public class ShowActiveProjectsActivity extends ActionBarActivity {

    // Local view
    private ListView lv_active_programs;
    public static String tag = "ShowActivePrograms";

    public static int INTENT_SHOW_SINGLE_PROGRAM = 101;

    // Local variable
    private DBManagerSoco dbmgrSoco;
    private ArrayList<Project> projects;
    private String loginEmail, loginPassword;
    ArrayList<Item> activeProjectItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_active_projects);
        findViewsById();

        loginEmail = ((SocoApp)getApplicationContext()).loginEmail;
        loginPassword = ((SocoApp)getApplicationContext()).loginPassword;
        Log.i(tag, "onCreate, get login info: " + loginEmail + ", " + loginPassword);

        dbmgrSoco = new DBManagerSoco(this);
        ((SocoApp)getApplicationContext()).dbManagerSoco = dbmgrSoco;
        projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_PROJECT_ACTIVE);

//        listProjects(null);
        listProjectsTest(null);

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
                    ((SocoApp)getApplicationContext()).pid = pid;
                    String pid_onserver = dbmgrSoco.findProjectIdOnserver(pid);
                    ((SocoApp)getApplicationContext()).pid_onserver = pid_onserver;
                    Log.i(tag, "pid/pid_onserver: " + pid + ", " + pid_onserver);

                    Intent intent = new Intent(view.getContext(), ShowSingleProjectActivity.class);
                    startActivityForResult(intent, INTENT_SHOW_SINGLE_PROGRAM);
//                    Intent i = new Intent(view.getContext(), ShowSingleProjectNewActivity.class);
//                    startActivityForResult(i, INTENT_SHOW_SINGLE_PROGRAM);
                }

//                ListView listView = (ListView) parent;
//                HashMap<String, String> map = (HashMap<String, String>)
//                        listView.getItemAtPosition(position);
//                String name = map.get(Config.PROJECT_PNAME);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (101): {
                Log.i(tag, "onActivityResult, return from ShowSingleProject");
                Log.i(tag, "Current email and password: " + loginEmail + ", " + loginPassword);
                projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_PROJECT_ACTIVE);
//                listProjects(null);
                listProjectsTest(null);
                break;
            }
        }
    }

    private void findViewsById() {
        lv_active_programs = (ListView) findViewById(R.id.lv_active_programs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_active_programs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_clean_up) {
//            Log.i("setting", "Click on Clean up.");
//            new AlertDialog.Builder(this)
//                    .setMessage("Delete all programs?")
//                    .setCancelable(true)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dbmgrSoco.cleanDB();
//                            Toast.makeText(getApplicationContext(),
//                                    "All Programs deleted.", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(getApplicationContext(),
//                                    ShowActiveProjectsActivity.class);
//                            i.putExtra(Config.LOGIN_EMAIL, loginEmail);
//                            startActivity(i);
//                            ;
//                        }
//                    })
//                    .setNegativeButton("No", null)
//                    .show();
//            return true;
//        }
        if (id == R.id.action_profile) {
            Log.i("setting", "Click on Profile.");
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
            intent.putExtra(Config.LOGIN_PASSWORD, loginPassword);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void createProject(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Create new project");
        alert.setMessage("So I want to ...");
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String n = input.getText().toString();
                Project p = new Project(n);
                int pid = dbmgrSoco.addProject(p);
                Toast.makeText(getApplicationContext(),
                        "Project created.", Toast.LENGTH_SHORT).show();
                ProjectUtil.serverCreateProject(n, getApplicationContext(),
                        loginEmail, loginPassword, String.valueOf(pid));
                projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_PROJECT_ACTIVE);
//                listProjects(null);
                listProjectsTest(null);
            }
        });
        alert.setNeutralButton("Details", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String n = input.getText().toString();
                Project p = new Project(n);
                int pid = dbmgrSoco.addProject(p);
                ProjectUtil.serverCreateProject(n, getApplicationContext(),
                        loginEmail, loginPassword, String.valueOf(pid));
                Intent intent = new Intent(getApplicationContext(), ShowSingleProjectActivity.class);
//                intent.putExtra(Config.PROGRAM_PNAME, n);
//                intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
//                intent.putExtra(Config.LOGIN_PASSWORD, loginPassword);
                ((SocoApp)getApplicationContext()).pid = pid;
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

//    public void listProjects(View view) {
//        Log.d(tag, "List projects");
//        ArrayList<Map<String, String>> list = new ArrayList<>();
//        for (Project p : projects) {
//            HashMap<String, String> map = new HashMap<>();
//            map.put(Config.PROJECT_PNAME, p.pname);
//            map.put(Config.PROJECT_PINFO, p.getMoreInfo());
//            list.add(map);
//        }
//        SimpleAdapter adapter = new SimpleAdapter(this, list,
//                android.R.layout.simple_list_item_2,
//                new String[]{Config.PROJECT_PNAME, Config.PROJECT_PINFO},
//                new int[]{android.R.id.text1, android.R.id.text2});
//        lv_active_programs.setAdapter(adapter);
//    }

    public void listProjectsTest(View view) {
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

//        activeProjectItems.add(new SectionItem("Default"));
//        for (Project p : projects) {
//            activeProjectItems.add(new EntryItem(p.pname, p.getMoreInfo()));
//        }

//        activeProjectItems.add(new SectionItem("Category 1"));
//        activeProjectItems.add(new EntryItem("Item 1", "This is item 1.1"));
//        activeProjectItems.add(new EntryItem("Item 2", "This is item 1.2"));
//        activeProjectItems.add(new EntryItem("Item 3", "This is item 1.3"));

//
//        items.add(new SectionItem("Category 2"));
//        items.add(new EntryItem("Item 4", "This is item 2.1"));
//        items.add(new EntryItem("Item 5", "This is item 2.2"));
//        items.add(new EntryItem("Item 6", "This is item 2.3"));
//        items.add(new EntryItem("Item 7", "This is item 2.4"));
//
//        items.add(new SectionItem("Category 3"));
//        items.add(new EntryItem("Item 8", "This is item 3.1"));
//        items.add(new EntryItem("Item 9", "This is item 3.2"));
//        items.add(new EntryItem("Item 10", "This is item 3.3"));
//        items.add(new EntryItem("Item 11", "This is item 3.4"));
//        items.add(new EntryItem("Item 12", "This is item 3.5"));

        EntryAdapter adapter = new EntryAdapter(this, activeProjectItems);
        lv_active_programs.setAdapter(adapter);

    }

    public void showCompletedProjects(View view) {
        Intent intent = new Intent(this, ShowInactiveProjectsActivity.class);
        intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
        intent.putExtra(Config.LOGIN_PASSWORD, loginPassword);
        startActivity(intent);
    }

    public void exit(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(LoginActivity.FLAG_EXIT, true);
        startActivity(intent);
    }

    public void quickAdd(View view){
        String name = ((EditText)findViewById(R.id.et_quickadd)).getText().toString();
        Project p = new Project(name);
        int pid = dbmgrSoco.addProject(p);
        Toast.makeText(getApplicationContext(),
                "Project created success.", Toast.LENGTH_SHORT).show();
        ProjectUtil.serverCreateProject(name, getApplicationContext(),
                loginEmail, loginPassword, String.valueOf(pid));
        projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_PROJECT_ACTIVE);
//        listProjects(null);
        listProjectsTest(null);
        ((EditText)findViewById(R.id.et_quickadd)).setText("", TextView.BufferType.EDITABLE);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow((findViewById(R.id.et_quickadd)).getWindowToken(), 0);
    }

    protected void onResume() {
        super.onResume();
        Log.i(tag, "onResume start, reload active projects");
        projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_PROJECT_ACTIVE);
//        listProjects(null);
        listProjectsTest(null);
    }

}
