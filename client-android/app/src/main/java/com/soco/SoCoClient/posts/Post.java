package com.soco.SoCoClient.posts;

import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.topics.Topic;
import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class Post {

    String id;
    String time;
    User user = new User();
    String comment;
    ArrayList<Photo> photos = new ArrayList<>();
    static int counter = 100000;
    Event event;
    Topic topic;

    public Post(){}

    public Post(String username, String comment){
        this.user.setUser_name(username);
        this.comment = comment;
        counter++;
        this.id=Integer.toString(counter);
        this.photos = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", user=" + user +
                ", comment='" + comment + '\'' +
                ", photos=" + photos +
                ", event=" + event +
                ", topic=" + topic +
                '}';
    }
}
