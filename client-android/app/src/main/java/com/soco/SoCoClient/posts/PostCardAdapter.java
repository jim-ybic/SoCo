package com.soco.SoCoClient.posts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.topics.Topic;

import java.util.ArrayList;

public class PostCardAdapter
        extends RecyclerView.Adapter<PostCardAdapter.PostCardViewHolder>
{
    static final String tag = "PostCardAdapter";

    private ArrayList<Post> posts = new ArrayList();
    private Context mContext;

    public PostCardAdapter(Context context, ArrayList<Post> posts)
    {
        this.mContext = context;
        this.posts = posts;
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

        holder.username.setText(p.getUsername());
        holder.comment.setText(String.valueOf(p.getComment()));

        //todo: show other data on ui/viewholder
    }

    @Override
    public int getItemCount()
    {
        return posts.size();
    }

    public static class PostCardViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView comment;

        public PostCardViewHolder(View v) {
            super(v);
            username = (TextView) v.findViewById(R.id.username);
            comment = (TextView) v.findViewById(R.id.comment);
        }
    }
}