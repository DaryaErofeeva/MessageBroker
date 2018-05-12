package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.TopicMessageMapper;
import com.griddynamics.internship.models.db.TopicMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TopicMessageDAO implements GenericDAO<TopicMessage, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TopicMessageMapper topicMessageMapper;

    @Override
    public List<TopicMessage> getAll() {
        return jdbcTemplate.query("SELECT * FROM TOPICS_CONSUMERS_MESSAGES",
                topicMessageMapper);
    }

    @Override
    public int update(TopicMessage entity) {
        return jdbcTemplate.update("UPDATE TOPICS_CONSUMERS_MESSAGES SET TOPIC_CONSUMER_ID = ?, MESSAGE_ID = ?, STATE = ?, TIMESTAMP = ? WHERE ID = ?",
                entity.getTopicConsumer().getId(), entity.getMessage().getId(), entity.getState(), entity.getTimestamp(), entity.getId());
    }

    @Override
    public TopicMessage getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM TOPICS_CONSUMERS_MESSAGES WHERE ID = ?",
                new Object[]{id}, topicMessageMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM TOPICS_CONSUMERS_MESSAGES WHERE ID = ?", id);
    }

    @Override
    public int create(TopicMessage entity) {

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO TOPICS_CONSUMERS_MESSAGES (TOPIC_CONSUMER_ID, MESSAGE_ID, STATE, TIMESTAMP) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getTopicConsumer().getId());
            statement.setInt(2, entity.getMessage().getId());
            statement.setString(3, entity.getState());
            statement.setTimestamp(4, entity.getTimestamp());
            return statement;
        }, holder);
        return holder.getKey().intValue();
    }
}
