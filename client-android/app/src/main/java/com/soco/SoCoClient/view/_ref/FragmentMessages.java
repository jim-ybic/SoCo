package com.soco.SoCoClient.view._ref;

//import info.androidhive.tabsswipe.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.view.config.ActivityProfile;

@Deprecated
public class FragmentMessages extends Fragment implements View.OnClickListener {

    String tag = "FragmentMessages";
    View rootView;

//    int pid, pid_onserver;
//    SocoApp socoApp;

    /** An array of strings to populate dropdown list */
//    String[] actions = new String[] {
//            "Bookmark",
//            "Subscribe",
//            "Share"
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

//        socoApp = (SocoApp)(getActivity().getApplication());
//        pid = socoApp.pid;
//        pid_onserver = socoApp.pid_onserver;
//        Log.d(tag, "pid is " + pid + ", pid_onserver is " + pid_onserver);

//        //drop down navigation - start
//        /** Create an array adapter to populate dropdownlist */
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actions);
//
//        /** Enabling dropdown list navigation for the action bar */
//        ((ActionBarActivity)getActivity()).getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//
//        /** Defining Navigation listener */
//        android.support.v7.app.ActionBar.OnNavigationListener navigationListener = new android.support.v7.app.ActionBar.OnNavigationListener() {
//            @Override
//            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//                Toast.makeText(getActivity().getBaseContext(), "You selected : " + actions[itemPosition], Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        };
//
//        /** Setting dropdown items and item navigation listener for the actionbar */
//        ((ActionBarActivity)getActivity()).getSupportActionBar().setListNavigationCallbacks(
//                adapter, navigationListener);
//        //drop down navigation - end
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(tag, "create fragment view");
        rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        return rootView;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bt_add:
//                break;
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_messages, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profile) {
            Log.i("setting", "Click on Profile.");
            Intent intent = new Intent(getActivity().getApplicationContext(), ActivityProfile.class);
//            intent.putExtra(GeneralConfigV1.LOGIN_EMAIL, loginEmail);
//            intent.putExtra(GeneralConfigV1.LOGIN_PASSWORD, loginPassword);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}