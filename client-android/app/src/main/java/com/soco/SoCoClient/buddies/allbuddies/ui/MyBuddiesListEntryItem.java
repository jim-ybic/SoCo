package com.soco.SoCoClient.buddies.allbuddies.ui;

import com.soco.SoCoClient.userprofile.model.UserBrief;

public class MyBuddiesListEntryItem extends UserBrief//implements Item
{

    static final String tag = "MyBuddiesListEntryItem";

    private String location = "";
    public MyBuddiesListEntryItem() {
        super();
    }
    public MyBuddiesListEntryItem(String Puser_id, String Puser_name, String Puser_icon_url) {
        super(Puser_id,Puser_name,Puser_icon_url);
    }
    public MyBuddiesListEntryItem(String Puser_id, String Puser_name, String Puser_icon_url, String Plocation) {
        super(Puser_id,Puser_name,Puser_icon_url);
        this.location=Plocation;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String Plocation) {
        this.location = Plocation;
    }

//    @Override
//	public boolean isSection() {
//		return false;
//	}
//
//    @Override
//    public String getType() {
//        return GeneralConfigV1.LIST_ITEM_TYPE_ENTRY;
//    }

}
