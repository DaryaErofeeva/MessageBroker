package com.griddynamics.internship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

@Service
public class WatcherService {
//    @Value("${rootFolder}")
    private String rootFolder;

    public WatcherService(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public void poll() throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(rootFolder);

        path.register(
                watchService,
                ENTRY_CREATE,
                ENTRY_MODIFY,
                ENTRY_DELETE);

        for (; ; ) {
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");
                }
                key.reset();
            }
        }
    }
}
