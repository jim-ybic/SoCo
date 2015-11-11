package com.soco.SoCoClient.common.ui;


import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient.events.ui.Item;

@Deprecated
public class FolderItem implements Item {

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
