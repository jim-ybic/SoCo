package com.soco.SoCoClient.buddies.allbuddies.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.soco.SoCoClient.R;

import java.util.ArrayList;

public class MyBuddiesListAdapter extends ArrayAdapter<MyBuddiesListEntryItem> {

	private Context context;
	private ArrayList<MyBuddiesListEntryItem> items;
	private LayoutInflater vi;

    String tag = "MyBuddiesListAdapter";

	public MyBuddiesListAdapter(Context context, ArrayList<MyBuddiesListEntryItem> items) {
		super(context,0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		final MyBuddiesListEntryItem i = items.get(position);
		if (i != null) {

			MyBuddiesListEntryItem ei = (MyBuddiesListEntryItem)i;
				v = vi.inflate(R.layout.mybuddies_listentry, null);

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
