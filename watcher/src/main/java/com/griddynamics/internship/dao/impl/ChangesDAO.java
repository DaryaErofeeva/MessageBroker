package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.dao.DatabaseConnection;
import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.models.Change;
import com.griddynamics.internship.models.Channel;
import com.griddynamics.internship.models.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChangesDAO implements GenericDAO<Change, Integer> {
    @Override
    public List<Change> getAll() {
        List<Change> changes = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CHANGES");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while ((resultSet.next())) {
                Change change = new Change();
                change.setId(resultSet.getInt(1));
                change.setMessage(DAOFactory.getInstance().getMessagesDAO().getEntityById(resultSet.getInt(2)));
                change.setContent(resultSet.getString(3));
                change.setTimestamp(resultSet.getTimestamp(4));
                changes.add(change);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return changes;
    }

    @Override
    public int update(Change entity) {
        int affectedRowsAmount = 0;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE CHANGES " +
                     "SET MESSAGE_ID = ?, CONTENT = ?, TIMESTAMP = ?" +
                     "WHERE ID = ?")) {
            preparedStatement.setInt(1, entity.getMessage().getId());
            preparedStatement.setString(2, entity.getContent());
            preparedStatement.setTimestamp(3, entity.getTimestamp());
            preparedStatement.setInt(4, entity.getId());
            affectedRowsAmount = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRowsAmount;
    }

    @Override
    public Change getEntityById(Integer id) {
        Change change = new Change();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CHANGES WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while ((resultSet.next())) {
                    change.setId(resultSet.getInt(1));
                    change.setMessage(DAOFactory.getInstance().getMessagesDAO().getEntityById(resultSet.getInt(2)));
                    change.setContent(resultSet.getString(3));
                    change.setTimestamp(resultSet.getTimestamp(4));
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return change;
    }

    @Override
    public int delete(Integer id) {
        int affectedRowsAmount = 0;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM CHANGES WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            affectedRowsAmount = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRowsAmount;
    }

    @Override
    public int create(Change entity) {
        int affectedRowsAmount = 0;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CHANGES " +
                     "(MESSAGE_ID, CONTENT, TIMESTAMP) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, entity.getMessage().getId());
            preparedStatement.setString(2, entity.getContent());
            preparedStatement.setTimestamp(3, entity.getTimestamp());

            affectedRowsAmount = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRowsAmount;
    }

    @Override
    public Integer getIdByEntity(Change entity) {
        int id = 0;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM CHANGES WHERE MESSAGE_ID = ? AND TIMESTAMP = ?")) {
            preparedStatement.setInt(1, entity.getMessage().getId());
            preparedStatement.setTimestamp(2, entity.getTimestamp());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while ((resultSet.next())) {
                    id = resultSet.getInt(1);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public Change createEntityIfNotExists(Change entity) {
        if (getIdByEntity(entity) == 0) create(entity);
        entity.setId(getIdByEntity(entity));
        return entity;
    }
}
