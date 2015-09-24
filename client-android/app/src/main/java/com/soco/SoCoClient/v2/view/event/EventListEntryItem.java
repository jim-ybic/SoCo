package com.soco.SoCoClient.v2.view.event;


import com.soco.SoCoClient.v2.control.config.ref.GeneralConfigV1;
import com.soco.SoCoClient.v2.view.sectionlist.Item;

public class EventListEntryItem implements Item{

	public final String name;
    public final String desc;
    public final String date;

//	public ContactListEntryItem(String name, String phone, String email) {
//		this.name = name;
//        this.phone = phone;
//		this.email = email;
//        status = "";
//    }

    public EventListEntryItem(String name, String desc, String date) {
        this.name = name;
        this.desc = desc;
        this.date = date;
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
