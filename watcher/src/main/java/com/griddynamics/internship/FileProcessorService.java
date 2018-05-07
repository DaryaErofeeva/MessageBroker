package com.griddynamics.internship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class FileProcessorService {
    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private MoverService moverService;

    public void process(Path path) {
        databaseService.insert(path);
        moverService.move(path);
    }
}
