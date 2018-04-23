package com.dyerofieieva;

import com.beust.jcommander.Parameter;

public class MainParameters {
    @Parameter(names = {"-?", "--help"}, help = true,
            description = "Displays help information")
    private boolean help = false;

    @Parameter(names = {"-r", "--rootFolder"}, required = true,
            description = "Absolute or relative path to folder where folders will be created")
    private String rootFolder;

    @Parameter(names = {"-fldrs", "--foldersNumber"}, required = true,
            description = "Number of folders to be created")
    private int foldersNumber;

    @Parameter(names = {"-fls", "--filesNumber"}, required = true,
            description = "Number of files to be created")
    private int filesNumber;

    public boolean isHelp() {
        return help;
    }

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public int getFoldersNumber() {
        return foldersNumber;
    }

    public void setFoldersNumber(int foldersNumber) {
        this.foldersNumber = foldersNumber;
    }

    public int getFilesNumber() {
        return filesNumber;
    }

    public void setFilesNumber(int filesNumber) {
        this.filesNumber = filesNumber;
    }
}
