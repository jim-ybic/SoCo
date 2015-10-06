package com.soco.SoCoClient.view.friends.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control._ref.GeneralConfigV1;
import com.soco.SoCoClient.view.common.Item;
import com.soco.SoCoClient.view.common.SectionItem;

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
//        Log.v(tag, "getView: " + position);
		View v = convertView;

		final Item i = items.get(position);
		if (i != null) {
//            Log.v(tag, "item type: " + i.getType() + ", " + i.toString());

			if(i.getType().equals(GeneralConfigV1.LIST_ITEM_TYPE_SECTION)){   //section
				SectionItem si = (SectionItem)i;
//                Log.v(tag, "item name: " + si.getTitle());
				v = vi.inflate(R.layout.contact_list_section, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
				sectionView.setText(si.getTitle());
			}
			if(i.getType().equals(GeneralConfigV1.LIST_ITEM_TYPE_ENTRY)){ //entry
				ContactListEntryItem ei = (ContactListEntryItem)i;
//                Log.v(tag, "item name: " + ei.name);
				v = vi.inflate(R.layout.contact_list_entry, null);
				final TextView name = (TextView)v.findViewById(R.id.name);
                final TextView phone = (TextView)v.findViewById(R.id.phone);
				final TextView email = (TextView)v.findViewById(R.id.email);
                final TextView status = (TextView)v.findViewById(R.id.status);

				if (name != null)
					name.setText(ei.name);
                if (phone != null)
                    phone.setText(ei.phone);
				if (email != null)
					email.setText(ei.email);
                if (status != null)
                    status.setText(ei.status);
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
