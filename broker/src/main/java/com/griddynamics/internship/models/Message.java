package com.griddynamics.internship.models;

import java.sql.Timestamp;

public class Message {
    private int id;
    private String name;
    private String content;
    private Timestamp timestamp;

    public Message() {
    }

    public Message(String name, String content, Timestamp timestamp) {
        this.name = name;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
