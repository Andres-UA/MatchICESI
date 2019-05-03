package com.appmoviles.andres.matchicesi.model;

public class Friend {

    private String id;
    private String name;

    public Friend() {
    }

    public Friend(String id, String name) {
        id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        id = id;
    }

    public String getName() {
        return name;
    }

    public void setNombre(String name) {
        this.name = name;
    }
}
