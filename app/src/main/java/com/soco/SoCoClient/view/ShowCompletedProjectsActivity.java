package com.soco.SoCoClient.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.soco.SoCoClient.control.config.Config;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.model.Program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowCompletedProjectsActivity extends ActionBarActivity {
    static String tag = "ShowCompletedPrograms";

    // Local view
    private ListView lv_completed_programs;

    // Local variable
    private DBManagerSoco dbmgrSoco;
    private List<Program> programs;
    String loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_completed_programs);
        findViewsById();

        Intent intent = getIntent();
        loginEmail = intent.getStringExtra(Config.LOGIN_EMAIL);
        loginPassword = intent.getStringExtra(Config.LOGIN_PASSWORD);

        dbmgrSoco = new DBManagerSoco(this);
        programs = dbmgrSoco.loadPrograms(Config.PROGRAM_COMPLETED);

        listPrograms(lv_completed_programs, programs);

        lv_completed_programs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                HashMap<String, String> map = (HashMap<String, String>)
                        listView.getItemAtPosition(position);
                final String name = map.get(Config.PROGRAM_PNAME);

                Log.i(tag, "Click on completed programName.");
                new AlertDialog.Builder(ShowCompletedProjectsActivity.this)
                        .setTitle(name)
                        .setMessage("Program complete, shall we:")
                        .setPositiveButton("Re-Activate", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Program p = dbmgrSoco.loadProgram(name);
                                p.pcomplete = 0;
                                dbmgrSoco.update(name, p);
                                programs = dbmgrSoco.loadPrograms(Config.PROGRAM_COMPLETED);
                                listPrograms(lv_completed_programs, programs);
                            }})
                        .setNegativeButton("Cancel", null)
                        .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Program p = dbmgrSoco.loadProgram(name);
                                Log.i(tag, "Click on delete programName, pid: " + p.pid +
                                        ", pname: " + p.pname);
                                dbmgrSoco.delete(p.pid);
                                programs = dbmgrSoco.loadPrograms(Config.PROGRAM_COMPLETED);
                                listPrograms(lv_completed_programs, programs);
                            }})
                        .show();
            }
        });
    }

    private void findViewsById() {
        lv_completed_programs = (ListView) findViewById(R.id.lv_completed_programs);
    }

    void gotoPreviousScreen(){
        Intent intent = new Intent(this, ShowActiveProjectsActivity.class);
        intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
        intent.putExtra(Config.LOGIN_PASSWORD, loginPassword);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                gotoPreviousScreen();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        gotoPreviousScreen();
    }

    public void listPrograms(ListView listView, List<Program> programs) {
        ArrayList<Map<String, String>> data = new ArrayList<>();
        for (Program program : programs) {
            HashMap<String, String> map = new HashMap<>();
            map.put(Config.PROGRAM_PNAME, program.pname);
            map.put(Config.PROGRAM_PINFO, program.getMoreInfo());
            data.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[]{Config.PROGRAM_PNAME, Config.PROGRAM_PINFO},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_completed_programs, menu);
        return true;
    }


}
