package com.soco.SoCoClient.buddies.allbuddies.ui;

import com.soco.SoCoClient.userprofile.model.UserBrief;

public class MyMatchListEntryItem extends UserBrief//implements Item
{

    static final String tag = "MyMatchListEntryItem";


    private String suggest_reason = "";
    public MyMatchListEntryItem() {
        super();
    }
    public MyMatchListEntryItem(String Puser_id, String Puser_name, String Puser_icon_url) {
        super(Puser_id,Puser_name,Puser_icon_url);
    }
    public MyMatchListEntryItem(String Puser_id, String Puser_name, String Puser_icon_url, String Psuggest_reason) {
        super(Puser_id,Puser_name,Puser_icon_url);
        this.suggest_reason=Psuggest_reason;
    }

    public String getSuggest_reason() {
        return suggest_reason;
    }

    public void setSuggest_reason(String suggest_reason) {
        this.suggest_reason = suggest_reason;
    }
}
