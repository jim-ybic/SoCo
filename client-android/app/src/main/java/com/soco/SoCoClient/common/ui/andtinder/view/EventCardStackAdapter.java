package com.soco.SoCoClient.common.ui.andtinder.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.ui.andtinder.model.CardModel;

//import com.andtinder.R;
//import com.andtinder.model.CardModel;

public final class EventCardStackAdapter extends CardStackAdapter {

	static String tag = "EventCardStackAdapter";

	public EventCardStackAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public View getCardView(int position, CardModel model, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.card_event, parent, false);
			assert convertView != null;
		}

		((ImageView) convertView.findViewById(R.id.banner)).setImageDrawable(model.getCardImageDrawable());
		((TextView) convertView.findViewById(R.id.title)).setText(model.getTitle());
//		((TextView) convertView.findViewById(R.id.description)).setText(model.getDescription());

		return convertView;
	}


}
