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

        listProjects(null);

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

                    //new fragment-based activity
                    Intent i = new Intent(view.getContext(), ShowSingleProjectActivity.class);
                    startActivityForResult(i, INTENT_SHOW_SINGLE_PROGRAM);
                }
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
                listProjects(null);
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
                        loginEmail, loginPassword, String.valueOf(pid),
                        p.getSignature(), p.getTag(), p.getType());
                projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_PROJECT_ACTIVE);
                listProjects(null);
            }
        });
        alert.setNeutralButton("Details", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String n = input.getText().toString();
                Project p = new Project(n);
                int pid = dbmgrSoco.addProject(p);
                ProjectUtil.serverCreateProject(n, getApplicationContext(),
                        loginEmail, loginPassword, String.valueOf(pid),
                        p.getSignature(), p.getTag(), p.getType());
                Intent intent = new Intent(getApplicationContext(), ShowSingleProjectActivity.class);
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

    public void listProjects(View view) {
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
                loginEmail, loginPassword, String.valueOf(pid),
                p.getSignature(), p.getTag(), p.getType());
        projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_PROJECT_ACTIVE);
        listProjects(null);
        ((EditText)findViewById(R.id.et_quickadd)).setText("", TextView.BufferType.EDITABLE);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow((findViewById(R.id.et_quickadd)).getWindowToken(), 0);
    }

    protected void onResume() {
        super.onResume();
        Log.i(tag, "onResume start, reload active projects");
        projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_PROJECT_ACTIVE);
        listProjects(null);
    }

}
