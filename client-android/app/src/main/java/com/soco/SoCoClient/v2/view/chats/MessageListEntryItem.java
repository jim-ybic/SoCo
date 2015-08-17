package com.soco.SoCoClient.v2.view.chats;


import com.soco.SoCoClient.obsolete.v1.control.config.GeneralConfig;
import com.soco.SoCoClient.v2.view.sectionlist.Item;

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
        return GeneralConfig.LIST_ITEM_TYPE_ENTRY;
    }

}
