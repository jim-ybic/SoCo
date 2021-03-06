package com.soco.SoCoClient.events._ref;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient.events.ui.Item;

import java.util.ArrayList;

@Deprecated
public class EventListAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;

    String tag = "EventListAdapter";

	public EventListAdapter(Context context, ArrayList<Item> items) {
		super(context,0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		final Item i = items.get(position);
		if (i != null) {
			if(i.getType().equals(GeneralConfigV1.LIST_ITEM_TYPE_ENTRY)){ //entry
				EventListEntryItem ei = (EventListEntryItem)i;
				v = vi.inflate(R.layout.event_list_entry, null);
				final TextView name = (TextView)v.findViewById(R.id.name);
                final TextView desc = (TextView)v.findViewById(R.id.test_view);
				final TextView date = (TextView)v.findViewById(R.id.startdate);

				if (name != null)
					name.setText(ei.name);
				if (desc!= null)
					desc.setText(ei.desc);
				if (date!= null)
					date.setText(ei.date);
			}
//			if(i.getType().equals(GeneralConfigV1.LIST_ITEM_TYPE_FOLDER)){
//                FolderItem fi = (FolderItem)i;
//                Log.v(tag, "item name: " + fi.name);
//                v = vi.inflate(R.layout.v1_list_item_folder, null);
//                final TextView name = (TextView)v.findViewById(R.id.list_item_entry_title);
//                final TextView email = (TextView)v.findViewById(R.id.list_item_entry_summary);
//
//                if (name != null)
//                    name.setText(fi.name);
//                if(email != null)
//                    email.setText(fi.email);
//            }
		}
		return v;
	}

}
