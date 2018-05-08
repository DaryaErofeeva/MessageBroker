package com.griddynamics.internship.models;

public class Queue {
    private int id;
    private String name;

    private Producer producer;
    private Consumer consumer;

    public Queue() {
    }

    public Queue(String name, Producer producer, Consumer consumer) {
        this.name = name;
        this.producer = producer;
        this.consumer = consumer;
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

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
}
