package com.soco.SoCoClient.events.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.events.details.EventBuddiesFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David_WANG on 11/20/2015.
 */
public class BuddiesGridSimpleAdapter extends SimpleAdapter {
    private Context mContext;
    public LayoutInflater inflater=null;
    public BuddiesGridSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.eventbuddiesgrid_entry, null);

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        TextView text = (TextView)vi.findViewById(R.id.name);
        String name = (String) data.get(EventBuddiesFragment.ItemName);
        text.setText(name);

        ImageView image=(ImageView)vi.findViewById(R.id.image);
        String url = (String) data.get(EventBuddiesFragment.ItemImage);
        IconUrlUtil.setImageForButtonNormal(mContext.getResources(), image, url);
        return vi;
    }
}
