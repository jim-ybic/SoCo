package com.soco.SoCoClient.buddies.suggested.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;


public final class BuddyCardStackAdapter extends BaseBuddyCardStackAdapter {

	static String tag = "BuddyCardStackAdapter";

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
		return convertView;
	}
	private void updateUserInfoToView(User user){
		((TextView) mConvertView.findViewById(R.id.textUserName)).setText(user.getUser_name());
		((TextView) mConvertView.findViewById(R.id.textLocation)).setText(user.getLocation());
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

		LinearLayout categoryList = (LinearLayout) mConvertView.findViewById(R.id.interests);

		for(int i=0; i<interests.size(); i++){
			String cat = interests.get(i);
			TextView view = new TextView(mContext);
			view.setText(cat);
			view.setBackgroundResource(R.drawable.eventcategory_box);
			view.setPadding(10, 5, 10, 5);
//			view.setTextColor(Color.GRAY);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 5, 10, 5);
			view.setLayoutParams(params);

			categoryList.addView(view);
		}
	}
}
