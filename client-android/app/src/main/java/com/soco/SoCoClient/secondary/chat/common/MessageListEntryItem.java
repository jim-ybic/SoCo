package com.soco.SoCoClient.secondary.chat.common;


import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient.events.ui.Item;

public class MessageListEntryItem implements Item{

	public final String content;
    public final String timestamp;

//	public ContactListEntryItem(String name, String phone, String email) {
//		this.name = name;
//        this.phone = phone;
//		this.email = email;
//        status = "";
//    }

    public MessageListEntryItem(String content, String timestamp) {
        this.content = content;
        this.timestamp = timestamp;
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
