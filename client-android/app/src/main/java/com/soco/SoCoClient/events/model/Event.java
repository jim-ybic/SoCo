package com.soco.SoCoClient.events.model;

public class Event {

    static final String tag = "Event";
    public static final String LIKED="LIKED";
    public static final String NOT_YET_LIKED="NOT YET LIKED";
    double id;
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

    String banner_url;
    String event_url;
    String status;

    String likeStatus;

    public Event() {
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
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

    public String getLikeStatus() {
        return likeStatus;
    }
    public boolean isLiked() {
        return likeStatus!=null&&LIKED.equalsIgnoreCase(likeStatus)?true:false;
    }
    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }

    @Override
    public String toString() {
        return "Event{" +
                "status='" + status + '\'' +
                ", event_url='" + event_url + '\'' +
                ", banner_url='" + banner_url + '\'' +
                ", number_of_views=" + number_of_views +
                ", number_of_comments=" + number_of_comments +
                ", number_of_photos=" + number_of_photos +
                ", number_of_likes=" + number_of_likes +
                ", introduction='" + introduction + '\'' +
                ", end_time='" + end_time + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_date='" + end_date + '\'' +
                ", start_date='" + start_date + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", mainThemeColor='" + mainThemeColor + '\'' +
                ", title='" + title + '\'' +
                '}';
    }


}
