package com.soco.SoCoClient.buddies.model.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.buddies.model.ui.BuddyCardModel;
import com.soco.SoCoClient.common.ui.card.view.BaseCardStackAdapter;

import java.util.ArrayList;
import java.util.Collection;

//import com.andtinder.R;
//import com.andtinder.model.CardModel;

public abstract class BaseBuddyCardStackAdapter extends BaseCardStackAdapter {
	private final Context mContext;

	/**
	 * Lock used to modify the content of {@link #mData}. Any write operation
	 * performed on the deque should be synchronized on this lock.
	 */
	private final Object mLock = new Object();
	private ArrayList<BuddyCardModel> mData;

	public BaseBuddyCardStackAdapter(Context context) {
		mContext = context;
		mData = new ArrayList<BuddyCardModel>();
	}

	public BaseBuddyCardStackAdapter(Context context, Collection<? extends BuddyCardModel> items) {
		mContext = context;
		mData = new ArrayList<BuddyCardModel>(items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FrameLayout wrapper = (FrameLayout) convertView;
		FrameLayout innerWrapper;
		View cardView;
		View convertedCardView;
		if (wrapper == null) {
			wrapper = new FrameLayout(mContext);
			wrapper.setBackgroundResource(R.drawable.card_bg);
			if (shouldFillCardBackground()) {
				innerWrapper = new FrameLayout(mContext);
				innerWrapper.setBackgroundColor(mContext.getResources().getColor(R.color.card_bg));
				wrapper.addView(innerWrapper);
			} else {
				innerWrapper = wrapper;
			}
			cardView = getCardView(position, getCardModel(position), null, parent);
			innerWrapper.addView(cardView);
		} else {
			if (shouldFillCardBackground()) {
				innerWrapper = (FrameLayout) wrapper.getChildAt(0);
			} else {
				innerWrapper = wrapper;
			}
			cardView = innerWrapper.getChildAt(0);
			convertedCardView = getCardView(position, getCardModel(position), cardView, parent);
			if (convertedCardView != cardView) {
				wrapper.removeView(cardView);
				wrapper.addView(convertedCardView);
			}
		}

		return wrapper;
	}

	protected abstract View getCardView(int position, BuddyCardModel model, View convertView, ViewGroup parent);

	public boolean shouldFillCardBackground() {
		return true;
	}

	public void add(BuddyCardModel item) {
		synchronized (mLock) {
			mData.add(item);
		}
		notifyDataSetChanged();
	}

	public BuddyCardModel pop() {
		BuddyCardModel model;
		synchronized (mLock) {
			model = mData.remove(mData.size() - 1);
		}
		notifyDataSetChanged();
		return model;
	}

	@Override
	public Object getItem(int position) {
		return getCardModel(position);
	}

	public BuddyCardModel getCardModel(int position) {
		synchronized (mLock) {
			return mData.get(mData.size() - 1 - position);
		}
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	public Context getContext() {
		return mContext;
	}
}
