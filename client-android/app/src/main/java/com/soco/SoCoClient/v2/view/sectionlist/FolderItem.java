package com.soco.SoCoClient.v2.view.sectionlist;


import com.soco.SoCoClient.v2.control.config.ref.GeneralConfigV1;

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
