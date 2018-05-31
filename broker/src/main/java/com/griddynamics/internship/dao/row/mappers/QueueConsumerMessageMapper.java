package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.entities.Message;
import com.griddynamics.internship.models.entities.SourceConsumerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class QueueConsumerMessageMapper implements RowMapper<SourceConsumerMessage> {
    @Autowired
    private DAOFactory daoFactory;

    @Override
    public SourceConsumerMessage mapRow(ResultSet resultSet, int i) throws SQLException {
        SourceConsumerMessage sourceConsumerMessage = new SourceConsumerMessage();
        sourceConsumerMessage.setId(resultSet.getInt(1));
        sourceConsumerMessage.setState(resultSet.getString(2));
        sourceConsumerMessage.setTimestamp(resultSet.getTimestamp(3));
        sourceConsumerMessage.setSource(daoFactory.getQueueDAO().getEntityById(resultSet.getInt(4)));
        sourceConsumerMessage.setConsumer(daoFactory.getConsumerDAO().getEntityById(resultSet.getInt(5)));

        Message message = new Message();
        message.setId(resultSet.getInt(6));
        message.setContent(resultSet.getString(7));
        message.setState(resultSet.getString(8));
        message.setTimestamp(resultSet.getTimestamp(9));

        sourceConsumerMessage.setMessage(message);
        return sourceConsumerMessage;
    }
}
