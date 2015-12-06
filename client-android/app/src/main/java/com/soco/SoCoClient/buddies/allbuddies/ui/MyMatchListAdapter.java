package com.soco.SoCoClient.buddies.allbuddies.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;

import java.util.ArrayList;

public class MyMatchListAdapter extends RecyclerView.Adapter<MyMatchListAdapter.UserViewHolder> {

    private Context context;
    private ArrayList<MyMatchListEntryItem> items;
    private LayoutInflater vi;

    String tag = "MyMatchListAdapter";

    public MyMatchListAdapter(Context context, ArrayList<MyMatchListEntryItem> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mymatch_listentry, viewGroup, false);
        Log.v(tag, "ViewHolder view: " + v);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder simpleEventCardViewHolder, int i) {

        MyMatchListEntryItem ei = items.get(i);
        ImageView ib = (ImageView) simpleEventCardViewHolder.itemView.findViewById(R.id.user_icon);
        IconUrlUtil.setImageForButtonSmall(context.getResources(), ib, UrlUtil.getUserIconUrl(ei.getUser_id()));
        ((TextView) simpleEventCardViewHolder.itemView.findViewById(R.id.name)).setText(ei.getUser_name());
        ((TextView) simpleEventCardViewHolder.itemView.findViewById(R.id.comment)).setText(ei.getSuggest_reason());
        simpleEventCardViewHolder.itemView.setTag(ei.getUser_id());
        simpleEventCardViewHolder.itemView.findViewById(R.id.addFriend).setTag(ei.getUser_id());
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        static final String tag = "SimpleEventCardAdapter";

        public TextView mTextView;
        public ImageView mImageView;

        public UserViewHolder(View v) {
            super(v);
            Log.d(tag, "mTextView: " + mTextView + ", mImageView: " + mImageView);
        }
    }
}
