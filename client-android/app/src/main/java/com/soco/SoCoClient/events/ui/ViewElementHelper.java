package com.soco.SoCoClient.events.ui;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.events.allevents.SimpleEventCardAdapter;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;
import java.util.HashSet;

public class ViewElementHelper {

    final static String tag = "EventViewHelper";
    static final int MAX_NUMBER_BUDDIES_SHOW_ON_CARD = 6;


    public static void showCategories(Event e, LinearLayout layout, Context context){
        ArrayList<String> categories = e.getCategories();
        Log.v(tag, "show event categories: " + categories);

        Log.v(tag, "remove all child views");
        layout.removeAllViews();

        for(int i=0; i<categories.size()&&i<2; i++){
            String cat = categories.get(i);
            TextView view = new TextView(context);
            view.setText(cat);
            view.setBackgroundResource(R.drawable.eventcategory_box);
            view.setPadding(10, 5, 10, 5);
            view.setTextColor(Color.BLACK);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 10, 5);
            view.setLayoutParams(params);

            layout.addView(view);
        }
    }

    public static void showCategories(Group g, LinearLayout layout, Context context){
        ArrayList<String> categories = g.getCategories();
        Log.v(tag, "show group categories: " + categories);

        Log.v(tag, "remove all child views");
        layout.removeAllViews();

        for(int i=0; i<categories.size()&&i<2; i++){
            String cat = categories.get(i);
            TextView view = new TextView(context);
            view.setText(cat);
            view.setBackgroundResource(R.drawable.eventcategory_box);
            view.setPadding(10, 5, 10, 5);
            view.setTextColor(Color.BLACK);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 10, 5);
            view.setLayoutParams(params);

            layout.addView(view);
        }
    }

    public static void showBuddies(Event e, LinearLayout layout, Context context){
        Log.v(tag, "check event buddies: " + e.getTitle());

        Log.v(tag, "remove all child views");
        layout.removeAllViews();

//        LinearLayout layout = (LinearLayout) findViewById(R.id.eventbuddies);

        int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};
        TypedArray ta = context.obtainStyledAttributes(attrs);
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
                    addImageButtonToView(params, backgroundResource, u, layout, context);
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
                    addImageButtonToView(params, backgroundResource, u, layout, context);
                    Log.v(tag, "added liked buddy into view: " + u.toString());
                    if (++countBuddy >= MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
                        break;
                }
                else
                    Log.v(tag, "user already added on card: " + u.toString());
            }
        }

        Log.v(tag, "added event buddies: " + countBuddy);
    }

    public static void addImageButtonToView(LinearLayout.LayoutParams params,int backgroundResource, User u, LinearLayout list, Context context){
        ImageButton imageButton = new ImageButton(context);
//		ImageView user = new ImageView(mContext);
        imageButton.setLayoutParams(params);
        imageButton.setBackgroundResource(backgroundResource);
        imageButton.setPadding(10, 2, 10, 2);
        imageButton.setClickable(false);
        IconUrlUtil.setImageForButtonSmall(context.getResources(), imageButton, UrlUtil.getUserIconUrl(u.getUser_id()));
        list.addView(imageButton);
    }

    public static void addImageViewToView(LinearLayout.LayoutParams params,int backgroundResource, User u, LinearLayout list, Context context){
        ImageView imageView = new ImageButton(context);
        imageView.setLayoutParams(params);
        imageView.setBackgroundResource(backgroundResource);
        imageView.setPadding(10, 2, 10, 2);
        imageView.setClickable(false);
        IconUrlUtil.setImageForButtonSmall(context.getResources(), imageView, UrlUtil.getUserIconUrl(u.getUser_id()));
        list.addView(imageView);
    }

}
