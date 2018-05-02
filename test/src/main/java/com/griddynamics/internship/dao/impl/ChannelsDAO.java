package com.griddynamics.internship.dao.impl;

import com.griddynamics.internship.dao.DatabaseConnection;
import com.griddynamics.internship.dao.GenericDAO;
import com.griddynamics.internship.models.Channel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChannelsDAO implements GenericDAO<Channel, Integer> {
    @Override
    public List<Channel> getAll() {
        List<Channel> channels = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CHANNELS");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while ((resultSet.next())) {
                Channel channel = new Channel();
                channel.setId(resultSet.getInt(1));
                channel.setPath(resultSet.getString(2));
                channel.setName(resultSet.getString(3));
                channels.add(channel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return channels;
    }

    @Override
    public int update(Channel entity) {
        int affectedRowsAmount = 0;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE CHANNELS " +
                     "SET PATH = ?, NAME = ?" +
                     "WHERE ID = ?")) {
            preparedStatement.setString(1, entity.getPath());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setInt(3, entity.getId());
            affectedRowsAmount = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRowsAmount;
    }

    @Override
    public Channel getEntityById(Integer id) {
        Channel channel = new Channel();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CHANNELS WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while ((resultSet.next())) {
                    channel.setId(resultSet.getInt(1));
                    channel.setPath(resultSet.getString(2));
                    channel.setName(resultSet.getString(3));
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return channel;
    }

    @Override
    public int delete(Integer id) {
        int affectedRowsAmount = 0;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM CHANNELS WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            affectedRowsAmount = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRowsAmount;
    }

    @Override
    public int create(Channel entity) {
        int affectedRowsAmount = 0;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CHANNELS " +
                     "(PATH, NAME) VALUES (?, ?)")) {
            preparedStatement.setString(1, entity.getPath());
            preparedStatement.setString(2, entity.getName());
            affectedRowsAmount = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRowsAmount;
    }
}
