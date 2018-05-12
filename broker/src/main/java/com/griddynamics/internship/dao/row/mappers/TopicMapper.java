package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.db.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TopicMapper implements RowMapper<Topic> {
    @Autowired
    private DAOFactory daoFactory;

    @Override
    public Topic mapRow(ResultSet resultSet, int i) throws SQLException {
        Topic topic = new Topic();
        topic.setId(resultSet.getInt(1));
        topic.setProducer(daoFactory.getProducerDAO().getEntityById(resultSet.getInt(2)));
        topic.setName(resultSet.getString(3));
        return topic;
    }
}
