package com.soco.SoCoClient.events.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.soco.SoCoClient.R;

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
                Log.v(tag, "item name: " + si.getLabel());
				v = vi.inflate(R.layout.eventgrouplist_section, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				Log.v(tag, "set view data");
				TextView label = (TextView) v.findViewById(R.id.label);
				label.setText(si.getLabel());
			}
			else if (i.getType().equals(Item.LIST_ITEM_TYPE_ENTRY)){ //entry
				EventGroupListEntryItem ei = (EventGroupListEntryItem)i;
                Log.v(tag, "item name: " + ei.getGroup_name());
				v = vi.inflate(R.layout.eventgrouplist_entry, null);

				Log.v(tag, "set view data");
				TextView name = (TextView) v.findViewById(R.id.name);
				name.setText(ei.getGroup_name());
				//todo: show other group properties

			}
			else if (i.getType().equals(Item.LIST_ITEM_TYPE_USER)){ //user
				EventGroupListUserItem item = (EventGroupListUserItem) i;
				Log.v(tag, "item name: " + ((EventGroupListUserItem) i).getUser_name());
				v = vi.inflate(R.layout.eventgrouplist_user, null);

				Log.v(tag, "set view data");
				TextView name = (TextView) v.findViewById(R.id.name);
				name.setText(item.getUser_name());
				//todo: show other user properties
			}
		}
		return v;
	}

}
