package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.MessageMapper;
import com.griddynamics.internship.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessagesDAO implements GenericDAO<Message, Integer> {

    @Autowired
    private DAOFactory daoFactory;

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
        daoFactory.getChannelsDAO().update(entity.getChannel());
        return jdbcTemplate.update("UPDATE MESSAGES SET CHANNEL_ID = ?, NAME = ? WHERE ID = ?",
                entity.getChannel().getId(), entity.getName(), entity.getId());
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
        return jdbcTemplate.update("INSERT INTO MESSAGES (CHANNEL_ID, NAME) VALUES (?, ?)",
                entity.getChannel().getId(), entity.getName());
    }

    @Override
    public Integer getIdByEntity(Message entity) {
        try {
            return jdbcTemplate.queryForObject("SELECT ID FROM MESSAGES WHERE CHANNEL_ID = ? AND NAME = ?",
                    new Object[]{entity.getChannel().getId(), entity.getName()}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public Message createEntityIfNotExists(Message entity) {
        if (getIdByEntity(entity) == 0) create(entity);
        entity.setId(getIdByEntity(entity));
        return entity;
    }
}
