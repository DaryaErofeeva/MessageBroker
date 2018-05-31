package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.TopicConsumerMessageMapper;
import com.griddynamics.internship.models.SourceConsumerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TopicConsumerMessageDAO implements GenericDAO<SourceConsumerMessage, Integer> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TopicConsumerMessageMapper topicConsumerMessageMapper;

    @Override
    public List<SourceConsumerMessage> getAll() {
        return jdbcTemplate.query("SELECT TOPICS_CONSUMERS_MESSAGES.ID, TOPICS_CONSUMERS_MESSAGES.STATE, TOPICS_CONSUMERS_MESSAGES.TIMESTAMP," +
                "  TOPICS.ID," +
                "  CONSUMERS.ID," +
                "  TOPICS_MESSAGES.ID, TOPICS_MESSAGES.CONTENT, TOPICS_MESSAGES.STATE, TOPICS_MESSAGES.TIMESTAMP" +
                " FROM TOPICS_CONSUMERS_MESSAGES" +
                "  JOIN TOPICS_CONSUMERS ON TOPICS_CONSUMERS_MESSAGES.TOPIC_CONSUMER_ID = TOPICS_CONSUMERS.ID" +
                "  JOIN TOPICS ON TOPICS_CONSUMERS.TOPIC_ID = TOPICS.ID" +
                "  JOIN CONSUMERS ON TOPICS_CONSUMERS.CONSUMER_ID = CONSUMERS.ID" +
                "  JOIN TOPICS_MESSAGES on TOPICS.ID = TOPICS_MESSAGES.TOPIC_ID", topicConsumerMessageMapper);
    }

    @Override
    public int update(SourceConsumerMessage entity) {
        return jdbcTemplate.update("UPDATE TOPICS_CONSUMERS_MESSAGES SET " +
                        "TOPIC_CONSUMER_ID = (SELECT ID FROM TOPICS_CONSUMERS WHERE TOPIC_ID = ? AND CONSUMER_ID = ?)" +
                        ", TOPIC_MESSAGE_ID = ?, STATE = ?, TIMESTAMP = ? WHERE ID = ?",
                entity.getSource().getId(), entity.getConsumer().getId(),
                entity.getMessage().getId(), entity.getState(), entity.getTimestamp(), entity.getId());
    }

    @Override
    public SourceConsumerMessage getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT TOPICS_CONSUMERS_MESSAGES.ID, TOPICS_CONSUMERS_MESSAGES.STATE, TOPICS_CONSUMERS_MESSAGES.TIMESTAMP," +
                        "  TOPICS.ID," +
                        "  CONSUMERS.ID," +
                        "  TOPICS_MESSAGES.ID, TOPICS_MESSAGES.CONTENT, TOPICS_MESSAGES.STATE, TOPICS_MESSAGES.TIMESTAMP" +
                        " FROM TOPICS_CONSUMERS_MESSAGES" +
                        "  JOIN TOPICS_CONSUMERS ON TOPICS_CONSUMERS_MESSAGES.TOPIC_CONSUMER_ID = TOPICS_CONSUMERS.ID" +
                        "  JOIN TOPICS ON TOPICS_CONSUMERS.TOPIC_ID = TOPICS.ID" +
                        "  JOIN CONSUMERS ON TOPICS_CONSUMERS.CONSUMER_ID = CONSUMERS.ID" +
                        "  JOIN TOPICS_MESSAGES on TOPICS.ID = TOPICS_MESSAGES.TOPIC_ID" +
                        " WHERE TOPICS_CONSUMERS_MESSAGES.ID = ?",
                new Object[]{id}, topicConsumerMessageMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM TOPICS_CONSUMERS_MESSAGES WHERE ID = ?", id);
    }

    @Override
    public int create(SourceConsumerMessage entity) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO TOPICS_CONSUMERS_MESSAGES (TOPIC_CONSUMER_ID, TOPIC_MESSAGE_ID, STATE, TIMESTAMP) " +
                    "VALUES ((SELECT ID FROM TOPICS_CONSUMERS WHERE TOPIC_ID = ? AND CONSUMER_ID = ?),?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getSource().getId());
            statement.setInt(2, entity.getConsumer().getId());
            statement.setInt(3, entity.getMessage().getId());
            statement.setString(4, entity.getState());
            statement.setTimestamp(5, entity.getTimestamp());
            return statement;
        }, holder);
        entity.setId(holder.getKey().intValue());
        return holder.getKey().intValue();
    }

    public List<SourceConsumerMessage> getFailed() {
        return jdbcTemplate.query("SELECT TOPICS_CONSUMERS_MESSAGES.ID, TOPICS_CONSUMERS_MESSAGES.STATE, TOPICS_CONSUMERS_MESSAGES.TIMESTAMP," +
                "  TOPICS.ID," +
                "  CONSUMERS.ID," +
                "  TOPICS_MESSAGES.ID, TOPICS_MESSAGES.CONTENT, TOPICS_MESSAGES.STATE, TOPICS_MESSAGES.TIMESTAMP" +
                " FROM TOPICS_CONSUMERS_MESSAGES" +
                "  JOIN TOPICS_CONSUMERS ON TOPICS_CONSUMERS_MESSAGES.TOPIC_CONSUMER_ID = TOPICS_CONSUMERS.ID" +
                "  JOIN TOPICS ON TOPICS_CONSUMERS.TOPIC_ID = TOPICS.ID" +
                "  JOIN CONSUMERS ON TOPICS_CONSUMERS.CONSUMER_ID = CONSUMERS.ID" +
                "  JOIN TOPICS_MESSAGES on TOPICS.ID = TOPICS_MESSAGES.TOPIC_ID" +
                " WHERE TOPICS_CONSUMERS_MESSAGES.STATE = 'failed'" +
                "AND TIMESTAMPDIFF(HOUR, TOPICS_CONSUMERS_MESSAGES.TIMESTAMP, TIMESTAMPADD(HOUR, 3, NOW())) < 5", topicConsumerMessageMapper);
    }
}
