package com.griddynamics.internship;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.Change;
import com.griddynamics.internship.models.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

@Service
public class WatcherService {
//    @Value("${rootFolder}")
//    private String rootFolder;

//    public WatcherService(String rootFolder) {
//        this.rootFolder = rootFolder;
//    }
//
//    public void poll() throws IOException, InterruptedException {
//        WatchService watchService = FileSystems.getDefault().newWatchService();
//        Path path = Paths.get(rootFolder);
//
//        path.register(
//                watchService,
//                ENTRY_CREATE,
//                ENTRY_MODIFY,
//                ENTRY_DELETE);
//
//        for (; ; ) {
//            WatchKey key;
//            while ((key = watchService.take()) != null) {
//                for (WatchEvent<?> event : key.pollEvents()) {
//                    System.out.println(
//                            "Event kind:" + event.kind()
//                                    + ". File affected: " + event.context() + ".");
//                }
//                key.reset();
//            }
//        }
//    }


    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private DAOFactory daoFactory;

    public WatcherService(Path dir) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();

        this.daoFactory = DAOFactory.getInstance();

        walkAndRegisterDirectories(dir);
    }

    private void walkAndRegisterDirectories(final Path start) throws IOException {
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

        Channel channel = new Channel();
        channel.setPath(dir.toAbsolutePath().toString());
        channel.setName(dir.getFileName().toString());
        daoFactory.getChannelsDAO().create(channel);
    }

    void processEvents() {
        for (; ; ) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                @SuppressWarnings("rawtypes")
                WatchEvent.Kind kind = event.kind();

                // Context for directory entry event is the file name of entry
                @SuppressWarnings("unchecked")
                Path name = ((WatchEvent<Path>) event).context();
                Path child = dir.resolve(name);

                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);

                // if directory is created, and watching recursively, then register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child)) {
                            walkAndRegisterDirectories(child);
                        }
                    } catch (IOException x) {
                        // do something useful
                    }
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
}
