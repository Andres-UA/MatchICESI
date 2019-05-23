package com.appmoviles.andres.matchicesi.model;

public class Message {

    private String name;
    private String text;
    private String id;

    public Message() {
    }

    public Message(String name, String text, String id) {
        this.name = name;
        this.text = text;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
