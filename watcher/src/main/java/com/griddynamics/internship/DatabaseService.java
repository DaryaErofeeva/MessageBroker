package com.griddynamics.internship;

import com.griddynamics.internship.dao.impl.ChangesDAO;
import com.griddynamics.internship.dao.impl.ChannelsDAO;
import com.griddynamics.internship.dao.impl.MessagesDAO;
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
    private ChannelsDAO channelsDAO;

    @Autowired
    private MessagesDAO messagesDAO;

    @Autowired
    private ChangesDAO changesDAO;

    public void insert(Path path) {
        try {
            changesDAO.createEntityIfNotExists(new Change(
                    messagesDAO.createEntityIfNotExists(
                            new Message(
                                    channelsDAO.createEntityIfNotExists(
                                            new Channel(
                                                    path.getParent().toAbsolutePath().toString(),
                                                    path.getParent().getFileName().toString())).getId(),
                                    path.getFileName().toString())).getId(),
                    new String(Files.readAllBytes(path)),
                    new Timestamp(System.currentTimeMillis())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
