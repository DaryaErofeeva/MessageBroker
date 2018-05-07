package com.griddynamics.internship.models;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("message")
public class Message {
    private int id;
    private Channel channel;
    private String name;

    public Message() {
    }

    public Message(Channel channel, String name) {
        this.channel = channel;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
