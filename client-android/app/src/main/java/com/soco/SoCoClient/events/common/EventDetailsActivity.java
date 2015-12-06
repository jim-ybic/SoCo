package com.soco.SoCoClient.events.common;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events._ref.EventBuddiesActivity;
import com.soco.SoCoClient.events._ref.EventOrganizersActivity;
import com.soco.SoCoClient.events.comments.EventCommentsActivity;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.photos.EventPhotosActivity;
import com.soco.SoCoClient.events.service.EventDetailsTask;
import com.soco.SoCoClient.groups.GroupDetailsActivity;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;
import java.util.HashSet;


public class EventDetailsActivity extends ActionBarActivity implements TaskCallBack {

    static final String tag = "EventDetailsActivity";
    private long Current_Event_Id = 0;
    private SocoApp socoApp;
    public static final String EVENT_ID = "EVENT_ID";
    private Event event;
    static final int MAX_NUMBER_BUDDIES_SHOW_ON_CARD = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent i = getIntent();
        getSupportActionBar().hide();
        socoApp = (SocoApp) getApplicationContext();
//        Event event;
        Current_Event_Id = i.getLongExtra(EVENT_ID, 0);
        EventDetailsTask edt = new EventDetailsTask(SocoApp.user_id,SocoApp.token,this);
        edt.execute(Long.toString(Current_Event_Id));
        showDetails(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == R.id.close) {
            Log.d(tag, "tap menu item: close");
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void close(View view){
        finish();
    }
    public void eventphotos (View view){
        Log.d(tag, "show all event photos");
        Intent i = new Intent(getApplicationContext(), EventPhotosActivity.class);
        startActivity(i);
    }

    public void eventcomments (View view){
        Log.d(tag, "show all event comments");
        Intent i = new Intent(getApplicationContext(), EventCommentsActivity.class);
        startActivity(i);
    }

    public void eventorganizers (View view){
        Log.d(tag, "show all event organizers");
        Intent i = new Intent(getApplicationContext(), EventOrganizersActivity.class);
        startActivity(i);

    }

    public void eventfriends (View view){
        Log.d(tag, "show all event friends");
        Intent i = new Intent(getApplicationContext(), EventBuddiesActivity.class);
        startActivity(i);

    }
    public void joinevent(View view){
        Log.v(tag, "tap join event");
        Intent i = new Intent(getApplicationContext(), JoinEventActivity.class);
        i.putExtra(Event.EVENT_ID,Long.toString(Current_Event_Id));
        startActivity(i);
    }
    public void groupdetails (View view){
        Log.v(tag, "tap on group details");
        Intent i = new Intent(this, GroupDetailsActivity.class);

        //todo: pass group id as parameters

        startActivity(i);
    }

    private void showDetails(Event event){
        if(event==null){
            return;
        }
        Log.v(tag, "show event details: " + event.toString());

//        ((TextView)this.findViewById(R.id.textNoOfViews)).setText(Integer.toString(event.getNumber_of_views()));
//        ((TextView)this.findViewById(R.id.textNoOfLikes)).setText(Integer.toString(event.getNumber_of_likes()));
//        ((TextView)this.findViewById(R.id.textNoOfComments)).setText(Integer.toString(event.getNumber_of_comments()));

        Log.v(tag, "set address: " + event.getAddress());
        ((TextView)this.findViewById(R.id.address)).setText(event.getAddress());

        Log.v(tag, "set intro: " + event.getIntroduction());
        ((TextView)this.findViewById(R.id.textIntroduction)).setText(event.getIntroduction());

        Log.v(tag, "set title: " + event.getTitle());
        ((TextView)this.findViewById(R.id.textTitle)).setText(event.getTitle());

        Log.v(tag, "set datetime");
        if(!StringUtil.isEmptyString(event.getStart_date())) {
            ((TextView) this.findViewById(R.id.textStartDate)).setText(TimeUtil.getTextDate(event.getStart_date(), "dd-MMM"));
            ((TextView) this.findViewById(R.id.textStartDayOfWeek)).setText(TimeUtil.getDayOfStartDate(event.getStart_date()));
        }
        if(!StringUtil.isEmptyString(event.getStart_time())||StringUtil.isEmptyString(event.getEnd_time())) {
            ((TextView) this.findViewById(R.id.textStartEndTime)).setText(TimeUtil.getTextStartEndTime(event));
        }

        if(event.getCategories() != null && !event.getCategories().isEmpty())
            showCategories(event.getCategories());

        showBuddies(event);

        if(!StringUtil.isEmptyString(event.getCreator_id())) {
            ImageButton ib = (ImageButton) this.findViewById(R.id.creator_icon);
            IconUrlUtil.setImageForButtonNormal(getResources(), ib, UrlUtil.getUserIconUrl(event.getCreator_id()));
            ib.setTag(event.getCreator_id());
        }
        if(!StringUtil.isEmptyString(event.getCreator_name())){
            ((TextView) this.findViewById(R.id.creator_name)).setText(event.getCreator_name());
            this.findViewById(R.id.creator_name).setTag(event.getCreator_id());
        }
    }
    void showCategories(ArrayList<String> categories){
        Log.v(tag, "show event categories: " + categories);

        LinearLayout categoryList = (LinearLayout) findViewById(R.id.categories);

        for(int i=0; i<categories.size(); i++){
            String cat = categories.get(i);
            TextView view = new TextView(this);
            view.setText(cat);
            view.setBackgroundResource(R.drawable.eventcategory_box);
            view.setPadding(10, 5, 10, 5);
            view.setTextColor(Color.BLACK);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 10, 5);
            view.setLayoutParams(params);

            categoryList.addView(view);
        }

    }
    void showBuddies(Event e){
        Log.v(tag, "check event buddies: " + e.getTitle());

        LinearLayout list = (LinearLayout) findViewById(R.id.eventbuddies);

        int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};
        TypedArray ta = obtainStyledAttributes(attrs);
        int backgroundResource = ta.getResourceId(0, 0);
//        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
        ta.recycle();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		params.weight = 1.0f;
        params.gravity = Gravity.LEFT;

        int countBuddy = 0;
        HashSet<String> buddyIds = new HashSet<>();

//		for(User u : e.getJoinedFriends()){
//			addImageButtonToView(params,backgroundResource,u,list);
//			Log.v(tag, "added joined friend into view: " + u.toString());
//			if(++countBuddy>=MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
//				break;
//		}
//
//		if(countBuddy<MAX_NUMBER_BUDDIES_SHOW_ON_CARD) {
//			for (User u : e.getJoinedGroupMemebers()) {
//				addImageButtonToView(params, backgroundResource, u, list);
//				Log.v(tag, "added joined group members into view: " + u.toString());
//				if(++countBuddy>=MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
//					break;
//			}
//		}

        if(countBuddy<MAX_NUMBER_BUDDIES_SHOW_ON_CARD) {
            for (User u : e.getJoinedBuddies()) {
                if(!buddyIds.contains(u.getUser_id())) {
                    buddyIds.add(u.getUser_id());
                    addImageButtonToView(params, backgroundResource, u, list);
                    Log.v(tag, "added joined buddies into view: " + u.toString());
                    if (++countBuddy >= MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
                        break;
                }
                else
                    Log.v(tag, "user already added on card: " + u.toString());
            }
        }

//		if(countBuddy<MAX_NUMBER_BUDDIES_SHOW_ON_CARD) {
//			for (User u : e.getLikedFriends()) {
//				addImageButtonToView(params, backgroundResource, u, list);
//				Log.v(tag, "added liked friend into view: " + u.toString());
//				if (++countBuddy>=MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
//					break;
//			}
//		}
//
//		if(countBuddy<MAX_NUMBER_BUDDIES_SHOW_ON_CARD) {
//			for (User u : e.getLikedGroupMembers()) {
//				addImageButtonToView(params, backgroundResource, u, list);
//				Log.v(tag, "added liked group members into view: " + u.toString());
//				if (++countBuddy>=MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
//					break;
//			}
//		}

        if(countBuddy<MAX_NUMBER_BUDDIES_SHOW_ON_CARD) {
            for (User u : e.getLikedBuddies()) {
                if(!buddyIds.contains(u.getUser_id())) {
                    buddyIds.add(u.getUser_id());
                    addImageButtonToView(params, backgroundResource, u, list);
                    Log.v(tag, "added liked buddy into view: " + u.toString());
                    if (++countBuddy >= MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
                        break;
                }
                else
                    Log.v(tag, "user already added on card: " + u.toString());
            }
        }
    }
    private void addImageButtonToView(LinearLayout.LayoutParams params,int backgroundResource, User u, LinearLayout list){
        ImageButton user = new ImageButton(this);
//		ImageView user = new ImageView(mContext);
        user.setLayoutParams(params);
        user.setBackgroundResource(backgroundResource);
        user.setPadding(10, 2, 10, 2);
        user.setClickable(false);
        IconUrlUtil.setImageForButtonSmall(getResources(), user, UrlUtil.getUserIconUrl(u.getUser_id()));
        list.addView(user);
    }
    public void doneTask(Object o){
        if(o==null){
            return;
        }
        event = (Event) o;
        showDetails(event);
    }
}
