package com.soco.SoCoClient.buddies.allbuddies.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient._ref.ContactListAdapter;
import com.soco.SoCoClient._ref.ContactListEntryItem;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.model.Person;
import com.soco.SoCoClient.common.ui.Item;
import com.soco.SoCoClient.common.ui.SectionItem;

import java.util.ArrayList;


public class MyMatchFragment extends Fragment implements View.OnClickListener {

    static String tag = "MyMatchFragment";

    View rootView;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_my_match, container, false);

        showMyMatches();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    void showMyMatches() {
        Log.v(tag, "show my matches");

        ArrayList<MyMatchListEntryItem> items = new ArrayList<>();

        Log.v(tag, "add dummy suggested buddies");
        for(int i=0; i<20; i++)
            items.add(new MyMatchListEntryItem());

        //todo: fill in the items with the list of suggested buddies


        MyMatchListAdapter adapter = new MyMatchListAdapter(getActivity(), items);

        ListView list = (ListView) rootView.findViewById(R.id.friends);
        list.setAdapter(adapter);
    }
}
