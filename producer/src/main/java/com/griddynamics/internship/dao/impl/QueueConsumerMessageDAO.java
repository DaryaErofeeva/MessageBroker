package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.QueueConsumerMessageMapper;
import com.griddynamics.internship.models.entities.Consumer;
import com.griddynamics.internship.models.entities.SourceConsumerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class QueueConsumerMessageDAO implements GenericDAO<SourceConsumerMessage, Integer> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueueConsumerMessageMapper queueConsumerMessageMapper;

    @Override
    public List<SourceConsumerMessage> getAll() {
        return jdbcTemplate.query("SELECT QUEUES_CONSUMERS_MESSAGES.ID, QUEUES_CONSUMERS_MESSAGES.STATE, QUEUES_CONSUMERS_MESSAGES.TIMESTAMP," +
                "  QUEUES.ID," +
                "  CONSUMERS.ID," +
                "  QUEUES_MESSAGES.ID, QUEUES_MESSAGES.CONTENT, QUEUES_MESSAGES.STATE, QUEUES_MESSAGES.TIMESTAMP" +
                " FROM QUEUES_CONSUMERS_MESSAGES" +
                "  JOIN QUEUES_CONSUMERS ON QUEUES_CONSUMERS_MESSAGES.QUEUE_CONSUMER_ID = QUEUES_CONSUMERS.ID" +
                "  JOIN QUEUES ON QUEUES_CONSUMERS.QUEUE_ID = QUEUES.ID" +
                "  JOIN CONSUMERS ON QUEUES_CONSUMERS.CONSUMER_ID = CONSUMERS.ID" +
                "  JOIN QUEUES_MESSAGES on QUEUES.ID = QUEUES_MESSAGES.QUEUE_ID", queueConsumerMessageMapper);
    }

    @Override
    public int update(SourceConsumerMessage entity) {
        return jdbcTemplate.update("UPDATE QUEUES_CONSUMERS_MESSAGES SET " +
                        "QUEUE_CONSUMER_ID = (SELECT ID FROM QUEUES_CONSUMERS WHERE QUEUE_ID = ? AND CONSUMER_ID = ?)" +
                        ", QUEUE_MESSAGE_ID = ?, STATE = ?, TIMESTAMP = ? WHERE ID = ?",
                entity.getSource().getId(), entity.getConsumer().getId(),
                entity.getMessage().getId(), entity.getState(), entity.getTimestamp(), entity.getId());
    }

    @Override
    public SourceConsumerMessage getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT QUEUES_CONSUMERS_MESSAGES.ID, QUEUES_CONSUMERS_MESSAGES.STATE, QUEUES_CONSUMERS_MESSAGES.TIMESTAMP," +
                        "  QUEUES.ID," +
                        "  CONSUMERS.ID," +
                        "  QUEUES_MESSAGES.ID, QUEUES_MESSAGES.CONTENT, QUEUES_MESSAGES.STATE, QUEUES_MESSAGES.TIMESTAMP" +
                        " FROM QUEUES_CONSUMERS_MESSAGES" +
                        "  JOIN QUEUES_CONSUMERS ON QUEUES_CONSUMERS_MESSAGES.QUEUE_CONSUMER_ID = QUEUES_CONSUMERS.ID" +
                        "  JOIN QUEUES ON QUEUES_CONSUMERS.QUEUE_ID = QUEUES.ID" +
                        "  JOIN CONSUMERS ON QUEUES_CONSUMERS.CONSUMER_ID = CONSUMERS.ID" +
                        "  JOIN QUEUES_MESSAGES on QUEUES.ID = QUEUES_MESSAGES.QUEUE_ID" +
                        " WHERE QUEUES_CONSUMERS_MESSAGES.ID = ?",
                new Object[]{id}, queueConsumerMessageMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM QUEUES_CONSUMERS_MESSAGES WHERE ID = ?", id);
    }

    @Override
    public int create(SourceConsumerMessage entity) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO QUEUES_CONSUMERS_MESSAGES (QUEUE_CONSUMER_ID, QUEUE_MESSAGE_ID, STATE, TIMESTAMP) " +
                    "VALUES ((SELECT ID FROM QUEUES_CONSUMERS WHERE QUEUE_ID = ? AND CONSUMER_ID = ?),?,?,?)", Statement.RETURN_GENERATED_KEYS);
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

    public int getLastConsumerId(int queueId) {
        return jdbcTemplate.queryForObject("SELECT CONSUMER_ID " +
                        "FROM QUEUES_CONSUMERS_MESSAGES " +
                        "  JOIN QUEUES_CONSUMERS on QUEUES_CONSUMERS_MESSAGES.QUEUE_CONSUMER_ID = QUEUES_CONSUMERS.ID " +
                        "WHERE QUEUES_CONSUMERS_MESSAGES.ID = (SELECT MAX(ID) FROM QUEUES_CONSUMERS_MESSAGES WHERE QUEUE_ID = ?)",
                new Object[]{queueId}, Integer.class);
    }
}
