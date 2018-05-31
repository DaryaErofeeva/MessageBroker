package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.SourceDAO;
import com.griddynamics.internship.dao.impl.result.set.extractors.QueueExtractor;
import com.griddynamics.internship.dao.row.mappers.MessageMapper;
import com.griddynamics.internship.models.Consumer;
import com.griddynamics.internship.models.Message;
import com.griddynamics.internship.models.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class QueueDAO implements SourceDAO<Queue> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueueExtractor queueExtractor;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Queue> getAll() {
        return jdbcTemplate.query("SELECT QUEUES.ID, QUEUES.NAME," +
                " CONSUMERS.ID, CONSUMERS.HOST, CONSUMERS.PORT, " +
                " QUEUES_MESSAGES.ID, QUEUES_MESSAGES.CONTENT, QUEUES_MESSAGES.STATE, QUEUES_MESSAGES.TIMESTAMP " +
                "FROM QUEUES " +
                "  LEFT JOIN QUEUES_CONSUMERS ON QUEUES.ID = QUEUES_CONSUMERS.QUEUE_ID " +
                "  LEFT JOIN CONSUMERS ON QUEUES_CONSUMERS.CONSUMER_ID = CONSUMERS.ID " +
                "  LEFT JOIN QUEUES_MESSAGES on QUEUES.ID = QUEUES_MESSAGES.QUEUE_ID", queueExtractor);
    }

    @Override
    public int update(Queue entity) {
        return jdbcTemplate.update("UPDATE QUEUES SET NAME = ? WHERE ID = ?",
                entity.getName(), entity.getId());
    }

    @Override
    public Queue getEntityById(Integer id) {
        return jdbcTemplate.query("SELECT QUEUES.ID, QUEUES.NAME," +
                        " CONSUMERS.ID, CONSUMERS.HOST, CONSUMERS.PORT, " +
                        " QUEUES_MESSAGES.ID,  QUEUES_MESSAGES.CONTENT, QUEUES_MESSAGES.STATE, QUEUES_MESSAGES.TIMESTAMP " +
                        "FROM QUEUES " +
                        "  LEFT JOIN QUEUES_CONSUMERS ON QUEUES.ID = QUEUES_CONSUMERS.QUEUE_ID " +
                        "  LEFT JOIN CONSUMERS ON QUEUES_CONSUMERS.CONSUMER_ID = CONSUMERS.ID " +
                        "  LEFT JOIN QUEUES_MESSAGES on QUEUES.ID = QUEUES_MESSAGES.QUEUE_ID " +
                        "WHERE QUEUES.ID = ?",
                new Object[]{id}, queueExtractor).get(0);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM QUEUES WHERE ID = ?", id);
    }

    @Override
    public int create(Queue entity) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO QUEUES (NAME) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            return statement;
        }, holder);
        entity.setId(holder.getKey().intValue());

        entity.getConsumers().stream().forEach(consumer -> addConsumer(entity, consumer));
        entity.getMessages().stream().forEach(message -> createMessage(entity, message));

        return holder.getKey().intValue();
    }

    @Override
    public Queue getEntityByName(String name) {
        return jdbcTemplate.query("SELECT QUEUES.ID, QUEUES.NAME," +
                        " CONSUMERS.ID, CONSUMERS.HOST, CONSUMERS.PORT, " +
                        " QUEUES_MESSAGES.ID,  QUEUES_MESSAGES.CONTENT, QUEUES_MESSAGES.STATE, QUEUES_MESSAGES.TIMESTAMP " +
                        "FROM QUEUES " +
                        "  LEFT JOIN QUEUES_CONSUMERS ON QUEUES.ID = QUEUES_CONSUMERS.QUEUE_ID " +
                        "  LEFT JOIN CONSUMERS ON QUEUES_CONSUMERS.CONSUMER_ID = CONSUMERS.ID " +
                        "  LEFT JOIN QUEUES_MESSAGES on QUEUES.ID = QUEUES_MESSAGES.QUEUE_ID " +
                        "WHERE QUEUES.NAME = ?",
                new Object[]{name}, queueExtractor).get(0);
    }

    @Override
    public void merge(String name) {
        jdbcTemplate.update("MERGE INTO QUEUES (NAME) KEY (NAME) VALUES (?)", name);
    }

    @Override
    public int createMessage(Queue entity, Message message) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO QUEUES_MESSAGES (QUEUE_ID, CONTENT, STATE, TIMESTAMP) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getId());
            statement.setString(2, message.getContent());
            statement.setString(3, message.getState());
            statement.setTimestamp(4, message.getTimestamp());
            return statement;
        }, holder);
        message.setId(holder.getKey().intValue());
        return holder.getKey().intValue();
    }

    @Override
    public int updateMessageState(Message message) {
        return jdbcTemplate.update("UPDATE QUEUES_MESSAGES SET STATE = ? WHERE ID = ?",
                message.getState(), message.getId());
    }

    @Override
    public int addConsumer(Queue entity, Consumer consumer) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO QUEUES_CONSUMERS (QUEUE_ID, CONSUMER_ID) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getId());
            statement.setInt(2, consumer.getId());
            return statement;
        }, holder);
        return holder.getKey().intValue();
    }

    @Override
    public Message getMessageByIdAndEntityName(String entityName, int messageId) {
        return jdbcTemplate.queryForObject("SELECT * FROM QUEUES_MESSAGES " +
                        "WHERE QUEUE_ID = (SELECT ID FROM QUEUES WHERE NAME = ?) AND ID = ?",
                new Object[]{entityName, messageId}, messageMapper);
    }

    public List<Queue> getAllFailed() {
        return jdbcTemplate.query("SELECT QUEUES.ID, QUEUES.NAME," +
                " CONSUMERS.ID, CONSUMERS.HOST, CONSUMERS.PORT, " +
                " QUEUES_MESSAGES.ID, QUEUES_MESSAGES.CONTENT, QUEUES_MESSAGES.STATE, QUEUES_MESSAGES.TIMESTAMP " +
                "FROM QUEUES " +
                "  LEFT JOIN QUEUES_CONSUMERS ON QUEUES.ID = QUEUES_CONSUMERS.QUEUE_ID " +
                "  LEFT JOIN CONSUMERS ON QUEUES_CONSUMERS.CONSUMER_ID = CONSUMERS.ID " +
                "  LEFT JOIN QUEUES_MESSAGES on QUEUES.ID = QUEUES_MESSAGES.QUEUE_ID" +
                " WHERE QUEUES_MESSAGES.STATE = 'failed'" +
                "AND TIMESTAMPDIFF(HOUR, QUEUES_MESSAGES.TIMESTAMP, TIMESTAMPADD(HOUR, 3, NOW())) < 5", queueExtractor);
    }
}
