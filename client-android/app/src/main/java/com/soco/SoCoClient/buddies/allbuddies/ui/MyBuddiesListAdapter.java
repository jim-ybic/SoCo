package com.soco.SoCoClient.buddies.allbuddies.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.util.IconUrlUtil;

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
			ImageView ib = (ImageView)v.findViewById(R.id.user_icon);
			IconUrlUtil.setImageForButtonSmall(v.getResources(), ib, UrlUtil.getUserIconUrl(i.getUser_id()));
			((TextView) v.findViewById(R.id.name)).setText(i.getUser_name());
			((TextView) v.findViewById(R.id.location)).setText(i.getLocation());
			v.setTag(i.getUser_id());
		}
		return v;
	}

}
