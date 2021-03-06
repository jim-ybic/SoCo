package com.soco.SoCoClient.events.model.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.LikeUtil;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.ui.ViewElementHelper;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;
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
		Event e = model.getEvent();
//		((ImageView) convertView.findViewById(R.id.banner)).setImageDrawable(model.getCardImageDrawable());
		((TextView) convertView.findViewById(R.id.title)).setText(e.getTitle());

		if(!StringUtil.isEmptyString(e.getAddress())){
			((TextView) convertView.findViewById(R.id.address)).setText(e.getAddress());
		}
//		if(!StringUtil.isEmptyString(model.getDescription())) {
//			((TextView) convertView.findViewById(R.id.description)).setText(model.getDescription());
//		}
//		if(!StringUtil.isEmptyString(model.getDescription())) {
//			((TextView) convertView.findViewById(R.id.description)).setText(model.getDescription());
//		}
		//date time
		if(!StringUtil.isEmptyString(e.getStart_date())) {
			((TextView) convertView.findViewById(R.id.textStartDate)).setText(TimeUtil.getTextDate(e.getStart_date(), "dd-MMM"));
			((TextView) convertView.findViewById(R.id.textStartDayOfWeek)).setText(TimeUtil.getDayOfStartDate(e.getStart_date()));
			Log.v(tag, "start day of week: " + TimeUtil.getDayOfStartDate(e.getStart_date()));
		}
		if(!StringUtil.isEmptyString(e.getStart_time())||StringUtil.isEmptyString(e.getEnd_time())) {
			((TextView) convertView.findViewById(R.id.textStartEndTime)).setText(TimeUtil.getTextStartEndTime(e));
			Log.v(tag, "start end time: " + TimeUtil.getTextStartEndTime(e));
		}

		Log.v(tag, "comment out below line as event comment function not available yet");
//		((TextView) convertView.findViewById(R.id.textNoOfComments)).setText(Integer.toString(model.getNumber_of_comments()));

		((TextView) convertView.findViewById(R.id.likeevent)).setText(Integer.toString(e.getNumber_of_likes()));
		//for now, make it as not yet liked
		LikeUtil.initialLikeButton(((Button) convertView.findViewById(R.id.likeevent)),e.isLikedEvent());

//		Log.v(tag, "set color");	//comment out, not beautiful
//		setTitleareaRandomColor(convertView);

//		Log.v(tag, "show event categories");
		if(model.getCategories() != null && !model.getCategories().isEmpty()) {
//			showCategories(model.getCategories());
			LinearLayout layout = (LinearLayout) mConvertView.findViewById(R.id.categories);
			ViewElementHelper.showCategories(e, layout, mContext);
		}

//		Log.v(tag, "show event organizers");
		showOrganizers(e);

		if(e.getLikedBuddies().size()>0 || e.getJoinedBuddies().size()>0) {
			LinearLayout layout = (LinearLayout) mConvertView.findViewById(R.id.eventbuddies);
			ViewElementHelper.showBuddies(e, layout, mContext);
		}
		else
			Log.v(tag, "no buddies for event: " + e.getTitle());

		return convertView;
	}

	void showOrganizers(Event e){
//		Event e = model.getEvent();
		Log.v(tag, "check event creator: " + e.getTitle() + ", creator: " + e.getCreator_id() + ", " + e.getCreator_name());

		ImageButton viewOrg1 = (ImageButton) mConvertView.findViewById(R.id.event_org1);
		if(e.getCreator_id() == null || e.getCreator_id().isEmpty()) {
			Log.v(tag, "no event creator info, hide the button org1");
			viewOrg1.setVisibility(View.INVISIBLE);
		}
		else{
			Log.v(tag, "show event creator: " + e.getCreator_id() + ", " + e.getCreator_name() + ", " + e.getCreator_icon_url());

			IconUrlUtil.setImageForButtonSmall(mContext.getResources(),viewOrg1,UrlUtil.getUserIconUrl(e.getCreator_id()));
//			Drawable image1 = mContext.getResources().getDrawable(R.drawable.idshk);	//testing icon
//			viewOrg1.setImageDrawable(image1);
		}

		//todo: handle enterprise info when data available

		Log.v(tag, "check event supporting groups (show at most two on event card): " + e.getSupporting_groups());
		ImageButton viewOrg2 = (ImageButton) mConvertView.findViewById(R.id.event_org2);
		ImageButton viewOrg3 = (ImageButton) mConvertView.findViewById(R.id.event_org3);
		if(e.getSupporting_groups() == null || e.getSupporting_groups().isEmpty()){
			Log.v(tag, "no event supporting group info, hide the button org2 and org3");
			viewOrg2.setVisibility(View.INVISIBLE);
			viewOrg3.setVisibility(View.INVISIBLE);
		}
		else{
			Log.v(tag, "show event supporting groups: " + e.getSupporting_groups().toString());
			//todo: download supporting group icon and show
			ArrayList<Group> groups = e.getSupporting_groups();
			int number_of_supporting_groups = groups.size();
			if(number_of_supporting_groups == 1) {	//only one group, show it
				Group g = groups.get(0);
				IconUrlUtil.setImageForButtonSmall(mContext.getResources(), viewOrg2, UrlUtil.getUserIconUrl(g.getGroup_id()));
				viewOrg3.setVisibility(View.INVISIBLE);
			}
			else if (number_of_supporting_groups > 1){	//two or more groups, show the first two
				Group g1 = groups.get(0);
				IconUrlUtil.setImageForButtonSmall(mContext.getResources(), viewOrg2, UrlUtil.getUserIconUrl(g1.getGroup_id()));
				Group g2 = groups.get(1);
				IconUrlUtil.setImageForButtonSmall(mContext.getResources(), viewOrg3, UrlUtil.getUserIconUrl(g2.getGroup_id()));
			}
		}
	}

	void setTitleareaRandomColor(View view) {
		Log.v(tag, "set title area random color: begin");
		View titleareaView = view.findViewById(R.id.bannerarea);
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

	private void addImageButtonToView(LinearLayout.LayoutParams params,int backgroundResource, User u, LinearLayout list){
		ImageButton user = new ImageButton(mContext);
//		ImageView user = new ImageView(mContext);
		user.setLayoutParams(params);
		user.setBackgroundResource(backgroundResource);
		user.setPadding(10, 2, 10, 2);
		user.setClickable(false);
		IconUrlUtil.setImageForButtonSmall(mContext.getResources(), user, UrlUtil.getUserIconUrl(u.getUser_id()));
		list.addView(user);
	}
}
