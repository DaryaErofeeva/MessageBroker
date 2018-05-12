package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.db.TopicMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TopicMessageMapper implements RowMapper<TopicMessage> {

    @Autowired
    private DAOFactory daoFactory;

    @Override
    public TopicMessage mapRow(ResultSet resultSet, int i) throws SQLException {
        TopicMessage topicMessage = new TopicMessage();
        topicMessage.setId(resultSet.getInt(1));
        topicMessage.setTopicConsumer(daoFactory.getTopicConsumerDAO().getEntityById(resultSet.getInt(2)));
        topicMessage.setMessage(daoFactory.getMessageDAO().getEntityById(resultSet.getInt(3)));
        topicMessage.setState(resultSet.getString(4));
        topicMessage.setTimestamp(resultSet.getTimestamp(5));
        return topicMessage;
    }
}
