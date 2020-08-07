package com.david0926.sunrinhack2020.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatModel {

    private String author, time, text, image;

    public ChatModel(String author, String time, String text, String image) {
        this.author = author;
        this.time = time;
        this.text = text;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
