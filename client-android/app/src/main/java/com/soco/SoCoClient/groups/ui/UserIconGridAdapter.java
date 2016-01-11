package com.soco.SoCoClient.groups.ui;

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
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

/**
 * Created by David_WANG on 11/20/2015.
 */
public class UserIconGridAdapter extends RecyclerView.Adapter<UserIconGridAdapter.UserIconViewHolder> {
    static final String tag = "UserIconGridAdapter";
    private Context mContext;
    public LayoutInflater inflater=null;
    private ArrayList<User> users;
    public UserIconGridAdapter(Context context, ArrayList<User> users)
    {
        this.mContext = context;
        this.users = users;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View vi=convertView;
//        if(convertView==null)
//            vi = inflater.inflate(R.layout.eventbuddiesgrid_entry, null);
//
//        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
//        TextView text = (TextView)vi.findViewById(R.id.name);
//        String name = (String) data.get(EventBuddiesFragment.ItemName);
//        text.setText(name);
//
//        ImageView image=(ImageView)vi.findViewById(R.id.image);
//        String url = (String) data.get(EventBuddiesFragment.ItemImage);
//        IconUrlUtil.setImageForButtonNormal(mContext.getResources(), image, url);
//        return vi;
//    }

    @Override
    public UserIconViewHolder onCreateViewHolder( ViewGroup viewGroup, int i )
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_entry, viewGroup, false);
        Log.v(tag, "ViewHolder view: " + v);
        return new UserIconViewHolder(v);
    }
    @Override
    public void onBindViewHolder(UserIconViewHolder userIconViewHolder, int i )
    {
        User u = users.get(i);
        ImageButton ib = (ImageButton) userIconViewHolder.itemView.findViewById(R.id.image);
        IconUrlUtil.setImageForButtonNormal(mContext.getResources(),ib,u.getUser_icon_url());
        ((TextView) userIconViewHolder.itemView.findViewById(R.id.name)).setText(u.getUser_name());
        userIconViewHolder.itemView.findViewById(R.id.image).setTag(u.getUser_id());
        userIconViewHolder.itemView.findViewById(R.id.name).setTag(u.getUser_id());
//        userIconViewHolder.itemView.findViewById(R.id.item).setTag(u.getUser_id());
    }
    @Override
    public int getItemCount()
    {
        return users == null ? 0 : users.size();
    }

    public static class UserIconViewHolder extends RecyclerView.ViewHolder
    {
        static final String tag = "SimpleEventCardAdapter";

        public TextView mTextView;
        public ImageView mImageView;

        public UserIconViewHolder(View v)
        {
            super(v);
            Log.d(tag, "mTextView: " + mTextView + ", mImageView: " + mImageView);
        }
    }
}
