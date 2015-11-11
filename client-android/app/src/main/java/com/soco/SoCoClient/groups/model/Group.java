package com.soco.SoCoClient.groups.model;

public class Group {

    String group_id, group_name, group_icon_url;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_icon_url() {
        return group_icon_url;
    }

    public void setGroup_icon_url(String group_icon_url) {
        this.group_icon_url = group_icon_url;
    }

    @Override
    public String toString() {
        return "Group{" +
                "group_id='" + group_id + '\'' +
                ", group_name='" + group_name + '\'' +
                ", group_icon_url='" + group_icon_url + '\'' +
                '}';
    }
}
