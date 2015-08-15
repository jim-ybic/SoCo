package com.soco.SoCoClient.v2.view.chats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.obsolete.v1.control.config.DataConfigObs;
import com.soco.SoCoClient.v2.control.config.DataConfig;
import com.soco.SoCoClient.v2.control.config.SocoApp;
import com.soco.SoCoClient.v2.control.database.DataLoader;
import com.soco.SoCoClient.v2.control.util.ActivityUtil;
import com.soco.SoCoClient.v2.model.Person;
import com.soco.SoCoClient.v2.view.sectionlist.EntryItem;
import com.soco.SoCoClient.v2.view.sectionlist.Item;
import com.soco.SoCoClient.v2.view.sectionlist.SectionItem;

import java.util.ArrayList;

public class ContactList extends ActionBarActivity {

    static String tag = "ContactList";

    static final int CONTACT_SECTION_COUNT = 2; //number of sections: my friends, all phone contacts
    static final String CONTEXT_MENU_ITEM_INVITE = "Invite";
    static final String CONTEXT_MENU_ITEM_DETAILS = "Details";
    static final String CONTEXT_MENU_ITEM_CALL = "Call";
    static final String CONTEXT_MENU_ITEM_EMAIL = "Email";

    //local variables
    Context context;
    ListView contactList;
    ArrayList<Person> friends;
    ArrayList<Person> phoneContacts;
    DataLoader dataLoader;
    String[] contextMenuPhoneContact = {
            CONTEXT_MENU_ITEM_INVITE,
            CONTEXT_MENU_ITEM_DETAILS,
            CONTEXT_MENU_ITEM_CALL,
            CONTEXT_MENU_ITEM_EMAIL
    };
    String[] contextMenuFriend = {
            CONTEXT_MENU_ITEM_DETAILS,
            CONTEXT_MENU_ITEM_CALL,
            CONTEXT_MENU_ITEM_EMAIL
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        context = getApplicationContext();

//        Log.v(tag, "get contact contactList");
//        SocoApp socoApp = (SocoApp)getApplicationContext();
//        phoneContacts = socoApp.loadPhoneContacts(getApplicationContext());
//        Log.d(tag, "load contacts complete: " + phoneContacts.size() + " found");

        dataLoader = new DataLoader(getApplicationContext());
        friends = dataLoader.loadFriends();
        phoneContacts = dataLoader.loadPhoneContacts();

        findViewItems();
        showContacts(friends, phoneContacts);
    }

    void findViewItems(){
        contactList = (ListView) findViewById(R.id.contacts);
        registerForContextMenu(contactList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.import_phonebook) {
            new AlertDialog.Builder(this)
                    .setTitle("Please confirm")
                    .setMessage("Are you sure to import all contacts from phone book?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getApplicationContext(), "Importing phonebook...", Toast.LENGTH_SHORT).show();
                            SocoApp socoApp = (SocoApp)getApplicationContext();
                            ArrayList<Person> phoneContacts = socoApp.loadPhoneContacts(getApplicationContext());
                            int counter = 0; //testing
                            for(Person p : phoneContacts){
                                p.setCategory(DataConfig.CONTACT_LIST_SECTION_MYPHONECONTACTS);
                                p.setStatus(DataConfig.PERSON_STATUS_NOTCONNECTED);
                                p.addContext(context);
                                p.save();
                                if (counter ++ > 20)   //testing
                                    break;
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        Log.v(tag, "create context menu");
        for (int i = 0; i< contextMenuPhoneContact.length; i++)
            menu.add(Menu.NONE, i, i, contextMenuPhoneContact[i]);

//        if (v.getId()==R.id.contacts) {
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            int position = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;
            Log.v(tag, "get position: " + position);

            if(position >= CONTACT_SECTION_COUNT + friends.size()){    //tap on phone contact
                int pos = position - CONTACT_SECTION_COUNT - friends.size();
                Person p = phoneContacts.get(pos);
                Log.d(tag, "position: " + position + ", phone contact pos " + pos + ": " + p.toString());
                menu.setHeaderTitle(p.getName());
            }
            else {  //tap on friend
                //todo
            }
//        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
        Log.d(tag, "select context item: " + item.toString() + ", position is " + position);

        if(position >= CONTACT_SECTION_COUNT + friends.size()) {    //tap on phone contact
            int pos = position - CONTACT_SECTION_COUNT - friends.size();
            Person p = phoneContacts.get(pos);
            Log.d(tag, "position is " + position + ", phone contact pos " + pos + ": " + p.toString());
            //todo: check the contact status - if already invited as friend

            if (item.getTitle() == CONTEXT_MENU_ITEM_INVITE) {
                Log.d(tag, "invite phone contact: " + p.getName());
                p.setStatus(DataConfig.PERSON_STATUS_INVITED);
                //todo: send invitation

                p.setCategory(DataConfig.CONTACT_LIST_SECTION_MYFRIENDS);
                p.setStatus(DataConfig.PERSON_STATUS_ACCEPTED); //testing: accepted now
                p.addContext(context);
                p.save();
                Toast.makeText(getApplicationContext(), "Invitation sent", Toast.LENGTH_SHORT).show();

                friends = dataLoader.loadFriends(); //reload friend list
                phoneContacts = dataLoader.loadPhoneContacts();
                showContacts(friends, phoneContacts);
                return true;
            }
        }

        return true;
    }

    public void showContacts(ArrayList<Person> persons, ArrayList<Person> phoneContacts) {
        Log.v(tag, "show contacts: " + persons.size() + " friends, " + phoneContacts.size() + " phone contacts");
        ArrayList<Item> items = new ArrayList<>();

        items.add(new SectionItem(DataConfig.CONTACT_LIST_SECTION_MYFRIENDS));
        for(Person p : persons){
            items.add(new EntryItem(p.getName(), p.getEmail(), p.getStatus()));
        }

        items.add(new SectionItem(DataConfig.CONTACT_LIST_SECTION_MYPHONECONTACTS));
        for(Person p : phoneContacts){
            items.add(new EntryItem(p.getName(), p.getEmail(), p.getStatus()));
        }

        ContactListAdapter adapter = new ContactListAdapter(this, items);
        contactList.setAdapter(adapter);
    }

}
