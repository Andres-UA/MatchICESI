package com.appmoviles.andres.matchicesi.model;

import java.util.Date;

public class User {

    private String id;
    private String names;
    private String surnames;
    private String email;
    private String birthDate;
    private String career;
    private String genre;
    private boolean firstLogin;

    public User() {
    }

    public User(String id, String names, String surnames, String email, String birthDate, String career, String genre, boolean firstLogin) {
        this.id = id;
        this.names = names;
        this.surnames = surnames;
        this.email = email;
        this.birthDate = birthDate;
        this.career = career;
        this.genre = genre;
        this.firstLogin = firstLogin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }
}
