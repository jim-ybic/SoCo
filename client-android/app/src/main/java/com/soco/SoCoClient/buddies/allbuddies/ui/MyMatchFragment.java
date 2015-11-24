package com.soco.SoCoClient.buddies.allbuddies.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.buddies.service.DownloadMyMatchService;
import com.soco.SoCoClient.common.util.SocoApp;

import java.util.ArrayList;


public class MyMatchFragment extends Fragment implements View.OnClickListener {

    static String tag = "MyMatchFragment";

    static final int WAIT_INTERVAL_IN_SECOND = 1;
    static final int WAIT_ITERATION = 5;
    static final int THOUSAND = 1000;
    View rootView;
    Context context;
    SocoApp socoApp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();
        socoApp = (SocoApp)context;
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
        Log.v(tag, "show my match");
        new Thread(new Runnable(){
            public void run(){
                downloadMyMatchInBackgroud(getActivity(), socoApp);
                downloadMyMatchHandler.sendEmptyMessage(0);
            }
        }).start();
    }
    public static void downloadMyMatchInBackgroud(Context context, SocoApp socoApp) {
        Log.v(tag, "start download users service at backend");
        Intent i = new Intent(context, DownloadMyMatchService.class);
        context.startService(i);

        Log.v(tag, "set response flag as false");
        socoApp.downloadMyMatchResponse = false;

        Log.v(tag, "wait and check response flag");
        int count = 0;
        while(!socoApp.downloadMyMatchResponse && count < WAIT_ITERATION) {   //wait for 10s
            Log.d(tag, "wait for response: " + count * WAIT_INTERVAL_IN_SECOND + "s");
            long endTime = System.currentTimeMillis() + WAIT_INTERVAL_IN_SECOND*THOUSAND;
            while (System.currentTimeMillis() < endTime) {
                synchronized (context) {
                    try {
                        context.wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                        Log.e(tag, "Error in waiting");
                    }
                }
            }
            count++;
        }
    }

    Handler downloadMyMatchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(tag, "handle receive message and dismiss dialog");
            if(socoApp.downloadMyMatchResponse && socoApp.downloadMyMatchResult){
                Log.v(tag, "download my match buddy - success");
                MyMatchListAdapter  adapter = new MyMatchListAdapter(getActivity(), socoApp.myMatch);
                ListView list = (ListView) rootView.findViewById(R.id.friends);
                list.setAdapter(adapter);
            }
            else{
                Log.e(tag, "download my match buddy fail, notify user");
                Toast.makeText(getActivity().getApplicationContext(), "Download events error, please try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
