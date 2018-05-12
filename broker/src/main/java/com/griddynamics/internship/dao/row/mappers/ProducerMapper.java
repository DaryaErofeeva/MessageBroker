package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.models.db.Producer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProducerMapper implements RowMapper<Producer> {
    @Override
    public Producer mapRow(ResultSet resultSet, int i) throws SQLException {
        Producer producer = new Producer();
        producer.setId(resultSet.getInt(1));
        producer.setName(resultSet.getString(2));
        return producer;
    }
}
