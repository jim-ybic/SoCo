package com.soco.SoCoClient.v2.view.chats;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.obsolete.v1.control.config.GeneralConfig;
import com.soco.SoCoClient.v2.view.sectionlist.EntryItem;
import com.soco.SoCoClient.v2.view.sectionlist.Item;
import com.soco.SoCoClient.v2.view.sectionlist.SectionItem;

import java.util.ArrayList;

public class ContactListAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;

    String tag = "ContactListAdapter";

	public ContactListAdapter(Context context, ArrayList<Item> items) {
		super(context,0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(tag, "getView: " + position);
		View v = convertView;

		final Item i = items.get(position);
		if (i != null) {
            Log.v(tag, "item type: " + i.getType() + ", " + i.toString());

			if(i.getType().equals(GeneralConfig.LIST_ITEM_TYPE_SECTION)){
				SectionItem si = (SectionItem)i;
                Log.v(tag, "item title: " + si.getTitle());
				v = vi.inflate(R.layout.contact_list_section, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
				sectionView.setText(si.getTitle());
			}
			if(i.getType().equals(GeneralConfig.LIST_ITEM_TYPE_ENTRY)){
				EntryItem ei = (EntryItem)i;
                Log.v(tag, "item title: " + ei.title);
				v = vi.inflate(R.layout.contact_list_entry, null);
				final TextView title = (TextView)v.findViewById(R.id.title);
				final TextView subtitle = (TextView)v.findViewById(R.id.subtitle);
				
				if (title != null) 
					title.setText(ei.title);
				if(subtitle != null)
					subtitle.setText(ei.subtitle);
			}
//			if(i.getType().equals(GeneralConfig.LIST_ITEM_TYPE_FOLDER)){
//                FolderItem fi = (FolderItem)i;
//                Log.v(tag, "item title: " + fi.title);
//                v = vi.inflate(R.layout.v1_list_item_folder, null);
//                final TextView title = (TextView)v.findViewById(R.id.list_item_entry_title);
//                final TextView subtitle = (TextView)v.findViewById(R.id.list_item_entry_summary);
//
//                if (title != null)
//                    title.setText(fi.title);
//                if(subtitle != null)
//                    subtitle.setText(fi.subtitle);
//            }
		}
		return v;
	}

}
