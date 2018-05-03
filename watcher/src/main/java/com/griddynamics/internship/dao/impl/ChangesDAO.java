package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.models.Change;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@DependsOn("source")
public class ChangesDAO implements GenericDAO<Change, Integer> {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ChangesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Change> getAll() {
        return jdbcTemplate.query("SELECT * FROM CHANGES", new BeanPropertyRowMapper(Change.class));
    }

    @Override
    public int update(Change entity) {
        return jdbcTemplate.update("UPDATE CHANGES SET MESSAGE_ID = ?, CONTENT = ?, TIMESTAMP = ? WHERE ID = ?",
                entity.getMessageId(), entity.getContent(), entity.getTimestamp(), entity.getId());
    }

    @Override
    public Change getEntityById(Integer id) {
        return (Change) jdbcTemplate.queryForObject("SELECT * FROM CHANGES WHERE ID = ?",
                new Object[]{id}, new BeanPropertyRowMapper(Change.class));
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM CHANGES WHERE ID = ?", id);
    }

    @Override
    public int create(Change entity) {
        return jdbcTemplate.update("INSERT INTO CHANGES (MESSAGE_ID, CONTENT, TIMESTAMP) VALUES (?, ?, ?)",
                entity.getMessageId(), entity.getContent(), entity.getTimestamp());
    }

    @Override
    public Integer getIdByEntity(Change entity) {
        return jdbcTemplate.queryForObject("SELECT ID FROM CHANGES WHERE MESSAGE_ID = ? AND TIMESTAMP = ?",
                new Object[]{entity.getMessageId(), entity.getTimestamp()}, Integer.class);
    }

    @Override
    public Change createEntityIfNotExists(Change entity) {
        if (getIdByEntity(entity) == 0) create(entity);
        entity.setId(getIdByEntity(entity));
        return entity;
    }
}
