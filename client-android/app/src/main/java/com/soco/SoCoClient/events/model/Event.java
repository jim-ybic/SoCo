package com.soco.SoCoClient.events.model;

public class Event {

    static final String tag = "Event";

    String title;
    String mainThemeColor;
    String location;
    String date;
    String time;
    String introduction;

    public Event() {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", mainThemeColor='" + mainThemeColor + '\'' +
                ", location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
