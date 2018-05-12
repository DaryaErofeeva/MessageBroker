package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.ConsumerMapper;
import com.griddynamics.internship.models.entities.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ConsumerDAO implements GenericDAO<Consumer, Integer> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConsumerMapper consumerMapper;

    @Override
    public List<Consumer> getAll() {
        return jdbcTemplate.query("SELECT * FROM CONSUMERS", consumerMapper);
    }

    @Override
    public int update(Consumer entity) {
        return jdbcTemplate.update("UPDATE CONSUMERS SET NAME = ? WHERE ID = ?",
                entity.getName(), entity.getId());
    }

    @Override
    public Consumer getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM CONSUMERS WHERE ID = ?",
                new Object[]{id}, consumerMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM CONSUMERS WHERE ID = ?", id);
    }

    @Override
    public int create(Consumer entity) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO CONSUMERS (NAME) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            return statement;
        }, holder);
        entity.setId(holder.getKey().intValue());
        return holder.getKey().intValue();
    }
}
