package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.models.entities.Consumer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ConsumerMapper implements RowMapper<Consumer> {

    @Override
    public Consumer mapRow(ResultSet resultSet, int i) throws SQLException {
        Consumer consumer = new Consumer();
        consumer.setId(resultSet.getInt(1));
        consumer.setName(resultSet.getString(2));
        return consumer;
    }
}
