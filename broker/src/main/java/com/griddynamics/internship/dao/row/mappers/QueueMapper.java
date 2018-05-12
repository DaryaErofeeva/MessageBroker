package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.db.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class QueueMapper implements RowMapper<Queue> {

    @Autowired
    private DAOFactory daoFactory;

    @Override
    public Queue mapRow(ResultSet resultSet, int i) throws SQLException {
        Queue queue = new Queue();
        queue.setId(resultSet.getInt(1));
        queue.setProducer(daoFactory.getProducerDAO().getEntityById(resultSet.getInt(2)));
        queue.setConsumer(daoFactory.getConsumerDAO().getEntityById(resultSet.getInt(3)));
        queue.setName(resultSet.getString(4));
        return queue;
    }
}
