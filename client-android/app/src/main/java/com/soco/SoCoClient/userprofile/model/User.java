package com.soco.SoCoClient.userprofile.model;

import com.soco.SoCoClient.common.util.StringUtil;

import java.util.ArrayList;

public class User extends UserBrief{

   private String location;
    private ArrayList<String> interests;
    private int number_common_event;
    private String common_event_name;
    private int number_common_group;
    private String common_group_name;
    private int number_common_buddy;
    private ArrayList<UserBrief> common_buddies;

   public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interest) {
        this.interests = interest;
    }

    public void addInterest(String interest){
        if(this.interests==null){
            this.interests = new ArrayList<>();
        }
        this.interests.add(interest);
    }
    public int getNumber_common_event() {
        return number_common_event;
    }

    public void setNumber_common_event(int number_common_event) {
        this.number_common_event = number_common_event;
    }

    public String getCommon_event_name() {
        return common_event_name;
    }

    public void setCommon_event_name(String common_event_name) {
        this.common_event_name = common_event_name;
    }

    public int getNumber_common_group() {
        return number_common_group;
    }

    public void setNumber_common_group(int number_common_group) {
        this.number_common_group = number_common_group;
    }

    public String getCommon_group_name() {
        return common_group_name;
    }

    public void setCommon_group_name(String common_group_name) {
        this.common_group_name = common_group_name;
    }

    public int getNumber_common_buddy() {
        return number_common_buddy;
    }

    public void setNumber_common_buddy(int number_common_buddy) {
        this.number_common_buddy = number_common_buddy;
    }

    public ArrayList<UserBrief> getCommon_buddies() {
        return common_buddies;
    }

    public void setCommon_buddies(ArrayList<UserBrief> common_buddies) {
        this.common_buddies = common_buddies;
    }
    public void addCommon_buddy(UserBrief userBrief){
        if(this.common_buddies==null){
            this.common_buddies = new ArrayList<>();
        }
        this.common_buddies.add(userBrief);
    }

    @Override
    public String toString() {
        String interestString = (interests==null||interests.size()==0)?"":interests.toString();
        StringBuffer sb = new StringBuffer();
        if(common_buddies!=null&&common_buddies.size()>0){
            for(UserBrief ub:common_buddies){
                sb.append(ub.toString());
            }
        }

        return "User{" +
                "user_id='" + this.getUser_id()+ '\'' +
                ", user_name='" + this.getUser_name() + '\'' +
                ", user_icon_url='" + this.getUser_icon_url() + '\'' +
                ", location='" + location + '\'' +
                ", interest='" + interestString + '\'' +
                ", number_common_events='" + number_common_event + '\'' +
                ", first_common_event_name='" + common_event_name + '\'' +
                ", number_of_common_groups='" + number_common_group + '\'' +
                ", first_common_group_name='" + common_group_name + '\'' +
                ", number_of_common_buddies='" + number_common_buddy + '\'' +
                ", common_buddies ='" + sb.toString() + '\'' +
                '}';
    }
}
