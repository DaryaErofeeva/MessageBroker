package com.griddynamics.internship.dao.impl;

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
                message.setChannel(resultSet.getObject(2, Channel.class));
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
        return 0;
    }

    @Override
    public Message getEntityById(Integer id) {
        return null;
    }

    @Override
    public int delete(Integer id) {
        return 0;
    }

    @Override
    public int create(Message entity) {
        return 0;
    }
}

