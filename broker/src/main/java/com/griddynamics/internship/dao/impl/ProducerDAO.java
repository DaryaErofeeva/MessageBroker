package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.ProducerMapper;
import com.griddynamics.internship.models.db.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProducerDAO implements GenericDAO<Producer, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProducerMapper producerMapper;

    @Override
    public List<Producer> getAll() {
        return jdbcTemplate.query("SELECT * FROM PRODUCERS", producerMapper);
    }

    @Override
    public int update(Producer entity) {
        return jdbcTemplate.update("UPDATE PRODUCERS SET NAME = ? WHERE ID = ?",
                entity.getName(), entity.getId());
    }

    @Override
    public Producer getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM PRODUCERS WHERE ID = ?",
                new Object[]{id}, producerMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM PRODUCERS WHERE ID = ?", id);
    }

    @Override
    public int create(Producer entity) {

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO PRODUCERS (NAME) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            return statement;
        }, holder);
        return holder.getKey().intValue();

//        return jdbcTemplate.update("INSERT INTO PRODUCERS (NAME) VALUES (?)",
//                entity.getName());
    }
}
