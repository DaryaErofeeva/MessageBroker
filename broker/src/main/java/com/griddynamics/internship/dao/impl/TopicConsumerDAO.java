package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.TopicConsumerMapper;
import com.griddynamics.internship.models.db.TopicConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TopicConsumerDAO implements GenericDAO<TopicConsumer, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TopicConsumerMapper topicConsumerMapper;

    @Override
    public List<TopicConsumer> getAll() {
        return jdbcTemplate.query("SELECT * FROM TOPICS_CONSUMERS",
                topicConsumerMapper);
    }

    @Override
    public int update(TopicConsumer entity) {
        return jdbcTemplate.update("UPDATE TOPICS_CONSUMERS SET TOPIC_ID = ?, CONSUMER_ID = ? WHERE ID = ?",
                entity.getTopic().getId(), entity.getConsumer().getId(), entity.getId());
    }

    @Override
    public TopicConsumer getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM TOPICS_CONSUMERS WHERE ID = ?",
                new Object[]{id}, topicConsumerMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM TOPICS_CONSUMERS WHERE ID = ?", id);
    }

    @Override
    public int create(TopicConsumer entity) {

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO TOPICS_CONSUMERS (TOPIC_ID, CONSUMER_ID) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getTopic().getId());
            statement.setInt(2, entity.getConsumer().getId());
            return statement;
        }, holder);
        return holder.getKey().intValue();

//        return jdbcTemplate.update("INSERT INTO TOPICS_CONSUMERS (TOPIC_ID, CONSUMER_ID) VALUES (?, ?)",
//                entity.getTopic().getId(), entity.getConsumer().getId());
    }

    public int getIdByEntity(TopicConsumer entity) {
        try {
            Integer id = jdbcTemplate.queryForObject("SELECT ID FROM TOPICS_CONSUMERS WHERE TOPIC_ID = ? AND CONSUMER_ID = ?",
                    new Object[]{entity.getTopic().getId(), entity.getConsumer().getId()}, Integer.class);
            return id;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }
}
