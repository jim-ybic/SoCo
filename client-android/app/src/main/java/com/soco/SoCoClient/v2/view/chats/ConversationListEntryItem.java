package com.soco.SoCoClient.v2.view.chats;


import com.soco.SoCoClient.v2.control.config.ref.GeneralConfigV1;
import com.soco.SoCoClient.v2.view.sectionlist.Item;

public class ConversationListEntryItem implements Item{

	public final String name;
    public final String lastMsgContent;
    public final String lastMsgTimestamp;

//	public ContactListEntryItem(String name, String phone, String email) {
//		this.name = name;
//        this.phone = phone;
//		this.email = email;
//        status = "";
//    }

    public ConversationListEntryItem(String name, String lastMsgContent, String lastMsgTimestamp) {
        this.name = name;
        this.lastMsgContent = lastMsgContent;
        this.lastMsgTimestamp = lastMsgTimestamp;
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
