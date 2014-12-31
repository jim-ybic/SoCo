package com.soco.SoCoClient;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.soco.SoCoClient.datamodel.DBManagerSoco;
import com.soco.SoCoClient.datamodel.Program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowCompletedProgramsActivity extends ActionBarActivity {

    private DBManagerSoco dbmgrSoco;
    private ListView listViewSoco;
    private List<Program> programs;
    String loginEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_completed_programs);

        Intent intent = getIntent();
        loginEmail = intent.getStringExtra(Config.LOGIN_EMAIL);

        dbmgrSoco = new DBManagerSoco(this);
        programs = dbmgrSoco.loadPrograms(Config.PROGRAM_COMPLETED);

        listViewSoco = (ListView) findViewById(R.id.lv_complete_programs);
        listPrograms(listViewSoco, programs);

        listViewSoco.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView)parent;
                HashMap<String, String> map = (HashMap<String, String>)
                        listView.getItemAtPosition(position);
                String name = map.get(Config.PROGRAM_PNAME);

                Intent intent = new Intent(view.getContext(), ShowSingleProgramActivity.class);
                intent.putExtra(Config.PROGRAM_PNAME, name);
                startActivity(intent);
            }
        });
    }

    void gotoPreviousScreen(){
        Intent intent = new Intent(this, ShowActiveProgramsActivity.class);
        intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_completed_programs, menu);
        return true;
    }


}
