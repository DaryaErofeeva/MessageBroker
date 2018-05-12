package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.MessageMapper;
import com.griddynamics.internship.models.db.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class MessageDAO implements GenericDAO<Message, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> getAll() {
        return jdbcTemplate.query("SELECT * FROM MESSAGES", messageMapper);
    }

    @Override
    public int update(Message entity) {
        return jdbcTemplate.update("UPDATE MESSAGES SET NAME = ?, CONTENT = ?, TIMESTAMP = ? WHERE ID = ?",
                entity.getName(), entity.getContent(), entity.getTimestamp(), entity.getId());
    }

    @Override
    public Message getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM MESSAGES WHERE ID = ?",
                new Object[]{id}, messageMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM MESSAGES WHERE ID = ?", id);
    }

    @Override
    public int create(Message entity) {

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO MESSAGES (NAME, CONTENT, TIMESTAMP) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getContent());
            statement.setTimestamp(3, entity.getTimestamp());
            return statement;
        }, holder);
        return holder.getKey().intValue();

//        return jdbcTemplate.update("INSERT INTO MESSAGES (NAME, CONTENT, TIMESTAMP) VALUES (?, ?, ?)",
//                entity.getName(), entity.getContent(), entity.getTimestamp());
    }
}
