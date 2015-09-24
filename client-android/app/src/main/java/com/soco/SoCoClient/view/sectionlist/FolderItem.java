package com.soco.SoCoClient.view.sectionlist;


import com.soco.SoCoClient.control.config._ref.GeneralConfigV1;

public class FolderItem implements Item{

	public final String title;
	public final String subtitle;

	public FolderItem(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
	}
	
	@Override
	public boolean isSection() {
		return false;
	}

    @Override
    public String getType() {
        return GeneralConfigV1.LIST_ITEM_TYPE_FOLDER;
    }

}
