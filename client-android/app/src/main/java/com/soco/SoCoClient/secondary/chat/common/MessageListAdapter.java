package com.soco.SoCoClient.secondary.chat.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient.common.ui.Item;

import java.util.ArrayList;

public class MessageListAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;

    String tag = "MessageListAdapter";

	public MessageListAdapter(Context context, ArrayList<Item> items) {
		super(context,0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//        Log.v(tag, "getView: " + position);
		View v = convertView;

		final Item i = items.get(position);
		if (i != null) {
//            Log.v(tag, "item type: " + i.getType() + ", " + i.toString());

			if(i.getType().equals(GeneralConfigV1.LIST_ITEM_TYPE_ENTRY)){ //entry
				MessageListEntryItem ei = (MessageListEntryItem)i;
//                Log.v(tag, "item name: " + ei.content);
				v = vi.inflate(R.layout.message_list_entry, null);
				final TextView content = (TextView)v.findViewById(R.id.content);
                final TextView timestamp = (TextView)v.findViewById(R.id.timestamp);

				if (content != null)
					content.setText(ei.content);
                if (timestamp != null)
					timestamp.setText(ei.timestamp);
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
