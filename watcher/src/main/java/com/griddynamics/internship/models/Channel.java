package com.griddynamics.internship.models;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("channel")
public class Channel {
    private int id;
    private String path;
    private String name;

    public Channel(){}

    public Channel(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
