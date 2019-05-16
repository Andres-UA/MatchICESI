package com.appmoviles.andres.matchicesi.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable {

    private String id;
    private ArrayList<String> descriptors;
    private int movieRank;
    private ArrayList<String> movies;
    private int musicRank;
    private ArrayList<String> musics;
    private int bookRank;
    private ArrayList<String> books;
    private ArrayList<String> funs;

    public UserData() {
    }

    public UserData(String id, ArrayList<String> descriptors, int movieRank, ArrayList<String> movies, int musicRank, ArrayList<String> musics, int bookRank, ArrayList<String> books, ArrayList<String> funs) {
        this.id = id;
        this.descriptors = descriptors;
        this.movieRank = movieRank;
        this.movies = movies;
        this.musicRank = musicRank;
        this.musics = musics;
        this.bookRank = bookRank;
        this.books = books;
        this.funs = funs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(ArrayList<String> descriptors) {
        this.descriptors = descriptors;
    }

    public int getMovieRank() {
        return movieRank;
    }

    public void setMovieRank(int movieRank) {
        this.movieRank = movieRank;
    }

    public ArrayList<String> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<String> movies) {
        this.movies = movies;
    }

    public int getMusicRank() {
        return musicRank;
    }

    public void setMusicRank(int musicRank) {
        this.musicRank = musicRank;
    }

    public ArrayList<String> getMusics() {
        return musics;
    }

    public void setMusics(ArrayList<String> musics) {
        this.musics = musics;
    }

    public int getBookRank() {
        return bookRank;
    }

    public void setBookRank(int bookRank) {
        this.bookRank = bookRank;
    }

    public ArrayList<String> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<String> books) {
        this.books = books;
    }

    public ArrayList<String> getFuns() {
        return funs;
    }

    public void setFuns(ArrayList<String> funs) {
        this.funs = funs;
    }
}

