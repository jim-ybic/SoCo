package com.soco.SoCoClient.events.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;

import java.util.ArrayList;

public class EventGroupListAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;

    String tag = "EventGroupListAdapter";

	public EventGroupListAdapter(Context context, ArrayList<Item> items) {
		super(context,0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(tag, "getView: " + position + ", " + convertView + ", " + parent);
		View v = convertView;

		final Item i = items.get(position);
		if (i != null) {
            Log.v(tag, "item type: " + i.getType() + ", " + i.toString());

			if (i.getType().equals(Item.LIST_ITEM_TYPE_SECTION)){   //section
				EventGroupListSectionItem si = (EventGroupListSectionItem)i;
                Log.v(tag, "section item name: " + si.getLabel());
				v = vi.inflate(R.layout.eventgrouplist_section, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				Log.v(tag, "set view data");
				TextView label = (TextView) v.findViewById(R.id.label);
				label.setText(si.getLabel());
			}
			else if (i.getType().equals(Item.LIST_ITEM_TYPE_ENTRY)){ //entry (group)
				EventGroupListEntryItem ei = (EventGroupListEntryItem)i;
                Log.v(tag, "entry (group) item name: " + ei.getGroup_name());
				v = vi.inflate(R.layout.eventgrouplist_entry, null);

				Log.v(tag, "set view data, name: " + ei.getGroup_name());
				TextView name = (TextView) v.findViewById(R.id.name);
				name.setText(ei.getGroup_name());

				Log.v(tag, "set view data, image: " + UrlUtil.getUserIconUrl(ei.getGroup_id()));
				ImageButton image = (ImageButton) v.findViewById(R.id.image);
				IconUrlUtil.setImageForButtonNormal(context.getResources(), image, UrlUtil.getUserIconUrl(ei.getGroup_id()));

				Log.v(tag, "set tag: " + ei.getGroup_id());
				image.setTag(ei.getGroup_id());

				//todo: show other group properties
			}
			else if (i.getType().equals(Item.LIST_ITEM_TYPE_USER)){ //user
				EventGroupListUserItem item = (EventGroupListUserItem) i;
				Log.v(tag, "user item name: " + ((EventGroupListUserItem) i).getUser_name());
				v = vi.inflate(R.layout.eventgrouplist_user, null);

				Log.v(tag, "set view data, name: " + item.getUser_name());
				TextView name = (TextView) v.findViewById(R.id.name);
				name.setText(item.getUser_name());

				Log.v(tag, "set view data, image: " + UrlUtil.getUserIconUrl(item.getUser_id()));
				ImageButton image = (ImageButton) v.findViewById(R.id.image);
				IconUrlUtil.setImageForButtonNormal(context.getResources(), image, UrlUtil.getUserIconUrl(item.getUser_id()));

				Log.v(tag, "set tag: " + item.getUser_id());
				image.setTag(item.getUser_id());
				//todo: show other user properties
			}
		}
		return v;
	}

}
