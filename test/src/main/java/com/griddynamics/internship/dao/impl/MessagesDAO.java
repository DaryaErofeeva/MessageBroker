package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.dao.DatabaseConnection;
import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.models.Channel;
import com.griddynamics.internship.models.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessagesDAO implements GenericDAO<Message, Integer> {
    @Override
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM MESSAGES");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while ((resultSet.next())) {
                Message message = new Message();
                message.setId(resultSet.getInt(1));
                message.setChannel(DAOFactory.getInstance().getChannelsDAO().getEntityById(resultSet.getInt(2)));
                message.setName(resultSet.getString(3));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public int update(Message entity) {
        int affectedRowsAmount = 0;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE MESSAGES " +
                     "SET CHANNEL_ID = ?, NAME = ?" +
                     "WHERE ID = ?")) {
            preparedStatement.setInt(1, entity.getChannel().getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setInt(3, entity.getId());
            affectedRowsAmount = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRowsAmount;
    }

    @Override
    public Message getEntityById(Integer id) {
        Message message = new Message();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM MESSAGES WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while ((resultSet.next())) {
                    message.setId(resultSet.getInt(1));
                    message.setChannel(DAOFactory.getInstance().getChannelsDAO().getEntityById(resultSet.getInt(2)));
                    message.setName(resultSet.getString(3));
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public int delete(Integer id) {
        int affectedRowsAmount = 0;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM MESSAGES WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            affectedRowsAmount = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRowsAmount;
    }

    @Override
    public int create(Message entity) {
        int affectedRowsAmount = 0;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO MESSAGES " +
                     "(CHANNEL_ID, NAME) VALUES (?, ?)")) {
            preparedStatement.setInt(1, entity.getChannel().getId());
            preparedStatement.setString(2, entity.getName());
            affectedRowsAmount = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRowsAmount;
    }
}

