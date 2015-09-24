package com.soco.SoCoClient.v2.view.sectionlist;

import com.soco.SoCoClient.v2.control.config.ref.GeneralConfigV1;

public class SectionItem implements Item{

	private final String title;
	
	public SectionItem(String title) {
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	@Override
	public boolean isSection() {
		return true;
	}

    @Override
    public String getType() {
        return GeneralConfigV1.LIST_ITEM_TYPE_SECTION;
    }

}
