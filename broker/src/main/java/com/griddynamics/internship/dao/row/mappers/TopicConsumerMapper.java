package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.db.TopicConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TopicConsumerMapper implements RowMapper<TopicConsumer> {

    @Autowired
    private DAOFactory daoFactory;

    @Override
    public TopicConsumer mapRow(ResultSet resultSet, int i) throws SQLException {
        TopicConsumer topicConsumer = new TopicConsumer();
        topicConsumer.setId(resultSet.getInt(1));
        topicConsumer.setTopic(daoFactory.getTopicDAO().getEntityById(resultSet.getInt(2)));
        topicConsumer.setConsumer(daoFactory.getConsumerDAO().getEntityById(resultSet.getInt(3)));
        return topicConsumer;
    }
}
