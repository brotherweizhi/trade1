package com.bean;

public class Message {
    int PicRes,id;
    String Title,Content,date;

    public Message(int picRes, int id, String title, String content, String date) {
        PicRes = picRes;
        this.id = id;
        Title = title;
        Content = content;
        this.date = date;
    }

    public int getPicRes() {
        return PicRes;
    }

    public void setPicRes(int picRes) {
        PicRes = picRes;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
