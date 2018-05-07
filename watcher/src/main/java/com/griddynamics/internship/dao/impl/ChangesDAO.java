package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.ChangeMapper;
import com.griddynamics.internship.models.Change;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChangesDAO implements GenericDAO<Change, Integer> {

    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ChangeMapper changeMapper;

    @Override
    public List<Change> getAll() {
        return jdbcTemplate.query("SELECT * FROM CHANGES", changeMapper);
    }

    @Override
    public int update(Change entity) {
        daoFactory.getMessagesDAO().update(entity.getMessage());
        return jdbcTemplate.update("UPDATE CHANGES SET MESSAGE_ID = ?, CONTENT = ?, TIMESTAMP = ? WHERE ID = ?",
                entity.getMessage().getId(), entity.getContent(), entity.getTimestamp(), entity.getId());
    }

    @Override
    public Change getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM CHANGES WHERE ID = ?",
                new Object[]{id}, changeMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM CHANGES WHERE ID = ?", id);
    }

    @Override
    public int create(Change entity) {
        return jdbcTemplate.update("INSERT INTO CHANGES (MESSAGE_ID, CONTENT, TIMESTAMP) VALUES (?, ?, ?)",
                entity.getMessage().getId(), entity.getContent(), entity.getTimestamp());
    }

    @Override
    public Integer getIdByEntity(Change entity) {
        try {
            return jdbcTemplate.queryForObject("SELECT ID FROM CHANGES WHERE MESSAGE_ID = ? AND TIMESTAMP = ?",
                    new Object[]{entity.getMessage().getId(), entity.getTimestamp()}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public Change createEntityIfNotExists(Change entity) {
        if (getIdByEntity(entity) == 0) create(entity);
        entity.setId(getIdByEntity(entity));
        return entity;
    }
}
