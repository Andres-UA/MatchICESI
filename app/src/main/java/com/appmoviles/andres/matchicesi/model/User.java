package com.appmoviles.andres.matchicesi.model;

import java.util.Date;

public class User {

    private String id;
    private String names;
    private String surnames;
    private String email;
    private Date birthDate;
    private String career;
    private String genre;
    private boolean firstLogin;
    private String profilePic;
    private String description;

    public User() {
    }

    public User(String id, String names, String surnames, String email, Date birthDate, String career, String genre, boolean firstLogin, String profilePic, String description) {
        this.id = id;
        this.names = names;
        this.surnames = surnames;
        this.email = email;
        this.birthDate = birthDate;
        this.career = career;
        this.genre = genre;
        this.firstLogin = firstLogin;
        this.profilePic = profilePic;
        this.description = description;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
