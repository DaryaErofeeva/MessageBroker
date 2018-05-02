package com.griddynamics.internship.dao;

import com.griddynamics.internship.dao.impl.ChangesDAO;
import com.griddynamics.internship.dao.impl.ChannelsDAO;
import com.griddynamics.internship.dao.impl.MessagesDAO;

public class DAOFactory {
    private static ChannelsDAO channelsDAO;
    private static MessagesDAO messagesDAO;
    private static ChangesDAO changesDAO;

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static synchronized DAOFactory getInstance() {
        if (daoFactory == null)
            daoFactory = new DAOFactory();
        return daoFactory;
    }

    public ChannelsDAO getChannelsDAO() {
        if (channelsDAO == null)
            channelsDAO = new ChannelsDAO();
        return channelsDAO;
    }

    public MessagesDAO getMessagesDAO() {
        if (messagesDAO == null)
            messagesDAO = new MessagesDAO();
        return messagesDAO;
    }

    public ChangesDAO getChangesDAO() {
        if (changesDAO == null)
            changesDAO = new ChangesDAO();
        return changesDAO;
    }
}
