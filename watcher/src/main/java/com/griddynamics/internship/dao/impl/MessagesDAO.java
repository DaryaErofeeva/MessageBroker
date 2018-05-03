package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@DependsOn("source")
public class MessagesDAO implements GenericDAO<Message, Integer> {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MessagesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Message> getAll() {
        return jdbcTemplate.query("SELECT * FROM MESSAGES", new BeanPropertyRowMapper(Message.class));
    }

    @Override
    public int update(Message entity) {
        return jdbcTemplate.update("UPDATE MESSAGES SET CHANNEL_ID = ?, NAME = ? WHERE ID = ?",
                entity.getChannelId(), entity.getName(), entity.getId());
    }

    @Override
    public Message getEntityById(Integer id) {
        return (Message) jdbcTemplate.queryForObject("SELECT * FROM MESSAGES WHERE ID = ?",
                new Object[]{id}, new BeanPropertyRowMapper(Message.class));
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM MESSAGES WHERE ID = ?", id);
    }

    @Override
    public int create(Message entity) {
        return jdbcTemplate.update("INSERT INTO MESSAGES (CHANNEL_ID, NAME) VALUES (?, ?)",
                entity.getChannelId(), entity.getName());
    }

    @Override
    public Integer getIdByEntity(Message entity) {
        return jdbcTemplate.queryForObject("SELECT ID FROM MESSAGES WHERE CHANNEL_ID = ? AND NAME = ?",
                new Object[]{entity.getChannelId(), entity.getName()}, Integer.class);
    }

    @Override
    public Message createEntityIfNotExists(Message entity) {
        if (getIdByEntity(entity) == 0) create(entity);
        entity.setId(getIdByEntity(entity));
        return entity;
    }
}
