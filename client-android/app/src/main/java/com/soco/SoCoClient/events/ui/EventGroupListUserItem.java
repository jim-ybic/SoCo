package com.soco.SoCoClient.events.ui;


import java.util.ArrayList;

public class EventGroupListUserItem implements Item{

    //todo: remove below obsolte fields
//	public final String name;
//    public final String phone;
//	public final String email;
//    public final String status;

    String user_id;
//    String user_icon_url;
    String user_name;
    String number_of_followers;

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

    public EventGroupListUserItem(){}

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

//    public String getUser_icon_url() {
//        return user_icon_url;
//    }

//    public void setUser_icon_url(String user_icon_url) {
//        this.user_icon_url = user_icon_url;
//    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNumber_of_followers() {
        return number_of_followers;
    }

    public void setNumber_of_followers(String number_of_followers) {
        this.number_of_followers = number_of_followers;
    }

    @Override
	public boolean isSection() {
		return false;
	}

    @Override
    public String getType() {
        return Item.LIST_ITEM_TYPE_USER;
    }

}
