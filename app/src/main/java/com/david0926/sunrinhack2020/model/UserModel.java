package com.david0926.sunrinhack2020.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;

public class UserModel {

    public UserModel(){}

    private String name, email, time, profile;
    private ArrayList<HashMap<String, String>> chat;

    public UserModel(String name, String email, String time, String profile, ArrayList<HashMap<String, String>> chat) {
        this.name = name;
        this.email = email;
        this.time = time;
        this.profile = profile;
        this.chat = chat;
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

    public ArrayList<ChatModel> getChat() {
        Gson gson = new Gson();
        ArrayList<ChatModel> list = new ArrayList<>();

        for(HashMap<String, String> map:chat){
            JsonElement e = gson.toJsonTree(map);
            list.add(gson.fromJson(e, ChatModel.class));
        }

        return list;
    }

    public void setChat(ArrayList<HashMap<String, String>> chat) {
        this.chat = chat;
    }
}
