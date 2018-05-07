package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.ChannelMapper;
import com.griddynamics.internship.models.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChannelsDAO implements GenericDAO<Channel, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public List<Channel> getAll() {
        return jdbcTemplate.query("SELECT * FROM CHANNELS", channelMapper);
    }

    @Override
    public int update(Channel entity) {
        return jdbcTemplate.update("UPDATE CHANNELS SET PATH = ?, NAME = ? WHERE ID = ?",
                entity.getPath(), entity.getName(), entity.getId());
    }

    @Override
    public Channel getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM CHANNELS WHERE ID = ?",
                new Object[]{id}, channelMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM CHANNELS WHERE ID = ?", id);
    }

    @Override
    public int create(Channel entity) {
        return jdbcTemplate.update("INSERT INTO CHANNELS (PATH, NAME) VALUES (?, ?)",
                entity.getPath(), entity.getName());
    }

    @Override
    public Integer getIdByEntity(Channel entity) {
        try {
            return jdbcTemplate.queryForObject("SELECT ID FROM CHANNELS WHERE PATH = ? AND NAME = ?",
                    new Object[]{entity.getPath(), entity.getName()}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public Channel createEntityIfNotExists(Channel entity) {
        if (getIdByEntity(entity) == 0) create(entity);
        entity.setId(getIdByEntity(entity));
        return entity;
    }
}
