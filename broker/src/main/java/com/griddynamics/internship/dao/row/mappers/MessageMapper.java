package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.models.db.Message;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet resultSet, int i) throws SQLException {
        Message message=new Message();
        message.setId(resultSet.getInt(1));
        message.setName(resultSet.getString(2));
        message.setContent(resultSet.getString(3));
        message.setTimestamp(resultSet.getTimestamp(4));
        return message;
    }
}
