package com.david0926.sunrinhack2020.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatModel {

    private String author, time, text;

    public ChatModel(){}

    public ChatModel(String author, String time, String text) {
        this.author = author;
        this.time = time;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
