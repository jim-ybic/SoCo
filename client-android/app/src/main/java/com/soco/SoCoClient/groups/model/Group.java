package com.soco.SoCoClient.groups.model;

import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class Group {

    String group_id, group_name, group_icon_url;
    ArrayList<User> members = new ArrayList<>();
    String description;
    String numberOfMembers;

    String location;
    ArrayList<Event> upcomingEvents = new ArrayList();
    ArrayList<Event> pastEvents = new ArrayList();

    ArrayList<String> categories = new ArrayList<>();

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<Event> getUpcomingEvents() {
        return upcomingEvents;
    }

    public void setUpcomingEvents(ArrayList<Event> upcomingEvents) {
        this.upcomingEvents = upcomingEvents;
    }

    public void addUpcomingEvent(Event e){
        this.upcomingEvents.add(e);
    }

    public ArrayList<Event> getPastEvents() {
        return pastEvents;
    }

    public void setPastEvents(ArrayList<Event> pastEvents) {
        this.pastEvents = pastEvents;
    }

    public void addPastEvent(Event e){
        this.pastEvents.add(e);
    }

    public String getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(String numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }


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

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public void addMember(User user){
        this.members.add(user);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public void addCategory(String cat){
        this.categories.add(cat);
    }

    @Override
    public String toString() {
        return "Group{" +
                "group_id='" + group_id + '\'' +
                ", group_name='" + group_name + '\'' +
                ", group_icon_url='" + group_icon_url + '\'' +
                ", members=" + members +
                ", description='" + description + '\'' +
                '}';
    }
}
