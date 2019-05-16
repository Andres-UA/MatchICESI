package com.appmoviles.andres.matchicesi.model;

import java.util.Date;

public class Request {
    private Date date;
    private String user_one;
    private String user_two;
    private String type;

    public Request() {
    }

    public Request(Date date, String user_one, String user_two, String type) {
        this.date = date;
        this.user_one = user_one;
        this.user_two = user_two;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUser_one() {
        return user_one;
    }

    public void setUser_one(String user_one) {
        this.user_one = user_one;
    }

    public String getUser_two() {
        return user_two;
    }

    public void setUser_two(String user_two) {
        this.user_two = user_two;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
