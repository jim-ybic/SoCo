package com.soco.SoCoClient.groups;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.groups.ui.SimpleGroupCardAdapter;
import com.soco.SoCoClient.userprofile.UserProfileActivity;

import java.util.ArrayList;

public class AllGroupsActivity extends ActionBarActivity {

    static final String tag = "AllGroupsActivity";
    static final int CREATE_GROUP = 1001;

    RecyclerView mRecyclerView;
    SimpleGroupCardAdapter simpleGroupCardAdapter;
    ArrayList<Group> groups = new ArrayList<>();

    android.support.v7.app.ActionBar actionBar;
    View actionbarView;

    SocoApp socoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_groups);

        socoApp = (SocoApp) getApplicationContext();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

//        generateDummyEntries();

        simpleGroupCardAdapter = new SimpleGroupCardAdapter(this, groups);
        mRecyclerView.setAdapter(simpleGroupCardAdapter);


    }

//    private void generateDummyEntries() {
//
//        //todo: use groups, instead of events
//
//        Log.v(tag, "add dummy entries");
//        if(events == null)
//            events = new ArrayList<>();
//        for(int i=0; i<20; i++)
//            events.add(new Event());
//     }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_all_groups, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void creategroup(View view){
        Log.v(tag, "tap create new group");
        Intent i = new Intent(this, CreateGroupActivity.class);
        startActivityForResult(i, CREATE_GROUP);
    }

    public void groupdetails(View view){
        Log.v(tag, "tap on a single group, show details");
        Intent i = new Intent(this, GroupDetailsActivity.class);

        //todo: pass group id as parameter (currently only testing ui)

        startActivity(i);
    }

    public void mygroups(View view){
        Log.v(tag, "tap show my groups");
        Intent i = new Intent(this, UserProfileActivity.class);

        //todo: pass parameters - user id, go to group tab

        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(tag, "on activity result: request code" + requestCode
                + ", result code: " + resultCode
                + ", intent data: " + data);

        if(requestCode == CREATE_GROUP && socoApp.createGroupResult){
            Log.v(tag, "create group success, continue to the new group details screen");

            Intent i = new Intent(this, GroupDetailsActivity.class);
            //todo: pass group id as parameter
            startActivity(i);
        }

        return;
    }


}
