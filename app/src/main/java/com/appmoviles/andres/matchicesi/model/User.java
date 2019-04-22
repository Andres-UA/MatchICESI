package com.appmoviles.andres.matchicesi.model;

public class User {

    private String id;
    private String names;
    private String surnames;
    private String email;
    private boolean firstLogin;

    public User() {
    }

    public User(String id, String names, String surnames, String email, boolean firstLogin) {
        this.id = id;
        this.names = names;
        this.surnames = surnames;
        this.email = email;
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

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }
}
