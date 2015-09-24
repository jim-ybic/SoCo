package com.soco.SoCoClient.view.events.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.soco.SoCoClient.control.config._ref.DataConfigV1;
import com.soco.SoCoClient.control.config._ref.GeneralConfigV1;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.database._ref.DBManagerSoco;
import com.soco.SoCoClient.control.util.ActivityUtil;
import com.soco.SoCoClient.model._ref.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Deprecated
public class CompletedActivitiessActivity extends ActionBarActivity {
    static String tag = "ShowCompletedPrograms";

    // Local view
    private ListView lv_inactive_projects;

    // Local variable
    private DBManagerSoco dbmgrSoco;
    List<Activity> activities;
    String loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v1_activity_show_completed_activities);
        findViewsById();

        Intent intent = getIntent();
        loginEmail = intent.getStringExtra(GeneralConfigV1.LOGIN_EMAIL);
        loginPassword = intent.getStringExtra(GeneralConfigV1.LOGIN_PASSWORD);

        dbmgrSoco = new DBManagerSoco(this);
        activities = dbmgrSoco.loadActivitiessByActiveness(DataConfigV1.VALUE_ACTIVITY_INACTIVE);

        listProjects(lv_inactive_projects, activities);

        lv_inactive_projects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                HashMap<String, String> map = (HashMap<String, String>)
                        listView.getItemAtPosition(position);
                final String name = map.get(GeneralConfigV1.ACTIVITY_NAME);

                Log.i(tag, "Click on completed programName.");
                new AlertDialog.Builder(CompletedActivitiessActivity.this)
                        .setTitle(name)
                        .setMessage("Program complete, shall we:")
                        .setPositiveButton("Re-Activate", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int pid = ActivityUtil.findPidByPname(activities, name);
                                dbmgrSoco.updateActivityActiveness(pid,
                                        DataConfigV1.VALUE_ACTIVITY_ACTIVE);
                                activities = dbmgrSoco.loadActivitiessByActiveness(
                                        DataConfigV1.VALUE_ACTIVITY_INACTIVE);
                                listProjects(lv_inactive_projects, activities);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int pid = ActivityUtil.findPidByPname(activities, name);
                                dbmgrSoco.deleteActivityByPid(pid);
                                activities = dbmgrSoco.loadActivitiessByActiveness(
                                        DataConfigV1.VALUE_ACTIVITY_INACTIVE);
                                listProjects(lv_inactive_projects, activities);
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
//        intent.putExtra(GeneralConfigV1.LOGIN_EMAIL, loginEmail);
//        intent.putExtra(GeneralConfigV1.LOGIN_PASSWORD, loginPassword);
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

    public void listProjects(ListView listView, List<Activity> activities) {
        ArrayList<Map<String, String>> data = new ArrayList<>();
        for (Activity activity : activities) {
            HashMap<String, String> map = new HashMap<>();
            map.put(GeneralConfigV1.ACTIVITY_NAME, activity.pname);
            map.put(GeneralConfigV1.ACTIVITY_INFO, activity.getMoreInfo());
            data.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[]{GeneralConfigV1.ACTIVITY_NAME, GeneralConfigV1.ACTIVITY_INFO},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_show_completed_programs, menu);
//        return true;
//    }


}
