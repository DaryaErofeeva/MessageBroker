package com.griddynamics.internship.parameters;

import com.beust.jcommander.Parameter;
import org.springframework.stereotype.Component;

@Component
public class Parameters {

    @Parameter(names = {"-?", "--help"}, help = true,
            description = "Displays help information")
    private boolean help = false;

    @Parameter(names = {"-bh", "--brokerHost"}, required = true,
            description = "Broker's hosy")
    private String brokerHost;

    @Parameter(names = {"-bp", "--brokerPort"}, required = true,
            description = "Broker's port")
    private String brokerPort;

    @Parameter(names = {"-q", "--queue"}, required = true,
            description = "Queue's name")
    private String queue;

    @Parameter(names = {"-t", "--topic"}, required = true,
            description = "Topic's name")
    private String topic;

    @Parameter(names = {"-m", "--messages"}, required = true,
            description = "Amount of messages")
    private int messages;

    public boolean isHelp() {
        return help;
    }

    public String getBrokerHost() {
        return brokerHost;
    }

    public void setBrokerHost(String brokerHost) {
        this.brokerHost = brokerHost;
    }

    public String getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(String brokerPort) {
        this.brokerPort = brokerPort;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getMessages() {
        return messages;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }

    public boolean isValid() {
        return ((this.getQueue() != null && !this.getQueue().isEmpty())
                || (this.getTopic() != null && !this.getTopic().isEmpty()))
                && (this.getBrokerHost() != null && !this.getBrokerHost().isEmpty())
                && (this.getBrokerPort() != null && !this.getBrokerPort().isEmpty());
    }

    public void setParameters(String queue, String topic, int messages, String brokerHost, String brokerPort) {
        this.setQueue(queue);
        this.setTopic(topic);
        this.setMessages(messages);
        this.setBrokerHost(brokerHost);
        this.setBrokerPort(brokerPort);
    }
}
