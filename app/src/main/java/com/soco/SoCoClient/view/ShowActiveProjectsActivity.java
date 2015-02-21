package com.soco.SoCoClient.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.Config;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.util.ProjectUtil;
import com.soco.SoCoClient.model.Program;
import com.soco.SoCoClient.model.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowActiveProjectsActivity extends ActionBarActivity {

    // Local view
    private ListView lv_active_programs;
    public static String tag = "ShowActivePrograms";

    public static int INTENT_SHOW_SINGLE_PROGRAM = 101;

    // Local variable
    private DBManagerSoco dbmgrSoco;
    private List<Project> projects;
    private String loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_active_projects);
        findViewsById();

        loginEmail = ((SocoApp)getApplicationContext()).loginEmail;
        loginPassword = ((SocoApp)getApplicationContext()).loginPassword;
        Log.i(tag, "onCreate, get login info: " + loginEmail + ", " + loginPassword);

        dbmgrSoco = new DBManagerSoco(this);
        projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_PROJECT_ACTIVE);
        listProjects(null);

        lv_active_programs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                HashMap<String, String> map = (HashMap<String, String>)
                        listView.getItemAtPosition(position);
                String name = map.get(Config.PROJECT_PNAME);
                int pid = ProjectUtil.findPidByPname(projects, name);

                Intent intent = new Intent(view.getContext(), ShowSingleProjectActivity.class);
                ((SocoApp)getApplicationContext()).pid = pid;
                startActivityForResult(intent, INTENT_SHOW_SINGLE_PROGRAM);
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
        if (id == R.id.action_clean_up) {
            Log.i("setting", "Click on Clean up.");
            new AlertDialog.Builder(this)
                    .setMessage("Delete all programs?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbmgrSoco.cleanDB();
                            Toast.makeText(getApplicationContext(),
                                    "All Programs deleted.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),
                                    ShowActiveProjectsActivity.class);
                            i.putExtra(Config.LOGIN_EMAIL, loginEmail);
                            startActivity(i);
                            ;
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }
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
                Project p2 = new Project(n);
                dbmgrSoco.addProject(p2);
                Log.i(tag, "New project added: " + p2);
                Toast.makeText(getApplicationContext(),
                        "Project created.", Toast.LENGTH_SHORT).show();
                ProjectUtil.createProjectOnServer(n, getApplicationContext(), loginEmail, loginPassword);
                projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_PROJECT_ACTIVE);
                listProjects(null);
            }
        });
        alert.setNeutralButton("Details", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String n = input.getText().toString();
                Project p2 = new Project(n);
                dbmgrSoco.addProject(p2);
                Log.i(tag, "New project created: " + n);
                ProjectUtil.createProjectOnServer(n, getApplicationContext(), loginEmail, loginPassword);
                Intent intent = new Intent(getApplicationContext(), ShowSingleProjectActivity.class);
                intent.putExtra(Config.PROGRAM_PNAME, n);
                intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
                intent.putExtra(Config.LOGIN_PASSWORD, loginPassword);
                Log.i(tag, "Start activity to view programName details");
                startActivity(intent);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    public void listProjects(View view) {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        for (Project p : projects) {
            HashMap<String, String> map = new HashMap<>();
            map.put(Config.PROJECT_PNAME, p.pname);
            map.put(Config.PROJECT_PINFO, p.getMoreInfo());
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2,
                new String[]{Config.PROJECT_PNAME, Config.PROJECT_PINFO},
                new int[]{android.R.id.text1, android.R.id.text2});
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

}
