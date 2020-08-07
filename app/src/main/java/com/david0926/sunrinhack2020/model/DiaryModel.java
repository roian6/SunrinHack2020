package com.david0926.sunrinhack2020.model;

public class DiaryModel {

    private String question, answer, time, image;

    public DiaryModel(){};

    public DiaryModel(String question, String answer, String time, String image) {
        this.question = question;
        this.answer = answer;
        this.time = time;
        this.image = image;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
