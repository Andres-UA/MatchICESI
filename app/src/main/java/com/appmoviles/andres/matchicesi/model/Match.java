package com.appmoviles.andres.matchicesi.model;

public class Match {

    private String id;
    private String name;
    private String description;
    private String image;
    private int age;

    public Match() {
    }

    public Match(String id, String name, String description, String image, int age) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
