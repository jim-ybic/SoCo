package com.soco.SoCoClient.userprofile.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.userprofile.model.User;


public class UserProfileFragment extends Fragment implements View.OnClickListener {

    static String tag = "UserProfileFragment";

    View rootView;
    Context context;
    SocoApp socoApp;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        socoApp = (SocoApp) context;

        user = socoApp.getCurrentSuggestedBuddy();
        Log.v(tag, "get user: " + user.toString());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        showBasics();
        showInterests();
        showFriends();

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

    private void showBasics(){
        Log.v(tag, "show basics: " + user.toString());

        //todo: hometown is missing in interface

        //todo: bio is missing in interface
    }

    private void showInterests(){
        Log.v(tag, "show interests: " + user.getInterests().toString());

        LinearLayout interestsList = (LinearLayout) rootView.findViewById(R.id.interests);
        interestsList.removeAllViews();

        for(int i=0; i<user.getInterests().size(); i++){
            String inter = user.getInterests().get(i);
            TextView view = new TextView(context);
            view.setText(inter);
            view.setBackgroundResource(R.drawable.eventcategory_box);
            view.setPadding(10, 5, 10, 5);
            view.setTextColor(Color.BLACK);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 10, 5);
            view.setLayoutParams(params);

            interestsList.addView(view);
        }

    }

    private void showFriends(){
        Log.v(tag, "show friends: ");

        //todo: need to call interface GET /v1/user to friend list

    }

}
