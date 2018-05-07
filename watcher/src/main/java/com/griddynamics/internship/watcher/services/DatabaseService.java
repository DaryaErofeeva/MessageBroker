package com.griddynamics.internship.watcher.services;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.Change;
import com.griddynamics.internship.models.Channel;
import com.griddynamics.internship.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;

@Service
public class DatabaseService {
    @Autowired
    private DAOFactory daoFactory;

    public void insert(Path path) {
        try {
            daoFactory.getChangesDAO().createEntityIfNotExists(new Change(
                    daoFactory.getMessagesDAO().createEntityIfNotExists(
                            new Message(
                                    daoFactory.getChannelsDAO().createEntityIfNotExists(
                                            new Channel(
                                                    path.getParent().toAbsolutePath().toString(),
                                                    path.getParent().getFileName().toString())),
                                    path.getFileName().toString())),
                    new String(Files.readAllBytes(path)),
                    new Timestamp(System.currentTimeMillis())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
