package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.QueueMessageMapper;
import com.griddynamics.internship.models.db.QueueMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class QueueMessageDAO implements GenericDAO<QueueMessage, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueueMessageMapper queueMessageMapper;

    @Override
    public List<QueueMessage> getAll() {
        return jdbcTemplate.query("SELECT * FROM QUEUE_MESSAGES", queueMessageMapper);
    }

    @Override
    public int update(QueueMessage entity) {
        return jdbcTemplate.update("UPDATE QUEUE_MESSAGES SET QUEUE_ID = ?, MESSAGE_ID = ?, STATE = ?, TIMESTAMP = ? WHERE ID = ?",
                entity.getQueue().getId(), entity.getMessage().getId(), entity.getState(), entity.getTimestamp(), entity.getId());
    }

    @Override
    public QueueMessage getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM QUEUE_MESSAGES WHERE ID = ?",
                new Object[]{id}, queueMessageMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM QUEUE_MESSAGES WHERE ID = ?", id);
    }

    @Override
    public int create(QueueMessage entity) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO QUEUE_MESSAGES (QUEUE_ID, MESSAGE_ID, STATE, TIMESTAMP) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getQueue().getId());
            statement.setInt(2, entity.getMessage().getId());
            statement.setString(3, entity.getState());
            statement.setTimestamp(4, entity.getTimestamp());
            return statement;
        }, holder);
        return holder.getKey().intValue();
    }
}
