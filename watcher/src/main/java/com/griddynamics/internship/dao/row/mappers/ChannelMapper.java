package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.models.Channel;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ChannelMapper implements RowMapper<Channel> {

    @Override
    public Channel mapRow(ResultSet resultSet, int i) throws SQLException {
        Channel channel = new Channel();
        channel.setId(resultSet.getInt(1));
        channel.setPath(resultSet.getString(2));
        channel.setName(resultSet.getString(3));
        return channel;
    }
}
