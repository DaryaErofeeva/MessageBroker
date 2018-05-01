package com.griddynamics.internship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LogService {

    @Autowired
    private ReaderService readerService;

    private Logger logger;

    @Autowired
    public void setLogger(CommandLineRunner commandLineRunner) {
        this.logger = LoggerFactory.getLogger(commandLineRunner.getClass());
    }

    public void log() {
        File file = new File(readerService.getRootFolder());

        if (file.exists()) print(file);
    }

    private void print(File file) {
        if (file.isFile())
            logger.info("\tFile: {}", file.getName());
        else logger.info("Directory: {}", file.getName());

        if (file.isDirectory())
            for (File f : file.listFiles())
                print(f);
    }
}
