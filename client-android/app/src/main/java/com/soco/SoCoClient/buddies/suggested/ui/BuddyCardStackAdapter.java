package com.soco.SoCoClient.buddies.suggested.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soco.SoCoClient.R;

//import com.andtinder.R;
//import com.andtinder.model.CardModel;

public final class BuddyCardStackAdapter extends BaseBuddyCardStackAdapter {

	static String tag = "PersonCardStackAdapter";

	public BuddyCardStackAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public View getCardView(int position, BuddyCardModel model, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
//			convertView = inflater.inflate(R.layout.card_friend_v1, parent, false);
			convertView = inflater.inflate(R.layout.card_buddy, parent, false);
			assert convertView != null;
		}

//		((ImageView) convertView.findViewById(R.id.banner)).setImageDrawable(model.getCardImageDrawable());
//		((TextView) convertView.findViewById(R.id.title)).setText(model.getTitle());
//		((TextView) convertView.findViewById(R.id.description)).setText(model.getDescription());

		return convertView;
	}


}
