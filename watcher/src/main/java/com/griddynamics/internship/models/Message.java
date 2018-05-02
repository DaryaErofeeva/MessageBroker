package com.griddynamics.internship.models;

public class Message {
    private int id;
    private Channel channel;
    private String name;

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

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", channel=" + channel +
                ", name='" + name + '\'' +
                '}';
    }
}
