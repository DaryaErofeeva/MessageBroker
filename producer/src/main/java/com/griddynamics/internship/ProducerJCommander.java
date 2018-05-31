package com.griddynamics.internship;

import com.beust.jcommander.JCommander;
import com.griddynamics.internship.parameters.Parameters;
import org.springframework.stereotype.Service;

@Service
public class ProducerJCommander {

    private JCommander jCommander;

    public ProducerJCommander() {
        jCommander = JCommander
                .newBuilder()
                .addObject(Parameters.class)
                .build();
    }

    public void addObject(Parameters parameters) {
        jCommander.addObject(parameters);
    }

    public void parse(String[] args) {
        jCommander.parse(args);
    }

    public void usage() {
        jCommander.usage();
    }
}
