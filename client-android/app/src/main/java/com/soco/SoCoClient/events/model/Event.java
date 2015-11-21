package com.soco.SoCoClient.events.model;

import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class Event {
    public static final String EVENT_ID="EVENT_ID";

    long id;
    String title;
    String mainThemeColor;
    String address;
    String city;
    String start_date, end_date;
    String start_time, end_time;
    String introduction;

    int number_of_likes;
    int number_of_photos;
    int number_of_comments;
    int number_of_views;

    boolean isLikedEvent;
    String banner_url;
    String event_url;
    String status;

    //event categories
    ArrayList<String> categories = new ArrayList<>();

    //event organizers
    String creator_id, creator_name, creator_icon_url;
    String enterprise_id, enterprise_name, enterprise_icon_url;
    ArrayList<Group> supporting_groups = new ArrayList<>();

    //event buddies
    ArrayList<User> joinedFriends = new ArrayList<>();
    ArrayList<User> joinedGroupMemebers = new ArrayList<>();
    ArrayList<User> likedFriends = new ArrayList<>();
    ArrayList<User> likedGroupMembers = new ArrayList<>();
    ArrayList<User> joinedBuddies = new ArrayList<>();
    ArrayList<User> likedBuddies = new ArrayList<>();



    public Event() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainThemeColor() {
        return mainThemeColor;
    }

    public void setMainThemeColor(String mainThemeColor) {
        this.mainThemeColor = mainThemeColor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getNumber_of_likes() {
        return number_of_likes;
    }

    public void setNumber_of_likes(int number_of_likes) {
        this.number_of_likes = number_of_likes;
    }

    public int getNumber_of_photos() {
        return number_of_photos;
    }

    public void setNumber_of_photos(int number_of_photos) {
        this.number_of_photos = number_of_photos;
    }

    public int getNumber_of_comments() {
        return number_of_comments;
    }

    public void setNumber_of_comments(int number_of_comments) {
        this.number_of_comments = number_of_comments;
    }

    public int getNumber_of_views() {
        return number_of_views;
    }

    public void setNumber_of_views(int number_of_views) {
        this.number_of_views = number_of_views;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getEvent_url() {
        return event_url;
    }

    public void setEvent_url(String event_url) {
        this.event_url = event_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public void addCategory(String category){
        this.categories.add(category);
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String getCreator_icon_url() {
        return creator_icon_url;
    }

    public void setCreator_icon_url(String creator_icon_url) {
        this.creator_icon_url = creator_icon_url;
    }

    public String getEnterprise_id() {
        return enterprise_id;
    }

    public void setEnterprise_id(String enterprise_id) {
        this.enterprise_id = enterprise_id;
    }

    public String getEnterprise_name() {
        return enterprise_name;
    }

    public void setEnterprise_name(String enterprise_name) {
        this.enterprise_name = enterprise_name;
    }

    public String getEnterprise_icon_url() {
        return enterprise_icon_url;
    }

    public void setEnterprise_icon_url(String enterprise_icon_url) {
        this.enterprise_icon_url = enterprise_icon_url;
    }

    public ArrayList<Group> getSupporting_groups() {
        return supporting_groups;
    }

    public void setSupporting_groups(ArrayList<Group> supporting_groups) {
        this.supporting_groups = supporting_groups;
    }

    public void addSupporting_group(Group g){
        this.supporting_groups.add(g);
    }

    public ArrayList<User> getJoinedFriends() {
        return joinedFriends;
    }

    public void setJoinedFriends(ArrayList<User> joinedFriends) {
        this.joinedFriends = joinedFriends;
    }

    public void addJoinedFriends(User user){
        this.joinedFriends.add(user);
    }

    public ArrayList<User> getJoinedGroupMemebers() {
        return joinedGroupMemebers;
    }

    public void setJoinedGroupMemebers(ArrayList<User> joinedGroupMemebers) {
        this.joinedGroupMemebers = joinedGroupMemebers;
    }

    public void addJoinedGroupMembers(User user){
        this.joinedGroupMemebers.add(user);
    }

    public ArrayList<User> getLikedFriends() {
        return likedFriends;
    }

    public void setLikedFriends(ArrayList<User> likedFriends) {
        this.likedFriends = likedFriends;
    }

    public void addLikedFriends(User user){
        this.likedFriends.add(user);
    }

    public ArrayList<User> getLikedGroupMembers() {
        return likedGroupMembers;
    }

    public void setLikedGroupMembers(ArrayList<User> likedGroupMembers) {
        this.likedGroupMembers = likedGroupMembers;
    }

    public void addLikedGroupMembers(User user){
        this.likedGroupMembers.add(user);
    }

    public ArrayList<User> getJoinedBuddies() {
        return joinedBuddies;
    }
    public void addJoinedBuddies(User user){
        this.joinedBuddies.add(user);
    }

    public ArrayList<User> getLikedBuddies() {
        return likedBuddies;
    }
    public void addLikedBuddies(User user){
        this.likedBuddies.add(user);
    }


    public boolean hasJoinedFriend(String uid){
        for(User u : joinedFriends){
            if(u.getUser_id().equals(uid))
                return true;
        }
        return false;
    }

    public boolean hasJoinedGroupMembers(String uid){
        for(User u : joinedGroupMemebers){
            if(u.getUser_id().equals(uid))
                return true;
        }
        return false;
    }

    public boolean hasLikedFriend(String uid){
        for(User u : likedFriends){
            if(u.getUser_id().equals(uid))
                return true;
        }
        return false;
    }

    public boolean hasLikedGroupMembers(String uid){
        for(User u : likedGroupMembers){
            if(u.getUser_id().equals(uid))
                return true;
        }
        return false;
    }

    public boolean isLikedEvent() {
        return isLikedEvent;
    }

    public void setIsLikedEvent(boolean isLikedEvent) {
        this.isLikedEvent = isLikedEvent;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", mainThemeColor='" + mainThemeColor + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", introduction='" + introduction + '\'' +
                ", number_of_likes=" + number_of_likes +
                ", number_of_photos=" + number_of_photos +
                ", number_of_comments=" + number_of_comments +
                ", number_of_views=" + number_of_views +
                ", banner_url='" + banner_url + '\'' +
                ", event_url='" + event_url + '\'' +
                ", status='" + status + '\'' +
                ", categories=" + categories +
                ", creator_id='" + creator_id + '\'' +
                ", creator_name='" + creator_name + '\'' +
                ", creator_icon_url='" + creator_icon_url + '\'' +
                ", enterprise_id='" + enterprise_id + '\'' +
                ", enterprise_name='" + enterprise_name + '\'' +
                ", enterprise_icon_url='" + enterprise_icon_url + '\'' +
                ", supporting_groups=" + supporting_groups +
                '}';
    }
}
