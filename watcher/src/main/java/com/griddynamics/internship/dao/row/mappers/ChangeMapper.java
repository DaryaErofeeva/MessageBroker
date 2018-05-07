package com.griddynamics.internship.dao.row.mappers;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.Change;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ChangeMapper implements RowMapper<Change> {

    @Autowired
    private DAOFactory daoFactory;

    @Override
    public Change mapRow(ResultSet resultSet, int i) throws SQLException {
        Change change = new Change();
        change.setId(resultSet.getInt(1));
        change.setMessage(daoFactory.getMessagesDAO().getEntityById(resultSet.getInt(2)));
        change.setContent(resultSet.getString(3));
        change.setTimestamp(resultSet.getTimestamp(4));
        return change;
    }
}
