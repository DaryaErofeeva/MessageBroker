package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.models.entities.Message;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet resultSet, int i) throws SQLException {
        Message message = new Message();
        message.setId(resultSet.getInt(1));
        message.setContent(resultSet.getString(3));
        message.setState(resultSet.getString(4));
        message.setTimestamp(resultSet.getTimestamp(5));
        return message;
    }
}
