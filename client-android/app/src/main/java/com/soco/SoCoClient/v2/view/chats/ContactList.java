package com.soco.SoCoClient.v2.view.chats;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.v2.control.config.SocoApp;
import com.soco.SoCoClient.v2.model.Person;
import com.soco.SoCoClient.v2.view.sectionlist.EntryItem;
import com.soco.SoCoClient.v2.view.sectionlist.Item;

import java.util.ArrayList;

public class ContactList extends ActionBarActivity {

    static String tag = "ContactList";

    //local variables
    ListView contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        Log.i(tag, "get contact contactList");
        SocoApp socoApp = (SocoApp)getApplicationContext();
//        ArrayList<Map<String, String>> listNameEmail = socoApp.loadNameEmailList();
        ArrayList<Person> persons = socoApp.loadPhoneContacts(getApplicationContext());
        Log.d(tag, "load contacts complete: " + persons.size() + " found");

        showContacts(persons);
    }

    void findViewItems(){
        contactList = (ListView) findViewById(R.id.contacts);
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
        Log.v(tag, "show contacts to ui");
        ArrayList<Item> items = new ArrayList<>();

        for(Person p : persons){
            items.add(new EntryItem(p.getName(), p.getEmail()));
        }

        ContactListAdapter adapter = new ContactListAdapter(this, items);
        contactList.setAdapter(adapter);
    }

}
