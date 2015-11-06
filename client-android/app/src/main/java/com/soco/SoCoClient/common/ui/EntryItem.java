package com.soco.SoCoClient.common.ui;


import com.soco.SoCoClient._ref.GeneralConfigV1;

@Deprecated
public class EntryItem implements Item{

	public final String title;
	public final String subtitle;
    public final String status;

	public EntryItem(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
        status = "";
    }

    public EntryItem(String title, String subtitle, String status) {
        this.title = title;
        this.subtitle = subtitle;
        this.status = status;
    }

    @Override
	public boolean isSection() {
		return false;
	}

    @Override
    public String getType() {
        return GeneralConfigV1.LIST_ITEM_TYPE_ENTRY;
    }

}
