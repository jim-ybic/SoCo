package com.soco.SoCoClient.posts;

import com.soco.SoCoClient.userprofile.model.User;

import java.util.ArrayList;

public class Post {

    String id;
    String time;
    User user = new User();
    String comment;
    ArrayList<Photo> photos = new ArrayList<>();
   static int counter = 100000;
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

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", user=" + user.getUser_name() +
                ", comment='" + comment + '\'' +
                ", photos=" + photos +
                '}';
    }
}
