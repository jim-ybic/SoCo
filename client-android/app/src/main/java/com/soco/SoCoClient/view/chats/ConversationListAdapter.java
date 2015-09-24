package com.soco.SoCoClient.view.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.config._ref.GeneralConfigV1;
import com.soco.SoCoClient.view.sectionlist.Item;

import java.util.ArrayList;

public class ConversationListAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;

    String tag = "ConversationListAdapter";

	public ConversationListAdapter(Context context, ArrayList<Item> items) {
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
				ConversationListEntryItem ei = (ConversationListEntryItem)i;
//                Log.v(tag, "item name: " + ei.content);
				v = vi.inflate(R.layout.conversation_list_entry, null);
				final TextView name = (TextView)v.findViewById(R.id.name);
                final TextView lastMsgContent = (TextView)v.findViewById(R.id.last_msg_content);
				final TextView lastMsgTimestamp = (TextView)v.findViewById(R.id.last_msg_timestamp);

				if (name != null)
					name.setText(ei.name);
				if (lastMsgContent!= null)
					lastMsgContent.setText(ei.lastMsgContent);
				if (lastMsgTimestamp!= null)
					lastMsgTimestamp.setText(ei.lastMsgTimestamp);
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
