package com.griddynamics.internship.watcher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
public class PollService {

    @Value("${rootFolder}")
    private Path rootFolder;

    @Autowired
    private WatchServiceComponent watcher;

    @Autowired
    private FileVisitorService fileVisitorService;

    public void poll() {
        try {
            if (!Files.exists(rootFolder)) Files.createDirectories(rootFolder);
            walkFileTree(rootFolder);
            processEvents();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void processEvents() throws InterruptedException, IOException {
        WatchKey key;
        while ((key = watcher.getWatchService().take()) != null) {
            processReceivedKey(key);
        }
    }

    private void processReceivedKey(WatchKey key) throws IOException {
        for (WatchEvent<?> event : key.pollEvents())
                    walkFileTree(((Path) key.watchable())
                        .resolve((Path) event.context()));
        key.reset();
    }

    private void walkFileTree(Path path) throws IOException {
        Files.walkFileTree(path, fileVisitorService);
    }
}
