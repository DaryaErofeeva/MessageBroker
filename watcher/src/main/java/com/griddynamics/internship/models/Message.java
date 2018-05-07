package com.griddynamics.internship.models;

public class Message {
    private int id;
    private int channelId;
    private String name;

    public Message() {
    }

    public Message(int channelId, String name) {
        this.channelId = channelId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
