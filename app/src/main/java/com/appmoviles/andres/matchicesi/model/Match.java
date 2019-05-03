package com.appmoviles.andres.matchicesi.model;

public class Match {

    private String name;
    private String image;
    private String age;

    public Match() {
    }

    public Match(String name, String image, String age) {
        this.name = name;
        this.image = image;
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
