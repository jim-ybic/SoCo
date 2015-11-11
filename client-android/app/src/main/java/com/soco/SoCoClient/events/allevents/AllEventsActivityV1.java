package com.soco.SoCoClient.events.allevents;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.database.DataLoader;
import com.soco.SoCoClient.events._ref.ActivityEventDetailV1;
import com.soco.SoCoClient.events.common.ui.EventListAdapter;
import com.soco.SoCoClient.events.common.ui.EventListEntryItem;
import com.soco.SoCoClient.events.model.EventV1;
import com.soco.SoCoClient.events.ui.Item;

import java.util.ArrayList;

public class AllEventsActivityV1 extends ActionBarActivity {

    static String tag = "AllEvents";

    ListView lv_active_programs;
    EditText et_quick_add;

    Context context;
    DataLoader dataLoader;
    ArrayList<EventV1> eventV1s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events_v1);

        context = getApplicationContext();
        dataLoader = new DataLoader(context);
        eventV1s = dataLoader.loadEvents();

        lv_active_programs = (ListView) findViewById(R.id.all_events);
        et_quick_add = ((EditText) findViewById(R.id.et_quickadd));
//        findViewById(R.id.join).setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                add();
//            }
//        });


        showEvents(eventV1s);

        lv_active_programs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventV1 e = eventV1s.get(position);
                Log.d(tag, "tap on event: " + e.toString());

                Intent i = new Intent(view.getContext(), ActivityEventDetailV1.class);
                i.putExtra(Config.EXTRA_EVENT_SEQ, e.getSeq());
                startActivity(i);
            }
        });
    }

    void showEvents(ArrayList<EventV1> eventV1s) {
        Log.v(tag, "show events to ui");
        ArrayList<Item> allListItems = new ArrayList<>();

        for(EventV1 e : eventV1s){
            allListItems.add(new EventListEntryItem(e.getName(), e.getDesc(), e.getDate()));
        }

//        Log.d(tag, "refresh UI");
        EventListAdapter adapter = new EventListAdapter(this, allListItems);
        lv_active_programs.setAdapter(adapter);
    }

    public void add(View view){
        String name = et_quick_add.getText().toString();
        Log.d(tag, "quick add event: " + name);

        Log.v(tag, "create new event");
        EventV1 e = new EventV1(getApplicationContext());
        e.setName(name);
//        e.addContext(context);
        e.save();

        DataLoader dataLoader = new DataLoader(context);
        eventV1s = dataLoader.loadEvents();
        showEvents(eventV1s);

        //clean up
        et_quick_add.setText("", TextView.BufferType.EDITABLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow((findViewById(R.id.et_quickadd)).getWindowToken(), 0);
    }

    public void onResume() {
        super.onResume();
//        Log.i(tag, "onResume start, reload active projects");
//        activities = dbmgrSoco.loadActivitiessByActiveness(DataConfigV1.VALUE_ACTIVITY_ACTIVE);
//        activities = dbmgrSoco.loadActiveActivitiesByPath(socoApp.currentPath);
//        refreshList();
        eventV1s = dataLoader.loadEvents();
        showEvents(eventV1s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_all_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            //insert code
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
