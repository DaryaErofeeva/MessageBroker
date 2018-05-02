package com.griddynamics.internship;

import com.beust.jcommander.JCommander;
import org.springframework.stereotype.Service;

@Service
public class CreatorJCommander {

    private JCommander jCommander;

    public CreatorJCommander() {
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
