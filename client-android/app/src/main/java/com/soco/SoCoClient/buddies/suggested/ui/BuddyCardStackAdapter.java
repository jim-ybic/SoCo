package com.soco.SoCoClient.buddies.suggested.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.userprofile.model.User;
import com.soco.SoCoClient.userprofile.model.UserBrief;

import java.util.ArrayList;
import java.util.HashSet;


public final class BuddyCardStackAdapter extends BaseBuddyCardStackAdapter {

	static String tag = "BuddyCardStackAdapter";
	static final int MAX_NUMBER_BUDDIES_SHOW_ON_CARD = 6;

	View mConvertView;
	Context mContext;

	public BuddyCardStackAdapter(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public View getCardView(int position, BuddyCardModel model, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.card_buddy, parent, false);
			assert convertView != null;
		}
		mConvertView = convertView;
		User user = model.getUser();
		//user should hold all the information we need for populate to the view
		updateUserInfoToView(user);
		showBuddyIcons(user);
		return convertView;
	}
	private void updateUserInfoToView(User user){
		((TextView) mConvertView.findViewById(R.id.textUserName)).setText(user.getUser_name());
		if(!StringUtil.isEmptyString(user.getLocation())) {
			((TextView) mConvertView.findViewById(R.id.textLocation)).setText(user.getLocation());
		}
//		((TextView) mConvertView.findViewById(R.id.textComEvent)).setText(user.getCommon_event_name());
		((TextView) mConvertView.findViewById(R.id.textNoOfComEvent)).setText("+"+Integer.toString(user.getNumber_common_event()));
//		((TextView) mConvertView.findViewById(R.id.textComGroup)).setText(user.getCommon_group_name());
		((TextView) mConvertView.findViewById(R.id.textNoOfComGroup)).setText("+" + Integer.toString(user.getNumber_common_group()));
		if(user.getInterests()!=null&&user.getInterests().size()>0){
			showInterests(user.getInterests());
		}

	}

	void showInterests(ArrayList<String> interests){
		Log.v(tag, "show user interests: " + interests);

		LinearLayout interestsList = (LinearLayout) mConvertView.findViewById(R.id.interests);
		interestsList.removeAllViews();

		for(int i=0; i<interests.size(); i++){
			String inter = interests.get(i);
			TextView view = new TextView(mContext);
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

	void showBuddyIcons(User u){
		Log.v(tag, "show suggested buddy's icon");
		ImageButton ib = (ImageButton) mConvertView.findViewById(R.id.iconUser);
		IconUrlUtil.setImageForButtonNormal(mContext.getResources(), ib, u.getUser_icon_url());

		Log.v(tag, "show common buddy's icons");
		int countBuddy = 0;
		HashSet<String> buddyIds = new HashSet<>();

		if(u.getCommon_buddies()!=null&&u.getCommon_buddies().size()>0){
			Log.v(tag, "loading photo for common users : " + u.getCommon_buddies());
			LinearLayout commonBuddiesList = (LinearLayout) mConvertView.findViewById(R.id.layoutCommonBuddies);
			commonBuddiesList.removeAllViews();
			int[] attrs = new int[]{R.attr.selectableItemBackground};
			TypedArray typedArray = mContext.obtainStyledAttributes(attrs);
			int backgroundResource = typedArray.getResourceId(0, 0);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//			view.setBackgroundResource(backgroundResource);
			typedArray.recycle();
			for(int i=0; i<u.getCommon_buddies().size()&&i<6; i++) {
				UserBrief ub = u.getCommon_buddies().get(i);
				if(buddyIds.contains(ub.getUser_id())) {
					Log.w(tag, "user already added to card: " + ub.toString());
					continue;
				}
				else
					buddyIds.add(ub.getUser_id());

				ImageButton b = new ImageButton(mContext);
				b.setLayoutParams(params);
				b.setBackgroundResource(backgroundResource);
				b.setPadding(10, 2, 10, 2);
				b.setClickable(false);
//				params.setMargins(0, 5, 10, 5);
//				b.setScaleType(ImageView.ScaleType.FIT_XY);
				IconUrlUtil.setImageForButtonSmall(mContext.getResources(),b,ub.getUser_icon_url());
				commonBuddiesList.addView(b);

				if(++countBuddy >= MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
					break;
			}
		}
	}
}
