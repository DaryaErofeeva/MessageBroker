package com.griddynamics.internship.models;

import java.sql.Timestamp;

public class Change {
    private int id;
    private int messageId;
    private String content;
    private Timestamp timestamp;

    public Change() {
    }

    public Change(int messageId, String content, Timestamp timestamp) {
        this.messageId = messageId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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
