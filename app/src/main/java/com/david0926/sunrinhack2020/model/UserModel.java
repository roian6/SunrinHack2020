package com.david0926.sunrinhack2020.model;

public class UserModel {

    private String name, email, time, profile;

    public UserModel(){}

    public UserModel(String name, String email, String time, String profile) {
        this.name = name;
        this.email = email;
        this.time = time;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
