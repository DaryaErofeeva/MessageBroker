package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.db.QueueMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class QueueMessageMapper implements RowMapper<QueueMessage> {

    @Autowired
    private DAOFactory daoFactory;

    @Override
    public QueueMessage mapRow(ResultSet resultSet, int i) throws SQLException {
        QueueMessage queueMessage = new QueueMessage();
        queueMessage.setId(resultSet.getInt(1));
        queueMessage.setQueue(daoFactory.getQueueDAO().getEntityById(resultSet.getInt(2)));
        queueMessage.setMessage(daoFactory.getMessageDAO().getEntityById(resultSet.getInt(3)));
        queueMessage.setState(resultSet.getString(4));
        queueMessage.setTimestamp(resultSet.getTimestamp(5));
        return queueMessage;
    }
}
