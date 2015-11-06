package com.soco.SoCoClient.buddies.allbuddies.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient._ref.ContactListEntryItem;
import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient.common.ui.Item;
import com.soco.SoCoClient.common.ui.SectionItem;

import java.util.ArrayList;

public class MyMatchListAdapter extends ArrayAdapter<MyMatchListEntryItem> {

	private Context context;
	private ArrayList<MyMatchListEntryItem> items;
	private LayoutInflater vi;

    String tag = "MyMatchListAdapter";

	public MyMatchListAdapter(Context context, ArrayList<MyMatchListEntryItem> items) {
		super(context,0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		final MyMatchListEntryItem i = items.get(position);
		if (i != null) {

				MyMatchListEntryItem ei = (MyMatchListEntryItem)i;
				v = vi.inflate(R.layout.mymatch_listentry, null);

				//todo: put data values to ui
//				final TextView name = (TextView)v.findViewById(R.id.name);
//                final TextView phone = (TextView)v.findViewById(R.id.phone);
//				final TextView email = (TextView)v.findViewById(R.id.email);
//                final TextView status = (TextView)v.findViewById(R.id.status);
//
//				if (name != null)
//					name.setText(ei.name);
//                if (phone != null)
//                    phone.setText(ei.phone);
//				if (email != null)
//					email.setText(ei.email);
//                if (status != null)
//                    status.setText(ei.status);

		}
		return v;
	}

}
