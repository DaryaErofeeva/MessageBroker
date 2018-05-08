package com.griddynamics.internship.models;

import java.sql.Timestamp;

public class TopicMessage {
    private int id;

    private String state;
    private Timestamp timestamp;

    private Topic topic;
    private Message message;

    public TopicMessage() {
    }

    public TopicMessage(String state, Timestamp timestamp, Topic topic, Message message) {
        this.state = state;
        this.timestamp = timestamp;
        this.topic = topic;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
