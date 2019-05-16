package com.appmoviles.andres.matchicesi.model;

import java.util.Date;

public class Friendship {

    private String id;
    private String sender;
    private String receiver;
    private String state;
    private Date data;

    public Friendship() {
    }

    public Friendship(String sender, String receiver, String state, Date data) {
        this.sender = sender;
        this.receiver = receiver;
        this.state = state;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
