package com.soco.SoCoClient.view.friends.contact;

//import info.androidhive.tabsswipe.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.config._ref.GeneralConfigV1;
import com.soco.SoCoClient.control.config.SocoApp;
import com.soco.SoCoClient.control.database._ref.DBManagerSoco;
import com.soco.SoCoClient.control.http.task._ref.AddFriendTaskAsync;
import com.soco.SoCoClient.view.common.EntryItem;
import com.soco.SoCoClient.view.common.Item;
import com.soco.SoCoClient.view.common.SectionEntryListAdapter;
import com.soco.SoCoClient.control.http.UrlUtil;
import com.soco.SoCoClient.model.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentContactsObs extends Fragment implements View.OnClickListener {

    String tag = "FragmentContactsObs";
    View rootView;

//    int pid;
//    String pid_onserver;
    SocoApp socoApp;
    Profile profile;
    DBManagerSoco dbManagerSoco;
    ArrayList<Item> contactItems;
    SectionEntryListAdapter contactsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        socoApp = (SocoApp)(getActivity().getApplication());
        profile = socoApp.profile;
        dbManagerSoco = socoApp.dbManagerSoco;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(tag, "create project members fragment view.....");
        rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        ((ListView)rootView.findViewById(R.id.listview_contacts)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //new
                if (!contactItems.get(position).isSection()) {  //click on item/name
                    EntryItem item = (EntryItem) contactItems.get(position);
                    Log.d(tag, "You clicked: " + item.title);

                    String name = item.title;
                    String email = item.subtitle;
//                    int pid = ProjectUtil.findPidByPname(projects, name);
//                    socoApp.pid = pid;
//                    String pid_onserver = dbmgrSoco.findActivityIdOnserver(pid);
//                    socoApp.pid_onserver = pid_onserver;
//                    Log.i(tag, "pid/pid_onserver: " + pid + ", " + pid_onserver);

                    //new fragment-based activity
                    Intent i = new Intent(view.getContext(), ContactDetailsActivityObs.class);
                    i.putExtra(GeneralConfigV1.INTENT_KEY_NAME, name);
                    i.putExtra(GeneralConfigV1.INTENT_KEY_EMAIL, email);
                    startActivity(i);
                }
            }
        });

//        rootView.findViewById(R.id.add).setOnClickListener(this);
        listContacts();

        return rootView;
    }

//    public void updateContactName(final String email) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//        alert.setTitle("New contact name");
//        alert.setMessage("So I want to ...");
//        final EditText input = new EditText(getActivity());
//        alert.setView(input);
//
//        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String name = input.getText().toString();
//                dbManagerSoco.updateContactName(email, name);
//            }
//        });
//
//        alert.show();
//    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.add:
//                add();
//                break;
//        }
    }


    public void add(String email, String nickname){
//        String email = ((EditText) rootView.findViewById(R.id.email)).getText().toString();
        Log.i(tag, "save into db new member " + email);
        dbManagerSoco.saveContact(email, nickname);

        //todo: send invitation to server and save contact id onserver
        Log.d(tag, "send add friend request to server: " + email);
        String url = UrlUtil.getAddFriendUrl(getActivity());
        AddFriendTaskAsync task = new AddFriendTaskAsync(url, email, getActivity().getApplicationContext());
        task.execute();

        Toast.makeText(getActivity().getApplicationContext(), "Sent invitation success",
                Toast.LENGTH_SHORT).show();

        listContacts();
    }

    public void listContacts() {
        Log.d(tag, "List contacts");

        contactItems = new ArrayList<>();
        HashMap<String, String> map = dbManagerSoco.getContacts();

        for(Map.Entry<String, String> e : map.entrySet()){
            Log.d(tag, "found contact: " + e.getValue() + ", " + e.getKey());
            contactItems.add(new EntryItem(e.getValue(), e.getKey()));
        }

        Log.d(tag, "set contacts adapter");
        contactsAdapter = new SectionEntryListAdapter(getActivity(), contactItems);
        ListView lv = (ListView) rootView.findViewById(R.id.listview_contacts);
        lv.setAdapter(contactsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

//        Log.i(tag, "onResume start, reload project attribute for pid: ");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard_contacts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void addContact() {
        Log.d(tag, "create dialog elements");
        Context context = getActivity();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText emailBox = new EditText(context);
        emailBox.setHint("Email address");
        emailBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(emailBox);

        final EditText nicknameBox = new EditText(context);
        nicknameBox.setHint("Nick name");
        layout.addView(nicknameBox);

//        dialog.setView(layout);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Add new contact");
//        alert.setMessage("Email address:");
//        final EditText input = new EditText(getActivity());
        alert.setView(layout);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String email = emailBox.getText().toString();
                String nickname = nicknameBox.getText().toString();
                add(email, nickname);
//                Toast.makeText(getActivity().getApplicationContext(),
//                        "Invited contact complete.", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            Log.i(tag, "Click on add.");
            addContact();
        }

        return super.onOptionsItemSelected(item);
    }



}