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
        Event e = events.get(i);
        Log.d(tag, "bind event: " + e.toString());

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