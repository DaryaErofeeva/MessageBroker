package com.griddynamics.internship.dao;

import com.griddynamics.internship.dao.impl.ChangesDAO;
import com.griddynamics.internship.dao.impl.ChannelsDAO;
import com.griddynamics.internship.dao.impl.MessagesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DAOFactory {
    @Autowired
    private ChannelsDAO channelsDAO;

    @Autowired
    private MessagesDAO messagesDAO;

    @Autowired
    private ChangesDAO changesDAO;

    public ChannelsDAO getChannelsDAO() {
        return channelsDAO;
    }

    public MessagesDAO getMessagesDAO() {
        return messagesDAO;
    }

    public ChangesDAO getChangesDAO() {
        return changesDAO;
    }
}
