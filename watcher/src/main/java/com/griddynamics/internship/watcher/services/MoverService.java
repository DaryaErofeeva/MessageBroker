package com.griddynamics.internship.watcher.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class MoverService {
    @Value("${rootFolder}")
    private Path rootFolder;

    public void move(Path path) {
        try {
            String processedPath = "processed_" + rootFolder.getFileName() + "/"
                    + rootFolder.relativize(path.getParent()).toString();

            File file = new File(processedPath);
            if (!file.exists())
                file.mkdirs();

            Files.move(
                    path,
                    Paths.get(processedPath + "/" + path.getFileName().toString()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
