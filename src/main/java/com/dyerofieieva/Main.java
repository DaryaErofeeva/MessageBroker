package com.dyerofieieva;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static final MainParameters mainParameters = new MainParameters();

    public static void main(String[] args) {
        if (!setParameters(args)) return;

        try {
            createDirectoriesWithFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean setParameters(String[] args) {
        if (args.length > 0 && setFromMainArgs(args)) return true;
        if (setFromSystemProperties()) return true;
        if (seFromEnvironmentVariables()) return true;

        return false;
    }

    private static boolean setFromMainArgs(String[] args) {
        return handleInputArgs(args);
    }

    private static boolean handleInputArgs(String[] args) {
        JCommander jCommander = JCommander.newBuilder()
                .addObject(mainParameters)
                .build();
        jCommander.setProgramName("messageBroker");

        try {
            jCommander.parse(args);

            if (mainParameters.isHelp()) {
                showUsage(jCommander);
                return false;
            }
        } catch (ParameterException ex) {
            ex.printStackTrace();
            showUsage(jCommander);
            return false;
        }

        return true;
    }

    private static void showUsage(JCommander jCommander) {
        jCommander.usage();
    }

    private static boolean setFromSystemProperties() {
//        try (InputStream inputStream = new FileInputStream("my.properties")) {
//            System.getProperties().load(inputStream);
        try {
            setReceivedParameters(System.getProperty("rootFolder"),
                    Integer.valueOf(System.getProperty("foldersNumber")),
                    Integer.valueOf(System.getProperty("filesNumber")));
//        } catch (IOException | NumberFormatException e) {
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean seFromEnvironmentVariables() {
        try {
            setReceivedParameters(System.getenv("ROOT_FOLDER"),
                    Integer.valueOf(System.getenv("FOLDERS_NUMBER")),
                    Integer.valueOf(System.getenv("FILES_NUMBER")));
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    private static void setReceivedParameters(String rootFolder, int foldersNumber, int filesNumber) {
        mainParameters.setRootFolder(rootFolder);
        mainParameters.setFoldersNumber(foldersNumber);
        mainParameters.setFilesNumber(filesNumber);
    }

    private static void createDirectoriesWithFiles() throws IOException {
        for (int i = 0; i < mainParameters.getFoldersNumber(); i++) {
            String directoryName = createDirectory(mainParameters.getRootFolder());
            for (int j = 0; j < mainParameters.getFilesNumber(); j++)
                writeDataToFile(createFile(directoryName));
        }
    }

    private static String createDirectory(String rootFolderPath) {
        String directoryName;

        String directoryNameBase = rootFolderPath + "/c";
        do {
            directoryName = directoryNameBase + String.format("%04d", ThreadLocalRandom.current().nextInt(0, 10000));
        } while (!new File(directoryName).mkdirs());
        return directoryName;
    }

    private static File createFile(String directoryName) throws IOException {
        File file;
        do {
            file = new File(directoryName + "/" +
                    String.format("%010d", ThreadLocalRandom.current().nextLong(0, 10000000000L)) +
                    ".txt");
        } while (!(file.exists() || file.createNewFile()));

        return file;
    }

    private static void writeDataToFile(File file) {
        try (Writer writer = new FileWriter(file)) {
            writer.write(String.valueOf(System.currentTimeMillis()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
