package com.soco.SoCoClient.v2.view.chats;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.v2.control.config.SocoApp;
import com.soco.SoCoClient.v2.control.database.DataLoader;
import com.soco.SoCoClient.v2.model.Person;
import com.soco.SoCoClient.v2.view.sectionlist.EntryItem;
import com.soco.SoCoClient.v2.view.sectionlist.Item;
import com.soco.SoCoClient.v2.view.sectionlist.SectionItem;

import java.util.ArrayList;

public class ContactList extends ActionBarActivity {

    static String tag = "ContactList";

    //local variables
    ListView contactList;
    ArrayList<Person> persons;
    ArrayList<Person> phoneContacts;
    DataLoader dataLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        Log.i(tag, "get contact contactList");
        SocoApp socoApp = (SocoApp)getApplicationContext();
//        ArrayList<Map<String, String>> listNameEmail = socoApp.loadNameEmailList();
        phoneContacts = socoApp.loadPhoneContacts(getApplicationContext());
        Log.d(tag, "load contacts complete: " + phoneContacts.size() + " found");

        dataLoader = new DataLoader(getApplicationContext());
        persons = dataLoader.loadPersons();

        findViewItems();
        showContacts(persons, phoneContacts);
    }

    void findViewItems(){
        contactList = (ListView) findViewById(R.id.contacts);
        registerForContextMenu(contactList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_list, menu);
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

    int getSectionCount(){
        return 2;   //number of sections: my friends, all phone contacts
    }

    int getFriendCount(){
        return persons.size();  //number of friends
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        Log.d(tag, "create context menu");

        String[] tabs = {
                "Invite",
                "Details",
                "Call",
                "Email"
        };
        for (int i = 0; i<tabs.length; i++) {
            menu.add(Menu.NONE, i, i, tabs[i]);
        }

        if (v.getId()==R.id.contacts) {
            ListView lv = (ListView) v;
            for(int i=0; i<lv.getCount(); i++)  //testing
                Log.v(tag, "lv item: " + lv.getItemAtPosition(i).toString());

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            int position = info.position;
            Log.d(tag, "get position: " + position);

            if(position > getSectionCount() + getFriendCount()){    //tap on phone contact
                int pos = position - getSectionCount();
                Person p = phoneContacts.get(pos);
                Log.d(tag, "phone contact pos " + pos + ": " + p.toString());
                menu.setHeaderTitle(p.getName());
            }
            else {  //tap on friend
                //todo
            }
        }
    }

    public void showContacts(ArrayList<Person> persons, ArrayList<Person> phoneContacts) {
        Log.v(tag, "show contacts: " + persons.size() + " persons, " + phoneContacts.size() + " phone contacts");
        ArrayList<Item> items = new ArrayList<>();

        items.add(new SectionItem("My friends"));
        for(Person p : persons){
            items.add(new EntryItem(p.getName(), p.getEmail()));
        }

        items.add(new SectionItem("All phone contacts"));
        int counter = 0;
        for(Person p : phoneContacts){
            items.add(new EntryItem(p.getName(), p.getEmail()));

            //testing
            if(counter ++ > 100)
                break;
        }

        ContactListAdapter adapter = new ContactListAdapter(this, items);
        contactList.setAdapter(adapter);
    }

}
