package com.griddynamics.internship.models;

import java.sql.Timestamp;

public class QueueMessage {
    private int id;
    private String state;
    private Timestamp timestamp;

    private Queue queue;
    private Message message;

    public QueueMessage() {
    }

    public QueueMessage(String state, Timestamp timestamp, Queue queue, Message message) {
        this.state = state;
        this.timestamp = timestamp;
        this.queue = queue;
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

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
