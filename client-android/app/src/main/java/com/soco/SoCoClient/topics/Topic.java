package com.soco.SoCoClient.topics;

import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class Topic {

    String name;
    int numberPosts;
    int numberEvents;

    public Topic(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberPosts() {
        return numberPosts;
    }

    public void setNumberPosts(int numberPosts) {
        this.numberPosts = numberPosts;
    }

    public int getNumberEvents() {
        return numberEvents;
    }

    public void setNumberEvents(int numberEvents) {
        this.numberEvents = numberEvents;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                ", numberPosts=" + numberPosts +
                ", numberEvents=" + numberEvents +
                '}';
    }
}
