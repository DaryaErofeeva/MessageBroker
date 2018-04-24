package com.dyerofieieva;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ThreadLocalRandom;

public class Creator {
    private Parameters parameters;

    public Creator(Parameters parameters) {
        this.parameters = parameters;
    }

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
                writeDataToFile(createFile(directoryName));
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

    private File createFile(String directoryName) throws IOException {
        File file;
        do {
            file = new File(directoryName + "/" +
                    String.format("%010d", ThreadLocalRandom.current().nextLong(0, 10000000000L)) +
                    ".txt");
        } while (!(file.exists() || file.createNewFile()));

        return file;
    }

    private void writeDataToFile(File file) {
        try (Writer writer = new FileWriter(file)) {
            writer.write(String.valueOf(System.currentTimeMillis()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
