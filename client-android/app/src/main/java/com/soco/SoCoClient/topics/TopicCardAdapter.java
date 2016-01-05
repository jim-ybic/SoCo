package com.soco.SoCoClient.topics;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.ui.ViewElementHelper;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TopicCardAdapter
        extends RecyclerView.Adapter<TopicCardAdapter.TopicCardViewHolder>
{
    static final String tag = "TopicCardAdapter";

    private ArrayList<Topic> topics = new ArrayList();
    private Context mContext;

    public TopicCardAdapter(Context context, ArrayList<Topic> topics)
    {
        this.mContext = context;
        this.topics = topics;
    }

    @Override
    public TopicCardViewHolder onCreateViewHolder( ViewGroup viewGroup, int i )
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_topic, viewGroup, false);
        Log.v(tag, "ViewHolder view: " + v);
        return new TopicCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicCardViewHolder holder, int i ) {
        Topic p = topics.get(i);

        holder.title.setText(p.getName());
        holder.numberPosts.setText(String.valueOf(p.getNumberPosts()));
        holder.numberEvents.setText(String.valueOf(p.getNumberEvents()));

        //todo: show other data on ui/viewholder
    }

    @Override
    public int getItemCount()
    {
        return topics.size();
    }

    public static class TopicCardViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView numberPosts;
        public TextView numberEvents;

        public TopicCardViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            numberPosts = (TextView) v.findViewById(R.id.number_posts);
            numberEvents = (TextView) v.findViewById(R.id.number_events);
        }
    }
}