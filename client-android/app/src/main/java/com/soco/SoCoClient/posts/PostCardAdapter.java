package com.soco.SoCoClient.posts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.PhotoPlacer;

import java.util.ArrayList;

public class PostCardAdapter
        extends RecyclerView.Adapter<PostCardAdapter.PostCardViewHolder>
{
    static final String tag = "PostCardAdapter";

    private ArrayList<Post> posts = new ArrayList();
    private Context mContext;
    private boolean isSourceFromSingleEvent = false;

    public PostCardAdapter(Context context, ArrayList<Post> posts)
    {
        this.mContext = context;
        this.posts = posts;
    }

    public void setIsSourceFromSingleEvent(boolean isSourceFromSingleEvent) {
        this.isSourceFromSingleEvent = isSourceFromSingleEvent;
    }

    @Override
    public PostCardViewHolder onCreateViewHolder( ViewGroup viewGroup, int i )
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_post, viewGroup, false);
        Log.v(tag, "ViewHolder view: " + v);
        return new PostCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostCardViewHolder holder, int i ) {
        Post p = posts.get(i);

        Log.v(tag, "set user icon");
        IconUrlUtil.setImageForButtonSmall(
                mContext.getResources(), holder.usericon, UrlUtil.getUserIconUrl(p.getUser().getUser_id()));

        Log.v(tag, "set user name");
        holder.username.setText(p.getUser().getUser_name());

        Log.v(tag, "set post time");
        holder.time.setText(p.getTime());

        Log.v(tag, "set photo");
        if(p.getPhotos().size()>0) {
            Photo pho = p.getPhotos().get(0);   //only support single photo now
            new PhotoPlacer().showPhotoInPost2(mContext.getResources(), holder.photo, pho.getUrl());
        }
        else {
            Log.v(tag, "no photo in the post, remove the view");
            holder.photo.setVisibility(View.GONE);
//            ((ViewManager) holder.photo.getParent()).removeView(holder.photo);    //too rude
        }

        Log.v(tag, "set comment");
        holder.comment.setText(String.valueOf(p.getComment()));
    }

    @Override
    public int getItemCount()
    {
        return posts.size();
    }

    public static class PostCardViewHolder extends RecyclerView.ViewHolder {
        public ImageButton usericon;
        public TextView username;
        public ImageView photo;
        public TextView time;
        public TextView comment;

        public PostCardViewHolder(View v) {
            super(v);
            usericon = (ImageButton) v.findViewById(R.id.usericon);
            username = (TextView) v.findViewById(R.id.username);
            time = (TextView) v.findViewById(R.id.time);
            photo = (ImageView) v.findViewById(R.id.photo);
            comment = (TextView) v.findViewById(R.id.comment);
        }
    }
}