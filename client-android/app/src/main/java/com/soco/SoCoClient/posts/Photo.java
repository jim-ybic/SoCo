package com.soco.SoCoClient.posts;

import com.soco.SoCoClient.userprofile.model.User;

public class Photo {

    String name;
    String url;

    public Photo(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
