package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.models.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConsumerDAO implements GenericDAO<Consumer, Integer> {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Consumer> getAll() {
        return null;
    }

    @Override
    public int update(Consumer entity) {
        return 0;
    }

    @Override
    public Consumer getEntityById(Integer id) {
        return null;
    }

    @Override
    public int delete(Integer id) {
        return 0;
    }

    @Override
    public int create(Consumer entity) {
        return 0;
    }

    @Override
    public Integer getIdByEntity(Consumer entity) {
        return null;
    }

    @Override
    public Consumer createEntityIfNotExists(Consumer entity) {
        return null;
    }
}
