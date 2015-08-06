package com.soco.SoCoClient.v1.view.common.sectionlist;


import com.soco.SoCoClient.v1.control.config.GeneralConfig;

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
        return GeneralConfig.LIST_ITEM_TYPE_FOLDER;
    }

}