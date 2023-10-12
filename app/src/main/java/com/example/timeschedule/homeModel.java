package com.example.timeschedule;

public class homeModel {

    String subject;
    String teacher;
    String color;



    String id,date;



    public homeModel(String subject, String teacher, String color,String id,String date) {
        this.subject = subject;
        this.teacher = teacher;
        this.color=color;
        this.id=id;
        this.date=date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
