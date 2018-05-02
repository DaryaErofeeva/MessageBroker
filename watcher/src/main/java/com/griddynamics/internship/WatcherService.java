package com.griddynamics.internship;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.dao.impl.ChangesDAO;
import com.griddynamics.internship.dao.impl.ChannelsDAO;
import com.griddynamics.internship.dao.impl.MessagesDAO;
import com.griddynamics.internship.models.Change;
import com.griddynamics.internship.models.Channel;
import com.griddynamics.internship.models.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

@Service
public class WatcherService {
    @Value("${rootFolder}")
    private Path rootFolder;

    private ChannelsDAO channelsDAO;
    private MessagesDAO messagesDAO;
    private ChangesDAO changesDAO;

    private WatchService watcher;
    private Map<WatchKey, Path> keys;

    public void poll() throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();

        DAOFactory daoFactory = DAOFactory.getInstance();
        channelsDAO = daoFactory.getChannelsDAO();
        messagesDAO = daoFactory.getMessagesDAO();
        changesDAO = daoFactory.getChangesDAO();

        walkAndRegisterDirectories(rootFolder);

        try {
            processEvents();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void walkAndRegisterDirectories(final Path start) throws IOException {

        if (!Files.exists(rootFolder)) Files.createDirectories(rootFolder);

        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                registerDirectory(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void registerDirectory(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);

        Files.list(dir)
                .filter(path -> !Files.isDirectory(path))
                .forEach(this::insert);
    }

    void processEvents() throws InterruptedException {

        WatchKey key;
        while ((key = watcher.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                Path name = ((WatchEvent<Path>) event).context();
                Path child = keys.get(key).resolve(name);

                if (event.kind() == ENTRY_CREATE && Files.isDirectory(child)) {
                    try {
                        walkAndRegisterDirectories(child);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (Files.isRegularFile(child)) insert(child);
            }
            key.reset();
        }
    }

    private void insert(Path path) {
        try {
            changesDAO.createEntityIfNotExists(new Change(
                    messagesDAO.createEntityIfNotExists(
                            new Message(
                                    channelsDAO.createEntityIfNotExists(
                                            new Channel(
                                                    path.getParent().toAbsolutePath().toString(),
                                                    path.getParent().getFileName().toString())),
                                    path.getFileName().toString())),
                    new String(Files.readAllBytes(path)),
                    new Timestamp(System.currentTimeMillis())));

            String processed = "processed_" + rootFolder.getFileName() + "/" + rootFolder.relativize(path.getParent()).toString();
            new File(processed).mkdirs();
            Files.move(path, Paths.get(processed + "/" + path.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
