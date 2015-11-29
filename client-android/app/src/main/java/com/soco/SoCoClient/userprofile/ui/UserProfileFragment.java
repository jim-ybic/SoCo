package com.soco.SoCoClient.userprofile.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.ui.ExpandableHeightGridView;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.events.common.EventBuddiesFragment;
import com.soco.SoCoClient.events.common.BuddiesGridSimpleAdapter;
import com.soco.SoCoClient.userprofile.UserProfileActivity;
import com.soco.SoCoClient.userprofile.model.User;
import com.soco.SoCoClient.userprofile.task.UserProfileTask;

import java.util.ArrayList;
import java.util.HashMap;


public class UserProfileFragment extends Fragment
        implements View.OnClickListener, TaskCallBack
{

    static String tag = "UserProfileFragment";

    static final String ItemImage = "ItemImage";
    static final String ItemName = "ItemName";
    static final String ItemId = "ItemId";

    View rootView;
    Context context;
    SocoApp socoApp;
    User user;
    ArrayList<HashMap<String, Object>> friends = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        socoApp = (SocoApp) context;

        user = socoApp.currentUserOnProfile;
        Log.v(tag, "get user: " + user.toString());

        Log.v(tag, "background task: user profile");
        new UserProfileTask(context, user, this).execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        showBasics();
        showInterests();
        BuddiesGridSimpleAdapter adapter = new BuddiesGridSimpleAdapter(getActivity(),
                friends,
                R.layout.eventbuddiesgrid_entry,
                new String[] {ItemImage,ItemName,ItemId},
                new int[] {R.id.image,R.id.name,R.id.id});
        ExpandableHeightGridView view = (ExpandableHeightGridView) rootView.findViewById(R.id.grid);
        view.setExpanded(true);
        view.setAdapter(adapter);
        view.setFocusable(false);

        view.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                            View arg1,//The view within the AdapterView that was clicked
                                            int arg2,//The position of the view in the adapter
                                            long arg3//The row id of the item that was clicked
                    ) {
                        HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                        Log.v(EventBuddiesFragment.tag, "text: " + item.get(ItemName) + ", " + item.get(ItemId));

                        Intent i = new Intent(context, UserProfileActivity.class);
                        i.putExtra(User.USER_ID, String.valueOf(item.get(ItemId)));
                        Log.v(tag, "put userid: " + item.get(ItemId));
                        startActivity(i);
                    }
                }
        );

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

        String hometown = user.getHometown();
        if(StringUtil.isEmptyString(hometown)){
            ((LinearLayout)rootView.findViewById(R.id.hometownarea)).removeAllViews();
        }else {
            ((TextView) rootView.findViewById(R.id.hometown)).setText(hometown);
            Log.v(tag, "set hometown " + hometown);
        }
        String bio = user.getBiography();
        if(StringUtil.isEmptyString(hometown)){
            ((LinearLayout)rootView.findViewById(R.id.bioarea)).removeAllViews();
        }else {
            ((TextView)rootView.findViewById(R.id.bio)).setText(bio);
            Log.v(tag, "set bio " + bio);
        }
    }

    private void showInterests(){
        Log.v(tag, "show interests: " + user.getInterests().toString());
        if(user.getInterests()==null||user.getInterests().size()==0){
            LinearLayout interestAreaList = (LinearLayout) rootView.findViewById(R.id.interestsarea);
            interestAreaList.removeAllViews();
        }else {
            LinearLayout interestsList = (LinearLayout) rootView.findViewById(R.id.interests);
            interestsList.removeAllViews();

            for (int i = 0; i < user.getInterests().size(); i++) {
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
    }


    public void doneTask(Object o){
        Log.v(tag, "done task: " + user.toString());

        TextView hometown = (TextView) rootView.findViewById(R.id.hometown);
        hometown.setText(user.getHometown());

        TextView bio = (TextView) rootView.findViewById(R.id.bio);
        bio.setText(user.getBiography());

        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Log.v(tag, "show friends: " + user.getFriends_list().toString());
        for(User u : user.getFriends_list()){
            HashMap<String, Object> item = getItemForShow(params,backgroundResource,u);
            friends.add(item);
            Log.v(tag, "added buddy: " + u.getUser_id() + ", " + u.getUser_name());
        }

        Log.v(tag, "show the data in grid");
        BuddiesGridSimpleAdapter adapter = new BuddiesGridSimpleAdapter(getActivity(),
                friends,
                R.layout.eventbuddiesgrid_entry,
                new String[] {ItemImage,ItemName,ItemId},
                new int[] {R.id.image,R.id.name,R.id.id});
//        GridView gridviewLB = (GridView) rootView.findViewById(R.id.grid);
        ExpandableHeightGridView view = (ExpandableHeightGridView) rootView.findViewById(R.id.grid);
        view.setExpanded(true);
        view.setAdapter(adapter);
        view.setFocusable(false);

        view.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                            View arg1,//The view within the AdapterView that was clicked
                                            int arg2,//The position of the view in the adapter
                                            long arg3//The row id of the item that was clicked
                    ) {
                        HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                        Log.v(EventBuddiesFragment.tag, "text: " + item.get(ItemName) + ", " + item.get(ItemId));

                        Intent i = new Intent(context, UserProfileActivity.class);
                        i.putExtra(User.USER_ID, String.valueOf(item.get(ItemId)));
                        Log.v(tag, "put userid: " + item.get(ItemId));
                        startActivity(i);
                    }
                }
        );

    }

    private HashMap<String,Object> getItemForShow(LinearLayout.LayoutParams params, int backgroundResource,User user){
        Log.v(tag, "show friend item: " + user.getUser_name() + ", " + UrlUtil.getUserIconUrl(user.getUser_id()));

        HashMap<String, Object> item = new HashMap<>();
        item.put(ItemImage, UrlUtil.getUserIconUrl(user.getUser_id()));
        item.put(ItemName, user.getUser_name());
        item.put(ItemId, user.getUser_id());
        return item;
    }




}
