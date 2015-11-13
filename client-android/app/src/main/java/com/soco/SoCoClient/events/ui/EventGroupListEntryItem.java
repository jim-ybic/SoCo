package com.soco.SoCoClient.events.ui;


import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient.events.ui.Item;

import java.util.ArrayList;

public class EventGroupListEntryItem implements Item{

    //todo: remove below obsolte fields
//	public final String name;
//    public final String phone;
//	public final String email;
//    public final String status;

    String group_icon_url;
    String group_name;
    String number_of_participants;
    ArrayList<String> representative_participants_icon_url;

//	public ContactListEntryItem(String name, String phone, String email) {
//		this.name = name;
//        this.phone = phone;
//		this.email = email;
//        status = "";
//    }

//    public EventGroupListEntryItem(
//            String name, String phone, String email, String status
//    ) {
//        this.name = name;
//        this.phone = phone;
//        this.email = email;
//        this.status = status;
//    }

    public EventGroupListEntryItem(){}

    public String getGroup_icon_url() {
        return group_icon_url;
    }

    public void setGroup_icon_url(String group_icon_url) {
        this.group_icon_url = group_icon_url;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getNumber_of_participants() {
        return number_of_participants;
    }

    public void setNumber_of_participants(String number_of_participants) {
        this.number_of_participants = number_of_participants;
    }

    public ArrayList<String> getRepresentative_participants_icon_url() {
        return representative_participants_icon_url;
    }

    public void setRepresentative_participants_icon_url(ArrayList<String> representative_participants_icon_url) {
        this.representative_participants_icon_url = representative_participants_icon_url;
    }

    @Override
	public boolean isSection() {
		return false;
	}

    @Override
    public String getType() {
        return Item.LIST_ITEM_TYPE_ENTRY;
    }

}
