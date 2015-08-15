package com.soco.SoCoClient.v2.view.chats;


import com.soco.SoCoClient.obsolete.v1.control.config.GeneralConfig;
import com.soco.SoCoClient.v2.view.sectionlist.Item;

public class ContactEntryItem implements Item{

	public final String name;
    public final String phone;
	public final String email;
    public final String status;

//	public ContactEntryItem(String name, String phone, String email) {
//		this.name = name;
//        this.phone = phone;
//		this.email = email;
//        status = "";
//    }

    public ContactEntryItem(String name, String phone, String email, String status) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.status = status;
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
