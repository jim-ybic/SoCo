package com.soco.SoCoClient.events.model.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.LikeUtil;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.model.ui.BaseEventCardStackAdapter;
import com.soco.SoCoClient.events.model.ui.EventCardModel;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


//import com.andtinder.R;
//import com.andtinder.model.CardModel;

public final class EventCardStackAdapter extends BaseEventCardStackAdapter {

	static String tag = "EventCardStackAdapter";

	static final int MAX_NUMBER_BUDDIES_SHOW_ON_CARD = 6;

	View mConvertView;
	Context mContext;

	public EventCardStackAdapter(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public View getCardView(int position, EventCardModel model, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.card_event, parent, false);
			assert convertView != null;
		}
		mConvertView = convertView;

//		((ImageView) convertView.findViewById(R.id.banner)).setImageDrawable(model.getCardImageDrawable());
		((TextView) convertView.findViewById(R.id.title)).setText(model.getTitle());

		if(!StringUtil.isEmptyString(model.getAddress())){
			((TextView) convertView.findViewById(R.id.address)).setText(model.getAddress());
		}
//		if(!StringUtil.isEmptyString(model.getDescription())) {
//			((TextView) convertView.findViewById(R.id.description)).setText(model.getDescription());
//		}
//		if(!StringUtil.isEmptyString(model.getDescription())) {
//			((TextView) convertView.findViewById(R.id.description)).setText(model.getDescription());
//		}

		//todo
		//set other card content

		//date time
		if(!StringUtil.isEmptyString(model.getStart_date())) {
			((TextView) convertView.findViewById(R.id.textStartDate)).setText(TimeUtil.getTextDate(model.getStart_date(), "dd-MMM"));
			((TextView) convertView.findViewById(R.id.textStartDayOfWeek)).setText(TimeUtil.getDayOfStartDate(model.getStart_date()));
		}
		if(!StringUtil.isEmptyString(model.getStart_time())||StringUtil.isEmptyString(model.getEnd_time())) {
			((TextView) convertView.findViewById(R.id.textStartEndTime)).setText(getTextTime(model));
		}

		Log.v(tag, "comment out below line as event comment function not available yet");
//		((TextView) convertView.findViewById(R.id.textNoOfComments)).setText(Integer.toString(model.getNumber_of_comments()));

		((TextView) convertView.findViewById(R.id.likeevent)).setText(Integer.toString(model.getNumber_of_likes()));
		//todo
		//to make this driven by the json response(like status)
		//for now, make it as not yet liked
		LikeUtil.initialLikeButton(((Button) convertView.findViewById(R.id.likeevent)),false);

//		Log.v(tag, "set color");	//comment out, not beautiful
//		setTitleareaRandomColor(convertView);

//		Log.v(tag, "show event categories");
		if(model.getCategories() != null && !model.getCategories().isEmpty())
			showCategories(model.getCategories());

//		Log.v(tag, "show event organizers");
		showOrganizers(model);
		showBuddies(model);

		return convertView;
	}

	void showOrganizers(EventCardModel model){
		Event e = model.getEvent();
		Log.v(tag, "check event creator: " + e.toString());

		ImageButton viewOrg1 = (ImageButton) mConvertView.findViewById(R.id.event_org1);
		if(model.getEvent().getCreator_id() == null || model.getEvent().getCreator_id().isEmpty()) {
			Log.v(tag, "no event creator info, hide the button org1");
			viewOrg1.setVisibility(View.INVISIBLE);
		}
		else{
			Log.v(tag, "show event creator: " + model.getEvent().getCreator_id() + ", " + model.getEvent().getCreator_name() + ", " + model.getEvent().getCreator_icon_url());
			//todo: download creator icon and show it

			Drawable image1 = mContext.getResources().getDrawable(R.drawable.idshk);	//testing icon
			viewOrg1.setImageDrawable(image1);
		}

		//todo: handle enterprise info when data available

		Log.v(tag, "check event supporting groups: " + model.getEvent().getSupporting_groups());
		ImageButton viewOrg2 = (ImageButton) mConvertView.findViewById(R.id.event_org2);
		ImageButton viewOrg3 = (ImageButton) mConvertView.findViewById(R.id.event_org3);
		if(model.getEvent().getSupporting_groups() == null || model.getEvent().getSupporting_groups().isEmpty()){
			Log.v(tag, "no event creator info, hide the button org2 and org3");
			viewOrg2.setVisibility(View.INVISIBLE);
			viewOrg3.setVisibility(View.INVISIBLE);
		}
		else{
			Log.v(tag, "show event supporting groups: " + model.getEvent().getSupporting_groups().toString());
			//todo: download supporting group icon and show

			Drawable image1 = mContext.getResources().getDrawable(R.drawable.group1);	//testing icon

			int number_of_supporting_groups = model.getEvent().getSupporting_groups().size();
			if(number_of_supporting_groups == 1) {	//show only one icon for group
				viewOrg2.setImageDrawable(image1);
				viewOrg3.setVisibility(View.INVISIBLE);
			}
			else{	//show at most two icons for group
				viewOrg2.setImageDrawable(image1);
				viewOrg3.setImageDrawable(image1);
			}
		}
	}

	void showBuddies(EventCardModel model){
		Event e = model.getEvent();
		Log.v(tag, "check event buddies: " + e.toString());

		LinearLayout list = (LinearLayout) mConvertView.findViewById(R.id.eventbuddies);

	    int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};
        TypedArray ta = mContext.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
        ta.recycle();

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1.0f;
        params.gravity = Gravity.CENTER_HORIZONTAL;

		int countBuddy = 0;

		for(User u : e.getJoinedFriends()){
			ImageView user = new ImageView(mContext);
			//todo: download user icon from url
			Drawable image1 = mContext.getResources().getDrawable(R.drawable.user1);
			user.setImageDrawable(image1);
			user.setLayoutParams(params);
			user.setBackground(drawableFromTheme);
			user.setPadding(0, 0, 15, 0);
			list.addView(user);
			Log.v(tag, "added joined friend into view: " + u.toString());
			if(++countBuddy>MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
				break;
		}

		if(countBuddy<MAX_NUMBER_BUDDIES_SHOW_ON_CARD) {
			for (User u : e.getJoinedGroupMemebers()) {
				ImageView user = new ImageView(mContext);
				//todo: download user icon from url
				Drawable image1 = mContext.getResources().getDrawable(R.drawable.user2);
				user.setImageDrawable(image1);
				user.setLayoutParams(params);
				user.setBackground(drawableFromTheme);
				list.addView(user);
				Log.v(tag, "added joined group members into view: " + u.toString());
				if(++countBuddy>MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
					break;
			}
		}

		if(countBuddy<MAX_NUMBER_BUDDIES_SHOW_ON_CARD) {
			for (User u : e.getLikedFriends()) {
				ImageView user = new ImageView(mContext);
				//todo: download user icon from url
				Drawable image1 = mContext.getResources().getDrawable(R.drawable.user3);
				user.setImageDrawable(image1);
				user.setLayoutParams(params);
				user.setBackground(drawableFromTheme);
				list.addView(user);
				Log.v(tag, "added liked friend into view: " + u.toString());
				if (++countBuddy > MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
					break;
			}
		}

		if(countBuddy<MAX_NUMBER_BUDDIES_SHOW_ON_CARD) {
			for (User u : e.getLikedGroupMembers()) {
				ImageView user = new ImageView(mContext);
				//todo: download user icon from url
				Drawable image1 = mContext.getResources().getDrawable(R.drawable.user3);
				user.setImageDrawable(image1);
				user.setLayoutParams(params);
				user.setBackground(drawableFromTheme);
				list.addView(user);
				Log.v(tag, "added liked group members into view: " + u.toString());
				if (++countBuddy > MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
					break;
			}
		}
	}

	void showCategories(ArrayList<String> categories){
		Log.v(tag, "show event categories: " + categories);

		LinearLayout categoryList = (LinearLayout) mConvertView.findViewById(R.id.categories);

		for(int i=0; i<categories.size(); i++){
			String cat = categories.get(i);
			TextView view = new TextView(mContext);
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

	void setTitleareaRandomColor(View view) {
		Log.v(tag, "set title area random color: begin");
		View titleareaView = view.findViewById(R.id.titlearea);
		Random rnd = new Random();
		int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
		titleareaView.setBackgroundColor(color);
	}


	//	private String getTextDate(EventCardModel model){
//		StringBuffer sb = new StringBuffer();
//		if(!StringUtil.isEmptyString(model.getStart_date())){
//			sb.append(model.getStart_date());
//		}
////		if(!StringUtil.isEmptyString(model.getEnd_date())){
////			sb.append("~");
////			sb.append(model.getEnd_date());
////		}
//		return sb.toString();
//	}
	private String getTextTime(EventCardModel model){
		StringBuffer sb = new StringBuffer();
		if(!StringUtil.isEmptyString(model.getStart_time())){
			sb.append(model.getStart_time());
		}
		if(!StringUtil.isEmptyString(model.getEnd_time())){
			sb.append("~");
			sb.append(model.getEnd_time());
		}
		return sb.toString();
	}
}
