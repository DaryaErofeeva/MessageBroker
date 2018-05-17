package com.griddynamics.internship.dao.impl.result.set.extractors;

import com.griddynamics.internship.models.entities.Consumer;
import com.griddynamics.internship.models.entities.Message;
import com.griddynamics.internship.models.entities.Topic;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TopicExtractor implements ResultSetExtractor<List<Topic>> {

    @Override
    public List<Topic> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Integer, Topic> map = new HashMap<>();

        while (resultSet.next()) {
            int topicId = resultSet.getInt(1);
            Topic topic = map.get(topicId);
            if (topic == null) {
                topic = new Topic();
                topic.setId(topicId);
                topic.setName(resultSet.getString(2));
                topic.setConsumers(new ArrayList<>());
                topic.setMessages(new ArrayList<>());
                map.put(topicId, topic);
            }

            int consumerId = resultSet.getInt(3);
            if (consumerId != 0 && !topic.getConsumers().stream().anyMatch(consumer -> consumer.getId() == consumerId))
                topic.getConsumers().add(new Consumer(consumerId, resultSet.getString(4)));

            int messageId = resultSet.getInt(5);
            if (messageId != 0 && !topic.getMessages().stream().anyMatch(message -> message.getId() == messageId))
                topic.getMessages().add(new Message(messageId,
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getTimestamp(8)));
        }
        return new ArrayList<>(map.values());
    }
}
