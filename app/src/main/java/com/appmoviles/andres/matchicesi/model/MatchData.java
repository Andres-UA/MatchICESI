package com.appmoviles.andres.matchicesi.model;

public class MatchData {

    private String user;
    private double movie;
    private double book;
    private double music;
    private double fun;
    private double identity;
    private double global;

    public MatchData() {
    }

    public MatchData(String user, double movie, double book, double music, double fun, double identity, double global) {
        this.user = user;
        this.movie = movie;
        this.book = book;
        this.music = music;
        this.fun = fun;
        this.identity = identity;
        this.global = global;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getMovie() {
        return movie;
    }

    public void setMovie(double movie) {
        this.movie = movie;
    }

    public double getBook() {
        return book;
    }

    public void setBook(double book) {
        this.book = book;
    }

    public double getMusic() {
        return music;
    }

    public void setMusic(double music) {
        this.music = music;
    }

    public double getFun() {
        return fun;
    }

    public void setFun(double fun) {
        this.fun = fun;
    }

    public double getIdentity() {
        return identity;
    }

    public void setIdentity(double identity) {
        this.identity = identity;
    }

    public double getGlobal() {
        return global;
    }

    public void setGlobal(double global) {
        this.global = global;
    }
}
