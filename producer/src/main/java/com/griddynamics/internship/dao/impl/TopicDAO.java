package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.SourceDAO;
import com.griddynamics.internship.dao.impl.result.set.extractors.TopicExtractor;
import com.griddynamics.internship.dao.row.mappers.MessageMapper;
import com.griddynamics.internship.models.entities.Consumer;
import com.griddynamics.internship.models.entities.Message;
import com.griddynamics.internship.models.entities.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TopicDAO implements SourceDAO<Topic> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TopicExtractor topicExtractor;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Topic> getAll() {
        return jdbcTemplate.query("SELECT TOPICS.ID, TOPICS.NAME," +
                " CONSUMERS.ID, CONSUMERS.HOST, CONSUMERS.PORT, " +
                " TOPICS_MESSAGES.ID,  TOPICS_MESSAGES.CONTENT, TOPICS_MESSAGES.STATE, TOPICS_MESSAGES.TIMESTAMP " +
                "FROM TOPICS " +
                "  LEFT JOIN TOPICS_CONSUMERS ON TOPICS.ID = TOPICS_CONSUMERS.TOPIC_ID " +
                "  LEFT JOIN CONSUMERS ON TOPICS_CONSUMERS.CONSUMER_ID = CONSUMERS.ID " +
                "  LEFT JOIN TOPICS_MESSAGES on TOPICS.ID = TOPICS_MESSAGES.TOPIC_ID", topicExtractor);
    }

    @Override
    public int update(Topic entity) {
        return jdbcTemplate.update("UPDATE TOPICS SET NAME = ? WHERE ID = ?",
                entity.getName(), entity.getId());
    }

    @Override
    public Topic getEntityById(Integer id) {
        return jdbcTemplate.query("SELECT TOPICS.ID, TOPICS.NAME," +
                        " CONSUMERS.ID, CONSUMERS.HOST, CONSUMERS.PORT, " +
                        " TOPICS_MESSAGES.ID,  TOPICS_MESSAGES.CONTENT, TOPICS_MESSAGES.STATE, TOPICS_MESSAGES.TIMESTAMP " +
                        "FROM TOPICS " +
                        "  LEFT JOIN TOPICS_CONSUMERS ON TOPICS.ID = TOPICS_CONSUMERS.TOPIC_ID " +
                        "  LEFT JOIN CONSUMERS ON TOPICS_CONSUMERS.CONSUMER_ID = CONSUMERS.ID " +
                        "  LEFT JOIN TOPICS_MESSAGES on TOPICS.ID = TOPICS_MESSAGES.TOPIC_ID " +
                        "WHERE TOPICS.ID = ?",
                new Object[]{id}, topicExtractor).get(0);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM TOPICS WHERE ID = ?", id);
    }

    @Override
    public int create(Topic entity) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO TOPICS (NAME) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            return statement;
        }, holder);
        entity.setId(holder.getKey().intValue());

        entity.getConsumers().stream().forEach(consumer -> addConsumer(entity, consumer));
        entity.getMessages().stream().forEach(message -> createMessage(entity, message));

        return holder.getKey().intValue();
    }

    @Override
    public Topic getEntityByName(String name) {
        return jdbcTemplate.query("SELECT TOPICS.ID, TOPICS.NAME," +
                        " CONSUMERS.ID, CONSUMERS.HOST, CONSUMERS.PORT, " +
                        " TOPICS_MESSAGES.ID,  TOPICS_MESSAGES.CONTENT, TOPICS_MESSAGES.STATE, TOPICS_MESSAGES.TIMESTAMP " +
                        "FROM TOPICS " +
                        "  LEFT JOIN TOPICS_CONSUMERS ON TOPICS.ID = TOPICS_CONSUMERS.TOPIC_ID " +
                        "  LEFT JOIN CONSUMERS ON TOPICS_CONSUMERS.CONSUMER_ID = CONSUMERS.ID " +
                        "  LEFT JOIN TOPICS_MESSAGES on TOPICS.ID = TOPICS_MESSAGES.TOPIC_ID " +
                        "WHERE TOPICS.NAME = ?",
                new Object[]{name}, topicExtractor).get(0);
    }

    @Override
    public int createMessage(Topic entity, Message message) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO TOPICS_MESSAGES (TOPIC_ID, CONTENT, STATE, TIMESTAMP) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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
        return jdbcTemplate.update("UPDATE TOPICS_MESSAGES SET STATE = ? WHERE ID = ?",
                message.getState(), message.getId());
    }

    @Override
    public int addConsumer(Topic entity, Consumer consumer) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO TOPICS_CONSUMERS (TOPIC_ID, CONSUMER_ID) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getId());
            statement.setInt(2, consumer.getId());
            return statement;
        }, holder);
        return holder.getKey().intValue();
    }

    @Override
    public Message getMessageByIdAndEntityName(String entityName, int messageId) {
        return jdbcTemplate.queryForObject("SELECT * FROM TOPICS_MESSAGES " +
                        "WHERE TOPIC_ID = (SELECT ID FROM TOPICS WHERE NAME = ?) AND ID = ?",
                new Object[]{entityName, messageId}, messageMapper);
    }

    public List<Topic> getAllFailed(){
        return jdbcTemplate.query("SELECT TOPICS.ID, TOPICS.NAME," +
                " CONSUMERS.ID, CONSUMERS.HOST, CONSUMERS.PORT, " +
                " TOPICS_MESSAGES.ID, TOPICS_MESSAGES.CONTENT, TOPICS_MESSAGES.STATE, TOPICS_MESSAGES.TIMESTAMP " +
                "FROM TOPICS " +
                "  LEFT JOIN TOPICS_CONSUMERS ON TOPICS.ID = TOPICS_CONSUMERS.TOPIC_ID " +
                "  LEFT JOIN CONSUMERS ON TOPICS_CONSUMERS.CONSUMER_ID = CONSUMERS.ID " +
                "  LEFT JOIN TOPICS_MESSAGES on TOPICS.ID = TOPICS_MESSAGES.TOPIC_ID" +
                " WHERE TOPICS_MESSAGES.STATE = 'failed'"+
                "AND TIMESTAMPDIFF(HOUR, TOPICS_MESSAGES.TIMESTAMP, TIMESTAMPADD(HOUR, 3, NOW())) < 5", topicExtractor);
    }
}
