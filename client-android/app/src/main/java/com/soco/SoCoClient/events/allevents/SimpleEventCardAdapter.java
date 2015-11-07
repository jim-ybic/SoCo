package com.soco.SoCoClient.events.allevents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events.model.Event;

import java.util.List;

public class SimpleEventCardAdapter
        extends RecyclerView.Adapter<SimpleEventCardAdapter.SimpleEventCardViewHolder>
{
    static final String tag = "SimpleEventCardAdapter";

    private List<Event> events;
    private Context mContext;

    public SimpleEventCardAdapter(Context context, List<Event> events)
    {
        this.mContext = context;
        this.events = events;
    }

    @Override
    public SimpleEventCardViewHolder onCreateViewHolder( ViewGroup viewGroup, int i )
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event_simple, viewGroup, false);
        Log.v(tag, "ViewHolder view: " + v);
        return new SimpleEventCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder( SimpleEventCardViewHolder simpleEventCardViewHolder, int i )
    {
        Event event = events.get(i);
        Log.d(tag, "bind event: " + event.toString());
        double eventId = event.getId();

        ((TextView) simpleEventCardViewHolder.itemView.findViewById(R.id.title)).setText(event.getTitle());
         simpleEventCardViewHolder.itemView.findViewById(R.id.title).setTag(eventId);
        ((TextView) simpleEventCardViewHolder.itemView.findViewById(R.id.address)).setText(event.getAddress());
         simpleEventCardViewHolder.itemView.findViewById(R.id.address).setTag(eventId);

        ((TextView) simpleEventCardViewHolder.itemView.findViewById(R.id.textNoOfComments)).setText(Integer.toString(event.getNumber_of_comments()));
        simpleEventCardViewHolder.itemView.findViewById(R.id.textNoOfComments).setTag(eventId);
        //date time
        if(!StringUtil.isEmptyString(event.getStart_date())) {
            ((TextView) simpleEventCardViewHolder.itemView.findViewById(R.id.textStartDate)).setText(TimeUtil.getTextDate(event.getStart_date(), "dd-MMM"));
            simpleEventCardViewHolder.itemView.findViewById(R.id.textStartDate).setTag(eventId);
            ((TextView) simpleEventCardViewHolder.itemView.findViewById(R.id.textStartDayOfWeek)).setText(TimeUtil.getDayOfStartDate(event.getStart_date()));
            simpleEventCardViewHolder.itemView.findViewById(R.id.textStartDayOfWeek).setTag(eventId);
        }
        if(!StringUtil.isEmptyString(event.getStart_time())||StringUtil.isEmptyString(event.getEnd_time())) {
            ((TextView) simpleEventCardViewHolder.itemView.findViewById(R.id.textStartEndTime)).setText(TimeUtil.getTextStartEndTime(event));
            simpleEventCardViewHolder.itemView.findViewById(R.id.textStartEndTime).setTag(eventId);
        }

        //todo
        //get data attributes and update UI elements
//        viewHolder.mTextView.setText(e.name);
//        viewHolder.mImageView.setImageDrawable(mContext.getDrawable(e.getImageResourceId(mContext)));
    }

    @Override
    public int getItemCount()
    {
        return events == null ? 0 : events.size();
    }

    public static class SimpleEventCardViewHolder extends RecyclerView.ViewHolder
    {
        static final String tag = "SimpleEventCardAdapter";

        public TextView mTextView;
        public ImageView mImageView;

        //todo
        //add more UI elements according to the simple event card

        public SimpleEventCardViewHolder(View v)
        {
            super(v);

            //todo
            //connecting UI elements with class members

//            mTextView = (TextView) v.findViewById(R.id.name);
//            mImageView = (ImageView) v.findViewById(R.id.pic);

            Log.d(tag, "mTextView: " + mTextView + ", mImageView: " + mImageView);
        }
    }
}