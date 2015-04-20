package com.soco.SoCoClient.view.ui.dashboard;

//import info.androidhive.tabsswipe.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.config.HttpConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.http.task.InviteProjectMemberTaskAsync;
import com.soco.SoCoClient.model.Profile;
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

        rootView.findViewById(R.id.add).setOnClickListener(this);
        listContacts();

        return rootView;
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


}