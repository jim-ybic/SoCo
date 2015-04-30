package com.soco.SoCoClient.view.common.sectionlist;


import com.soco.SoCoClient.control.config.GeneralConfig;

public class EntryItem implements Item{

	public final String title;
	public final String subtitle;

	public EntryItem(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
	}
	
	@Override
	public boolean isSection() {
		return false;
	}

    @Override
    public String getType() {
        return GeneralConfig.LIST_ITEM_TYPE_ENTRY;
    }

}
