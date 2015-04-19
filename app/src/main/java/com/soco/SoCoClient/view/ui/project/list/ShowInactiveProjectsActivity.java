package com.soco.SoCoClient.view.ui.project.list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.config.GeneralConfig;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.util.ProjectUtil;
import com.soco.SoCoClient.model.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowInactiveProjectsActivity extends ActionBarActivity {
    static String tag = "ShowCompletedPrograms";

    // Local view
    private ListView lv_inactive_projects;

    // Local variable
    private DBManagerSoco dbmgrSoco;
    List<Project> projects;
    String loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_completed_projects);
        findViewsById();

        Intent intent = getIntent();
        loginEmail = intent.getStringExtra(GeneralConfig.LOGIN_EMAIL);
        loginPassword = intent.getStringExtra(GeneralConfig.LOGIN_PASSWORD);

        dbmgrSoco = new DBManagerSoco(this);
        projects = dbmgrSoco.loadProjectsByActiveness(DataConfig.VALUE_ACTIVITY_INACTIVE);

        listProjects(lv_inactive_projects, projects);

        lv_inactive_projects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                HashMap<String, String> map = (HashMap<String, String>)
                        listView.getItemAtPosition(position);
                final String name = map.get(GeneralConfig.PROJECT_PNAME);

                Log.i(tag, "Click on completed programName.");
                new AlertDialog.Builder(ShowInactiveProjectsActivity.this)
                        .setTitle(name)
                        .setMessage("Program complete, shall we:")
                        .setPositiveButton("Re-Activate", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int pid = ProjectUtil.findPidByPname(projects, name);
                                dbmgrSoco.updateProjectActiveness(pid,
                                        DataConfig.VALUE_ACTIVITY_ACTIVE);
                                projects = dbmgrSoco.loadProjectsByActiveness(
                                        DataConfig.VALUE_ACTIVITY_INACTIVE);
                                listProjects(lv_inactive_projects, projects);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int pid = ProjectUtil.findPidByPname(projects, name);
                                dbmgrSoco.deleteProjectByPid(pid);
                                projects = dbmgrSoco.loadProjectsByActiveness(
                                        DataConfig.VALUE_ACTIVITY_INACTIVE);
                                listProjects(lv_inactive_projects, projects);
                            }
                        })
                        .show();
            }
        });
    }

    private void findViewsById() {
        lv_inactive_projects = (ListView) findViewById(R.id.lv_completed_programs);
    }

//    void gotoPreviousScreen(){
//        Intent intent = new Intent(this, ShowActiveProjectsActivity.class);
//        intent.putExtra(GeneralConfig.LOGIN_EMAIL, loginEmail);
//        intent.putExtra(GeneralConfig.LOGIN_PASSWORD, loginPassword);
//        startActivity(intent);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                gotoPreviousScreen();
//        }
//        return true;
//    }

//    @Override
//    public void onBackPressed() {
//        gotoPreviousScreen();
//    }

    public void listProjects(ListView listView, List<Project> projects) {
        ArrayList<Map<String, String>> data = new ArrayList<>();
        for (Project project : projects) {
            HashMap<String, String> map = new HashMap<>();
            map.put(GeneralConfig.PROJECT_PNAME, project.pname);
            map.put(GeneralConfig.PROJECT_PINFO, project.getMoreInfo());
            data.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[]{GeneralConfig.PROJECT_PNAME, GeneralConfig.PROJECT_PINFO},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_show_completed_programs, menu);
//        return true;
//    }


}
