package com.griddynamics.internship.watcher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@Service
public class FileVisitorService extends SimpleFileVisitor<Path> {
    @Autowired
    private WatchServiceComponent watcher;

    @Autowired
    private FileProcessorService fileProcessor;

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) throws IOException {
        path.register(watcher.getWatchService(), ENTRY_CREATE, ENTRY_MODIFY);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        fileProcessor.process(path);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
        e.printStackTrace();
        return FileVisitResult.CONTINUE;
    }
}
