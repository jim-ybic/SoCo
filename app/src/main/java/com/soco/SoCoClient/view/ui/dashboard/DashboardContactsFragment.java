package com.soco.SoCoClient.view.ui.dashboard;

//import info.androidhive.tabsswipe.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.config.GeneralConfig;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.http.task.InviteProjectMemberTaskAsync;
import com.soco.SoCoClient.control.util.ProjectUtil;
import com.soco.SoCoClient.model.Profile;
import com.soco.SoCoClient.model.Project;
import com.soco.SoCoClient.view.ui.project.single.ShowSingleProjectActivity;
import com.soco.SoCoClient.view.ui.section.EntryItem;
import com.soco.SoCoClient.view.ui.section.Item;
import com.soco.SoCoClient.view.ui.section.SectionEntryListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardContactsFragment extends Fragment implements View.OnClickListener {

    String tag = "ContactsFragment";
    View rootView;

    int pid;
    String pid_onserver;
    SocoApp socoApp;
    Profile profile;
    DBManagerSoco dbManagerSoco;

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

        Log.d(tag, "create project members fragment view");
        rootView = inflater.inflate(R.layout.fragment_dashboard_contacts, container, false);


        ((ListView)rootView.findViewById(R.id.listview_contacts)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                EntryItem item = (EntryItem)listView.getItemAtPosition(position);
                String name = item.title;
                final String email = item.subtitle;
                Log.i(tag, "click on contact list: " + name + ", " + email);

                new AlertDialog.Builder(getActivity())
                        .setTitle(name)
                        .setMessage("Do you want to:")
                        .setPositiveButton("New Activity", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.d(tag, "create new activity with contact");
                                //todo
                            }
                        })
                        .setNegativeButton("Update Name", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.d(tag, "edit contact details");
                                //todo
                                updateContactName(email);
                                listContacts();
                            }
                        })
                        .show();
            }
        });

        //        rootView.findViewById(R.id.add).setOnClickListener(this);
        listContacts();

        return rootView;
    }

    public void updateContactName(final String email) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("New contact name");
//        alert.setMessage("So I want to ...");
        final EditText input = new EditText(getActivity());
        alert.setView(input);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = input.getText().toString();
                dbManagerSoco.updateContactName(email, name);
            }
        });

        alert.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                add();
                break;
        }
    }


    public void add(){
        String email = ((EditText) rootView.findViewById(R.id.email)).getText().toString();
        Log.i(tag, "save into db new member " + email);
        dbManagerSoco.saveContact(email);
        Toast.makeText(getActivity().getApplicationContext(), "Sent invitation success",
                Toast.LENGTH_SHORT).show();

        listContacts();
    }

    public void listContacts() {
        Log.d(tag, "List contacts");

        ArrayList<Item> contactItems = new ArrayList<Item>();
        HashMap<String, String> map = dbManagerSoco.getContacts();

        for(Map.Entry<String, String> e : map.entrySet()){
            Log.d(tag, "found contact: " + e.getValue() + ", " + e.getKey());
            contactItems.add(new EntryItem(e.getValue(), e.getKey()));
        }

        SectionEntryListAdapter adapter = new SectionEntryListAdapter(getActivity(), contactItems);
        ListView lv = (ListView) rootView.findViewById(R.id.listview_contacts);
        lv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(tag, "onResume start, reload project attribute for pid: ");
    }

    }