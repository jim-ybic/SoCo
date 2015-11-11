package com.soco.SoCoClient.events.ui;

import com.soco.SoCoClient._ref.GeneralConfigV1;

public class EventGroupListSectionItem implements Item {

	private final String label;
	
	public EventGroupListSectionItem(String label) {
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
	@Override
	public boolean isSection() {
		return true;
	}

    @Override
    public String getType() {
        return Item.LIST_ITEM_TYPE_SECTION;
    }

}
