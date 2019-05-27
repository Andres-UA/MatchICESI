package com.appmoviles.andres.matchicesi.model;

public class SearchPreferences {

    private int minAge;
    private int maxAge;
    private boolean male;
    private boolean female;

    public SearchPreferences() {
    }

    public SearchPreferences(int minAge, int maxAge, boolean male, boolean female) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.male = male;
        this.female = female;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public boolean isFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

}
