package com.griddynamics.internship.models;

public class Producer {
    private int id;
    private String name;

    public Producer() {
    }

    public Producer(String name) {
        this.name = name;
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
}
