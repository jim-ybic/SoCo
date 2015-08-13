package com.soco.SoCoClient.v2.view.sectionlist;

import com.soco.SoCoClient.obsolete.v1.control.config.GeneralConfig;

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
        return GeneralConfig.LIST_ITEM_TYPE_SECTION;
    }

}
