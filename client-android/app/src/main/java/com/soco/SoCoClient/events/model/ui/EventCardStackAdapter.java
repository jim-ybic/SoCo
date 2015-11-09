package com.soco.SoCoClient.events.model.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events.model.ui.BaseEventCardStackAdapter;
import com.soco.SoCoClient.events.model.ui.EventCardModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


//import com.andtinder.R;
//import com.andtinder.model.CardModel;

public final class EventCardStackAdapter extends BaseEventCardStackAdapter {

	static String tag = "EventCardStackAdapter";

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

		Log.v(tag, "set color");
		setTitleareaRandomColor(convertView);

		if(model.getCategories() != null && !model.getCategories().isEmpty());
		showCategories(model.getCategories());

		return convertView;
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
