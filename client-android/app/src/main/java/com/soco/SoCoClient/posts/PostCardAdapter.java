package com.soco.SoCoClient.posts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;

import java.util.List;

public class PostCardAdapter
        extends RecyclerView.Adapter<PostCardAdapter.PostCardViewHolder>
{
    static final String tag = "PostCardAdapter";

    private List<Post> posts;
    private Context mContext;
    private boolean isSourceFromSingleEvent = false;

    public PostCardAdapter(Context context, List<Post> posts)
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
                mContext.getResources(), (ImageButton) holder.itemView.findViewById(R.id.usericon), UrlUtil.getUserIconUrl(p.getUser().getUser_id()));

        Log.v(tag, "set user name");
        ((TextView) holder.itemView.findViewById(R.id.username)).setText(p.getUser().getUser_name());

        Log.v(tag, "set post time");
        ((TextView) holder.itemView.findViewById(R.id.time)).setText(p.getTime());    //modifying time format

        Log.v(tag, "set photo");
        holder.itemView.findViewById(R.id.photo).setVisibility(View.VISIBLE);
        if(p.getPhotos()!=null&&p.getPhotos().size()>0) {
            Photo photo = p.getPhotos().get(0);
             IconUrlUtil.setImageForViewWithSize(
                     mContext.getResources(), (ImageView) holder.itemView.findViewById(R.id.photo), photo.getUrl());
        }
        else {
            Log.v(tag, "no photo in the post, hide the view");
//            holder.itemView.findViewById(R.id.photo).setVisibility(View.GONE);    //show testing photo
        }

        Log.v(tag, "set comment");
        ((TextView) holder.itemView.findViewById(R.id.comment)).setText(String.valueOf(p.getComment()));

        Log.v(tag, "set topic/event");
        if(p.getTopic() != null){
            ((TextView) holder.itemView.findViewById(R.id.topicORevent)).setText(String.valueOf(p.getTopic().getTitle()));
        }
        else if(p.getEvent() != null){
            ((TextView) holder.itemView.findViewById(R.id.topicORevent)).setText(String.valueOf(p.getEvent().getTitle()));
        }
        else {
            Log.w(tag, "no related topic/event found for this post: " + p.getComment());
            ((TextView) holder.itemView.findViewById(R.id.topicORevent)).setText("");
        }

    }

    @Override
    public int getItemCount()
    {
        return posts==null?0:posts.size();
    }

    public static class PostCardViewHolder extends RecyclerView.ViewHolder {
//        public ImageButton usericon;
//        public TextView username;
//        public ImageButton photo;
//        public TextView time;
//        public TextView comment;

        public PostCardViewHolder(View v) {
            super(v);
//            usericon = (ImageButton) v.findViewById(R.id.usericon);
//            username = (TextView) v.findViewById(R.id.username);
//            time = (TextView) v.findViewById(R.id.time);
//            photo = (ImageButton) v.findViewById(R.id.photo);
//            comment = (TextView) v.findViewById(R.id.comment);
        }
    }
}