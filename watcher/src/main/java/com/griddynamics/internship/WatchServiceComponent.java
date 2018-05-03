package com.griddynamics.internship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;

@Component
public class WatchServiceComponent {
    private WatchService watchService;

    @Autowired
    public WatchServiceComponent() {
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WatchService getWatchService() {
        return watchService;
    }
}
