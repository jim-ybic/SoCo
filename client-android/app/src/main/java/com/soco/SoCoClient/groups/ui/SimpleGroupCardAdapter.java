package com.soco.SoCoClient.groups.ui;

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
import com.soco.SoCoClient.events.ui.ViewElementHelper;
import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;
import java.util.List;

public class SimpleGroupCardAdapter
        extends RecyclerView.Adapter<SimpleGroupCardAdapter.SimpleGroupCardViewHolder>
{
    static final String tag = "SimpleGroupCardAdapter";

    private List<Group> groups;

    private Context mContext;

    public SimpleGroupCardAdapter(Context context, ArrayList<Group> groups)
    {
        this.mContext = context;
        this.groups = groups;
    }

    @Override
    public SimpleGroupCardViewHolder onCreateViewHolder( ViewGroup viewGroup, int i )
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_group_simple, viewGroup, false);
        Log.v(tag, "ViewHolder view: " + v);
        return new SimpleGroupCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder( SimpleGroupCardViewHolder simpleGroupCardViewHolder, int i )
    {
        //get data attributes and update UI elements
        Group g = groups.get(i);
        String groupId = g.getGroup_id();

        simpleGroupCardViewHolder.itemView.findViewById(R.id.title).setTag(groupId);

//        simpleGroupCardViewHolder.mTextView.setText(g.getGroup_name());
        //set basic group information
        ((TextView)simpleGroupCardViewHolder.itemView.findViewById(R.id.title)).setText(g.getGroup_name());
        ((TextView)simpleGroupCardViewHolder.itemView.findViewById(R.id.description)).setText(g.getDescription());
        ((TextView)simpleGroupCardViewHolder.itemView.findViewById(R.id.number_of_members)).setText(g.getNumberOfMembers()+" group members");
        //set detail for categories
//        if(g.getCategories()!=null && g.getCategories().size()>0) {
//            LinearLayout categoryList = (LinearLayout) simpleGroupCardViewHolder.itemView.findViewById(R.id.categories);
//            categoryList.removeAllViews();
//            showCategories(categoryList, g.getCategories());
//        }
        //set icon for group
        ImageButton groupIcon = (ImageButton)simpleGroupCardViewHolder.itemView.findViewById(R.id.group_icon);
//        Log.e(tag, "======================================================"+UrlUtil.getGroupIconUrl(g.getGroup_id()));
        IconUrlUtil.setImageForButtonSmall(mContext.getResources(), groupIcon, UrlUtil.getGroupIconUrl(g.getGroup_id()));

        //set icon for group members
        LinearLayout list = (LinearLayout) simpleGroupCardViewHolder.itemView.findViewById(R.id.groupMembers);
        list.removeAllViews();
        int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};
        TypedArray ta = mContext.obtainStyledAttributes(attrs);
        int backgroundResource = ta.getResourceId(0, 0);
        ta.recycle();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.LEFT;

        ArrayList<User> total = g.getMembers();
        for(int j=0;j<total.size()&&j<3;j++){
            User u = total.get(j);
//            addImageButtonToView(params,backgroundResource,u,list);
            ViewElementHelper.addImageButtonToView(params, backgroundResource, u, list, mContext);
        }

        if(g.getCategories().size()>0) {
            LinearLayout layout = (LinearLayout)  simpleGroupCardViewHolder.itemView.findViewById(R.id.categories);
            ViewElementHelper.showCategories(g, layout, mContext);
        }
    }

    @Override
    public int getItemCount()
    {
        return groups == null ? 0 : groups.size();
    }

    public static class SimpleGroupCardViewHolder extends RecyclerView.ViewHolder
    {
        static final String tag = "SimpleEventCardAdapter";

        public TextView mTextView;
        public ImageView mImageView;

        //todo
        //add more UI elements according to the simple event card

        public SimpleGroupCardViewHolder(View v)
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