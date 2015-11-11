package com.soco.SoCoClient._ref;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.database.DataLoader;
import com.soco.SoCoClient.events.ui.EventGroupListEntryItem;
import com.soco.SoCoClient.secondary.chat.model.SingleConversation;
import com.soco.SoCoClient.common.model.Person;
import com.soco.SoCoClient.secondary.chat.ConversationDetail;
import com.soco.SoCoClient.events.ui.Item;
import com.soco.SoCoClient.events.ui.EventGroupListSectionItem;

import java.util.ArrayList;

@Deprecated
public class AllBuddiesActivityV1 extends ActionBarActivity {

    static String tag = "ContactListActivity";

    static final int CONTACT_SECTION_COUNT = 2; //number of sections: my friends, all phone contacts
    static final String CONTEXT_MENU_ITEM_INVITE = "Invite";
    static final String CONTEXT_MENU_ITEM_DETAILS = "Details";
    static final String CONTEXT_MENU_ITEM_CALL = "Call";
    static final String CONTEXT_MENU_ITEM_EMAIL = "Email";
    static final String CONTEXT_MENU_ITEM_CHAT = "Chat";

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
            CONTEXT_MENU_ITEM_CHAT,
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
//        phoneContacts = socoApp.loadRawPhoneContacts(getApplicationContext());
//        Log.d(tag, "load contacts complete: " + phoneContacts.size() + " found");

        dataLoader = new DataLoader(getApplicationContext());
        friends = dataLoader.loadFriends();
        phoneContacts = dataLoader.loadPhoneContacts();

        findViewItems();
        showContacts(friends, phoneContacts);
    }

    void findViewItems(){
        contactList = (ListView) findViewById(R.id.friends);
        registerForContextMenu(contactList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_all_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            new AlertDialog.Builder(this)
                    .setTitle("Please confirm")
                    .setMessage("Are you sure to import all contacts from phone book?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getApplicationContext(), "Importing phonebook...", Toast.LENGTH_SHORT).show();
                            SocoApp socoApp = (SocoApp) getApplicationContext();
                            ArrayList<Person> phoneContacts = socoApp.loadRawPhoneContacts(getApplicationContext());
                            int counter = 0; //testing
                            for (Person p : phoneContacts) {
                                p.setCategory(Config.CONTACT_LIST_SECTION_MYPHONECONTACTS);
                                p.setStatus(Config.PERSON_STATUS_NOTCONNECTED);
//                                p.addContext(context);
                                p.save();
                                if (counter++ > 20)   //testing
                                    break;
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

            phoneContacts = dataLoader.loadPhoneContacts(); //reload list
            showContacts(friends, phoneContacts);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        Log.v(tag, "create context menu");

//        if (v.getId()==R.id.contacts) {
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            int position = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;
            Log.v(tag, "get position: " + position);

            if(position >= CONTACT_SECTION_COUNT + friends.size()) {    //tap on phone contact
                for (int i = 0; i < contextMenuPhoneContact.length; i++){
                    menu.add(Menu.NONE, i, i, contextMenuPhoneContact[i]);
                }
                int pos = position - CONTACT_SECTION_COUNT - friends.size();
                Person p = phoneContacts.get(pos);
                Log.d(tag, "position: " + position + ", phone contact pos " + pos + ": " + p.toString());
                menu.setHeaderTitle(p.getName());
            }
            else {  //tap on friend
                for (int i = 0; i< contextMenuFriend.length; i++) {
                    menu.add(Menu.NONE, i, i, contextMenuFriend[i]);
                }
                int pos = position - 1; //one section ahead
                Person p = friends.get(pos);
                Log.d(tag, "position: " + position + ", friend pos " + pos + ": " + p.toString());
                menu.setHeaderTitle(p.getName());
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

            if (item.getTitle() == CONTEXT_MENU_ITEM_INVITE) {  //invite phone contact
                if(p.getStatus().equals(Config.PERSON_STATUS_ACCEPTED))
                    Toast.makeText(getApplicationContext(), "Already in friend list", Toast.LENGTH_SHORT).show();
                else
                    return invite(p);
            }
        }
        else{  //tap on friend
            int pos = position - 1;
            Person p = friends.get(pos);
            Log.d(tag, "position is " + position + ", friend pos " + pos + ": " + p.toString());

            if (item.getTitle() == CONTEXT_MENU_ITEM_CHAT) {    //chat with friend
                SingleConversation c = dataLoader.loadSingleConversationByCtpySeq(p.getSeq());
                if(c == null){
                    Log.v(tag, "create new conversation");
                    c = new SingleConversation(context);
                    c.setCounterpartySeq(p.getSeq());
                    c.setCounterpartyName(p.getName());
//                    c.addContext(context);
                    c.save();
                    Log.d(tag, "saved new conversation: " + c.toString());
                }
                Intent i = new Intent(this, ConversationDetail.class);
                i.putExtra(Config.EXTRA_CONVERSATION_SEQ, c.getSeq());
                startActivity(i);
            }
        }

        return true;
    }

    private boolean invite(Person p) {
        Log.d(tag, "invite phone contact: " + p.getName());
        //todo: send invitation
        p.setStatus(Config.PERSON_STATUS_ACCEPTED); //testing: accepted now
//        p.addContext(context);
        p.save();

        Person p2 = new Person(context, p.getName(), p.getPhone(), p.getEmail());
        p2.setCategory(Config.CONTACT_LIST_SECTION_MYFRIENDS);
        p2.setStatus(Config.PERSON_STATUS_ACCEPTED);
//        p2.addContext(context);
        p2.save();
        Toast.makeText(getApplicationContext(), "Invitation sent", Toast.LENGTH_SHORT).show();

        friends = dataLoader.loadFriends(); //reload friend list
        phoneContacts = dataLoader.loadPhoneContacts();
        showContacts(friends, phoneContacts);
        return true;
    }

    void showContacts(ArrayList<Person> persons, ArrayList<Person> phoneContacts) {
        Log.v(tag, "show contacts: " + persons.size() + " friends, " + phoneContacts.size() + " phone contacts");
        ArrayList<Item> items = new ArrayList<>();

        items.add(new EventGroupListSectionItem(Config.CONTACT_LIST_SECTION_MYFRIENDS));
        for(Person p : persons){
            items.add(new EventGroupListEntryItem(p.getName(), p.getPhone(), p.getEmail(), p.getStatus()));
        }

        items.add(new EventGroupListSectionItem(Config.CONTACT_LIST_SECTION_MYPHONECONTACTS));
        for(Person p : phoneContacts){
            items.add(new EventGroupListEntryItem(p.getName(), p.getPhone(), p.getEmail(), p.getStatus()));
        }

        ContactListAdapter adapter = new ContactListAdapter(this, items);
        contactList.setAdapter(adapter);
    }

}
