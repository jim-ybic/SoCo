package com.soco.SoCoClient.events.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.model.Event;

public class EventGroupsFragment extends Fragment implements View.OnClickListener {

    static String tag = "EventGroupsFragment";

//    public static final String EVENT_ID = "event_id";
    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 10;
    static final int THOUSAND = 1000;

    View rootView;
    Context context;
    SocoApp socoApp;
    Event event;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        socoApp = (SocoApp) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_groups, container, false);

        showGroups();

        return rootView;
    }

    private void showGroups() {
//        Log.v(tag, "wait and check register status");
//        int count = 0;
//        while(!socoApp.eventGroupsBuddiesResponse && count < WAIT_ITERATION) {   //wait for 10s
//            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
//            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
//            while (System.currentTimeMillis() < endTime) {
//                synchronized (this) {
//                    try {
//                        wait(endTime - System.currentTimeMillis());
//                    } catch (Exception e) {
//                        Log.e(tag, "Error in waiting");
//                    }
//                }
//            }
//            count++;
//        }
        event = socoApp.getCurrentSuggestedEvent();
        Log.v(tag, "current event: " + event.toString());

        Log.v(tag, "show groups on the screen");
        ImageButton viewOrg1Icon = (ImageButton) rootView.findViewById(R.id.org1icon);
        //todo: download group image
        Drawable image1 = context.getResources().getDrawable(R.drawable.eventgroups_group1);	//testing icon
        viewOrg1Icon.setImageDrawable(image1);

        TextView viewOrg1Name = (TextView) rootView.findViewById(R.id.org1name);
        viewOrg1Name.setText("abc group");

        //todo: not finished yet
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_fragment_event_groups, menu);
//
////        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.badge).getActionView();
////        TextView tv = (TextView) badgeLayout.findViewById(R.id.actionbar_notifcation_textview);
////        tv.setText("12");
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_profile) {
//            Log.i("setting", "Click on Profile.");
//            Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
//            intent.putExtra(GeneralConfigV1.LOGIN_EMAIL, loginEmail);
//            intent.putExtra(GeneralConfigV1.LOGIN_PASSWORD, loginPassword);
//            startActivity(intent);
//        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.add:
//                add();
//                break;
        }
    }


}
