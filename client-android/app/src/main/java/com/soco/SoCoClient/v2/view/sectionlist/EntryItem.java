package com.soco.SoCoClient.v2.view.sectionlist;


import com.soco.SoCoClient.v2.control.config.ref.GeneralConfigV1;

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
