package com.appmoviles.andres.matchicesi.model;

import java.util.Date;

public class Photo implements Comparable<Photo> {

    private String id;
    private String userId;
    private String url;
    private Date dateTime;

    public Photo() {
    }

    public Photo(String id, String userId, String url) {
        this.id = id;
        this.userId = userId;
        this.url = url;
        this.dateTime = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int compareTo(Photo o) {
        return getDateTime().compareTo(o.getDateTime());
    }
}
