package com.griddynamics.internship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CreatorService {

    @Autowired
    private Parameters parameters;

    private final String tmpDir = System.getProperty("java.io.tmpdir");

    public void create() {
        try {
            createDirectoriesWithFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDirectoriesWithFiles() throws IOException {
        for (int i = 0; i < parameters.getFoldersNumber(); i++) {
            String directoryName = createDirectory(parameters.getRootFolder());
            for (int j = 0; j < parameters.getFilesNumber(); j++)
                move(writeDataToFile(createFile()), directoryName);
        }
    }

    private String createDirectory(String rootFolderPath) {
        String directoryName;

        String directoryNameBase = rootFolderPath + "/c";
        do {
            directoryName = directoryNameBase + String.format("%04d", ThreadLocalRandom.current().nextInt(0, 10000));
        } while (!new File(directoryName).mkdirs());
        return directoryName;
    }

    private Path createFile() throws IOException {
        return Files.createFile(Paths.get(tmpDir + "/" +
                String.format("%010d", ThreadLocalRandom.current().nextLong(0, 10000000000L)) +
                ".txt"));
    }

    private Path writeDataToFile(Path path) throws IOException {
        Files.write(path, String.valueOf(System.currentTimeMillis()).getBytes());
        return path;
    }

    private void move(Path path, String directoryPath) throws IOException {
        Files.move(path,
                Paths.get(directoryPath + "/" + path.getFileName()),
                StandardCopyOption.ATOMIC_MOVE);
    }
}
