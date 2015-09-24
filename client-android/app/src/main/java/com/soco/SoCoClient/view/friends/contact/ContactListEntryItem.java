package com.soco.SoCoClient.view.friends.contact;


import com.soco.SoCoClient.control.config._ref.GeneralConfigV1;
import com.soco.SoCoClient.view.common.Item;

public class ContactListEntryItem implements Item{

	public final String name;
    public final String phone;
	public final String email;
    public final String status;

//	public ContactListEntryItem(String name, String phone, String email) {
//		this.name = name;
//        this.phone = phone;
//		this.email = email;
//        status = "";
//    }

    public ContactListEntryItem(String name, String phone, String email, String status) {
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
        return GeneralConfigV1.LIST_ITEM_TYPE_ENTRY;
    }

}
