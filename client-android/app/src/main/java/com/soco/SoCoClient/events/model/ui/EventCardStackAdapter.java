package com.soco.SoCoClient.events.model.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events.model.ui.BaseEventCardStackAdapter;
import com.soco.SoCoClient.events.model.ui.EventCardModel;

import java.util.Date;


//import com.andtinder.R;
//import com.andtinder.model.CardModel;

public final class EventCardStackAdapter extends BaseEventCardStackAdapter {

	static String tag = "EventCardStackAdapter";

	public EventCardStackAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public View getCardView(int position, EventCardModel model, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.card_event, parent, false);
			assert convertView != null;
		}

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
			((TextView) convertView.findViewById(R.id.textStartDate)).setText(getTextDate(model.getStart_date(),"dd-MMM"));
			((TextView) convertView.findViewById(R.id.textStartDayOfWeek)).setText(getDayOfStartDate(model.getStart_date()));
		}
		if(!StringUtil.isEmptyString(model.getStart_time())||StringUtil.isEmptyString(model.getEnd_time())) {
			((TextView) convertView.findViewById(R.id.textStartEndTime)).setText(getTextTime(model));
		}

		((TextView) convertView.findViewById(R.id.textNoOfComments)).setText(Integer.toString(model.getNumber_of_comments()));

		((TextView) convertView.findViewById(R.id.likeevent)).setText(Integer.toString(model.getNumber_of_likes()));

		return convertView;
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
	private String getDayOfStartDate(String dateString){
		Date d =  TimeUtil.getDate(dateString);
		if(d==null){
			return "";
		}
		return TimeUtil.getDayOfWeek(d);
	}
	private String getTextDate(String dateString,String format){
		if("yyyy-MM-dd".equalsIgnoreCase(format)){
			return dateString;
		}
		Date date = TimeUtil.getDate(dateString);
		return TimeUtil.getDateToString(date, format);
	}
}
