package com.soco.SoCoClient.v2.view.chats;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.v2.control.config.SocoApp;
import com.soco.SoCoClient.v2.model.Event;
import com.soco.SoCoClient.v2.model.Person;
import com.soco.SoCoClient.v2.view.sectionlist.EntryItem;
import com.soco.SoCoClient.v2.view.sectionlist.Item;
import com.soco.SoCoClient.v2.view.sectionlist.SectionEntryListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactList extends ActionBarActivity {

    static String tag = "ContactList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        Log.i(tag, "get contact list");
        SocoApp socoApp = (SocoApp)getApplicationContext();
//        ArrayList<Map<String, String>> listNameEmail = socoApp.loadNameEmailList();
        ArrayList<Person> persons = socoApp.loadPhoneContacts(getApplicationContext());
        Log.d(tag, "load contacts complete: " + persons.size() + " found");

        showContacts(persons);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showContacts(ArrayList<Person> persons) {
        Log.d(tag, "show events to ui");
        ArrayList<Item> allListItems;
        allListItems = new ArrayList<>();

        for(Person p : persons){
//            allListItems.add(new EntryItem(e.getName(), e.getDesc()));
            allListItems.add(new EntryItem(p.getName(), p.getEmail()));
        }
//        HashMap<String, String> tags = new HashMap<>();

//        Log.d(tag, "grouping activities and add into list");
//        HashMap<String, ArrayList<Activity>> activitiesMap = ActivityUtil.groupingActivitiesByTag(activities);
//        for(Map.Entry<String, ArrayList<Activity>> e : activitiesMap.entrySet()){
//            String tag = e.getKey();
////            tags.put(tag, tag);
//            ArrayList<Activity> pp = e.getValue();
//            allListItems.add(new SectionItem(tag));   //add section
//            for(Activity p : pp) {  //add activity
//                //fix Bug #4 new activity created from invitation has delay in showing activity title
//                if(p.invitation_status == DataConfigObs.ACTIVITY_INVITATION_STATUS_COMPLETE)
//                    allListItems.add(new EntryItem(p.pname, p.getMoreInfo()));
//                else
//                    Log.d(tag, "skip showing project that pending invitation complete: " + p.pid);
//            }
//        }

//        Log.d(tag, "refresh UI");
        SectionEntryListAdapter activitiesAdapter = new SectionEntryListAdapter(this, allListItems);
        ListView lv_active_programs = (ListView) findViewById(R.id.contacts);
        lv_active_programs.setAdapter(activitiesAdapter);
    }

}
