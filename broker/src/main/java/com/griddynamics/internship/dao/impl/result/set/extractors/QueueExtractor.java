package com.griddynamics.internship.dao.impl.result.set.extractors;

import com.griddynamics.internship.models.Consumer;
import com.griddynamics.internship.models.Message;
import com.griddynamics.internship.models.Queue;
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
public class QueueExtractor implements ResultSetExtractor<List<Queue>> {

    @Override
    public List<Queue> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Integer, Queue> map = new HashMap<>();

        while (resultSet.next()) {
            int queueId = resultSet.getInt(1);
            Queue queue = map.get(queueId);
            if (queue == null) {
                queue = new Queue();
                queue.setId(queueId);
                queue.setName(resultSet.getString(2));
                queue.setConsumers(new ArrayList<>());
                queue.setMessages(new ArrayList<>());
                map.put(queueId, queue);
            }

            int consumerId = resultSet.getInt(3);
            if (consumerId != 0 && !queue.getConsumers().stream().anyMatch(consumer -> consumer.getId() == consumerId))
                queue.getConsumers().add(new Consumer(consumerId, resultSet.getString(4), resultSet.getString(5)));

            int messageId = resultSet.getInt(6);
            if (messageId != 0 && !queue.getMessages().stream().anyMatch(message -> message.getId() == messageId))
                queue.getMessages().add(new Message(messageId,
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getTimestamp(9)));
        }
        return new ArrayList<>(map.values());
    }
}
