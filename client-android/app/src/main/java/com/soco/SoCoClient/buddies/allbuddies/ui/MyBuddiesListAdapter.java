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

public class MyBuddiesListAdapter extends RecyclerView.Adapter<MyBuddiesListAdapter.UserViewHolder> {

    private Context context;
    private ArrayList<MyBuddiesListEntryItem> items;
    private LayoutInflater vi;

    String tag = "MyBuddiesListAdapter";

    public MyBuddiesListAdapter(Context context, ArrayList<MyBuddiesListEntryItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mybuddies_listentry, viewGroup, false);
        Log.v(tag, "ViewHolder view: " + v);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int i) {
        MyBuddiesListEntryItem ei = items.get(i);
        ImageView ib = (ImageView) userViewHolder.itemView.findViewById(R.id.user_icon);
        IconUrlUtil.setImageForButtonSmall(context.getResources(), ib, UrlUtil.getUserIconUrl(ei.getUser_id()));
        ((TextView) userViewHolder.itemView.findViewById(R.id.name)).setText(ei.getUser_name());
        ((TextView) userViewHolder.itemView.findViewById(R.id.location)).setText(ei.getLocation());
        userViewHolder.itemView.setTag(ei.getUser_id());
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
