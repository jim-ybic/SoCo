package com.soco.SoCoClient.events.common;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.userprofile.UserProfileActivity;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class EventBuddiesFragment extends Fragment implements View.OnClickListener {

    public static String tag = "EventBuddiesFragment";

    static final String ItemImage = "ItemImage";
    static final String ItemText = "ItemText";

    View rootView;
    SocoApp socoApp;
    Context context;
    Event event;
    ArrayList<HashMap<String, Object>> joinedBuddies = new ArrayList<>();
    ArrayList<HashMap<String, Object>> likedBuddies = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        socoApp = (SocoApp) context;

        event = socoApp.getCurrentSuggestedEvent();
        Log.v(tag, "current event: " + event.toString());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_event_buddies, container, false);

        loadBuddies();

        SimpleAdapter saJoinedBuddies = new SimpleAdapter(getActivity(),
                joinedBuddies,
                R.layout.eventbuddiesgrid_entry,
                new String[] {ItemImage,ItemText},
                new int[] {R.id.image,R.id.name});
        GridView gridviewJB = (GridView) rootView.findViewById(R.id.gridJoiners);
        gridviewJB.setAdapter(saJoinedBuddies);
        gridviewJB.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                            View arg1,//The view within the AdapterView that was clicked
                                            int arg2,//The position of the view in the adapter
                                            long arg3//The row id of the item that was clicked
                    ) {
                        HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                        Log.v(EventBuddiesFragment.tag, "text: " + item.get(ItemText));

                        Intent i = new Intent(context, UserProfileActivity.class);
                        //todo: pass parameter for user id
                        startActivity(i);
                    }
                }
        );

        SimpleAdapter saLikedBuddies = new SimpleAdapter(getActivity(),
                likedBuddies,
                R.layout.eventbuddiesgrid_entry,
                new String[] {ItemImage,ItemText},
                new int[] {R.id.image,R.id.name});
        GridView gridviewLB = (GridView) rootView.findViewById(R.id.gridLikers);
        gridviewLB.setAdapter(saLikedBuddies);
        gridviewLB.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                            View arg1,//The view within the AdapterView that was clicked
                                            int arg2,//The position of the view in the adapter
                                            long arg3//The row id of the item that was clicked
                    ) {
                        HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                        Log.v(EventBuddiesFragment.tag, "text: " + item.get(ItemText));

                        Intent i = new Intent(context, UserProfileActivity.class);
                        //todo: pass parameter for user id
                        startActivity(i);
                    }
                }
        );

//        addDummyBuddyLikers();
//        addDummyBuddyParticipants();

        return rootView;
    }

    private void loadBuddies() {

//        Log.v(tag, "add dummy buddies for testing");
//        for(int i=0;i<10;i++) {
//            HashMap<String, Object> item = new HashMap<>();
//            item.put(ItemImage, R.drawable.user1);
//            item.put(ItemText, "NO." + String.valueOf(i));
//            items.add(item);
//        }

        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Log.v(tag, "add joined friends");
//        for(User user : event.getJoinedFriends()){
////            HashMap<String, Object> item = new HashMap<>();
////            //todo: load user's image from url
////            item.put(ItemImage, R.drawable.user1);  //use user1's image for now
////            item.put(ItemText, user.getUser_name());
//            HashMap<String, Object> item = getItemForShow(params,backgroundResource,user);
//            joinedBuddies.add(item);
//            Log.v(tag, "added buddy: " + user.getUser_name());
//        }
//        Log.v(tag, "add joined group members");
//        for(User user : event.getJoinedGroupMemebers()){
////            HashMap<String, Object> item = new HashMap<>();
////            //todo: load user's image from url
////            item.put(ItemImage, R.drawable.user1);  //use user1's image for now
////            item.put(ItemText, user.getUser_name());
//
//            HashMap<String, Object> item = getItemForShow(params,backgroundResource,user);
//            joinedBuddies.add(item);
//            Log.v(tag, "added buddy: " + user.getUser_name());
//        }
        for(User user : event.getJoinedBuddies()){
            HashMap<String, Object> item = getItemForShow(params,backgroundResource,user);
            joinedBuddies.add(item);
            Log.v(tag, "added buddy: " + user.getUser_id() + ", " + user.getUser_name());
        }


        Log.v(tag, "add liked friends");
//        for(User user : event.getLikedFriends()){
////            HashMap<String, Object> item = new HashMap<>();
////            //todo: load user's image from url
////            item.put(ItemImage, R.drawable.user1);  //use user1's image for now
////            item.put(ItemText, user.getUser_name());
//            HashMap<String, Object> item = getItemForShow(params,backgroundResource,user);
//            likedBuddies.add(item);
//            Log.v(tag, "added buddy: " + user.getUser_name());
//        }
//        Log.v(tag, "add liked group members");
//        for(User user : event.getLikedGroupMembers()){
////            HashMap<String, Object> item = new HashMap<>();
////            //todo: load user's image from url
////            item.put(ItemImage, R.drawable.user1);  //use user1's image for now
////            item.put(ItemText, user.getUser_name());
//            HashMap<String, Object> item = getItemForShow(params,backgroundResource,user);
//            likedBuddies.add(item);
//            Log.v(tag, "added buddy: " + user.getUser_name());
//        }
        for(User user : event.getLikedBuddies()){
            HashMap<String, Object> item = getItemForShow(params,backgroundResource,user);
            likedBuddies.add(item);
            Log.v(tag, "added buddy: " + user.getUser_id() + ", " + user.getUser_name());
        }

    }
    private HashMap<String,Object> getItemForShow(LinearLayout.LayoutParams params, int backgroundResource,User user){
        Log.v(tag, "show buddy item: " + user.getUser_name() + ", " + UrlUtil.getUserIconUrl(user.getUser_id()));

        HashMap<String, Object> item = new HashMap<>();
        ImageButton image = new ImageButton(context);
        image.setLayoutParams(params);
        image.setBackgroundResource(backgroundResource);
        image.setPadding(10, 0, 10, 0);

        IconUrlUtil.setImageForButtonLarge(context.getResources(), image, UrlUtil.getUserIconUrl(user.getUser_id()));
        Drawable d = image.getDrawable();
        Log.v(tag, "image drawable: " + d);

        item.put(ItemImage, d);
        item.put(ItemText, user.getUser_name());
        return item;
    }

//    public void addDummyBuddyParticipants(){
//        Log.v(tag, "dynamically add a few ui elements for testing");
//
//        LinearLayout list1 = (LinearLayout) rootView.findViewById(R.id.participantlist1);
//
//        int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};
//        TypedArray ta = getActivity().obtainStyledAttributes(attrs);
//        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
//        ta.recycle();
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.weight = 1.0f;
//        params.gravity = Gravity.CENTER_HORIZONTAL;
//
//        LinearLayout layout1 = new LinearLayout(getActivity());
//        layout1.setLayoutParams(params);
//        layout1.setOrientation(LinearLayout.VERTICAL);
//        ImageButton user1 = new ImageButton(getActivity());
//        Drawable image1 = getResources().getDrawable(R.drawable.eventbuddies_person4);
//        user1.setImageDrawable(image1);
//        user1.setLayoutParams(params);
//        user1.setBackground(drawableFromTheme);
//        TextView name1 = new TextView(getActivity());
//        name1.setText("name1");
//        name1.setLayoutParams(params);
//        layout1.addView(user1);
//        layout1.addView(name1);
//        list1.addView(layout1);
//
//        LinearLayout layout2 = new LinearLayout(getActivity());
//        layout2.setLayoutParams(params);
//        layout2.setOrientation(LinearLayout.VERTICAL);
//        ImageButton user2 = new ImageButton(getActivity());
//        Drawable image2 = getResources().getDrawable(R.drawable.eventbuddies_person4);
//        user2.setImageDrawable(image2);
//        user2.setLayoutParams(params);
//        user2.setBackground(drawableFromTheme);
//        TextView name2 = new TextView(getActivity());
//        name2.setText("name2");
//        name2.setLayoutParams(params);
//        layout2.addView(user2);
//        layout2.addView(name2);
//        list1.addView(layout2);
//
//        LinearLayout layout3 = new LinearLayout(getActivity());
//        layout3.setLayoutParams(params);
//        layout3.setOrientation(LinearLayout.VERTICAL);
//        ImageButton user3 = new ImageButton(getActivity());
//        Drawable image3 = getResources().getDrawable(R.drawable.eventbuddies_person4);
//        user3.setImageDrawable(image3);
//        user3.setLayoutParams(params);
//        user3.setBackground(drawableFromTheme);
//        TextView name3 = new TextView(getActivity());
//        name3.setText("name3");
//        name3.setLayoutParams(params);
//        layout3.addView(user3);
//        layout3.addView(name3);
//        list1.addView(layout3);
//
//    }
//
//    public void addDummyBuddyLikers(){
//        Log.v(tag, "dynamically add a few ui elements for testing");
//
//        LinearLayout likerList1 = (LinearLayout) rootView.findViewById(R.id.likerlist1);
//
//        int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};
//        TypedArray ta = getActivity().obtainStyledAttributes(attrs);
//        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
//        ta.recycle();
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.weight = 1.0f;
//        params.gravity = Gravity.CENTER_HORIZONTAL;
//
//        LinearLayout layout1 = new LinearLayout(getActivity());
//        layout1.setLayoutParams(params);
//        layout1.setOrientation(LinearLayout.VERTICAL);
//        ImageButton user1 = new ImageButton(getActivity());
//        Drawable image1 = getResources().getDrawable(R.drawable.eventbuddies_person4);
//        user1.setImageDrawable(image1);
//        user1.setLayoutParams(params);
//        user1.setBackground(drawableFromTheme);
//        TextView name1 = new TextView(getActivity());
//        name1.setText("name1");
//        name1.setLayoutParams(params);
//        layout1.addView(user1);
//        layout1.addView(name1);
//        likerList1.addView(layout1);
//
//        LinearLayout layout2 = new LinearLayout(getActivity());
//        layout2.setLayoutParams(params);
//        layout2.setOrientation(LinearLayout.VERTICAL);
//        ImageButton user2 = new ImageButton(getActivity());
//        Drawable image2 = getResources().getDrawable(R.drawable.eventbuddies_person4);
//        user2.setImageDrawable(image2);
//        user2.setLayoutParams(params);
//        user2.setBackground(drawableFromTheme);
//        TextView name2 = new TextView(getActivity());
//        name2.setText("name2");
//        name2.setLayoutParams(params);
//        layout2.addView(user2);
//        layout2.addView(name2);
//        likerList1.addView(layout2);
//
//        LinearLayout layout3 = new LinearLayout(getActivity());
//        layout3.setLayoutParams(params);
//        layout3.setOrientation(LinearLayout.VERTICAL);
//        ImageButton user3 = new ImageButton(getActivity());
//        Drawable image3 = getResources().getDrawable(R.drawable.eventbuddies_person4);
//        user3.setImageDrawable(image3);
//        user3.setLayoutParams(params);
//        user3.setBackground(drawableFromTheme);
//        TextView name3 = new TextView(getActivity());
//        name3.setText("name3");
//        name3.setLayoutParams(params);
//        layout3.addView(user3);
//        layout3.addView(name3);
//        likerList1.addView(layout3);
//
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case com.soco.SoCoClient.control.config.DataConfigV1.INTENT_SHOW_EVENT_DETAIL: {
//                Log.i(tag, "return from event details");
//                Log.i(tag, "Current email and password: " + loginEmail + ", " + loginPassword);
//                activities = dbmgrSoco.loadActivitiessByActiveness(DataConfigV1.VALUE_ACTIVITY_ACTIVE);
//                activities = dbmgrSoco.loadActiveActivitiesByPath(socoApp.currentPath);
//                refreshList();
//                events = dataLoader.loadEvents();
//                show(events);
//                break;
//            }
//        }
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_fragment_event_buddies, menu);
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
//        if (id == R.id.archive) {
//            showCompletedProjects();
//        }
//        else if (id == R.id.add) {
//            createActivity(null);
//        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
//        Log.i(tag, "onResume start, reload active projects");
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

//class  ItemClickListener implements AdapterView.OnItemClickListener {
//    public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
//                            View arg1,//The view within the AdapterView that was clicked
//                            int arg2,//The position of the view in the adapter
//                            long arg3//The row id of the item that was clicked
//    ) {
//        HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
//        Log.v(EventBuddiesFragment.tag, "text: " + item.get("ItemText"));
//    }
//}
