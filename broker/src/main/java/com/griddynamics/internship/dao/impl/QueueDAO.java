package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.QueueMapper;
import com.griddynamics.internship.models.db.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class QueueDAO implements GenericDAO<Queue, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueueMapper queueMapper;

    @Override
    public List<Queue> getAll() {
        return jdbcTemplate.query("SELECT * FROM QUEUES", queueMapper);
    }

    @Override
    public int update(Queue entity) {
        return jdbcTemplate.update("UPDATE QUEUES SET PRODUCER_ID = ?, CONSUMER_ID = ?, NAME = ? WHERE ID = ?",
                entity.getProducer().getId(), entity.getConsumer().getId(), entity.getName(), entity.getId());
    }

    @Override
    public Queue getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM QUEUES WHERE ID = ?",
                new Object[]{id}, queueMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM QUEUES WHERE ID = ?", id);
    }

    @Override
    public int create(Queue entity) {

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO QUEUES (PRODUCER_ID, CONSUMER_ID, NAME) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getProducer().getId());
            statement.setInt(2, entity.getConsumer().getId());
            statement.setString(3, entity.getName());
            return statement;
        }, holder);
        return holder.getKey().intValue();

//        return jdbcTemplate.update("INSERT INTO QUEUES (PRODUCER_ID, CONSUMER_ID, NAME) VALUES (?, ?, ?)",
//                entity.getProducer().getId(), entity.getConsumer().getId(), entity.getName());
    }

    public Queue getEntityByName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM QUEUES WHERE NAME = ?",
                new Object[]{name}, queueMapper);
    }
}
