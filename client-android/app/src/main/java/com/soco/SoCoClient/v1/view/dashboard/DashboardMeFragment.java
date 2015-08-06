package com.soco.SoCoClient.v1.view.dashboard;

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
import com.soco.SoCoClient.v1.control.SocoApp;
import com.soco.SoCoClient.v1.view.config.ProfileActivity;

public class DashboardMeFragment extends Fragment implements View.OnClickListener {

    String tag = "MeFragment";
    View rootView;

    int pid, pid_onserver;
    SocoApp socoApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        socoApp = (SocoApp)(getActivity().getApplication());
        pid = socoApp.pid;
        pid_onserver = socoApp.pid_onserver;
        Log.d(tag, "pid is " + pid + ", pid_onserver is " + pid_onserver);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(tag, "create calendar fragment view");
        rootView = inflater.inflate(R.layout.v1_fragment_dashboard_me, container, false);

//        rootView.findViewById(R.id.bt_add).setOnClickListener(this);
//        listMembers();

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
        inflater.inflate(R.menu.menu_dashboard_me, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Log.i("setting", "Click on Profile.");
            Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
//            intent.putExtra(GeneralConfig.LOGIN_EMAIL, loginEmail);
//            intent.putExtra(GeneralConfig.LOGIN_PASSWORD, loginPassword);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}