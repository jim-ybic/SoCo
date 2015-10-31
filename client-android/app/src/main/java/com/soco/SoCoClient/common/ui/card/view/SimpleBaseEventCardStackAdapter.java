package com.soco.SoCoClient.common.ui.card.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.andtinder.R;
//import com.andtinder.model.CardModel;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.events.model.ui.BaseEventCardStackAdapter;
import com.soco.SoCoClient.events.model.ui.EventCardModel;

@Deprecated
public final class SimpleBaseEventCardStackAdapter extends BaseEventCardStackAdapter {

	public SimpleBaseEventCardStackAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public View getCardView(int position, EventCardModel model, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.std_card_inner, parent, false);
			assert convertView != null;
		}

		((ImageView) convertView.findViewById(R.id.banner)).setImageDrawable(model.getCardImageDrawable());
		((TextView) convertView.findViewById(R.id.title)).setText(model.getTitle());
		((TextView) convertView.findViewById(R.id.description)).setText(model.getDescription());

		return convertView;
	}
}
