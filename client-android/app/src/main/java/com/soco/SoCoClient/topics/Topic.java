package com.soco.SoCoClient.topics;

import com.soco.SoCoClient.groups.model.Group;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class Topic {

    String id;
    String title;
    String introduction;
    int numberPosts;
    int numberEvents;
    int numberViews;
    int numberPhotos;
    String createTimedate;
    Group group = new Group();
    User creator = new User();

    Topic(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public int getNumberViews() {
        return numberViews;
    }

    public void setNumberViews(int numberViews) {
        this.numberViews = numberViews;
    }

    public int getNumberPhotos() {
        return numberPhotos;
    }

    public void setNumberPhotos(int numberPhotos) {
        this.numberPhotos = numberPhotos;
    }

    public String getCreateTimedate() {
        return createTimedate;
    }

    public void setCreateTimedate(String createTimedate) {
        this.createTimedate = createTimedate;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", introduction='" + introduction + '\'' +
                ", numberPosts=" + numberPosts +
                ", numberEvents=" + numberEvents +
                ", numberViews=" + numberViews +
                ", numberPhotos=" + numberPhotos +
                ", createTimedate='" + createTimedate + '\'' +
                ", group=" + group +
                ", creator=" + creator +
                '}';
    }
}
