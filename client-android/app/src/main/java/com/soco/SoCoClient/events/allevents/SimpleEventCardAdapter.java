package com.soco.SoCoClient.events.allevents;

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
import com.soco.SoCoClient.events.ui.EventViewHelper;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleEventCardAdapter
        extends RecyclerView.Adapter<SimpleEventCardAdapter.SimpleEventCardViewHolder>
{
    static final String tag = "SimpleEventCardAdapter";

    static final int MAX_NUMBER_BUDDIES_SHOW_ON_CARD = 3;
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
        Long eventId = event.getId();

        Log.v(tag, "set title: " + event.getTitle() + ", set tag as eventid: " + eventId);
        ((TextView) simpleEventCardViewHolder.itemView.findViewById(R.id.title)).setText(event.getTitle());
         simpleEventCardViewHolder.itemView.findViewById(R.id.title).setTag(eventId);
        ((TextView) simpleEventCardViewHolder.itemView.findViewById(R.id.address)).setText(event.getAddress());
         simpleEventCardViewHolder.itemView.findViewById(R.id.address).setTag(eventId);

        Log.v(tag, "comment out below line due to the function (event comment) not available in system");
//        ((TextView) simpleEventCardViewHolder.itemView.findViewById(R.id.textNoOfComments)).setText(Integer.toString(event.getNumber_of_comments()));
//        simpleEventCardViewHolder.itemView.findViewById(R.id.textNoOfComments).setTag(eventId);

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

//        Log.v(tag, "set randome titlearea color");  //comment out due to not beautiful
//        setTitleareaRandomColor(simpleEventCardViewHolder.itemView);

        //todo
        //get data attributes and update UI elements
//        viewHolder.mTextView.setText(e.name);
//        viewHolder.mImageView.setImageDrawable(mContext.getDrawable(e.getImageResourceId(mContext)));

        if(event.getCategories()!=null && event.getCategories().size()>0) {
            LinearLayout layout = (LinearLayout) simpleEventCardViewHolder.itemView.findViewById(R.id.categories);
//            categoryList.removeAllViews();
            EventViewHelper.showCategories(event, layout, mContext);
        }

        //set icon for creator
        ImageButton ib = (ImageButton) simpleEventCardViewHolder.itemView.findViewById(R.id.creator);
        IconUrlUtil.setImageForButtonSmall(mContext.getResources(),ib, UrlUtil.getUserIconUrl(event.getCreator_id()));

        //set icon for Joiner. 3 maximum
        LinearLayout list = (LinearLayout) simpleEventCardViewHolder.itemView.findViewById(R.id.eventbuddies);
        list.removeAllViews();
        int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};
        TypedArray ta = mContext.obtainStyledAttributes(attrs);
        int backgroundResource = ta.getResourceId(0, 0);
        ta.recycle();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		params.weight = 1.0f;
        params.gravity = Gravity.LEFT;
        int countBuddy = 1;
        ArrayList<User> total = (ArrayList<User>)event.getJoinedFriends().clone();
        if(total.size()<MAX_NUMBER_BUDDIES_SHOW_ON_CARD){
            for(User u:event.getJoinedGroupMemebers()){
                total.add(u);
                if(total.size()==MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
                    break;
            }
            if(total.size()<3){
                for(User u:event.getJoinedBuddies()){
                    total.add(u);
                    if(total.size()==MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
                        break;
                }
            }
        }
        for(User u : total){
            addImageButtonToView(params,backgroundResource,u,list);
            Log.v(tag, "added joined friend into view: " + u.toString());
            if(++countBuddy>MAX_NUMBER_BUDDIES_SHOW_ON_CARD)
                break;
        }
    }
    private void addImageButtonToView(LinearLayout.LayoutParams params,int backgroundResource, User u, LinearLayout list){
        ImageButton user = new ImageButton(mContext);
        user.setLayoutParams(params);
        user.setBackgroundResource(backgroundResource);
        user.setPadding(10, 0, 10, 0);
        IconUrlUtil.setImageForButtonSmall(mContext.getResources(), user, UrlUtil.getUserIconUrl(u.getUser_id()));
        list.addView(user);
    }

    void setTitleareaRandomColor(View view) {
        Log.v(tag, "set title area random color: begin");
        View titleareaView = view.findViewById(R.id.titlearea);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        titleareaView.setBackgroundColor(color);
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