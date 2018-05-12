package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.dao.row.mappers.TopicMapper;
import com.griddynamics.internship.models.db.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TopicDAO implements GenericDAO<Topic, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public List<Topic> getAll() {
        return jdbcTemplate.query("SELECT ID, PRODUCER_ID, NAME FROM TOPICS", topicMapper);
    }

    @Override
    public int update(Topic entity) {
        return jdbcTemplate.update("UPDATE TOPICS SET PRODUCER_ID = ?, NAME = ? WHERE ID = ?",
                entity.getProducer().getId(), entity.getName(), entity.getId());
    }

    @Override
    public Topic getEntityById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT ID, PRODUCER_ID, NAME FROM TOPICS " +
                        "WHERE ID = ?",
                new Object[]{id}, topicMapper);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM TOPICS WHERE ID = ?", id);
    }

    @Override
    public int create(Topic entity) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO TOPICS (PRODUCER_ID,NAME) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getProducer().getId());
            statement.setString(2, entity.getName());
            return statement;
        }, holder);
        return holder.getKey().intValue();

//        return jdbcTemplate.update("INSERT INTO TOPICS (PRODUCER_ID,NAME) VALUES (?, ?)",
//                entity.getProducer().getId(), entity.getName());
    }

    public Topic getEntityByName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM TOPICS WHERE NAME = ?",
                new Object[]{name}, topicMapper);
    }
}
